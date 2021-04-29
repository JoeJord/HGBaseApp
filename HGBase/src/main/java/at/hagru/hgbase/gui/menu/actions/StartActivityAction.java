package at.hagru.hgbase.gui.menu.actions;

import android.app.Activity;
import android.view.MenuItem;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Starts the given activity as menu action.
 * 
 * @author hagru
 */
public class StartActivityAction extends AbstractMenuAction {

	private final Class<? extends Activity> activityClass;
	private final int requestCode;

	/**
	 * @param activity the activity to start the given activity from
	 * @param activity the class of the Activity to start
	 */
	public StartActivityAction(HGBaseActivity activity, Class<? extends Activity> activityClass) {
		this(activity, activityClass, HGBaseTools.INVALID_INT);
	}

	/**
	 * @param activity the activity to start the given activity from
	 * @param activity the class of the Activity to start
	 * @param requestCode the request code to start the activity for result
	 */
	public StartActivityAction(HGBaseActivity activity, Class<? extends Activity> activityClass, int requestCode) {
		super(activity);
		this.activityClass = activityClass;
		this.requestCode = requestCode;
	}
	
	/**
	 * @return the class of the activity to start
	 */
	public Class<? extends Activity> getStartActivityClass() {
		return activityClass;
	}

	/**
	 * @return the request code, may be invalid
	 */
	public int getRequestCode() {
		return requestCode;
	}

	@Override
	public void perform(int id, MenuItem item) {
		if (HGBaseTools.isValid(getRequestCode())) {
			HGBaseAppTools.startActivityForResult(getActivity(), getStartActivityClass(), getRequestCode());
		} else {
			HGBaseAppTools.startActivity(getActivity(), getStartActivityClass());
		}
	}
}
