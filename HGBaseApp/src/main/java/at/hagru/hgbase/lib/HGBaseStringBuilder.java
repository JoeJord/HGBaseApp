package at.hagru.hgbase.lib;

/**
 * A simple string builder that allows to append {@link String} objects and separates them by the given separator.
 * 
 * @author hagru
 */
public class HGBaseStringBuilder {
    
    private final String separator;
    private final StringBuilder builder = new StringBuilder();
    private boolean firstAppend = true;

    /**
     * 
     * @param separator the separator between the strings to append, may be null or empty but in that case the
     *                  use of {@link StringBuilder} is preferable
     */
    public HGBaseStringBuilder(String separator) {
        this.separator = (separator == null)? "" : separator;
    }
    
    /**
     * Appends the given text. If it is not the first text to append it will be separated by the separator
     * passed to the constructor.
     * 
     * @param text the text to append
     * @return this builder object
     */
    public HGBaseStringBuilder append(String text) {
        if (firstAppend) {
            firstAppend = false;
        } else {
            builder.append(separator);
        }
        builder.append(text);
        return this;
    }

    /**
     * Makes a string object calling the internal {@link StringBuilder}'s {@code toString} method.
     * 
     * @return the string representation of the appended string objects separated by the separator
     */
    @Override
    public String toString() {
        return builder.toString();
    }

}
