package nl.avans.glassy.Threads;

import java.lang.ref.WeakReference;

import nl.avans.glassy.Controllers.WijkFragment;
import nl.avans.glassy.Threads.ActieDownloadRunnable.TaskRunnableDownloadMethods;
import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

public class ActieTask implements TaskRunnableDownloadMethods {

	/*
	 * Creates a weak reference to the Fragment that this Task will populate.
	 * The weak reference prevents memory leaks and crashes, because it
	 * automatically tracks the "state" of the variable it backs. If the
	 * reference becomes invalid, the weak reference is garbage- collected. This
	 * technique is important for referring to objects that are part of a
	 * component lifecycle. Using a hard reference may cause memory leaks as the
	 * value continues to change; even worse, it can cause crashes if the
	 * underlying component is destroyed.
	 */
	private WeakReference<WijkFragment> mFragmentWeakRef;

	// The wijkID
	private int mWijkID;
	// Downloaded JSON
	private JSONObject result;
	// Task tag
	private String taskTag;

	// The Thread on which this task is currently running.
	private Thread mCurrentThread;

	/*
	 * An object that contains the ThreadPool singleton.
	 */
	private static ActieManager sActieManager;

	/*
	 * Field containing the Thread this task is running on.
	 */
	Thread mThreadThis;

	/*
	 * Fields containing references to the two runnable objects that handle
	 * downloading and decoding of the image.
	 */
	private ApiCommunicator mDownloadRunnable;
	private Runnable mDecodeRunnable;

	/**
	 * Creates an ActieTask containing a download object and a decoder object.
	 */
	public ActieTask() {
		// Create the runnables
		mDownloadRunnable = new ActieDownloadRunnable(this);
		// mDecodeRunnable = new ActieDecodeRunnable(this);
		sActieManager = ActieManager.getInstance();
	}

	// Delegates handling the current state of the task to the ActieManager
	// object
	void handleState(int state) {
		sActieManager.handleState(this, state);
	}

	/**
	 * Initializes the Task
	 * 
	 * @param ActieManager
	 *            A ThreadPool object
	 * @param wijkFragment
	 *            An WijkFragment instance that needs to be filled
	 */

	void initializeDownloaderTask(ActieManager actieManager,
			WijkFragment wijkFragment) {
		// Sets this object's ThreadPool field to be the input argument
		sActieManager = actieManager;

		// Gets the URL for the View
		mWijkID = wijkFragment.getWijkId();

		// Instantiates the weak reference to the incoming view
		mFragmentWeakRef = new WeakReference<WijkFragment>(wijkFragment);
	}

	// Returns the instance that downloaded the image
	ApiCommunicator getDownloadRunnable() {
		return mDownloadRunnable;
	}

	// Returns the ImageView that's being constructed.
	public WijkFragment getWijkFragment() {
		if (null != mFragmentWeakRef) {
			return mFragmentWeakRef.get();
		}
		return null;
	}

	/*
	 * Returns the Thread that this Task is running on. The method must first
	 * get a lock on a static field, in this case the ThreadPool singleton. The
	 * lock is needed because the Thread object reference is stored in the
	 * Thread object itself, and that object can be changed by processes outside
	 * of this app.
	 */
	public Thread getCurrentThread() {
		synchronized (sActieManager) {
			return mCurrentThread;
		}
	}

	/*
	 * Sets the identifier for the current Thread. This must be a synchronized
	 * operation; see the notes for getCurrentThread()
	 */
	public void setCurrentThread(Thread thread) {
		synchronized (sActieManager) {
			mCurrentThread = thread;
		}
	}

	@Override
	public void handleDownloadState(JSONObject jResult, int state) {
		int outState;
		this.result = jResult;

		// Converts the download state to the overall state
		switch (state) {
		case ActieDownloadRunnable.HTTP_STATE_COMPLETED:
			outState = ActieManager.DOWNLOAD_COMPLETE;
			break;
		case ActieDownloadRunnable.HTTP_STATE_FAILED:
			outState = ActieManager.DOWNLOAD_FAILED;
			break;
		default:
			outState = ActieManager.DOWNLOAD_STARTED;
			break;
		}
		// Passes the state to the ThreadPool object.
		handleState(outState);
	}

	@Override
	public int getWijkID() {
		return mWijkID;
	}

	public void setTask(String task) {
		taskTag = task;
	}

	public String getTask() {
		return taskTag;
	}

	public JSONObject getResult() {
		return result;
	}

	/**
	 * Recycles an ActieTask object before it's put back into the pool. One
	 * reason to do this is to avoid memory leaks.
	 */
	void recycle() {

		// Deletes the weak reference to the imageView
		if (null != mFragmentWeakRef) {
			mFragmentWeakRef.clear();
			mFragmentWeakRef = null;
		}
	}
}
