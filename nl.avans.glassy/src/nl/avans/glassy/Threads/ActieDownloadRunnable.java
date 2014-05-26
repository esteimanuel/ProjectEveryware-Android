package nl.avans.glassy.Threads;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

public class ActieDownloadRunnable extends ApiCommunicator {
    // Sets a tag for this class
    @SuppressWarnings("unused")
    private static final String LOG_TAG = "ActieDownloadRunnable";
    
    // Constants for indicating the state of the download
    static final int HTTP_STATE_FAILED = -1;
    static final int HTTP_STATE_STARTED = 0;
    static final int HTTP_STATE_COMPLETED = 1;
	
    // Defines a field that contains the calling object of type ActieTask.
    final TaskRunnableDownloadMethods mActieTask;
    
    /**
     *
     * An interface that defines methods that ActieTask implements. An instance of
     * ActieTask passes itself to an ActieDownloadRunnable instance through the
     * ActieDownloadRunnable constructor, after which the two instances can access each other's
     * variables.
     */
    interface TaskRunnableDownloadMethods {
        
        /**
         * Defines the actions for each state of the PhotoTask instance.
         * @param state The current state of the task
         */
        void handleDownloadState(JSONObject result, int state);
        
        /**
         * Gets the wijkID for the wijk being downloaded
         * @return The wijk id
         */
        int getWijkID();
    }

	public ActieDownloadRunnable(TaskRunnableDownloadMethods actieTask) {
		super(null);
		mActieTask = actieTask;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		// On download complete pass result to correct state
		mActieTask.handleDownloadState(result, HTTP_STATE_COMPLETED);
	}

}
