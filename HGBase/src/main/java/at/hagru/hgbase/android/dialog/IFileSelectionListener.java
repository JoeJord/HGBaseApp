package at.hagru.hgbase.android.dialog;

/**
 * Listens to file selections (in an open or save dialog).
 * 
 * @author hagru
 */
public interface IFileSelectionListener {
	
	/**
	 * Called when a file was selected.
	 * 
	 * @param fileName the file name with the full path
	 * @param shortFileName the short file name without path
	 */
	public void onFileSelected(String fileName, String shortFileName);
	
	/**
	 * Called when the selection of a file was cancelled.
	 */
	public void onCancelSelection();

}
