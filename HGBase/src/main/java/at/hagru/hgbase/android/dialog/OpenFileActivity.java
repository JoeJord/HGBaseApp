package at.hagru.hgbase.android.dialog;

import at.hagru.hgbase.R;

/**
 * Code taken from {@link http://www.codeproject.com/Tips/1019055/Open-File-Dialog-Save-File-Dialog} and adapted.
 * 
 * @author Rou1997, hagru
 */
public class OpenFileActivity extends AbstractFileActivity {

	private String selectedFilePath = null;
	private String selectedFileName = null;

	/**
	 * Create a open file activity.
	 */
	public OpenFileActivity() {
		super(R.layout.activity_open_file, R.string.file_open, R.id.OFA_LvList, R.id.OFA_BtnOK, R.id.OFA_BtnCancel);
	}	
	
	@Override
	protected String getResultFilePath() {
		return selectedFilePath;
	}

	@Override
	protected String getResultShortFileName() {
		return selectedFileName;
	}	
	
	@Override
	protected void onItemClickFile(String entryName) {
		selectedFilePath = currentPath + entryName;
		selectedFileName = entryName;
	}
}