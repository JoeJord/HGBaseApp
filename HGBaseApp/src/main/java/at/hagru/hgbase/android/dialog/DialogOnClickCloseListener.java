package at.hagru.hgbase.android.dialog;

import android.content.DialogInterface;

/**
 * A default dialog on click listener that closes the dialog.
 * 
 * @author hagru
 */
public class DialogOnClickCloseListener implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	}
}
