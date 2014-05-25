package nl.avans.glassy.Threads;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nl.avans.glassy.Controllers.WijkActivity;
import nl.avans.glassy.Controllers.WijkFragment;
import nl.avans.glassy.Interfaces.PagerAdapter;
import nl.avans.glassy.Models.Actie;
import nl.avans.glassy.Utils.ApiCommunicator;
import nl.avans.glassy.Utils.MyLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ActieManager {

	/*
	 * Context
	 */
	private WijkActivity myActivity;

	/*
	 * Status indicators
	 */
	public static final int INIT_START = 0;
	public static final int LOCATION_START = 1;
	public static final int LOCATION_FAILED = 2;
	public static final int LOCATION_OBTAINED = 3;
	public static final int CLOSEBY_DOWNLOAD_START = 4;
	public static final int CLOSEBY_DOWNLOAD_FAILED = 5;
	public static final int CLOSEBY_DOWNLOAD_COMPLETE = 6;
	public static final int ACTIE_INIT_START = 7;
	public static final int DOWNLOAD_STARTED = 8;
	public static final int DOWNLOAD_FAILED = 9;
	public static final int DOWNLOAD_COMPLETE = 10;
	public static final int DECODE_FAILED = 11;
	public static final int DECODE_COMPLETE = 12;
	public static final int TASK_COMPLETE = 13;

	// Sets the amount of time an idle thread will wait for a task before
	// terminating
	private static final int KEEP_ALIVE_TIME = 1;

	// Sets the Time Unit to seconds
	private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

	// Sets the initial threadpool size to 8
	private static final int CORE_POOL_SIZE = 8;

	// Sets the maximum threadpool size to 8
	private static final int MAXIMUM_POOL_SIZE = 8;

	/**
	 * NOTE: This is the number of total available cores. On current versions of
	 * Android, with devices that use plug-and-play cores, this will return less
	 * than the total number of cores. The total number of cores is not
	 * available in current Android implementations.
	 */
	private static int NUMBER_OF_CORES = Runtime.getRuntime()
			.availableProcessors();

	// A single instance of ActieManager, used to implement the singleton
	// pattern
	private static ActieManager sInstance = null;

	// A managed pool of background decoder threads
	private final ThreadPoolExecutor mDecodeThreadPool;

	// An object that manages Messages in a Thread
	public static Handler mHandler;

	// A queue of ActieManager tasks. Tasks are handed to a ThreadPool.
	private final Queue<ActieTask> mActieTaskWorkQueue;

	// A queue of Runnables for the decoding pool
	private final BlockingQueue<Runnable> mDecodeWorkQueue;

	// A static block that sets class fields
	static {

		// The time unit for "keep alive" is in seconds
		KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

		// Creates a single static instance of ActieManager
		sInstance = new ActieManager();
	}

	/**
	 * Returns the ActieManager object
	 * 
	 * @return The global ActieManager object
	 */
	public static ActieManager getInstance() {
		return sInstance;
	}

	public ActieManager() {

		/*
		 * Creates a work queue for the pool of Thread objects used for
		 * decoding, using a linked list queue that blocks when the queue is
		 * empty.
		 */
		mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();

		/*
		 * Creates a work queue for the set of of task objects that control
		 * downloading and decoding, using a linked list queue that blocks when
		 * the queue is empty.
		 */
		mActieTaskWorkQueue = new LinkedBlockingQueue<ActieTask>();

		/*
		 * Creates a new pool of Thread objects for the decoding work queue
		 */
		mDecodeThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES,
				NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				mDecodeWorkQueue);

		/*
		 * Instantiates a new anonymous Handler object and defines its
		 * handleMessage() method. The Handler *must* run on the UI thread,
		 * because it ... To force the Handler to run on the UI thread, it's
		 * defined as part of the ActieManager constructor. The constructor is
		 * invoked when the class is first referenced, and that happens when the
		 * WijkActivity invokes initActies. Since the Class runs on the UI
		 * Thread, so does the constructor and the Handler.
		 */
		mHandler = new Handler(Looper.getMainLooper()) {
			/*
			 * handleMessage() defines the operations to perform when the
			 * Handler receives a new Message to process.
			 */
			@Override
			public void handleMessage(Message inputMessage) {
				// Gets the actie task from the incoming Message object.
				ActieTask actieTask = (ActieTask) inputMessage.obj;

				// Sets an Actie that's a weak reference to the
				// input class
				// Actie actie = actieTask.getActie();

				/*
				 * Chooses the action to take, based on the incoming message
				 */
				switch (inputMessage.what) {
				case DECODE_COMPLETE:
					break;
				case TASK_COMPLETE:
					recycleTask(actieTask);
					break;
				case CLOSEBY_DOWNLOAD_FAILED:
					recycleTask(actieTask);
					break;
				default:
					super.handleMessage(inputMessage);
				}
			}
		};
	}

	/**
	 * Handles state messages for a particular task object
	 * 
	 * @param photoTask
	 *            A task object
	 * @param state
	 *            The state of the task
	 */
	@SuppressLint("HandlerLeak")
	public void handleState(Object resultObject, int state) {
		try {
			switch (state) {
			// Start defining geolocation
			case LOCATION_START:
				Log.d("ActieManager", "LOCATION_START");

				getDeviceLocation();
				break;
			// Unable to locate device position
			case LOCATION_FAILED:
				Log.d("ActieManager",
						"LOCATION_FAILED, obtaining device location failed.");
				Log.d("ActieManager",
						"LOCATION_FAILED, Using default: The center of Netherland: Utrecht.");

				/**
				 * TODO: Give user ability to input custom geolocation.
				 */

				setDefaultDeviceLocation();
				break;
			// Custom geolocation result
			case LOCATION_OBTAINED:
				Log.d("ActieManager", "LOCATION_OBTAINED");
				Location deviceLocation = (Location) resultObject;

				handleLocationResults(deviceLocation);
				break;
			// Start download of close by Acties JSON
			case CLOSEBY_DOWNLOAD_START:
				Log.d("ActieManager", "CLOSEBY_DOWNLOAD_START");
				String[] params = (String[]) resultObject;

				startClosebyDownload(params);
				break;
			// Unable to download JSON from API
			case CLOSEBY_DOWNLOAD_FAILED:
				Log.d("ActieManager",
						"CLOSEBY_DOWNLOAD_FAILED, Connection Time Out");
				/**
				 * TODO: Handle this outcome
				 */
				break;
			// Download of close by Acties completed
			case CLOSEBY_DOWNLOAD_COMPLETE:
				Log.d("ActieManager", "CLOSEBY_DOWNLOAD_COMPLETE");
				JSONObject result = (JSONObject) resultObject;

				parseJSONResult(result);
				break;
			// Start initialization of each JSONObject in acties Array
			case ACTIE_INIT_START:
				Log.d("ActieManager", "ACTIE_INIT_START");
				JSONArray actieArray = (JSONArray) resultObject;

				startActieInitilization(actieArray);
				break;
			// Result from a download task
			case DOWNLOAD_COMPLETE:
				ActieTask actieObject = (ActieTask) resultObject;
				Log.d("ActieManager", actieObject.getTask()
						+ " DOWNLOAD_COMPLETE");

				handleResult(actieObject);
				break;
			// Unable to parse JSON
			case DECODE_FAILED:
				Log.d("ActieManager", "DECODE_FAILED");
				break;
			// Decoding of JSON complete
			case DECODE_COMPLETE:
				Log.d("ActieManager", "DECODE_COMPLETE");
				break;
			// Initializing fragment complete
			case TASK_COMPLETE:
				Log.d("ActieManager", "TASK_COMPLETE");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.d("ActieManager", "Unable to parse resultObject");
			Log.d("ActieManager", resultObject.getClass().toString());
			e.printStackTrace();
		}
	}

	/**
	 * startInitialization, Called when the application starts.
	 * 
	 * It starts the process of filling the application with data from the API
	 * 
	 * @param context
	 *            reference to the parent Activity
	 */
	public void startInitialization(WijkActivity context) {
		// Set the context required for future reference
		this.myActivity = context;

		// Starts the cascade of states
		handleState(null, LOCATION_START);
	}

	/**
	 * getDeviceLocation, Called by LOCATION_START
	 * 
	 * It starts the process of locating current or last know device location
	 */
	private void getDeviceLocation() {
		new MyLocation().getLocation(myActivity);
	}

	/**
	 * setDefaultDeviceLocation, Called by LOCATION_FAILED when the class
	 * MyLocation is unable to set the current or last known location of this
	 * device.
	 * 
	 * It sets a default location, this location is central Utrecht
	 */
	private void setDefaultDeviceLocation() {
		// Create custom Location Object with the latitude and longitude of
		// Utrecht.
		Location locationResult = new Location("");
		locationResult.setLatitude(51.5852378);
		locationResult.setLongitude(4.7564221);

		// Pass the custom location to the next state.
		handleState(locationResult, LOCATION_OBTAINED);
	}

	/**
	 * handleLocationResults, Called by LOCATION_OBTAINED when the class
	 * MyLocation is able to set the current or last know location of this
	 * device, or when the default device location is set.
	 * 
	 * It reads the result and sets the correct API URL
	 * 
	 * @param deviceLocation
	 *            Location object with correct latitude and longitude
	 */
	private void handleLocationResults(Location deviceLocation) {
		// TODO: in global class zetten
		int wijkLimit = 10;

		// Set the correct API URL with the latitude and longitude results
		String API_CONTROLLER = "wijk/closeby?lat="
				+ deviceLocation.getLatitude() + "&long="
				+ deviceLocation.getLongitude() + "&limit=" + wijkLimit;
		String[] params = { "GET", API_CONTROLLER };

		// pass the params string array to the next state
		handleState(params, CLOSEBY_DOWNLOAD_START);
	}

	/**
	 * startClosebyDownload, Called by CLOSEBY_DOWNLOAD_START
	 * 
	 * It starts a download with given parameters
	 * 
	 * @param params
	 *            String array with API URL and call type
	 */
	private void startClosebyDownload(String[] params) {
		// Initializes ApiCommunicator with custom onPostExecute
		final ApiCommunicator mApiComm = new ApiCommunicator(myActivity) {

			@Override
			protected void onPostExecute(JSONObject result) {
				// On download complete pass result to correct state
				handleState(result, CLOSEBY_DOWNLOAD_COMPLETE);
			}
		};
		// Execute download
		mApiComm.execute(params);

		// Implement a connection time out when no result is given after 30
		// seconds.
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mApiComm.getStatus() == AsyncTask.Status.RUNNING) {
					mApiComm.cancel(true);
					// On download time out pass result to correct state
					handleState(null, CLOSEBY_DOWNLOAD_FAILED);
				}
			}
		}, 30000);
	}

	/**
	 * parseJSONResult, Called by CLOSEBY_DOWNLOAD_COMPLETE.
	 * 
	 * It parses the JSONObject to an JSONArray
	 * 
	 * @param result
	 *            JSONObject of results from the API call
	 */
	private void parseJSONResult(JSONObject result) {
		try {
			handleState(result.getJSONArray("entries"), ACTIE_INIT_START);
		} catch (JSONException e) {
			Log.d("ActieManager", "parseJSONResult, unable to parse result");
			e.printStackTrace();
		}
	}

	/**
	 * startActieInitilization, Called by ACTIE_INIT_START
	 * 
	 * It reads the actieArray and starts initialization for each entry.
	 * 
	 * @param actieArray
	 */
	private void startActieInitilization(JSONArray actieArray) {
		Log.d("ActieManager",
				"ACTIE_INIT_START, initializing " + actieArray.length()
						+ " acties");

		PagerAdapter scrollPagerAdapter = myActivity.getViewPagerAdapter();

		for (int i = 0; i < actieArray.length(); i++) {
			try {
				JSONObject temp = actieArray.getJSONObject(i);
				WijkFragment tempFragment = new WijkFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("wijk_id", temp.getInt("wijk_id"));
				tempFragment.setArguments(bundle);

				scrollPagerAdapter.addFragmentToAdapter(tempFragment);
			} catch (Exception e) {
				Log.d("ActieManager",
						"parseJSONResult, unable to parse actieArray");
				e.printStackTrace();
			}
		}
	}

	/**
	 * startInitialization Called by a WijkFragment. It starts the
	 * initialization of the WijkFragment
	 * 
	 * @param wijkFragment
	 *            The fragment that will be updated with results
	 */
	public static void startFragmentInitialization(WijkFragment wijkFragment) {

		Log.d("ActieManager", "Actie " + wijkFragment.getWijkId()
				+ ": startInitialization called");
		/*
		 * Gets a task from the pool of tasks, returning null if the pool is
		 * empty
		 */
		ActieTask downloadTask = sInstance.mActieTaskWorkQueue.poll();

		// If the queue was empty, create a new task instead.
		if (null == downloadTask) {
			downloadTask = new ActieTask();
		}

		// Initializes the task
		downloadTask.initializeDownloaderTask(ActieManager.sInstance,
				wijkFragment);

		// Start the general wijk information download

		/*
		 * Start the detail initialization Dit request in global class zetten
		 */
		downloadTask.setTask("DETAIL");
		String API_CONTROLLER = "wijk/?id=" + downloadTask.getWijkID();
		String[] params = { "GET", API_CONTROLLER };
		downloadTask.getDownloadRunnable().execute(params);
	}

	private void handleResult(ActieTask actieObject) {
		String task = actieObject.getTask();
		if (task == "DETAIL") {
			WijkFragment tempLink = actieObject.getWijkFragment();
			tempLink.setDetail(actieObject.getResult());
		}
	}

	/**
	 * Starts an JSON decode
	 */
	static public ActieTask startDecode(Actie actieClass) {
		/*
		 * Gets a task from the pool of tasks, returning null if the pool is
		 * empty
		 */
		ActieTask decodeTask = sInstance.mActieTaskWorkQueue.poll();

		// If the queue was empty, create a new task instead.
		if (null == decodeTask) {
			decodeTask = new ActieTask();
		}

		// Initializes the task
		// decodeTask.initializeDecodeTask(ActieManager.sInstance, actieClass);

		/*
		 * "Executes" the tasks' decode Runnable in order to decode the JSON. If
		 * no Threads are available in the thread pool, the Runnable waits in
		 * the queue.
		 */
		// sInstance.mDecodeThreadPool
		// .execute(decodeTask.getActieDecodeRunnable());

		// Returns a task object, either newly-created or one from the task pool
		return decodeTask;
	}

	/**
	 * Recycles tasks by calling their internal recycle() method and then
	 * putting them back into the task queue.
	 * 
	 * @param downloadTask
	 *            The task to recycle
	 */
	void recycleTask(ActieTask downloadTask) {

		// Frees up memory in the task
		downloadTask.recycle();

		// Puts the task object back into the queue for re-use.
		mActieTaskWorkQueue.offer(downloadTask);
	}
}