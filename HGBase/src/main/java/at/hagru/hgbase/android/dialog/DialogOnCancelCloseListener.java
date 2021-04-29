package at.hagru.hgbase.android.dialog;

import android.content.DialogInterface;

/**
 * A default dialog on click listener that closes the dialog.
 * 
 * @author hagru
 */
public class DialogOnCancelCloseListener implements DialogInterface.OnCancelListener {

	@Override
	public void onCancel(DialogInterface dialog) {
		dialog.dismiss();
	}
}
