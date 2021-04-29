package at.hagru.hgbase.android.view;

/**
 * Helper class to allow a tolerance for touch actions.
 * 
 * @param <T> the type of the action result
 * @author hagru
 */
public abstract class TolerantTouchPerformer<T> {
	
    private static final int TOUCH_TOLERANCE = 20;
    
    private final int xTolerance;
    private final int yTolerance;
	
    /**
     * Initialize the touch performance with default tolerance.
     */
    public TolerantTouchPerformer() {
		this(TOUCH_TOLERANCE);
	}

    /**
     * Initialize the touch performance with the given tolerance.
     * 
     * @param tolerance the x/y tolerance
     */
    public TolerantTouchPerformer(int tolerance) {
		this(tolerance, tolerance);
	}

    /**
     * Initialize the touch performance with the given tolerance.
     * 
     * @param xTolerance the x tolerance
     * @param yTolerance the y tolerance
     */
    public TolerantTouchPerformer(int xTolerance, int yTolerance) {
		this.xTolerance = xTolerance;
		this.yTolerance = yTolerance;
	}
    
    /**
     * Do the tolerance touch performance.
     * 
     * @param xPos the x position
     * @param yPos the y position
     * @return the success object or null if the touch was not successful
     */
    public T run(int xPos, int yPos) {
    	T result = performTouch(xPos, yPos);
    	if (result == null) {
        	for (int i = 0; i < 4 && result == null; i++) {
        		int newX = (i % 2 == 0) ? xPos + xTolerance : xPos - xTolerance;
        		int newY = (i % 2 == 1) ? yPos + yTolerance : yPos - yTolerance;
        		result = performTouch(newX, newY);
        	}    		
    	}
    	return result;
    }

    /**
     * Implement this method to perform the action for the given x/y position.
     * 
     * @param xPos the x position
     * @param yPos the y position
     * @return the success object or null if the touch was not successful
     */
    abstract protected T performTouch(int xPos, int yPos);
    
}
