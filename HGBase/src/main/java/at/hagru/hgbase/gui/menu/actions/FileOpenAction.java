package at.hagru.hgbase.gui.menu.actions;

import android.view.MenuItem;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.android.dialog.IFileSelectionListener;
import at.hagru.hgbase.gui.HGBaseDialog;

/**
 * Shows a dialog for opening a file.
 * 
 * @author hagru
 */
public class FileOpenAction extends AbstractFileDialogAction {

	/**
	 * @param activity the activity that calls the dialog
	 * @param fileListener the listener to handle the file open event
	 */
	public FileOpenAction(HGBaseActivity activity, IFileSelectionListener fileListener) {
		super(activity, fileListener);
	}

	@Override
	public void perform(int id, MenuItem item) {
		HGBaseDialog.showChooseFileDialog(getActivity(), getFileListener());
	}

}
