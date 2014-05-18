package nl.avans.glassy.Threads;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nl.avans.glassy.Controllers.WijkActivity;
import nl.avans.glassy.Models.Actie;
import nl.avans.glassy.Utils.ApiCommunicator;
import nl.avans.glassy.Utils.MyLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ActieManager {

	/*
	 * Parent activity
	 */
	private WijkActivity myActivity;

	private MyLocation myLocation;

	private ArrayList<Actie> actieArray;

	/*
	 * Status indicators
	 */
	static final int LOCATION_FAILED = -1;
	static final int LOCATION_OBTAINED = 0;
	static final int DOWNLOAD_FAILED = 1;
	static final int DOWNLOAD_STARTED = 2;
	static final int DOWNLOAD_COMPLETE = 3;
	static final int DECODE_COMPLETE = 4;
	static final int DECODE_FAILED = 5;
	static final int TASK_COMPLETE = 6;

	// Sets the amount of time an idle thread will wait for a task before
	// terminating
	private static final int KEEP_ALIVE_TIME = 1;

	// Sets the Time Unit to seconds
	private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

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

	// An object that manages Messages in a Thread
	private Handler mHandler;

	// A queue of ActieManager tasks. Tasks are handed to a ThreadPool.
	private final Queue<ActieTask> mActieTaskWorkQueue;

	// A queue of Runnables for the image decoding pool
	private final BlockingQueue<Runnable> mDecodeWorkQueue;

	// A managed pool of background decoder threads
	private final ThreadPoolExecutor mDecodeThreadPool;

	// A static block that sets class fields
	static {

		// The time unit for "keep alive" is in seconds
		KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

		// Creates a single static instance of ActieManager
		sInstance = new ActieManager();
	}

	public ActieManager() {

		actieArray = new ArrayList<Actie>();
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
				Actie actie = actieTask.getActie();

				/*
				 * Chooses the action to take, based on the incoming message
				 */
				switch (inputMessage.what) {
				case DOWNLOAD_STARTED:
					break;
				case DECODE_COMPLETE:
					actie.setAantal_huishoudens(actieTask
							.getAantal_huishoudens());
					actie.setTarget(actieTask.getTarget());
					actie.setWijk_id(actieTask.getWijk_id());
					actie.setWijk_naam(actieTask.getWijk_naam());
					myActivity.addFragment(actie);
					break;
				case TASK_COMPLETE:
					recycleTask(actieTask);
					break;
				case DOWNLOAD_FAILED:
					recycleTask(actieTask);
					break;
				default:
					super.handleMessage(inputMessage);
				}
			}
		};

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
		decodeTask.initializeDecodeTask(ActieManager.sInstance, actieClass);

		/*
		 * "Executes" the tasks' decode Runnable in order to decode the JSON. If
		 * no Threads are available in the thread pool, the Runnable waits in
		 * the queue.
		 */
		sInstance.mDecodeThreadPool
				.execute(decodeTask.getActieDecodeRunnable());

		// Returns a task object, either newly-created or one from the task pool
		return decodeTask;
	}

	/**
	 * Starts ScrollPager initialization
	 */
	public void init(WijkActivity cActivity) {
		myActivity = cActivity;
		Log.d("ActieManager", "init() called");

		myLocation = new MyLocation();

		Log.d("ActieManager", "Getting geolocation");
		myLocation.getLocation(myActivity);
	}

	/**
	 * Returns the ActieManager object
	 * 
	 * @return The global ActieManager object
	 */
	public static ActieManager getInstance() {

		return sInstance;
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
	public void handleState(ActieTask actieTask, int state) {
		switch (state) {

		// The task finished Decoding the JSONObject
		case DECODE_COMPLETE:
			Log.d("ActieManager", "Actie " + actieTask.getWijk_id()
					+ ": DECODE_COMPLETE:");

			// Gets a Message object, stores the state in it, and sends it to
			// the Handler
			Message completeMessage = mHandler.obtainMessage(state, actieTask);
			completeMessage.sendToTarget();
		default:
			break;
		}

	}

	/**
	 * Handles state messages for a particular location object
	 * 
	 * @param photoTask
	 *            A location object
	 * @param state
	 *            The state of the task
	 */
	public void handleLocationResult(Location locationResult, int state) {
		double lat = 52.0833;
		double lon = 5.1333;

		switch (state) {
		// Obtaining the location successful
		case LOCATION_OBTAINED:
			Log.d("ActieManager", locationResult.getLatitude() + " : "
					+ locationResult.getLongitude());
			lat = locationResult.getLatitude();
			lon = locationResult.getLongitude();

			// Prevent further change in location
			myLocation = null;
			break;
		// Obtaining the location failed
		case LOCATION_FAILED:
		default:
			// TODO
			Log.d("ActieManager",
					"getLocation failed, Using default location Utrecht");
			break;
		}

		getCloseByActies(lat, lon);

	}

	/**
	 * Download JSONObjects of nearest Acties
	 * 
	 * @param lat
	 *            A location object
	 * @param long A location object
	 */

	private void getCloseByActies(double lat, double lon) {

		String API_CONTROLLER = "wijk/closeby?lat=" + lat + "&long=" + lon;
		String[] params = { "GET", API_CONTROLLER };

		new ApiCommunicator(myActivity) {

			@Override
			protected void onPostExecute(JSONObject result) {
				this.handleState(result, DOWNLOAD_COMPLETE);
			}
		}.execute(params);

	}

	public void handleJSONDownloadState(JSONObject result, int state) {
		Log.d("ActieManager", "handleJSONDownloadState called");
		try {
			JSONArray acties = result.getJSONArray("entries");
			for (int i = 0; i < acties.length(); i++) {
				JSONObject temp = acties.getJSONObject(i);
				Actie tempActie = new Actie();
				tempActie.setActieJSON(temp);
				tempActie.startDecode();
				actieArray.add(tempActie);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
