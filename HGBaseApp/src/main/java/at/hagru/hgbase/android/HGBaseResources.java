package at.hagru.hgbase.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.InputStream;

import at.hagru.hgbase.lib.HGBaseFileTools;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Define constant values for Android resource and make simple access to resources.
 * 
 * @author hagru
 */
public final class HGBaseResources {
	
	public static final String ID = "id";
	public static final String STRING = "string";
	public static final String DRAWABLE = "drawable";
	public static final String XML = "xml";
	public static final String RAW = "raw";

	/**
	 * Avoid instantiation.
	 */
	private HGBaseResources() {
		super();
	}
	
	/**
	 * @param resId the resource id
	 * @return the drawable object
	 */
	public static Drawable getDrawable(int resId) {
		return HGBaseAppTools.getResources().getDrawable(resId);
	}
	
	/**
	 * @param resId the resource id
	 * @return the bitmap object
	 */
	public static Bitmap getBitmap(int resId) {
		return BitmapFactory.decodeResource(HGBaseAppTools.getContext().getResources(), resId);
	}
	
	/**
	 * @param resId the resource id
	 * @return the content of the file as string
	 */
	public static String getFileContent(int resId) {
		InputStream fileStream = HGBaseFileTools.openRawResourceFileStream(resId);
		return HGBaseFileTools.getString(fileStream);
	}

	/**
	 * @param name the name of the resource
	 * @param resourceType the resource type (i.e., the root folder of the resource)
	 * @return the id or 0 if not found
	 */
	public static int getResourceIdByName(String name, String resourceType) {
		Context context = HGBaseAppTools.getContext();
		if (context == null || name == null || HGBaseTools.isValid(HGBaseTools.toInt(name)) 
							|| HGBaseTools.isValid(HGBaseTools.toInt(HGBaseFileTools.removeFileExtension(name)))) {
			return 0;
		} else {
			name = HGBaseFileTools.removeFileExtension(name);
			Resources r = context.getResources();
			int resId = r.getIdentifier(name, resourceType, HGBaseAppTools.getPackageName());
			return (resId != 0)? resId : r.getIdentifier(name, resourceType, null);
		}
	}
	
	/**
	 * Returns the name of a given resource id.
	 * 
	 * @param resId the resource id
	 * @return the name of the resource or an empty string if id is invalid
	 */
	public static String getResourceNameById(int resId) {
		return (resId > 0) ? HGBaseAppTools.getResources().getResourceEntryName(resId) : "";
	}

}
