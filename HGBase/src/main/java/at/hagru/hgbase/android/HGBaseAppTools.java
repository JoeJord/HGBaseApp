package at.hagru.hgbase.android;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.Window;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Locale;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.gui.HGBaseDialog;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Tools for basic App functions.
 * 
 * @author hagru
 */
public final class HGBaseAppTools {
	
	private static Context appContext = null;
	
	/**
	 * Prevent instantiation.
	 */
	private HGBaseAppTools() {
		super();
	}
	
	/**
	 * Returns the context of the running app, i.e., the app that uses the library.<p> 
	 * Will be null if the main activity does not inherit from the {@link HGBaseActivity} class
	 * and also not set manually otherwise.
	 * 
	 * @return the context of the running app or null
	 */
	public static Context getContext() {
		return appContext;
	}
	
	/**
	 * @return the resources object as shortcut of getContext().getResources(), may be null
	 */
	public static Resources getResources() {
		return (getContext() == null) ? null : getContext().getResources();
	}
	
	/**
	 * Sets the context for the current app.<p> 
	 * This is only necessary if the main activity does not inherit from the {@link HGBaseActivity} class.
	 * 
	 * @param appContext the application context to set
	 */
	public static void setContext(Context appContext) {
		HGBaseAppTools.appContext = appContext;
	}
	
	/**
	 * Returns the default package name of the app.
	 * 
	 * @return the default package name
	 */
	public static String getPackageName() {
		return (appContext == null)? null : appContext.getPackageName();
	}
	
	/**
	 * Returns the name of the running app, i.e., the app that uses the library.
	 * 
	 * @return the name of the running app
	 */
	public static String getAppName() {
		Context context = getContext();
		String name = (context == null)? "" : context.getString(context.getApplicationInfo().labelRes);
		if (!HGBaseTools.hasContent(name) && HGBaseText.existsText("app_name")) {
		    name = HGBaseText.getText("app_name");
		}
		return name;
	}

	/**
	 * Returns the version of the running app, i.e., the app that uses the library.
	 * 
	 * @return the version of the running app
	 */
	public static String getAppVersion() {
		Context context = getContext();
		if (context == null) {
			return "";
		} else {
			try {
				return context.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				return "";
			}
		}
	}

	/**
	 * Returns the description of the running app, i.e., the app that uses the library.
	 * 
	 * @return the description of the running app
	 */
	public static String getAppDescription() {
		Context context = getContext();
		String description = "";
		if (context != null) {
			int resId = context.getApplicationInfo().descriptionRes;
			description = (resId == 0)? "" : context.getString(resId);
		}
        if (!HGBaseTools.hasContent(description) && HGBaseText.existsText("app_description")) {
            description = HGBaseText.getText("app_description");
        }
		return description;
	}
	
	/**
	 * Returns the ISO language code (e.g. "en" or "de).
	 *  
	 * @return the ISO language code
	 */
	public static String getLanguageCode() {
		return Locale.getDefault().getLanguage();
	}
	
	/**
	 * Try to find out a user name by device profiles and returns it.<p>
	 * Requires appropriate permissions (READ_PROFILE and READ_CONTACTS). 
	 * <b>Does not return a (profile) name by actual testing!</b>
	 * 
	 * @return the user name or an empty string
	 * @see android.permission.READ_PROFILE
	 * @see android.permission.READ_CONTACTS
	 */
	public static String getUserNameByProfile() {
		String[] projection = new String[] { ContactsContract.Profile.DISPLAY_NAME };
	    Uri dataUri = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, 
	    								   ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
	    ContentResolver contentResolver = getContext().getContentResolver();
    	Cursor c = contentResolver.query(dataUri, projection, null, null, null);
		if (c.moveToNext()) {
			String name = c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
			if (HGBaseTools.hasContent(name)) {
			    return name;
			}
		}
		c.close();
		// no profile with a name found
	    return "";
	}
	
	/**
	 * Try to find out a user name by mail accounts and returns it.<p>
	 * Requires appropriate permissions (GET_ACCOUNTS).
	 * 
	 * @return the user name or an empty string
	 * @see android.permission.GET_ACCOUNTS
	 */
	public static String getUserNameByMail() {
	    AccountManager am = AccountManager.get(getContext());
	    for (Account account : am.getAccounts()) {
	        String name = account.name;
	        if (HGBaseTools.hasContent(name)) {
	            name = name.split("@")[0];
	            name = name.replace('.', ' ');
	            name = name.replace('_', ' ');
	            return name;
	        }
	    }
	    // no account with a name found
	    return "";	    
	}
	
	/**
	 * Retrieve the user name by the default method.
	 * 
	 * @return the user name or an empty string
	 * @see #getUserNameByMail()
	 * @see #getUserNameByProfile()
	 */
	public static String getUserName() {
	    return getUserNameByMail();
	}
	
	/**
	 * Starts an activity taking a bundle to pass parameters.
	 * 
	 * @param activity the activity that starts a new activity
	 * @param bundle the bundle with parameters
	 * @param startActivityClass the class of the activity to start
	 */
	public static void startActivity(Activity activity, Class<? extends Activity> startActivityClass, Bundle bundle) {
		Intent activityIntent = new Intent(activity.getApplicationContext(), startActivityClass);
		if (bundle != null) {
			activityIntent.putExtras(bundle);
		}
		activity.startActivity(activityIntent);		
	}

	/**
	 * Starts an activity.
	 * 
	 * @param activity the activity that starts a new activity
	 * @param startActivityClass the class of the activity to start
	 */
	public static void startActivity(Activity activity, Class<? extends Activity> startActivityClass) {
		startActivity(activity, startActivityClass, null);		
	}

	/**
	 * Starts an activity taking a bundle to pass parameters and for getting a result.
	 * 
	 * @param activity the activity that starts a new activity and receives the result
	 * @param bundle the bundle with parameters
	 * @param startActivityClass the class of the activity to start
	 * @param requestCode the request code to handle the result in {@link Activity#onActivityResult}.
	 */
	public static void startActivityForResult(Activity activity, Class<? extends Activity> startActivityClass, Bundle bundle, int requestCode) {
		Intent activityIntent = new Intent(activity.getApplicationContext(), startActivityClass);
		if (bundle != null) {
			activityIntent.putExtras(bundle);
		}
		activity.startActivityForResult(activityIntent, requestCode);		
	}
	
	/**
	 * Starts an activity for getting a result.
	 * 
	 * @param activity the activity that starts a new activity and receives the result
	 * @param startActivityClass the class of the activity to start
	 * @param requestCode the request code to handle the result in {@link Activity#onActivityResult}.
	 */
	public static void startActivityForResult(Activity activity, Class<? extends Activity> startActivityClass, int requestCode) {
		startActivityForResult(activity, startActivityClass, null, requestCode);		
	}
	
	/**
	 * Starts an activity to send an object.
	 * 
	 * @param activity the activity that starts the new activity
	 * @param contentType the content type of the object
	 * @param objectUri the URI of the object
	 */
	public static void startSendActivity(Activity activity, String contentType, Uri objectUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, objectUri);
        shareIntent.setType(contentType);
        if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
        	activity.startActivity(Intent.createChooser(shareIntent, HGBaseText.getText("action_share")));	
        }
	}
	
	/**
	 * @return true if the current thread is a UI thread.
	 */
	public static boolean isOnUiThread() {
		return (Looper.getMainLooper().getThread() == Thread.currentThread()); 
	}
	
	/**
	 * Runs the given code on the UI thread. This is either directly on the current thread if this is the UI thread 
	 * or the runnable is passed to the particular method of the activity.
	 * 
	 * @param activity the activity to potentially get the runnable passed, must not be null
	 * @param action the runnable to be called, must not be null
	 */
	public static void runOnUiThread(Activity activity, Runnable action) {
		if (isOnUiThread()) {
			action.run();
		} else {
			activity.runOnUiThread(action);
		}
	}
	
    /**
     * @param activity the main activity
     * @return the height of the title bar, may be 0 if no title bar available
     */
    public static int getTitleBarHeight(Activity activity) {
    	ActionBar bar = activity.getActionBar();
    	boolean hasTitle = !activity.getWindow().hasFeature(Window.FEATURE_NO_TITLE) &&  bar!= null && bar.isShowing();
    	TypedValue tv = new TypedValue();
    	if (hasTitle && activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
    		int height = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
    		return (int) Math.ceil(height * activity.getResources().getDisplayMetrics().density);
    	} else {
    		return 0;
    	}
    }
    
    /**
     * Copies the given text to the clipboard.
     * 
     * @param text the text to copy to the clipboard
     */
    public static void copyToClipboard(String text) {
    	ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE); 
    	ClipData clip = ClipData.newPlainText(getAppName(), text);
    	clipboard.setPrimaryClip(clip);    	
    }
    
    /**
     * Checks if the permission is granted.
     * 
     * @param activity the activity to check the permission for
     * @param permission the permission to check
     * @return true if permission is granted
     * @see Manifest.permission
     */
    public static boolean checkPermission(Activity activity, String permission) {
    	return (ContextCompat.checkSelfPermission(activity, permission)== PackageManager.PERMISSION_GRANTED);
    }
    
    /**
     * Request a permission.
     * 
     * @param activity the activity that can react on the permission request
     * @param permission the permission to request
     * @param permissionCode the permission code to react on 
     */
    public static void requestPermission(Activity activity, String permission, int permissionCode) {
		ActivityCompat.requestPermissions(activity, new String[] {permission}, permissionCode);
    }
    
    /**
     * Checks if the permission is granted and displays an error message (in English) if not.
     * 
     * @param activity the activity to check the permission for
     * @param permission the permission to check
     * @return true if permission is granted
     * @see #checkPermission(Activity, String)
     */
    public static boolean checkPermissionWithErrorMessage(HGBaseActivity activity, String permission) {
    	boolean granted = checkPermission(activity, permission);
    	if (!granted) {
    		HGBaseDialog.printError("error_permission", new String[] {permission}, activity);
    	}
    	return granted;
    }

    /**
     * Opens the specified link in a web browser.
     *
     * @param activity The activity that opens the link.
     * @param uriString An RFC 2396-compliant, encoded URI.
     */
    public static void openLink(Activity activity, String uriString) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        activity.startActivity(browserIntent);
    }

    /**
     * Shows a tool tip.
     *
     * @param text The text to show.
     */
    public static void showToolTip(String text) {
        showToolTip(text, getContext());
    }

    /**
     * Shows a tool tip.
     *
     * @param text The text to show.
     * @param context The context to use.
     */
    public static void showToolTip(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Checks if an internet connection is available.
     * 
     * @return {@code true} if an internet connection is available.
     */
    public static boolean isInternetAvailable() {
	try {
	    Socket socket = new Socket();
	    socket.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
	    socket.close();
	    return true;
	} catch (IOException e) {
	    return false;
	}
    }
}
