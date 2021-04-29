/*
 * Created on 15.03.2005
 */
package at.hagru.hgbase.lib;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.android.HGBaseAppTools;

/**
 * Support for logging.<p>
 * This is a facade to the Android default logging class.
 * 
 * @author hagru
 * @see Log
 */
public final class HGBaseLog {
	
	public static final int FILE_LOG_PERMISSION_REQUEST = 77;
	private static final String LOG_PATH = HGBaseFileTools.getExternalDir().getAbsolutePath() + "/Download/";
	private static File logFile =  null;
	private static boolean fileLogging = false;

	/**
	 * Prevent instantiation.
	 */
    private HGBaseLog() {
    	super();
    }
    
    /**
     * Returns the default tag for logging, i.e., the application name.
     * 
     * @return the default tag for logging
     */
    private static String getTag() {
    	return HGBaseAppTools.getAppName();
    }

    /**
     * @return the name of the logging file
     */
    public static String getLogFile() {
        return (logFile == null)? "" : logFile.getAbsolutePath();
    }

    /**
     * Sets the name of the file, where the logging is printed out.
     * If the name is empty the default Android logging is used.
     * If the file path is invalid, logging won't work.
     *
     * @param activity the activity that sets the file logging
     * @param logFileName name of the file or empty
     */
    public static void setLogFile(Activity activity, String logFileName) {
    	if (HGBaseTools.hasContent(logFileName)) {
    		String absoluteFileName = (logFileName.contains("/") || logFileName.contains("\\")) ? logFileName : LOG_PATH + logFileName;
    		if (!isFileLogging() || !absoluteFileName.equals(logFile.getAbsolutePath())) {
    			logFile = new File(absoluteFileName);
    			if (HGBaseAppTools.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
    				createLogFile();
    			} else {
    				HGBaseAppTools.requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, FILE_LOG_PERMISSION_REQUEST);
    			}
    		}
    	} else {
            logFile = null;
            fileLogging = false;
    	}
    }
    
    /**
     * Creates the log file. For internal use only (called by this class or by {@link HGBaseActivity}).
     */
    public static void createLogFile() {
	    if (!logFile.exists()) {
	        try {
	        	logFile.createNewFile();
	        	fileLogging = true;
	        	log("Create log file: " + getLogFile());
	        } catch (IOException e) {
	        	logError(e.getLocalizedMessage());
	        	logFile = null;
	        	fileLogging = false;
	        }
	    } else {
	    	fileLogging = true;
        	log("Open log file: " + getLogFile());
	    }
    }
    
    /**
     * @return true if file logging is activated, false if default Android logging is used
     */
    public static boolean isFileLogging() {
    	return fileLogging;
    }
    
    /**
     * Prints out the message.
     *
     * @param msg msg to log (in verbose mode)
     */
    public static void log(String msg) {
    	if (isFileLogging()) {
    		logToFile("-", msg);
    	} else {
    		Log.v(getTag(), msg);
    	}
    }

    /**
     * @param msg error message
     */
    public static void logError(String msg) {
    	if (isFileLogging()) {
    		logToFile("E", msg);
    	} else {
    		Log.e(getTag(), msg);
    	}
    }

    /**
     * @param msg warning message
     */
    public static void logWarn(String msg) {
    	if (isFileLogging()) {
    		logToFile("W", msg);
    	} else {
    		Log.w(getTag(), msg);
    	}
    }

    /**
     * @param msg info message
     */
    public static void logInfo(String msg) {
    	if (isFileLogging()) {
    		logToFile("I", msg);
    	} else {
    		Log.i(getTag(), msg);
    	}
    }

    /**
     * @param msg debug message
     */
    public static void logDebug(String msg) {
    	if (isFileLogging()) {
    		logToFile("D", msg);
    	} else {
    		Log.d(getTag(), msg);
    	}
    }
    
    /**
     * Logs the message to file. The file has to be valid when calling this function - it won't be checked.
     * 
     * @param level Code for the log level
     * @param msg the log message
     */
    private static void logToFile(String level, String msg) {    	
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = formatter.format(new Date());
        String text = date + "\t" + level + "\t" + getTag()  + "\t" + msg;
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.flush();
            buf.close();
        } catch (IOException e) {
            logError(e.getLocalizedMessage());
        }        
    }
}
