package at.hagru.hgbase.android.dialog;

/**
 * A file selection listener that does nothing. 
 * 
 * @author hagru
 */
public class FileSelectionAdapter implements IFileSelectionListener {

	/**
	 * Create a new selection adapter.
	 */
	public FileSelectionAdapter() {
		super();
	}

	@Override
	public void onFileSelected(String fileName, String shortFileName) {
		// NOCHECK: do nothing
	}

	@Override
	public void onCancelSelection() {
		// NOCHECK: do nothing
	}
}
