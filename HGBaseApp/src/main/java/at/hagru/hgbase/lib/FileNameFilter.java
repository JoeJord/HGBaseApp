package at.hagru.hgbase.lib;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A file filter that accepts all file names that are specified.
 * 
 * @author hagru
 */
public class FileNameFilter implements FileFilter {
    
    private final Set<String> acceptNames;
    
    /**
     * Create a new file name filter by specifying names to accept.
     * 
     * @param fileNames an arbitrary number of file names to be accepted by the filter
     */
    public FileNameFilter(String ... fileNames) {
        this.acceptNames = new HashSet<>(Arrays.asList(fileNames));
    }

    /* (non-Javadoc)
     * @see java.io.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File pathname) {
        return acceptNames.contains(pathname.getName());
    }

}
