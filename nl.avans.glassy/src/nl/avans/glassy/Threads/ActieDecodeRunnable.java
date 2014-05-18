package nl.avans.glassy.Threads;

import org.json.JSONException;
import org.json.JSONObject;

public class ActieDecodeRunnable implements Runnable {
	// Constants for indicating the state of the decode
	static final int DECODE_STATE_FAILED = -1;
	static final int DECODE_STATE_STARTED = 0;
	static final int DECODE_STATE_COMPLETED = 1;

	// Defines a field that contains the calling object of type PhotoTask.
	final TaskRunnableDecodeMethods mActieTask;

	/**
	 * 
	 * An interface that defines methods that PhotoTask implements. An instance
	 * of PhotoTask passes itself to an PhotoDecodeRunnable instance through the
	 * PhotoDecodeRunnable constructor, after which the two instances can access
	 * each other's variables.
	 */
	interface TaskRunnableDecodeMethods {

		/**
		 * Sets the Thread that this instance is running on
		 * 
		 * @param currentThread
		 *            the current Thread
		 */
		void setJSONDecodeThread(Thread currentThread);

		JSONObject getActieJSON();

		/**
		 * Sets the actions for each state of the PhotoTask instance.
		 * 
		 * @param state
		 *            The state being handled.
		 */
		void handleDecodeState(int state);

		/**
		 * Sets the value of an parameter *
		 * 
		 * @param name
		 *            of the parmameter.
		 * @param value
		 *            the parmameter.
		 */
		void setIntValue(String name, int value);

		/**
		 * Sets the value of an parameter *
		 * 
		 * @param name
		 *            of the parmameter.
		 * @param value
		 *            the parmameter.
		 */
		void setStringValue(String name, String value);
	}

	ActieDecodeRunnable(TaskRunnableDecodeMethods downloadTask) {
		mActieTask = downloadTask;
	}

	@Override
	public void run() {
		/*
		 * Stores the current Thread in the the PhotoTask instance, so that the
		 * instance can interrupt the Thread.
		 */

		mActieTask.setJSONDecodeThread(Thread.currentThread());

		JSONObject actieObject = mActieTask.getActieJSON();
		try {
			mActieTask.setIntValue("wijk_id", actieObject.getInt("wijk_id"));
			mActieTask.setIntValue("target", actieObject.getInt("target"));
			mActieTask.setIntValue("aantal_huishoudens",
					actieObject.getInt("aantal_huishoudens"));
			mActieTask.setStringValue("wijk_naam",
					actieObject.getString("wijk_naam"));

			mActieTask.handleDecodeState(DECODE_STATE_COMPLETED);

		} catch (JSONException e) {			
			mActieTask.handleDecodeState(DECODE_STATE_FAILED);
			e.printStackTrace();
		}
	}

}
