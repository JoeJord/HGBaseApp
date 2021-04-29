package at.hagru.hgbase.android.activity;

import at.hagru.hgbase.HGBaseActivity;

/**
 * A simple progress task with void parameters and result.
 * 
 * @author hagru
 */
public abstract class SimpleProgressAsyncTask extends ProgressAsyncTask<Void, Void, Void> {

	public SimpleProgressAsyncTask(HGBaseActivity activity) {
		super(activity);
	}
}
