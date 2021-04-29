package at.hagru.hgbase.gui.menu.actions;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.android.dialog.IFileSelectionListener;

/**
 * An abstract action for calling file dialogs.
 * 
 * @author hagru
 */
public abstract class AbstractFileDialogAction extends AbstractMenuAction {

	private final IFileSelectionListener fileListener;

	/**
	 * @param activity the activity that calls the dialog
	 * @param fileListener the listener to handle the file selection event
	 */
	public AbstractFileDialogAction(HGBaseActivity activity, IFileSelectionListener fileListener) {
		super(activity);
		this.fileListener = fileListener;
	}

	/**
	 * @return the listener to handle the ok event
	 */
	public IFileSelectionListener getFileListener() {
		return fileListener;
	}

}
