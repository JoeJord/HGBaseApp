package at.hagru.hgbase.android.view;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Allow to react on zooming touches.
 */
public class OnTouchZoomProvider implements View.OnTouchListener {
    
    private static final int NO_ZOOM = 100;
	
    private ZoomListener listener;
	private int maxZoom;
	private int minZoom;
    private int zoomFactor = NO_ZOOM;
	private float oldDistance = 0;
	
	/**
	 * Creates a listener that reacts on zoom touch events of a view.
	 * 
	 * @param minZoom the minimum zoom factor (100 is no scale)
	 * @param maxZoom the maximum zoom factor (100 is no scale)
	 */
	public OnTouchZoomProvider(int minZoom, int maxZoom, ZoomListener listener) {
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		this.listener = listener;
	}
	
	/**
	 * @return the current zoom factor
	 */
	public int getZoomFactor() {
		return zoomFactor;
	}


	/**
	 * Allows to set the current zoom factor from outside.
	 * 
	 * @param zoomFactor the new zoom factor, 100 is no zoom
	 */
	public void setZoomFactor(int zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	/**
	 * Resets the zoom factor to the default (1.0f).
	 */
	public void resetZoomFactor() {
		setZoomFactor(NO_ZOOM);
	}
	
	/**
     * @return the the maximal zoom
     */
    public int getMaxZoom() {
        return maxZoom;
    }
    

    /**
     * @param maxZoom the new maximum zoom, does not influence the current zoom
     */
    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    /**
     * @return the minimum zoom
     */
    public int getMinZoom() {
        return minZoom;
    }

    /**
     * @param minZoom the new minimum zoom, does not influence the current zoom
     */
    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }
    
    /**
     * @return true if the zoom provider is currently zooming
     */
    public boolean isZooming() {
    	return (oldDistance != 0);
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE && event.getPointerCount() > 1) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			float newDistance =  Math.abs(x) + Math.abs(y);				
			if (oldDistance != 0) {
				float scale = newDistance / oldDistance;
				zoomFactor = (int) Math.min(Math.max(zoomFactor * scale, minZoom), maxZoom);
				listener.performZoom(v, zoomFactor, scale);
				oldDistance = newDistance;
				return true;
			}
			oldDistance = newDistance;
		} else if (isZooming()) {
			oldDistance = 0;
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP || 
					(event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_CANCEL) {
				return true;
			}
		}
		return false;
	}

}