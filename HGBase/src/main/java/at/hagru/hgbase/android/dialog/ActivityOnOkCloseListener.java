package at.hagru.hgbase.android.dialog;

import android.app.Activity;

/**
 * A default implementation for the ok button of an activity that will close the activity.
 * 
 * @author hagru
 */
public class ActivityOnOkCloseListener extends AbstractActivityOnClickCloseListener {
	
	public ActivityOnOkCloseListener(Activity activity) {
		super(activity, Activity.RESULT_OK);
	}
}
