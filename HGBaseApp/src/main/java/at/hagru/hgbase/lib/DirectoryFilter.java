package at.hagru.hgbase.lib;

import java.io.File;
import java.io.FileFilter;

/**
 * A file filter class that looks for directories.
 *
 * @author hagru
 */
public class DirectoryFilter implements FileFilter {

    /* (non-Javadoc)
     * @see java.io.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }

}
