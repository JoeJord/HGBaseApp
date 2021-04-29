package at.hagru.hgbase.android.view;

import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;

/**
 * A default implementation for an image getter to be used for displaying images in a text view.<p>
 * Names that are passed to the {@link #getDrawable(String)} method are tried to be taken from the 
 * {@code drawable} folders of the app or the HGBase library.
 * 
 * @author hagru
 */
public class DefaultImageGetter implements ImageGetter {

	/**
	 * Create a new image getter.
	 */
	public DefaultImageGetter() {
		super();
	}

    @Override
    public Drawable getDrawable(String source) {
    	int id = HGBaseResources.getResourceIdByName(source, HGBaseResources.DRAWABLE);
    	if (id == 0) {
    		return null;
    	} else {
	        Drawable image = HGBaseAppTools.getContext().getResources().getDrawable(id);
        	image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
	        return image;    		
    	}
    }

}
