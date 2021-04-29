package at.hagru.hgbase;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import at.hagru.hgbase.android.APPTYPE;
import at.hagru.hgbase.android.HGBaseAdvertisements;
import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;
import at.hagru.hgbase.android.dialog.AbstractFileActivity;
import at.hagru.hgbase.android.dialog.IFileSelectionListener;
import at.hagru.hgbase.gui.HGBaseDialog;
import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.gui.HGBaseStatusBar;
import at.hagru.hgbase.gui.ProgressState;
import at.hagru.hgbase.gui.config.HGBaseConfigMenuAction;
import at.hagru.hgbase.gui.menu.IMenuAction;
import at.hagru.hgbase.gui.menu.actions.HelpAboutAction;
import at.hagru.hgbase.gui.menu.actions.HtmlDialogAction;
import at.hagru.hgbase.gui.menu.actions.ShareScreenshotAction;
import at.hagru.hgbase.gui.menu.actions.StartActivityAction;
import at.hagru.hgbase.lib.HGBaseConfig;
import at.hagru.hgbase.lib.HGBaseLog;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * The main activity has to inherit from this activity in order to make all
 * library functions work.
 * 
 * @author hagru
 */
public abstract class HGBaseActivity extends FragmentActivity {

        /**
         * Possible fullscreen modes.
         */
        protected enum FullscreenMode {
            LEAN_BACK, IMMERSIVE, STICKY_IMMERSIVE;
            
            public int getVisibilityFlag() {
        	int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        	switch (this) {
        	    case IMMERSIVE:
        		flag |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        		break;
        	    case STICKY_IMMERSIVE:
        		flag |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        		break;
        	    default:
        		break;
        	}
        	return flag;
            }
        }

	public static final int FILE_OPEN_PERMISSION_REQUEST = 67;
	public static final int FILE_SAVE_PERMISSION_REQUEST = 72;
	public static final int OPEN_FILE_RESULT = 6736;
	public static final int SAVE_FILE_RESULT = 7283;
	
	public static final int WRITE_STORAGE_SHARE_SCREENSHOT_PERMISSION_REQUEST = 82;
	

	public static final String MENU_ID_HELP = "action_help";
	public static final String MENU_ID_ABOUT = "action_about";
	public static final String MENU_ID_PREFERENCES = "action_preferences";

	private static final String HTML_HELP_RAW_ID = "help";
	private static final int NAVIGATION_DRAWER_WIDTH = 500;

	private static HGBaseActivity instance; // hold the instance of this
											// activity

	private final int activityLayoutId;
	private final int optionsMenuId;
	private Map<Integer, IMenuAction> menuActionMap = new LinkedHashMap<>();
	private Map<Integer, MenuItem> menuItemMap = new LinkedHashMap<>();
	private IFileSelectionListener fileSelectionlistener;

	private boolean permanentStatusBar;
	private static HGBaseStatusBar statusBar;
	private static ProgressDialog waitDialog;
	private static boolean changed = false;

	final private Handler threadHandler = new Handler();
	/**
	 * The map with the additional information for the menu items.
	 */
	private HashMap<Integer, AddMenuItemInfo> addMenuItemInfo;

	/**
	 * Create a new main activity.
	 */
	public HGBaseActivity() {
		this(HGBaseTools.INVALID_INT);
	}

	public HGBaseActivity(int activityLayoutId) {
		this(activityLayoutId, HGBaseTools.INVALID_INT);
	}

	public HGBaseActivity(int activityLayoutId, int optionsMenuId) {
		this.activityLayoutId = activityLayoutId;
		this.optionsMenuId = optionsMenuId;
		Thread.setDefaultUncaughtExceptionHandler(new ExitOnUncaughtExceptionWithClipboard());
		addMenuItemInfo = null;
	}

	/**
	 * @return the current instance of the activity
	 */
	public static HGBaseActivity getInstance() {
		return instance;
	}

	/**
	 * @return the ID of the activity layout
	 */
	public int getActivityLayoutId() {
		return activityLayoutId;
	}

	/**
	 * @return the ID of the options menu
	 */
	public int getOptionsMenuId() {
		return optionsMenuId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		HGBaseAppTools.setContext(getApplicationContext());
		if (optionsMenuId > 0) {
			getWindow().requestFeature(Window.FEATURE_OPTIONS_PANEL);
		}
		if (activityLayoutId > 0) {
			setContentView(activityLayoutId);
		}
		setDefaultPreferences();
		registerDefaultOptionsMenuActions();
		registerOptionsMenuActions();
		if ((!isProVersion()) && (HGBaseTools.hasContent(getAdvertisementURL()))) {
		    HGBaseAdvertisements.initWebView(this);
		}
		//HGBaseLog.setLogFile(this, "hgbase.txt"); //FIXME
	}

	@Override
	protected void onResume() {
		super.onResume();
		HGBaseAppTools.setContext(getApplicationContext());
	}

	@Override
	public void onBackPressed() {
		if (HGBaseAdvertisements.isShowing()) {
			HGBaseAdvertisements.hideAdvertisementDialog(this);
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (checkIfPermissionGranted(HGBaseLog.FILE_LOG_PERMISSION_REQUEST, Manifest.permission.WRITE_EXTERNAL_STORAGE, 
									 requestCode, permissions, grantResults)) {
			HGBaseLog.createLogFile();			
		} else if (checkIfPermissionGranted(FILE_OPEN_PERMISSION_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE, 
											requestCode, permissions, grantResults)) {
			HGBaseDialog.showChooseFileDialog(this, fileSelectionlistener);
		} else if (checkIfPermissionGranted(FILE_SAVE_PERMISSION_REQUEST, Manifest.permission.WRITE_EXTERNAL_STORAGE, 
											requestCode, permissions, grantResults)) {
			HGBaseDialog.showSaveFileDialog(this, fileSelectionlistener);			
		} else if (checkIfPermissionGranted(WRITE_STORAGE_SHARE_SCREENSHOT_PERMISSION_REQUEST, Manifest.permission.WRITE_EXTERNAL_STORAGE, 
											requestCode, permissions, grantResults)) {
			for (Map.Entry<Integer, IMenuAction> action : menuActionMap.entrySet()) {
				if (action.getValue() instanceof ShareScreenshotAction) {
					action.getValue().perform(action.getKey(), null);
				}
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	/**
	 * Checks if the given permission for the given request code is currently granted.
	 * 
	 * @param checkCode the request code to check
	 * @param checkPermission the permission code to check
	 * @param requestCode the currently passed request code
	 * @param permissions the currently passed permissions
	 * @param grantResults the currently passed grant results
	 * @return true if the permission is granted now, otherwise false
	 */
	protected boolean checkIfPermissionGranted(int checkCode, String checkPermission, int requestCode, String[] permissions, int[] grantResults) {
		return (requestCode == checkCode && isPermissionGranted(checkPermission, permissions, grantResults));
	}
	
	
	/**
	 * Tests if the given permission is granted when reacting on permission request.
	 * 
	 * @param testPermission the permission to test
	 * @param permissions an array with permission got by {@link #onRequestPermissionsResult(int, String[], int[])}
	 * @param grantResults an array with grant results got by {@link #onRequestPermissionsResult(int, String[], int[])}
	 * @return
	 */
	protected boolean isPermissionGranted(String testPermission, String[] permissions, int[] grantResults) {
		for (int i = 0; i < permissions.length; i++) {
			if (permissions[i].equals(testPermission) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the default preferences from the {@code /xml/preferences.xml} file,
	 * if available. The default preferences are also considered by
	 * {@link HGBaseConfig}.
	 */
	protected void setDefaultPreferences() {
		int prefFileId = HGBaseResources.getResourceIdByName("preferences", HGBaseResources.XML);
		if (prefFileId != 0) {
			HGBaseConfig.setDefaultValuesFromPreferencesFile(prefFileId);
		}
	}

	/**
	 * Register default menu actions if accord menu names are available.
	 */
	protected void registerDefaultOptionsMenuActions() {
		if (HGBaseTools.isValid(optionsMenuId) && optionsMenuId != 0) {
			int helpId = HGBaseResources.getResourceIdByName(MENU_ID_HELP, HGBaseResources.ID);
			if (helpId != 0) {
				int htmlResId = HGBaseResources.getResourceIdByName(HTML_HELP_RAW_ID, HGBaseResources.RAW);
				if (htmlResId != 0) {
					String title = HGBaseText.getText(MENU_ID_HELP);
					registerOptionsMenuAction(helpId, new HtmlDialogAction(this, htmlResId, title));
				}
			}
			int aboutId = HGBaseResources.getResourceIdByName(MENU_ID_ABOUT, HGBaseResources.ID);
			if (aboutId != 0) {
				registerOptionsMenuAction(aboutId, new HelpAboutAction(this));
			}
			int prefsId = HGBaseResources.getResourceIdByName(MENU_ID_PREFERENCES, HGBaseResources.ID);
			if (prefsId != 0) {
				registerOptionsMenuAction(prefsId, new StartActivityAction(this, HGBasePreferenceActivity.class));
			}
		}
	}

	/**
	 * Register all menu actions using the method
	 * {@link #registerOptionsMenuAction(int, IMenuAction)}.
	 * <p>
	 * By registering actions this way, it is not necessary to override the
	 * {@link #onOptionsItemSelected(MenuItem)} method.
	 */
	abstract protected void registerOptionsMenuActions();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (HGBaseTools.isValid(optionsMenuId) && optionsMenuId != 0) {
			getMenuInflater().inflate(optionsMenuId, menu);
			addMenuItemInfo = parseAdditionalMenuItemInformation(optionsMenuId, menu);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Parses the specified menu resource for additional information about the
	 * menu items.
	 *
	 * @param menuRes
	 *            The resource ID for a menu.
	 * @param menu
	 *            The menu.
	 * @return A map with the additional information for the menu items.
	 */
	private HashMap<Integer, AddMenuItemInfo> parseAdditionalMenuItemInformation(int menuRes, Menu menu) {
		HashMap<Integer, AddMenuItemInfo> addInfo = new HashMap<>();
		XmlResourceParser parser = getResources().getXml(menuRes);
		try {
			int eventType = parser.next();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String id = null;
					String text = null;
					for (int attrIndex = 0; attrIndex < parser.getAttributeCount(); attrIndex++) {
						String attrName = parser.getAttributeName(attrIndex);
						if ("id".equals(attrName)) {
							id = parser.getAttributeValue(attrIndex);
						} else if ("text".equals(attrName)) {
							text = parser.getAttributeValue(attrIndex);
						}
					}
					if ((HGBaseTools.hasContent(id)) && (HGBaseTools.hasContent(text))) {
						MenuItem menuItem = menu.findItem(HGBaseTools.toInt(id.substring(1)));
						if (menuItem != null) {
							HashMap<String, String> keyValueList = HGBaseTools.parseKeyValueList(text, ";", "=");
							addInfo.put(menuItem.getItemId(),
									new AddMenuItemInfo(APPTYPE.valueOfIgnoreCase(keyValueList.get("version"))));
						}
					}
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
		return addInfo;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean result = super.onPrepareOptionsMenu(menu);
		menuItemMap.clear();
		for (int i = 0; i < menu.size(); i++) {
			MenuItem item = menu.getItem(i);
			int itemId = item.getItemId();
			menuItemMap.put(itemId, item);
			IMenuAction action = getOptionsMenuAction(itemId);
			if (action instanceof HGBaseConfigMenuAction) {
				((HGBaseConfigMenuAction) action).setIconByConfiguration();
			}
			AddMenuItemInfo addInfo = addMenuItemInfo.get(itemId);
			if (addInfo != null) {
				boolean available = APPTYPE.functionAvailable(isProVersion(), addInfo.appType);
				item.setEnabled(available);
				item.setVisible(available);
				if (!available) {
					// remove the menu item if not available to hide it in the navigation drawer
					menuItemMap.remove(itemId);
				}
			}
		}
		createNavigationDrawer();
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		IMenuAction action = getOptionsMenuAction(id);
		if (action == null) {
			return super.onOptionsItemSelected(item);
		} else {
			action.perform(id, item);
			return true;
		}
	}

	/**
	 * Register a menu action.
	 * <p>
	 * By registering actions this way, it is not necessary to override the
	 * {@link #onOptionsItemSelected(MenuItem)} method.
	 * 
	 * @param id
	 *            the menu item id
	 * @param action
	 *            the action to be performed
	 */
	public final void registerOptionsMenuAction(int id, IMenuAction action) {
		menuActionMap.put(id, action);
	}

	/**
	 * Returns the action for the given menu item id or null if no action was
	 * registered for this id.
	 *
	 * @param id
	 *            id of menu item
	 * @return the action or null
	 */
	public final IMenuAction getOptionsMenuAction(int id) {
		return menuActionMap.get(id);
	}

	/**
	 * Returns the menu item for the given menu item id or null if no menu item
	 * exists with this id.
	 * 
	 * @param id
	 *            id of menu item
	 * @return the menu item or null
	 */
	public final MenuItem getOptionsMenuItem(int id) {
		return menuItemMap.get(id);
	}

	/**
	 * Apply a navigation drawer by using the first child view of the root and the menu actions for the drawer list. 
	 * If the root view has no children or no actions are defined, no navigation drawer will be created.
	 */
	protected void createNavigationDrawer() {
		ViewGroup rootView = getRootView();
		View firstChild = (rootView != null && rootView.getChildCount() >= 1) ? rootView.getChildAt(0) : null;
		if (rootView != null && firstChild != null && !(firstChild instanceof DrawerLayout)) {
			DrawerLayout navigationDrawer = new DrawerLayout(this);
			ListView drawerList = createNavigationDrawerList(navigationDrawer, menuItemMap);
			if (drawerList != null) {
				HGBaseGuiTools.removeViewFromParent(firstChild);
				navigationDrawer.addView(firstChild);
				rootView.addView(navigationDrawer, 0);
				drawerList.bringToFront();
				rootView.requestLayout();
				addNavigationDrawerSupportForHomeButton(navigationDrawer, drawerList);
			}
		}
	}

	/**
	 * Opens and closes the navigation drawer by clicking the home button (app icon).
	 * 
	 * @param navDrawer the navigation drawer
	 * @param drawerList the mneu list
	 */
	protected void addNavigationDrawerSupportForHomeButton(final DrawerLayout navDrawer, final ListView drawerList) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null && actionBar.isShowing()) {
			actionBar.setHomeButtonEnabled(true);
			View homeButton = findViewById(android.R.id.home);
			if (homeButton != null) {
				homeButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (navDrawer.isDrawerOpen(drawerList)) {
							navDrawer.closeDrawer(drawerList);
						} else {
							navDrawer.openDrawer(drawerList);
						}
					}
				});
			}
		}
	}

	/**
	 * @return the width of the navigation drawer menu
	 */
	protected int getNavigationDrawerListWidth() {
		return NAVIGATION_DRAWER_WIDTH;
	}

	/**
	 * @return the background color of the navigation drawer menu
	 */
	protected int getNavigationDrawerListColor() {
		return Color.WHITE;
	}

	/**
	 * Returns the list menu for the navigation drawer or null if no menu
	 * actions are defined.
	 * 
	 * @param navDrawer the navigation drawer that holds the list menu
	 * @param itemMap the map with menu items
	 * @return the list menu or null
	 */
	protected ListView createNavigationDrawerList(final DrawerLayout navDrawer, final Map<Integer, MenuItem> itemMap) {
		if (itemMap.size() > 0) {
			final ListView drawerList = new ListView(this);
			drawerList.setBackgroundColor(getNavigationDrawerListColor());
			List<String> menuTextList = new ArrayList<>();
			for (MenuItem item : itemMap.values()) {
				menuTextList.add(item.getTitle().toString());
			}
			drawerList.setAdapter(HGBaseGuiTools.createListAdapter(this, menuTextList.toArray(new String[menuTextList.size()])));
			drawerList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (position >= 0 && position < itemMap.size()) {
						MenuItem item = itemMap.values().toArray(new MenuItem[itemMap.size()])[position];
						IMenuAction action = getOptionsMenuAction(item.getItemId());
						if (action != null) {
							action.perform(item.getItemId(), item);
							navDrawer.closeDrawer(drawerList);
						}
					}
				}
			});
			DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(getNavigationDrawerListWidth(), 
																		 LayoutParams.MATCH_PARENT, Gravity.START);
			lp.topMargin = HGBaseAppTools.getTitleBarHeight(this);
			if (lp.topMargin == 0) {
				lp.topMargin = 20; // don't know why the first item is partially hidden otherwise
			}
			navDrawer.addView(drawerList, lp);
			return drawerList;
		} else {
			return null;
		}
	}

	/**
	 * @return the root/decor view of the window or null if activity is not
	 *         visible
	 */
	public ViewGroup getRootView() {
		Window rootWindow = getWindow();
		return (rootWindow == null) ? null : (ViewGroup) rootWindow.getDecorView();
	}

	/**
	 * Returns the content view.
	 * 
	 * @return the content view or null
	 */
	public View getContentView() {
		View rootView = getRootView();
		return (rootView == null) ? null : rootView.findViewById(android.R.id.content);
	}

	/**
	 * @return the handler for UI threads
	 */
	public Handler getThreadHandler() {
		return threadHandler;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (fileSelectionlistener != null && (requestCode == OPEN_FILE_RESULT || requestCode == SAVE_FILE_RESULT)) {
			if (resultCode == RESULT_OK) {
				String fileName = data.getExtras().getString(AbstractFileActivity.RESULT_FILE_NAME);
				String shortFileName = data.getExtras().getString(AbstractFileActivity.RESULT_SHORT_FILE_NAME);
				fileSelectionlistener.onFileSelected(fileName, shortFileName);
			} else {
				fileSelectionlistener.onCancelSelection();
			}
			fileSelectionlistener = null;
		}
	}

	/**
	 * Sets a (temporary) file selection listener. This is used to handle result
	 * from file selection activities. The listener is automatically removed
	 * when the result was handled.
	 * 
	 * @param fileListener
	 *            the file listener to handle one file selection event, may be
	 *            null to reset
	 */
	public void setFileSelectionListener(IFileSelectionListener fileListener) {
		this.fileSelectionlistener = fileListener;
	}

	/**
	 * Shows or hides the action bar if there exists one.
	 * 
	 * @param show
	 *            true to show the action bar, false to hide
	 */
	public void setActionBarVisible(boolean show) {
		ActionBar bar = getActionBar();
		if (bar != null) {
			if (show) {
				bar.show();
			} else {
				bar.hide();
			}
		}
	}

	/**
	 * Sets whether the status bar at the bottom of the activity shall be shown
	 * permanently or only when a status text is set.
	 * 
	 * @param permanent
	 *            true to show the status bar permanently, false to show it only
	 *            on demand if a status text is set
	 */
	public void setStatusBar(boolean permanent) {
		permanentStatusBar = permanent;
		if (permanent) {
			showStatusBar();
		} else {
			hideStatusBar();
		}
	}

	/**
	 * @return the first layout that can be taken for the status bar, this
	 *         should be a linear layout
	 */
	private ViewGroup getLayoutForStatusBar() {
		View rootView = getRootView();
		View contentView = getContentView();
		//logViewHierarchy(rootView, "");
		//logViewHierarchy(contentView, "");
		rootView = (contentView == null) ? rootView : contentView;
		View firstLayout = HGBaseGuiTools.findFirstViewOfType(rootView, new HGBaseGuiTools.ClassTypeChecker<View>() {

			@Override
			public boolean isCorrectType(Class<?> classType, View checkObject) {
				if (classType.equals(LinearLayout.class)) {
					LinearLayout layout = (LinearLayout) checkObject;
					// try to find a suitable linear layout view
					if (layout.getOrientation() == LinearLayout.VERTICAL && layout.isShown()
							&& !HGBaseGuiTools.hasViewAnyParentOfType(layout, "ActionBarView")) {
						return true;
					}
				}
				return false;
			}
		});
		if (firstLayout == null) {
			firstLayout = HGBaseGuiTools.findFirstViewOfType(rootView, FrameLayout.class);
		}
		return (firstLayout == null) ? null : (ViewGroup) firstLayout;
		// ViewGroup rootView = getRootView();
		// return (ViewGroup) rootView.getChildAt(0);
		// return (ViewGroup) getRootLayout().getParent();
	}

	/**
	 * Debug method to log the hierarchy of a given view.
	 * 
	 * @param v the view to log the hierarchy for
	 * @param span the span for recursive call, should be an empty string at start 
	 */
	protected void logViewHierarchy(View v, String span) {
		HGBaseLog.logDebug(span+"- "+v+", shown="+v.isShown());
		if (v instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) v;
			for (int i=0; i<vg.getChildCount();i++) {
				logViewHierarchy(vg.getChildAt(i), span+"  ");
			}
		}
	}

	/**
	 * Shows a status bar at the bottom of the screen.
	 */
	private void showStatusBar() {
		setStatusBar(new HGBaseStatusBar(this));
	}

	/**
	 * Hides the status bar at the bottom of the screen if it was visible
	 */
	private void hideStatusBar() {
		if (statusBar != null) {
			HGBaseGuiTools.removeViewFromParent(statusBar);
			statusBar = null;
		}
	}

	/**
	 * Sets a status panel, can be null.
	 *
	 * @param s
	 *            new status panel to set.
	 */
	public void setStatusBar(HGBaseStatusBar s) {
		if (statusBar != null) {
			hideStatusBar();
		}
		statusBar = s;
		if (statusBar != null) {
			ViewGroup firstLayout = getLayoutForStatusBar();
			if (firstLayout != null) {
				if (firstLayout instanceof FrameLayout) {
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(statusBar.getLayoutParams());
					lp.gravity = Gravity.BOTTOM;
					statusBar.setLayoutParams(lp);
				}
				firstLayout.addView(statusBar);
			}
		}
	}

	/**
	 * @return The status bar or null.
	 */
	public HGBaseStatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * @param index
	 *            index of the desired status panel.
	 * @return the status panel with the given index or null.
	 */
	public View getStatusPanel(int index) {
		if (statusBar != null) {
			return statusBar.getPanel(index);
		}
		return null;
	}

	/**
	 * @param index
	 *            index of the desired status label.
	 * @return the status label with the given index or null.
	 */
	public TextView getStatusLabel(int index) {
		if (statusBar != null) {
			return statusBar.getLabel(index);
		}
		return null;
	}

	/**
	 * @param index
	 *            index of the desired status panel.
	 * @param text
	 *            text to set on the panel.
	 */
	public void setStatusText(int index, String text) {
		if (statusBar != null) {
			statusBar.setText(index, text);
		}
	}

	/**
	 * Displays a text on the status bar, will make the status bar visible if
	 * there is a text and the status bar is hidden.
	 * 
	 * @param text
	 *            text to set on the status bar, will hide status bar if text is
	 *            empty and status bar is not shown permanently.
	 * @see #setStatusBar(boolean)
	 */
	public void setStatusText(String text) {
		if (HGBaseTools.hasContent(text)) {
			if ((statusBar == null) && (isCreateDefaultStatusBarActive())) {
				showStatusBar();
			}
			statusBar.setText(text);
		} else {
			if (statusBar != null) {
				statusBar.setText("");
			}
			if (!permanentStatusBar) {
				hideStatusBar();
			}
		}
	}

	/**
	 * Shows or hides a status progress. This is done be showing/hiding the
	 * typical progress circle.
	 * <p>
	 * NOTE: The status bar will not be hidden by this method.
	 * 
	 * @param state
	 *            the state of the status progress
	 */
	public synchronized void setStatusProgress(final ProgressState state) {
		if ((statusBar == null) && (isCreateDefaultStatusBarActive())) {
			showStatusBar();
		}
		if (statusBar != null) {
			statusBar.setProgressState(state);
		}
	}

	/**
	 * Returns {@code true} if a default status bar should be created when needed.
	 *
	 * @return {@code true} if a default status bar should be created when needed.
	 */
	protected boolean isCreateDefaultStatusBarActive() {
		return true;
	}

	/**
	 * Changes the text of the wait dialog if it is displayed.
	 * 
	 * @param text
	 *            the text to set, must not be null
	 */
	public void setWaitDialogText(String text) {
		if (waitDialog != null) {
			synchronized (waitDialog) {
				waitDialog.setMessage(text);
			}
		}
	}

	/**
	 * Returns the wait dialog that is displayed to show a circle. Allows for
	 * instance to set a specific message text.
	 * 
	 * @return the wait dialog or null
	 */
	public ProgressDialog getStatusProgressBar() {
		return waitDialog;
	}

	/**
	 * Set the cursor to default.
	 */
	public void setCursorDefault() {
		HGBaseAppTools.runOnUiThread(this, new Runnable() {

			@Override
			public void run() {
				hideWaitDialog();
			}
		});
	}

	/**
	 * Set the cursor to wait.
	 */
	public void setCursorWait() {
		HGBaseAppTools.runOnUiThread(this, new Runnable() {

			@Override
			public void run() {
				showWaitDialog();
			}
		});
	}

	/**
	 * Hides the wait dialog.
	 */
	private void hideWaitDialog() {
		if (waitDialog != null) {
			waitDialog.dismiss();
			waitDialog = null;
		}
	}

	/**
	 * Shows the wait dialog, do nothing if the dialog is already visible.
	 */
	private void showWaitDialog() {
		// do not show the wait dialog if there already exists one or the
		// activity is finishing
		if (waitDialog == null && !isFinishing()) {
			waitDialog = new ProgressDialog(this);
			waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			waitDialog.setCancelable(false);
			waitDialog.setMessage(HGBaseText.getText(R.string.progress_wait));
			waitDialog.show();
		}
	}

	@Override
	protected void onDestroy() {
		if (waitDialog != null) {
			hideWaitDialog();
		}
		super.onDestroy();
	}

	/**
	 * Sets if that the data has changed.
	 *
	 * @param change
	 *            true if it has changed
	 */
	public void setChanged(boolean change) {
		HGBaseActivity.changed = change;
	}

	/**
	 * Returns if the data is changed.
	 * <p>
	 * NOTE: This flag has to be set manually by calling
	 * {@link #setChanged(boolean)}.
	 *
	 * @return true if there is a changed document
	 */
	public boolean isChanged() {
		return HGBaseActivity.changed;
	}

	/**
	 * Returns {@code true} if the application is currently the pro version.
	 * 
	 * @return {@code true} if the application is currently the pro version.
	 */
	public boolean isProVersion() {
		return true;
	}

	/**
	 * Returns the advertisement URL.
	 *
	 * @return The advertisement URL.
	 */
	public String getAdvertisementURL() {
	    return null;
	}
	
	/**
	 * Returns the URL of the page, which should be displayed if the advertisement page could not be loaded.
	 *
	 * @return The URL of the page, which should be displayed if the advertisement page could not be loaded.
	 */
	public String getAdvertisementErrorPageURL() {
	    return null;
	}

	/**
	 * Returns how much percent of the screen width the advertisement view should have.
	 * 
	 * @return The percentage of the screen width, how much the advertisement view should have.
	 */
	public int getAdvertisementViewWidthPercent() {
	    return (HGBaseGuiTools.isScreenLandscape(this)) ? 90 : 100;
	}

	/**
	 * Returns how much percent of the screen height the advertisement view should have.
	 *
	 * @return The percentage of the screen height, how much the advertisement view should have.
	 */
	public int getAdvertisementViewHeightPercent() {
	    return (HGBaseGuiTools.isScreenLandscape(this)) ? 60 : 65;
	}

	/**
	 * Show a dialog on an uncaught exception and exit the application.
	 */
	protected static class ExitOnUncaughtExceptionWithClipboard implements UncaughtExceptionHandler {

		private static final UncaughtExceptionHandler defaultExceptionHandler = Thread
				.getDefaultUncaughtExceptionHandler();

		public ExitOnUncaughtExceptionWithClipboard() {
			super();
		}

		@Override
		public void uncaughtException(Thread thread, final Throwable throwable) {
			HGBaseAppTools.copyToClipboard(HGBaseTools.toString(throwable));
			defaultExceptionHandler.uncaughtException(thread, throwable);
		}
	}

	/**
	 * Holds additional information for menu items.
	 */
	private class AddMenuItemInfo {
		// The application type in which the menu item should be available.
		public APPTYPE appType;

		/**
		 * Creates a new instance.
		 *
		 * @param appType
		 *            The application type in which the menu item should be
		 *            available.
		 */
		public AddMenuItemInfo(APPTYPE appType) {
			this.appType = appType;
		}
	}

	/**
	 * Returns the fullscreen mode. If {@code null} fullscreen is deactivated.
	 *
	 * @return The fullscreen mode.
	 */
	public FullscreenMode getFullscreenMode() {
	    return null;
	}

	/**
	 * Sets the application to fullscreen with the specified mode.
	 *
	 * @param mode The fullscreen mode.
	 */
	private void setFullscreen(FullscreenMode mode) {
	    if (mode != null) {
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(mode.getVisibilityFlag());
	    }
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);
	    if (hasFocus) {
		FullscreenMode fullscreenMode = getFullscreenMode();
		if (fullscreenMode != null) {
		    setFullscreen(fullscreenMode);
		}
	    }
	}
}