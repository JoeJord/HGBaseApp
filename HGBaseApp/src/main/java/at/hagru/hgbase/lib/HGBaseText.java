/*
 * Created on 16. J�n. 2016
 */
package at.hagru.hgbase.lib;

import android.content.Context;
import android.content.res.Resources;

import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;

/**
 * Facade to the string resource of Android to use simple access methods to get a text for the current language.
 * 
 * @author hagru
 */
public final class HGBaseText {

	/**
	 * Prevent instantiation.
	 */
	private HGBaseText() {
		super();
	}
	
	/**
	 * @param name the message code
	 * @return the id or 0 if not found
	 */
	private static int getIdByName(String name) {
		return HGBaseResources.getResourceIdByName(name, HGBaseResources.STRING);
	}	

    /**
     * @param name a message code.
     * @return true if there exists a valid text.
     */
    public static boolean existsText(String name) {
        return (getIdByName(name) > 0);
    }
    
    /**
     * Returns the text for a code. <p>
     * If there is code defined, the text itself will be returned (in that case, formatArgs will be ignored).
     *
     * @param name code of the message
    *  @param formatArgs the format arguments that will be used for substitutions
     * @return text for the code or the parameter itself if no text is defined
     */
    public static String getText(String name, Object... formatArgs) {
    	int id = getIdByName(name);
    	return (id == 0)? name : getText(id, formatArgs);
    }
    
   /** 
    * Returns the text for a code
    *
    *  @param id id of the message
    *  @param formatArgs the format arguments that will be used for substitutions
    *  @return text for the message id
    */
   public static String getText(int id, Object... formatArgs) {    
	   Context appContext = HGBaseAppTools.getContext();
		try {
		    return (appContext == null)? String.valueOf(id) : appContext.getString(id, formatArgs);
		} catch (Resources.NotFoundException e) {
			HGBaseLog.logWarn("Resources.NotFoundException in HGBaseText.getText: " + e.getMessage());
		    return String.valueOf(id);
		}
   }
   
   /**
    * Returns the message for the given text or the default text if the code does not exist.
    * 
    * @param name code of the message 
    * @param defaultText the default text to display
    * @return the message or the default text
    */
   public static String getTextOrDefault(String name, String defaultText) {
	   return (existsText(name))? getText(name) : defaultText;
   }

}
