package at.hagru.hgbase.android.view;

import android.view.View;

/**
 * Handle a zoom change.
 * 
 * @author hagru
 */
public interface ZoomListener {

    /**
     * Perform the zoom action.
     * 
     * @param v the view the zoom touch event happened
     * @param zoom the new zoom factor
     * @param scaleDiff the scale factor of the current zoom event (zoom in: > 1.0, zoom out: < 1.0)
     */
    public void performZoom(View v, int zoom, float scaleDiff);
        
}
