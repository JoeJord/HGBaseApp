package at.hagru.hgbase.android.dialog;

import android.app.Activity;

/**
 * A default implementation for the cancel button of an activity that will close the activity.
 * 
 * @author hagru
 */
public class ActivityOnCancelCloseListener extends AbstractActivityOnClickCloseListener {
	
	public ActivityOnCancelCloseListener(Activity activity) {
		super(activity, Activity.RESULT_CANCELED);
	}
}
