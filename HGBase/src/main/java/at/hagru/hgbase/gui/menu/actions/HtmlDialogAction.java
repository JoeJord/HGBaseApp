package at.hagru.hgbase.gui.menu.actions;

import android.view.MenuItem;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.gui.HGBaseDialog;

/**
 * Shows the given HTML resource as dialog.
 * 
 * @author hagru
 */
public class HtmlDialogAction extends AbstractMenuAction {

	private int htmlResId;
	private String title;

	/**
	 * @param activity the activity to start the dialog from
	 * @param htmlResId the resource id of the HTML page
	 */
	public HtmlDialogAction(HGBaseActivity activity, int htmlResId, String title) {
		super(activity);
		this.htmlResId = htmlResId;
		this.title = title;
	}

	@Override
	public void perform(int id, MenuItem item) {
		HGBaseDialog.showHtmlDialog(getActivity(), getHtmlResId(), getTitle());
	}

	/**
	 * @return the resource id of the HTML page
	 */
	protected int getHtmlResId() {
		return htmlResId;
	}

	/**
	 * @return the title of the dialog
	 */
	protected String getTitle() {
		return title;
	}
}
