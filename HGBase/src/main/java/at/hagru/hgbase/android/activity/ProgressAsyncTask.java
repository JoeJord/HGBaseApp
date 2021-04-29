package at.hagru.hgbase.android.activity;

import android.os.AsyncTask;

import at.hagru.hgbase.HGBaseActivity;

/**
 * An asynchronous task that starts and stops the progress circle automatically.
 * 
 * @author hagru
 */
public abstract class ProgressAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	
	private HGBaseActivity activity;

	public ProgressAsyncTask(HGBaseActivity activity) {
		super();
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		activity.setCursorWait();
	}
	
	@Override
	protected void onPostExecute(Result result) {
		activity.setCursorDefault();
	}	
	
	/**
	 * @return the activity corresponding to this task
	 */
	protected HGBaseActivity getActivity() {
		return activity;
	}

}