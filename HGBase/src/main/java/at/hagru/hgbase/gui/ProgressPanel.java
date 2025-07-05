package at.hagru.hgbase.gui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.Timer;
import java.util.TimerTask;

import at.hagru.hgbase.android.awt.Color;

/**
 * A panel that is able to show a progress.
 * 
 */
public class ProgressPanel extends AppCompatTextView {

    protected static final int COLOR_FINISH = Color.GREEN.getColorCode();

    protected final int colorNormal;
    protected final int colorFinish;
    protected final Bitmap imgProgress;

    private int numWaiting = -1;
    private ProgressState actState = ProgressState.STATE_NORMAL;

    public ProgressPanel(Activity activity, int colorNormal) {
        this(activity, colorNormal, 150);
    }

    protected ProgressPanel(Activity activity, int colorNormal, int progressTimer) {
        super(activity);
        this.colorNormal = colorNormal;
    	colorFinish = getProgressColorFinished();
        imgProgress = HGBaseGuiTools.loadImage("progress");
        if (progressTimer > 0) {
            // timer for waiting state
        	new Timer().schedule(new TimerTask() {
				
				@Override
				public void run() {
                    if (actState == ProgressState.STATE_WAITING) {
                        numWaiting++;
                        if (numWaiting > 100000) {
                            numWaiting = 0;
                        }
                    }
                    repaint();
				}
			}, 0, progressTimer);
        }
        setBackgroundColor(this.colorNormal);
    }
    
    /**
     * Repaints the panel in an extra thread that should work from non UI-threads.<p>
     * NOTE: use {@link #invalidate()} to perform a repaint from a UI thread.
     * 
     */
    public void repaint() {
    	postInvalidate();
    }    

    /**
	 * @return the color code for the progress panel in finished state, must not be null
	 */
    protected int getProgressColorFinished() {
		return COLOR_FINISH;
	}

	/**
     * Actualizes the display of the connection state
     */
    public void setState(ProgressState connState) {
        if (connState != this.actState) {
            this.actState = connState;
            repaint();
        }
    }

    /**
     * Returns the state, that is actual shown
     * 
     * @return actuals shown state
     */
    public ProgressState getState() {
        return this.actState;
    }

    /**
     * For repainting...
     * 
     * @param g the graphics object
     */
    @SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas g) {
        super.onDraw(g);
        if (this.actState == ProgressState.STATE_FINISHED) {
            this.setBackgroundColor(colorFinish);
        } else {
            this.setBackgroundColor(colorNormal);
        }
        // draw the progress bar
        if (this.actState == ProgressState.STATE_WAITING) {
            int pnW = this.getWidth();
            int pnH = this.getHeight();
            if (numWaiting >= 0) {
                int imgW = (imgProgress != null) ? imgProgress.getWidth() : pnW / 3;
                int imgMove = imgW / 3;
                int num = Math.max((pnW + imgMove) / imgMove, 1);
                int xPos = numWaiting % num;
                if (imgProgress != null) {
                	g.drawBitmap(imgProgress, null, new Rect(1 + imgMove * xPos, 1, imgMove * xPos + imgW, pnH - 2), null);
                } else {
                	Paint paint = new Paint();
                    paint.setColor(colorFinish);
                    paint.setStyle(Style.FILL_AND_STROKE);
                	g.drawRect(new RectF(1 + imgMove * xPos, 1, imgMove * xPos + imgW, pnH - 2), paint); 
                }
            }
        }
    }

}
