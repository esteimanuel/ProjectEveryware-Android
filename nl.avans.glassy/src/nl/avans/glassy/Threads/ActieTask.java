package nl.avans.glassy.Threads;

import java.lang.ref.WeakReference;

import nl.avans.glassy.Models.Actie;
import nl.avans.glassy.Threads.ActieDecodeRunnable.TaskRunnableDecodeMethods;

import org.json.JSONObject;

public class ActieTask implements TaskRunnableDecodeMethods {
	/*
	 * Creates a weak reference to the Actie that this Task will populate. The
	 * weak reference prevents memory leaks and crashes, because it
	 * automatically tracks the "state" of the variable it backs. If the
	 * reference becomes invalid, the weak reference is garbage- collected. This
	 * technique is important for referring to objects that are part of a
	 * component lifecycle. Using a hard reference may cause memory leaks as the
	 * value continues to change; even worse, it can cause crashes if the
	 * underlying component is destroyed. Using a weak reference to a View
	 * ensures that the reference is more transitory in nature.
	 */
	private WeakReference<Actie> mActieWeakRef;

	// The Thread on which this task is currently running.
	private Thread mCurrentThread;

	/*
	 * An object that contains the ThreadPool singleton.
	 */
	private static ActieManager sActieManager;

	/*
	 * Fields containing references to the two runnable objects that handle
	 * downloading and decoding of the image.
	 */
	private Runnable mDecodeRunnable;

	private JSONObject mJSONObject;

	private int wijk_id;
	private int target;
	private int aantal_huishoudens;
	private String wijk_naam;

	/**
	 * Creates an ActieTask containing a download object and a decoder object.
	 */
	public ActieTask() {
		// Create the runnables
		mDecodeRunnable = new ActieDecodeRunnable(this);
		sActieManager = ActieManager.getInstance();
	}

	// Returns the instance that decode the image
	Runnable getPhotoDecodeRunnable() {
		return mDecodeRunnable;
	}

	// Returns the Actie that's being constructed.
	public Actie getActie() {
		if (null != mActieWeakRef) {
			return mActieWeakRef.get();
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

	// Returns the instance that decode the image
	public Runnable getActieDecodeRunnable() {
		return mDecodeRunnable;
	}

	// Delegates handling the current state of the task to the PhotoManager
	// object
	void handleState(int state) {
		sActieManager.handleState(this, state);
	}

	/**
	 * Initializes the Task
	 * 
	 * @param photoManager
	 *            A ThreadPool object
	 * @param photoView
	 *            An ImageView instance that shows the downloaded image
	 * @param cacheFlag
	 *            Whether caching is enabled
	 */
	void initializeDecodeTask(ActieManager actieManager, Actie actieClass) {
		// Sets this object's ThreadPool field to be the input argument
		sActieManager = actieManager;

		// Gets the URL for the View
		mJSONObject = actieClass.getActieJSON();

		// Instantiates the weak reference to the incoming view
		mActieWeakRef = new WeakReference<Actie>(actieClass);

	}

	@Override
	public void setJSONDecodeThread(Thread currentThread) {
		setCurrentThread(currentThread);
	}

	/*
	 * Implements ActieDecodeRunnable.handleDecodeState(). Passes the decoding
	 * state to the ThreadPool object.
	 */
	@Override
	public void handleDecodeState(int state) {
		int outState;

		// Converts the decode state to the overall state.
		switch (state) {
		case ActieDecodeRunnable.DECODE_STATE_COMPLETED:
			outState = ActieManager.DECODE_COMPLETE;
			break;
		case ActieDecodeRunnable.DECODE_STATE_FAILED:
		default:
			outState = ActieManager.DECODE_FAILED;
			break;
		}

		// Passes the state to the ThreadPool object.
		handleState(outState);
	}

	@Override
	public void setIntValue(String name, int value) {
		if (name.equals("wijk_id")) {
			wijk_id = value;
		} else if (name.equals("target")) {
			target = value;
		} else if (name.equals("aantal_huishoudens")) {
			aantal_huishoudens = value;
		}
	}

	@Override
	public void setStringValue(String name, String value) {
		if (name.equals("wijk_naam")) {
			wijk_naam = value;
		}
	}

	@Override
	public JSONObject getActieJSON() {
		return mJSONObject;
	}

	public int getWijk_id() {
		return wijk_id;
	}

	public int getTarget() {
		return target;
	}

	public int getAantal_huishoudens() {
		return aantal_huishoudens;
	}

	public String getWijk_naam() {
		return wijk_naam;
	}
	
    /**
     * Recycles an ActieTask object before it's put back into the pool. One reason to do
     * this is to avoid memory leaks.
     */
    void recycle() {
        
        // Deletes the weak reference to the imageView
        if ( null != mActieWeakRef ) {
        	mActieWeakRef.clear();
        	mActieWeakRef = null;
        }
    }
}
