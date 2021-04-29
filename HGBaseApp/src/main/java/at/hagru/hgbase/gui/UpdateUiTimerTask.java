package at.hagru.hgbase.gui;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import at.hagru.hgbase.HGBaseActivity;

/**
 * A timer tasks that allows to update the UI by in the run method of the given runnable.<p>
 * The timer task can be scheduled by a {@link Timer}.
 * 
 * @author hagru
 */
public class UpdateUiTimerTask extends TimerTask {

	private Handler threadHandler;
	private Runnable updateUiRunner;
	
	/**
	 * Create the handler on creation of the timer task.<p>
	 * This constructor must only called in a UI thread that is prepared for creating a new handler.
	 * 
	 * @param activity the activity to take the thread handler from
	 * @param updateUiRunner a runner that can update the UI within the run method
	 */
	public UpdateUiTimerTask(HGBaseActivity activity, Runnable updateUiRunner) {
		this(activity.getThreadHandler(), updateUiRunner);
	}	

	/**
	 * @param threadHandler a thread handler that is required for doing the UI update
	 * @param updateUiRunner a runner that can update the UI within the run method
	 */
	public UpdateUiTimerTask(Handler threadHandler, Runnable updateUiRunner) {
		this.threadHandler = threadHandler;
		this.updateUiRunner = updateUiRunner;
	}

	@Override
	public void run() {
		threadHandler.post(updateUiRunner);
	}

}
