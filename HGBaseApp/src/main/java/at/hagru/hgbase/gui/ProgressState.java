package at.hagru.hgbase.gui;

import at.hagru.hgbase.HGBaseActivity;

/**
 * Define the possible progress state for the status progress of the activity.
 * 
 * @author hagru
 * @see HGBaseActivity#setStatusProgress(int)
 */
public enum ProgressState {
	
	STATE_NORMAL,
	STATE_WAITING,
	STATE_FINISHED;
	
	/**
	 * @return true if state is {@value #STATE_NORMAL}
	 */
	public boolean isNormal() {
		return STATE_NORMAL.equals(this);
	}
	
	/**
	 * @return true if state is {@value #STATE_WAITING}
	 */
	public boolean isWaiting() {
		return STATE_WAITING.equals(this);
	}
	
	/**
	 * @return true if state is {@value #STATE_FINISHED}
	 */
	public boolean isFinished() {
		return STATE_FINISHED.equals(this);
	}
	
}
