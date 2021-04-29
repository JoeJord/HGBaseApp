package at.hagru.hgbase.gui.menu.actions;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.MenuItem;

import androidx.core.content.FileProvider;

import java.io.File;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.gui.HGBaseDialog;
import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.lib.HGBaseFileTools;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Take a screenshot and share it.
 */
public class ShareScreenshotAction extends AbstractMenuAction {
	
	private static final String DEFAULT_IMAGE_TYPE = "png";
	private static final String DEFAULT_FILE_PREFIX = "screenshot";

	public ShareScreenshotAction(HGBaseActivity activity) {
		super(activity);
	}
	
	/**
	 * @return the type/extension of the image
	 */
	protected String getImageType() {
		return DEFAULT_IMAGE_TYPE;
	}

	/**
	 * @return the prefix of the file name
	 */
	protected String getFilePrefix() {
		return DEFAULT_FILE_PREFIX;
	}

	@Override
	public void perform(int id, MenuItem item) {
		if (HGBaseAppTools.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
	        Bitmap bitmap = createBitmap();
	        if (bitmap != null) {
		        File imageFile = saveBitmapToFile(bitmap);
				if (imageFile != null) {
					shareFile(imageFile);
				}
	        }
		} else {
			HGBaseAppTools.requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE,  
											 HGBaseActivity.WRITE_STORAGE_SHARE_SCREENSHOT_PERMISSION_REQUEST);
		}
	}

	/**
	 * @return the bitmap to be sent to share, by default an image of the current activity
	 */
	protected Bitmap createBitmap() {
		return HGBaseGuiTools.takeScreenshot(getActivity());
	}

	/**
	 * @param bitmap the bitmap to save, must not be null (created by {@link #createBitmap()})
	 * @return the file where the bitmap was saved or null in case of error
	 */
	protected File saveBitmapToFile(Bitmap bitmap) {
		String fileName = getFilePrefix()+ "_" + HGBaseTools.getCurrentTime("yyyyMMddHHmm") + "." + getImageType();
        return HGBaseFileTools.saveBitmapToPictureFolder(bitmap, fileName);
	}

	/**
	 * Starts the activity to share the file.
	 * 
	 * @param imageFile the image file, must not be null
	 */
	protected void shareFile(File imageFile) {
        // Uri imageUri = Uri.parse("file://" + imageFile.getAbsolutePath()); Does not work any more since API 24
        try {
	        Uri imageUri = FileProvider.getUriForFile(getActivity(), HGBaseAppTools.getPackageName(), imageFile);
	        HGBaseAppTools.startSendActivity(getActivity(), "image/" + getImageType(), imageUri);
        } catch (Exception ex) {
        	HGBaseDialog.printError(ex, getActivity());
        }
	}
	
}