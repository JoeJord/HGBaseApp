package at.hagru.hgbase.android.dialog;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Base class for activity click listeners that will close the activity.
 * 
 * @author hagru
 */
public abstract class AbstractActivityOnClickCloseListener implements View.OnClickListener {
	
	private final Activity activity;
	private final int resultCode;

	/**
	 * Create the activity on click listener.
	 * 
	 * @param activity the activity where the click action happens
	 * @param resultCode the result code
	 */
	protected AbstractActivityOnClickCloseListener(Activity activity, int resultCode) {
		this.activity = activity;
		this.resultCode = resultCode;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		setResultParameters(v, intent);
		activity.setResult(resultCode, intent);
		activity.finish();
	}

	/**
	 * Allow subclasses to add result parameters.
	 * 
	 * @param view the view where the click happened
	 * @param intent the intent to add the parameters
	 */
	protected void setResultParameters(View view, Intent intent) {
		// NOCHECK: nothing to do in the base class
	}

}
