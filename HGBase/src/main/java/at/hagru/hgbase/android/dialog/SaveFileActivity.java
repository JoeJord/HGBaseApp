package at.hagru.hgbase.android.dialog;

import android.os.Bundle;
import android.widget.EditText;

import at.hagru.hgbase.R;

/**
 * Code taken from {@link http://www.codeproject.com/Tips/1019055/Open-File-Dialog-Save-File-Dialog} and adapted.
 * 
 * @author Rou1997, hagru
 */
public class SaveFileActivity extends AbstractFileActivity {

	private EditText TxtFileName;
	
	/**
	 * Create a save file activity.
	 */
	public SaveFileActivity() {
		super(R.layout.activity_save_file, R.string.file_save, R.id.SFA_LvList, R.id.SFA_BtnOK, R.id.SFA_BtnCancel);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TxtFileName = (EditText) findViewById(R.id.SFA_TxtFileName);
		// the first directory is in the base dir, so do not allow adding a file name
		TxtFileName.setEnabled(false);
	}
	
	@Override
	protected void setCurrentPath(String... paths) {
		super.setCurrentPath(paths);
		if (TxtFileName != null) {
			TxtFileName.setEnabled(!isBaseDir(currentPath));
		}
	}
	
	@Override
	protected String getResultFilePath() {
		return currentPath + TxtFileName.getText().toString();
	}

	@Override
	protected String getResultShortFileName() {
		return TxtFileName.getText().toString();
	}
	
	@Override
	protected void onItemClickFile(String entryName) {
		TxtFileName.setText(entryName);
	}
}