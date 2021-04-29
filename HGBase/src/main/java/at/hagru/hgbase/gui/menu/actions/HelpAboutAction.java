package at.hagru.hgbase.gui.menu.actions;

import android.view.MenuItem;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.gui.HGBaseAboutDlg;

/**
 * Shows the about dialog for the app.
 * 
 * @author hagru
 */
public class HelpAboutAction extends AbstractMenuAction {

	/**
	 * @param activity the main activity that registers the action
	 */
	public HelpAboutAction(HGBaseActivity activity) {
		super(activity);
	}

	@Override
	public void perform(int id, MenuItem item) {
		HGBaseAboutDlg.show(getActivity());
	}
}
