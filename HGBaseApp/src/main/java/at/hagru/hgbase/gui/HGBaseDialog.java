package at.hagru.hgbase.gui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;

import java.io.File;
import java.util.Arrays;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.R;
import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;
import at.hagru.hgbase.android.awt.Color;
import at.hagru.hgbase.android.dialog.AbstractFileActivity;
import at.hagru.hgbase.android.dialog.DialogOnClickCloseListener;
import at.hagru.hgbase.android.dialog.HSVColorPickerDialog;
import at.hagru.hgbase.android.dialog.HSVColorPickerDialog.OnColorSelectedListener;
import at.hagru.hgbase.android.dialog.IFileSelectionListener;
import at.hagru.hgbase.android.dialog.OpenFileActivity;
import at.hagru.hgbase.android.dialog.SaveFileActivity;
import at.hagru.hgbase.lib.HGBaseFileTools;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * A class for all dialogs.
 * 
 * @author hagru
 */
public final class HGBaseDialog {
	
	/* temporary variable to store the last created dialog */
	private static AlertDialog newCreatedDialog;
	
	/**
	 * Avoid instantiation.
	 */
	private HGBaseDialog() {
		super();
	}
	
	/**
     * Shows a message dialog.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param messageId the id of the message to display
     * @param title the title string for the dialog
     */
    public static void showMessageDialog(Activity activity, int messageId, String title) {
        showMessageDialog(activity, messageId, title, new DialogOnClickCloseListener());
    }	
	
	/**
     * Shows a message dialog.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param messageId the id of the message to display
     * @param title the title string for the dialog
     * @param okListener the listener to do the action on ok
     */
    public static void showMessageDialog(Activity activity, int messageId, String title, OnClickListener okListener) {
        showMessageDialog(activity, HGBaseText.getText(messageId), title, okListener);
    }	

    
	/**
     * Shows a message dialog.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param message the message to display
     * @param title the title string for the dialog
     * @param okListener the listener to do the action on ok
     */
    public static void showMessageDialog(Activity activity, String message, String title, OnClickListener okListener) {
        View view = HGBaseGuiTools.createViewForMessage(activity, message);
        showOkDialog(activity, view, title, okListener);
    }
    
    /**
     * Shows an error message dialog where the message is specified by an error code.
     * 
     * @param errorCode the error code, typically a negative number
     * @param activity the activity in which the dialog is displayed
     */
    public static void printError(int errorCode, Activity activity) {
    	printError(String.valueOf(errorCode), activity);
    }
    
    /**
     * Shows an error message dialog where the message is specified by an error code.
     * 
     * @param errorCode the error code as text, typically a negative number
     * @param activity the activity in which the dialog is displayed
     */
    public static void printError(String errorCode, Activity activity) {
    	showErrorDialog(activity, HGBaseText.getText(errorCode));
    }
    
    /**
     * Shows an error message dialog where the message is specified by an error code.
     * 
     * @param errorCode the error code, typically a negative number
     * @param placeHolder array with place holders that replace "%s"  
     * @param activity the activity in which the dialog is displayed
     */
    public static void printError(String errorCode, String[] placeHolder, Activity activity) {
    	Object[] formatArgs = Arrays.asList(placeHolder).toArray();
    	showErrorDialog(activity, HGBaseText.getText(errorCode, formatArgs));
    }
    
    /**
     * Prints the given exception as an error.
     *
     * @param exception the exception to print
     * @param activity the activity in which the dialog is displayed
     */
    public static void printError(Throwable exception, Activity activity) {
        printError(exception.getMessage(), activity);
    }    
	
	/**
     * Shows an error dialog.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param messageId the id of the error message to display
     */
    public static void showErrorDialog(Activity activity, int messageId) {
        showErrorDialog(activity, HGBaseText.getText(messageId));
    }	    
	
	/**
     * Shows an error dialog.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param messageId the id of the error message to display
     * @param okListener the listener when pressing ok
     */
    public static void showErrorDialog(Activity activity, int messageId, OnClickListener okListener) {
        showErrorDialog(activity, HGBaseText.getText(messageId), okListener);
    }	    
	
	/**
     * Shows an error dialog.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param messageId the error message to display
     */
    public static void showErrorDialog(Activity activity, String message) {
    	showErrorDialog(activity, message, new DialogOnClickCloseListener());
    }
    
	/**
     * Shows an error dialog.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param messageId the error message to display
     * @param okListener the listener when pressing ok
     */
    public static void showErrorDialog(Activity activity, String message, OnClickListener okListener) {
        showMessageDialog(activity, message, HGBaseText.getText(R.string.error), okListener);
    }
    
    /**
     * Prints the given code as an information
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param code code of the message
     * @param placeHolder array with placeholders that replace "%s"
     */
    public static void printInfo(Activity activity, String code, String[] placeHolder) {
        Object[] formatArgs = Arrays.asList(placeHolder).toArray();
        showMessageDialog(activity, HGBaseText.getText(code, formatArgs), HGBaseText.getText("app_name"), new DialogOnClickCloseListener());
    }    
    
    /**
     * Displays a simple dialog with an ok button.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param view the Object to display
     * @param title the title string for the dialog
     * @return the new created dialog
     */
    public static AlertDialog showOkDialog(Activity activity, View view, String title) {
    	return showOkDialog(activity, view, title, new DialogOnClickCloseListener());
    }
    
    /**
     * Displays a simple dialog with an ok button.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param view the Object to display
     * @param title the title string for the dialog
     * @param okListener the listener to do the action on ok
     * @return the new created dialog
     */
    public static AlertDialog showOkDialog(Activity activity, View view, String title, OnClickListener okListener) {
    	return showOkCancelDialog(activity, view, title, okListener, null);
    }
    
    
    /**
     * Displays a simple dialog with an ok and a cancel button, but allows only reaction the the ok button.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param message the message to display
     * @param title the title string for the dialog
     * @param okListener the listener to do the action on ok
     */
    public static AlertDialog showOkCancelDialog(Activity activity, String message, String title, OnClickListener okListener) {
        View view = HGBaseGuiTools.createViewForMessage(activity, message);
    	return showOkCancelDialog(activity, view, title, okListener, new DialogOnClickCloseListener());
    }

    /**
     * Displays a simple dialog with an ok and a cancel button, but allows only reaction the the ok button.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param view the Object to display
     * @param title the title string for the dialog
     * @param okListener the listener to do the action on ok
     */
    public static AlertDialog showOkCancelDialog(Activity activity, View view, String title, OnClickListener okListener) {
    	return showOkCancelDialog(activity, view, title, okListener, new DialogOnClickCloseListener());
    }
    
    /**
     * Displays a simple dialog with an ok and a cancel button.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param message the message to display
     * @param title the title string for the dialog
     * @param okListener the listener to do the action on ok
     * @param cancelListener the listener to do the action on cancel
     */
    public static AlertDialog showOkCancelDialog(Activity activity, String message, String title, OnClickListener okListener, OnClickListener cancelListener) {
        View view = HGBaseGuiTools.createViewForMessage(activity, message);
        return showOkCancelDialog(activity, view, title, okListener, cancelListener);
    }
    
    /**
     * Displays a simple dialog with an ok and a cancel button.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param view the Object to display
     * @param title the title string for the dialog
     * @param okListener the listener to do the action on ok
     * @param cancelListener the listener to do the action on cancel
     * @return the dialog that is shown
     */
    public static AlertDialog showOkCancelDialog(Activity activity, View view, String title, OnClickListener okListener, OnClickListener cancelListener) {
        return showOkCancelDialog(activity, view, title, okListener, cancelListener, null);
    }

    /**
     * Displays a simple dialog with an ok and a cancel button.
     *
     * @param activity determines the activity in which the dialog is displayed
     * @param dlg the alert dialog to be used
     * @param view the Object to display
     * @param title the title string for the dialog
     * @param okListener the listener to do the action on ok
     * @param cancelListener the listener to do the action on cancel
     * @param dismissListener the listener to the the action when the dialog is dismissed in any way
     * @return the dialog that is shown
     */
    public static AlertDialog showOkCancelDialog(Activity activity, View view, String title, OnClickListener okListener, OnClickListener cancelListener, OnDismissListener dismissListener) {
    	AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
    	dlg.setTitle(HGBaseText.getText(title));
    	dlg.setIcon(HGBaseResources.getResourceIdByName("ic_launcher", HGBaseResources.DRAWABLE));
        dlg.setView(view);
        if (okListener != null) {
	        dlg.setPositiveButton(HGBaseText.getText("button_ok"), okListener);
        }
        if (cancelListener != null) {
	        dlg.setNegativeButton(HGBaseText.getText("button_cancel"), cancelListener);
        }
        if (dismissListener != null) {
        	dlg.setOnDismissListener(dismissListener);
        }
        return showDialogOnUiThread(activity, dlg);
    }

    /**
     * Shows a dialog for choosing a file on the external file system (typically for loading).<p>
     * Requires <b>&lt;uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/&gt;</b> in the manifest file!
     * 
     * @param activity the activity that calls the dialog
     * @param fileListener the file listener to handle the chosen file
     */
    public static void showChooseFileDialog(HGBaseActivity activity, IFileSelectionListener fileListener) {
    	//if (HGBaseAppTools.checkPermissionWithErrorMessage(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
    	if (HGBaseAppTools.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
    		showFileDialog(activity, OpenFileActivity.class, HGBaseActivity.OPEN_FILE_RESULT, fileListener);
    	} else {
    		activity.setFileSelectionListener(fileListener);
    		HGBaseAppTools.requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, 
    										 HGBaseActivity.FILE_OPEN_PERMISSION_REQUEST);
    	}
    }

    /**
     * Shows a dialog for saving a file on the external file system.<p>
     * Requires <b>&lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/&gt;</b> in the manifest file!
     * 
     * @param activity the activity that calls the dialog
     * @param fileListener the file listener to handle the saving of the file
     */
    public static void showSaveFileDialog(HGBaseActivity activity, IFileSelectionListener fileListener) {
    	//if (HGBaseAppTools.checkPermissionWithErrorMessage(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        if (HGBaseAppTools.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
    		showFileDialog(activity, SaveFileActivity.class, HGBaseActivity.SAVE_FILE_RESULT, fileListener);
    	} else {
    		activity.setFileSelectionListener(fileListener);
    		HGBaseAppTools.requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, 
    										 HGBaseActivity.FILE_SAVE_PERMISSION_REQUEST);
    	}
    }

    /**
     * Shows a file dialog on the external file system. Does not check for permissions.<p>
     * 
     * @param activity the activity that calls the dialog
     * @param fileActivity the file activity class
     * @param requestCode the request code to handle the result in {@link Activity#onActivityResult}.
     * @param fileListener the file listener to handle the selection event
     */
    public static void showFileDialog(HGBaseActivity activity, Class<? extends AbstractFileActivity> fileActivity, int requestCode, IFileSelectionListener fileListener) {
    	activity.setFileSelectionListener(fileListener);
        HGBaseAppTools.startActivityForResult(activity, fileActivity, requestCode);
    }
    
    /**
     * Show a dialog for selecting a color.
     * 
     * @param activity the activity to start the dialog from
     * @param color the initially selected color, if null it is allowed that no color is selected
     * @param listener the listener to react on selected color
     */
    public static void showColorDialog(Activity activity, Color color, OnColorSelectedListener listener) {
    	showColorDialog(activity, color, false, listener);
    }
    
    /**
     * Show a dialog for selecting a color.
     * 
     * @param activity the activity to start the dialog from
     * @param color the initially selected color, if null it is allowed that no color is selected
     * @param allowNoColor true to allow that no color is selected, false if a color is required
     * @param listener the listener to react on selected color
     */
    public static void showColorDialog(Activity activity, Color color, boolean allowNoColor, OnColorSelectedListener listener) {
        int colorCode = (color == null) ? -1 : color.getColorCode();
		HSVColorPickerDialog cpd = new HSVColorPickerDialog(activity, colorCode, listener);
		cpd.setTitle(HGBaseText.getText("select_color"));
		if (color == null || allowNoColor) {
			cpd.setNoColorButton(R.string.select_no_color);
		}
		showDialogOnUiThread(activity, cpd);
    }    
    
    /**
     * Shows an HTML page as dialog.
     * 
     * @param activity the activity to start the dialog from
     * @param htmlResId the (raw) resource id of the HTML page
     * @param title the title of the dialog
     */
    public static AlertDialog showHtmlDialog(Activity activity, int htmlResId, String title) {
    	HGBaseHTMLPageWebView htmlView = new HGBaseHTMLPageWebView(activity, htmlResId);
    	return showOkDialog(activity, htmlView, title);
    }

    /**
     * Shows an HTML page as dialog.
     * 
     * @param activity the activity to start the dialog from
     * @param htmlText the HTML coded text
     * @param title the title of the dialog
     * @return the new createrd dialog
     */
    public static AlertDialog showHtmlDialog(Activity activity, String htmlText, String title) {
    	HGBaseHTMLPageWebView htmlView = new HGBaseHTMLPageWebView(activity, htmlText);
    	return showOkDialog(activity, htmlView, title);
    }

    /**
     * Shows an HTML page as dialog.
     * 
     * @param activity the activity to start the dialog from
     * @param htmlText the HTML coded text
     * @param title the title of the dialog
     * @return the new created dialog
     */
    public static AlertDialog showHtmlDialog(Activity activity, File htmlFile, String title) {
    	String htmlText = HGBaseFileTools.getString(HGBaseFileTools.openFileStream(htmlFile, "Could not open " + htmlFile));
    	HGBaseHTMLPageWebView htmlView = new HGBaseHTMLPageWebView(activity, htmlText);
    	return showOkDialog(activity, htmlView, title);
    }
    
    /**
     * Shows the dialog on the UI thread of the given activity.
     * 
     * @param activity the activity
     * @param dlgBuilder the builder for the dialog to show
     * @return the dialog that is displayed
     */
    public static AlertDialog showDialogOnUiThread(Activity activity, final AlertDialog.Builder dlgBuilder) {
    	newCreatedDialog = null;
    	if (!activity.isFinishing()) {
	    	if (HGBaseAppTools.isOnUiThread()) {
	    		newCreatedDialog = dlgBuilder.show();
	    	} else {
		    	activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						newCreatedDialog = dlgBuilder.show();
					}
				});
		    	while (newCreatedDialog != null) {
		    		HGBaseTools.delay(100);
		    	}
	    	}
    	}
    	return newCreatedDialog;
    }
    /**
     * Shows the dialog on the UI thread of the given activity.
     * 
     * @param activity the activity
     * @param dlg the dialog to show
     */
    public static void showDialogOnUiThread(Activity activity, final Dialog dlg) {
		HGBaseAppTools.runOnUiThread(activity, new Runnable() {
			
			@Override
			public void run() {
				dlg.show();
			}
		});
    }

}
