package at.hagru.hgbase.android;

/**
 * The type of the application.
 *
 * @author Josef Jordan
 */
public enum APPTYPE {
    FREE, PRO;

    /**
     * Returns the application type corresponding to the specified string ignoring the case.<br>
     * If no corresponding application type is found, {@code null} is returned.
     * 
     * @param value The application type as string.
     * @return The application type corresponding to the specified string or {@code null}.
     */
    public static APPTYPE valueOfIgnoreCase(String value) {
	for (APPTYPE appType : APPTYPE.values()) {
	    if (appType.name().equalsIgnoreCase(value)) {
		return appType;
	    }
	}
	return null;
    }

    /**
     * Returns {@code true} if a function is available in the specified application type.
     * 
     * @param isProVersion Flag, if the application is currently the pro version.
     * @param linkAppType The flag in which application version the link should be shown. If {@code null}, then there is no restriction.
     * @return {@code true} if a function is available in the specified application type.
     */
    public static boolean functionAvailable(boolean isProVersion, APPTYPE linkAppType) {
	return (linkAppType == null)
		|| (isProVersion ? APPTYPE.PRO.equals(linkAppType) : APPTYPE.FREE.equals(linkAppType));
    }
}
