package at.hagru.hgbase.gui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.R;
import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;
import at.hagru.hgbase.lib.HGBaseText;

/**
 * Draws the welcome screen. Takes the image from the welcome.jpg
 * 
 * @author hagru
 */
public class HGBaseWelcome {
	
	public static final int WELCOME_ROOT_ID = Math.abs(HGBaseWelcome.class.getName().hashCode()) % 0x00FFFFFF;
	
	protected static final int TEXT_SIZE = 20;

    protected HGBaseWelcome() {
        super();
    }

    /**
     * Returns a dialog to be displayed as welcome/splash screen.
     * 
     * @paramn activity the main activity that will start the welcome dialog, must not be null
     * @return the welcome dialog (splash screen) or null if no image is set
     */
    public static Dialog createDialog(HGBaseActivity activity) {
    	int welcomeImageId = HGBaseResources.getResourceIdByName("welcome", HGBaseResources.DRAWABLE);
    	if (welcomeImageId > 0) {
        	int titleBarHeight = getTitleBarHeight(activity);
        	Dialog welcomeDialog = createPlainDialog(activity, titleBarHeight);
        	LinearLayout frame = createMainLayout(activity);        	
        	int imageHeight = HGBaseGuiTools.getScreenSize(activity).y - TEXT_SIZE * 4 - titleBarHeight;
        	ImageView welcomeImageView = createImageView(activity, welcomeImageId, imageHeight);
        	TextView appInfoText = createAppInfoView(activity, titleBarHeight, false);
        	frame.addView(welcomeImageView);
        	frame.addView(appInfoText);
        	welcomeDialog.setContentView(frame);
        	return welcomeDialog;
    	} else {
    		return null;
    	}
    }
    
    /**
     * Creates a plain dialog depending on the hieght of the title bar.
     * 
     * @param the activity related to the dialog
     * @param titleBarHeight the height of the title bar, is 0 if there is none visible
     * @return the dialog
     */
    protected static Dialog createPlainDialog(Activity activity, int titleBarHeight) {
		Dialog dialog = (titleBarHeight == 0) ? new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
											  : new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setCancelable(false);
    	if (titleBarHeight > 0) {
        	WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        	params.y = titleBarHeight;
        	params.height = LayoutParams.WRAP_CONTENT;
    	}
    	return dialog;
    }
    
    /**
     * @param activity the main activity
     * @return the height of the title bar, may be 0 if no title bar available
     */
    protected static int getTitleBarHeight(Activity activity) {
    	return HGBaseAppTools.getTitleBarHeight(activity);
    }

	/**
	 * @param activity the main activity
	 * @return the main layout to add images and text
	 */
	protected static LinearLayout createMainLayout(Activity activity) {
		LinearLayout frame = HGBaseGuiTools.createLinearLayout(activity, false);
		frame.setId(WELCOME_ROOT_ID);
		frame.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		frame.setBackgroundColor(Color.WHITE);
		return frame;
	}

	/**
	 * @param activity the main activity
	 * @param titleBarHeight the height of the title bar, may be 0
	 * @param blackWhite true for black on white, false for white on black
	 * @return the text view with the application information
	 */
	protected static TextView createAppInfoView(Activity activity, int titleBarHeight, boolean blackWhite) {
		TextView appInfoText = new TextView(activity);
		appInfoText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));        	
		appInfoText.setTextSize(TEXT_SIZE);
		appInfoText.setHeight(TEXT_SIZE * 4);
		appInfoText.setWidth(HGBaseGuiTools.getScreenSize(activity).x);
		appInfoText.setGravity(Gravity.CENTER);
		if (blackWhite) {
			appInfoText.setTextColor(Color.BLACK);
			appInfoText.setBackgroundColor(Color.WHITE);
		} else {
			appInfoText.setTextColor(Color.WHITE);
			appInfoText.setBackgroundColor(Color.BLACK);
		}
		appInfoText.setEnabled(false);
		String info = (titleBarHeight == 0) ? HGBaseAppTools.getAppName() : HGBaseText.getText(R.string.version) + ":";
		info = info +  " " + HGBaseAppTools.getAppVersion();
		appInfoText.setText(info);
		return appInfoText;
	}

	/**
	 * @param activity the main activity
	 * @param imageId the id of the image to display
	 * @param height the height of the image
	 * @return the image view with the given image
	 */
	protected static ImageView createImageView(Activity activity, int imageId, int height) {
	    return createImageView(activity, imageId, false, height);
	}

	   /**
     * @param activity the main activity
     * @param imageId the id of the image to display
     * @param fitToHeight true to fit to height, and define the widht as fixed or false to fit for width (the default)
     * @param heightOrWidht the height (fitToHeight=false) or width (fitToHeight=true) of the image 
     * @return the image view with the given image
     */
    protected static ImageView createImageView(Activity activity, int imageId, boolean fitToHeight, int heightOrWidth) {
        ImageView welcomeImageView = new ImageView(activity);
        if (fitToHeight) {
            welcomeImageView.setLayoutParams(new LayoutParams(heightOrWidth, LayoutParams.MATCH_PARENT));
        } else {
            welcomeImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, heightOrWidth));            
        }
        welcomeImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        welcomeImageView.setImageResource(imageId);
        return welcomeImageView;
    }


}
