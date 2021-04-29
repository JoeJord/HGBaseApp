package at.hagru.hgbase.android.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import at.hagru.hgbase.R;
import at.hagru.hgbase.gui.HGBaseDialog;
import at.hagru.hgbase.lib.HGBaseLog;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Code taken from {@link http://www.codeproject.com/Tips/1019055/Open-File-Dialog-Save-File-Dialog} and adapted.<p>
 * The abstract class was introduced as base class for the open and save activities to reduce code duplication.
 * 
 * @author Rou1997, hagru
 */
public abstract class AbstractFileActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	public static final String RESULT_FILE_NAME = "fileName";
	public static final String RESULT_SHORT_FILE_NAME = "shortFileName";
	
	private static final String FOLDER_BACK = "..";
	private static final String INTERNAL_SD_CARD = "sdcard0";
	private static final String EXTERNAL_SD_CARD = "extSdCard";

	private final Map<String,String> mapStorage = new HashMap<>();
	
	protected ListView LvList;
	protected ArrayList<String> listItems = new ArrayList<String>();
	protected ArrayAdapter<String> adapter;
	protected Button BtnOK;
	protected Button BtnCancel;
	protected String currentPath = "";
	
	private final int layoutId;
	private final int titleId;
	private int listId;
	private int btnOkId;
	private int btnCancelId;
	
	protected AbstractFileActivity(int layoutId, int titleId, int listId, int btnOkId, int btnCancelId) {
		this.layoutId = layoutId;
		this.titleId = titleId;
		this.listId = listId;
		this.btnOkId = btnOkId;
		this.btnCancelId = btnCancelId;
		initStorageMap();
	}

	/**
	 * By default it is not easy to access internal and external sd card. 
	 * As the path also depends on Android version, I need extra work to receive them.
	 */
	private void initStorageMap() {
		String internal = System.getenv("EXTERNAL_STORAGE");
		if (HGBaseTools.hasContent(internal)) {
			mapStorage.put(INTERNAL_SD_CARD, internal);
		}
		try {
			String external = System.getenv("SECONDARY_STORAGE");
			if (HGBaseTools.hasContent(internal)) {
				String externalSdCard = external.split(":")[0];
				if (HGBaseTools.hasContent(externalSdCard)) {
					mapStorage.put(EXTERNAL_SD_CARD, externalSdCard);
				}
			}
		} catch (Exception ex) {
			// NOCHECK: nothing to do, mobile has no external storage 
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layoutId);
		try {
			LvList = (ListView) findViewById(listId);
			BtnOK = (Button) findViewById(btnOkId);
			BtnCancel = (Button) findViewById(btnCancelId);
			setCurrentPath(getBaseDirs());
			LvList.setOnItemClickListener(this);
			BtnOK.setOnClickListener(this);
			BtnCancel.setOnClickListener(this);
		} catch (Exception ex) {
			Toast.makeText(this, "Error in AbstractFileActivity.onCreate: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		LvList.requestFocus();
	}
	
	/**
	 * @param name the file name to test
	 * @return true if the file name is invalid
	 */
	protected boolean isValidFileName(String name) {
		return (HGBaseTools.hasContent(name) && name.charAt(0) != '.' && name.charAt(0) != '~');
	}
	
	/**
	 * @return the base directory for file selection.
	 */
	private String[] getBaseDirs() {
		//return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
		//return Environment.getExternalStorageDirectory().getParent() + "/";
		return mapStorage.keySet().toArray(new String[mapStorage.size()]);
	}
	
	/**
	 * Tests if the given name is a base directory.
	 * 
	 * @param dirName the directory name to check
	 * @return true if this is one if the base directory names
	 */
	protected boolean isBaseDir(String dirName) {
		return !HGBaseTools.hasContent(dirName) || mapStorage.containsKey(dirName);
	}
	
	/**
	 * As the base dir is just a key in the storage map, it is necessary to correct the path name when working with it.
	 * 
	 * @param dirName the name of the directory with the short base dir
	 * @return the real directory name
	 */
	private String correctPathName(String dirName) {
		HGBaseLog.logDebug("correctPathName dirName="+dirName);//FIXME
		String baseDir = dirName.split("/")[0];
		if (mapStorage.containsKey(baseDir)) {
			return dirName.replaceFirst(baseDir, mapStorage.get(baseDir));
		} else {
			return dirName;
		}
	}

	protected void setCurrentPath(String ...paths) {
		ArrayList<String> folders = new ArrayList<String>();
		ArrayList<String> files = new ArrayList<String>();
		currentPath = paths[0];
		if (paths.length > 1) {
			// show the virtual base directories
			currentPath = "";
			for (String entryName : paths) {
				folders.add(entryName + "/");				
			}
		} else {
			folders.add(FOLDER_BACK);
			File[] allEntries = new File(correctPathName(currentPath)).listFiles();
			for (int i = 0; i < allEntries.length; i++) {
				String entryName = allEntries[i].getName();
				if (isValidFileName(entryName)) {
					if (allEntries[i].isDirectory()) {
						folders.add(entryName + "/");
					} else if (allEntries[i].isFile()) {
						files.add(entryName);
					}
				}
			}
		}
		Collections.sort(folders, new StringIgnoreCaseComparator());
		Collections.sort(files, new StringIgnoreCaseComparator());
		listItems.clear();
		listItems.addAll(folders);
		listItems.addAll(files);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		adapter.notifyDataSetChanged();
		LvList.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		if (HGBaseTools.hasContent(currentPath)) {
			if (currentPath.endsWith("/")) {
				currentPath = currentPath.substring(0, currentPath.length() - 1);
			}
			if (mapStorage.containsKey(currentPath) || mapStorage.containsValue(currentPath)) {
				setCurrentPath(getBaseDirs());
			} else {
				String parent = new File(correctPathName(currentPath)).getParent();
				setCurrentPath(parent + "/");
			}
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.OFA_BtnOK || v.getId() == R.id.SFA_BtnOK) {
			if (isValidFileName(getResultShortFileName())) {
				Intent intent = new Intent();
				intent.putExtra(RESULT_FILE_NAME, correctPathName(getResultFilePath()));
				intent.putExtra(RESULT_SHORT_FILE_NAME, getResultShortFileName());
				setResult(RESULT_OK, intent);
				this.finish();
			} else {
				HGBaseDialog.showErrorDialog(this, R.string.filename_invalid);
			}
		} else if (v.getId() == R.id.OFA_BtnCancel || v.getId() == R.id.SFA_BtnCancel) {
			Intent intent = new Intent();
			intent.putExtra(RESULT_FILE_NAME, "");
			intent.putExtra(RESULT_SHORT_FILE_NAME, "");
			setResult(RESULT_CANCELED, intent);
			this.finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			String entryName = (String) parent.getItemAtPosition(position);
			if (entryName.equals(FOLDER_BACK)) {
				onBackPressed();
			} else if (entryName.endsWith("/")) {
				if (!HGBaseTools.hasContent(currentPath) || currentPath.endsWith("/")) {
					setCurrentPath(currentPath + entryName);
				} else {
					setCurrentPath(currentPath + "/" + entryName);
				}
				// setCurrentPath(currentPath + entryName); did not work (any more)
			} else {
				this.setTitle(HGBaseText.getText(titleId) + " [" + entryName + "]");			
				onItemClickFile(entryName);
			}
		} catch (Exception ex) {
			HGBaseDialog.printError(ex, this);
		}			
	}
	
	/**
	 * Compare string by ignoring case sensitivity.
	 */
	protected final class StringIgnoreCaseComparator implements Comparator<String> {
		@Override
		public int compare(String s1, String s2) {
			return s1.compareToIgnoreCase(s2);
		}
	}	
	
	/**
	 * @return the file name that will taken as file name for the result
	 */
	abstract protected String getResultFilePath();

	/**
	 * @return the file name that will taken as short file name for the result
	 */
	abstract protected String getResultShortFileName();

	/**
	 * Do the action when a file was clicked.
	 * 
	 * @param entryName the name of the file
	 */
	abstract protected void onItemClickFile(String entryName);
}