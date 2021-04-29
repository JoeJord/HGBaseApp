package at.hagru.hgbase.gui.menu.actions;

import android.view.MenuItem;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.gui.config.HGBaseConfigDialog;

/**
 * 
 * 
 * @author hagru
 */
public class StartConfigDialogAction extends StartActivityAction {

	/**
	 * @param callingActivity the activity that calls the configuration dialog
	 * @param dialogClass the configuration dialog class
	 */
	public StartConfigDialogAction(HGBaseActivity callingActivity, Class<? extends HGBaseConfigDialog> dialogClass) {
		this(callingActivity, dialogClass, -1);
	}

	/**
	 * @param callingActivity the activity that calls the configuration dialog
	 * @param dialogClass the configuration dialog class
	 * @param requestCode the request code to let the calling activity react on result
	 */
	public StartConfigDialogAction(HGBaseActivity callingActivity, Class<? extends HGBaseConfigDialog> dialogClass, int requestCode) {
		super(callingActivity, dialogClass, requestCode);
	}
	
	/**
	 * @return the class of the configuration dialog (activity)
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends HGBaseConfigDialog> getConfigDialogClass() {
		return (Class<? extends HGBaseConfigDialog>) getStartActivityClass();
	}

	@Override
	public void perform(int id, MenuItem item) {
		HGBaseConfigDialog.show(getActivity(), getConfigDialogClass(), getRequestCode());
	}

}
