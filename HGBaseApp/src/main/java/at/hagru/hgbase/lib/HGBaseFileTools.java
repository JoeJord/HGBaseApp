package at.hagru.hgbase.lib;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;

/**
 * Utility class for file related operations, adapted for Android.
 * 
 * @author hagru
 */
public final class HGBaseFileTools {

    public static final String JAR_EXTENSION = "jar";
    public static final String ZIP_EXTENSION = "zip";

    private static final int BUFFER_SIZE = 1024;

    /**
     * Prevent external instantiation of utility class.
     */
    private HGBaseFileTools() {
        super();
    }

    /**
     * Returns the current directory as a file.
     * 
     * @return current dir
     */
    public static File getCurrentDir() {
        return HGBaseAppTools.getContext().getFilesDir();
    }
    
    /**
     * Returns the external root directory as file.
     * 
     * @return the external root directory
     */
    public static File getExternalDir() {
		return Environment.getExternalStorageDirectory();
    }

    /**
     * Return the extension of the file name
     * 
     * @param f a file object
     * @return extension
     */
    public static String getFileExtension(File f) {
        return (f != null) ? getFileExtension(f.getName()) : "";
    }

    /**
     * Return the extension of the file name
     * 
     * @param f a file object
     * @return extension
     */
    public static String getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) {
            return filename.substring(i + 1).toLowerCase(Locale.getDefault());
        } else {
            return "";
        }
    }

    /**
     * If there is no extension, the default extension is set.
     * 
     * @param f The file to check for the extension.
     * @param defaultExtension The default file extension (if the string is separated by semicolons, the first
     *            extension is taken).
     * @return file with correct extension.
     */
    public static File checkExtension(File f, String defaultExtension) {
        if (f == null) {
            return f;
        }
        String name = f.getName();
        if (!f.exists() && name.indexOf('.') == -1) {
            String extension = (defaultExtension == null) ? "" : defaultExtension;
            StringTokenizer st = new StringTokenizer(extension, ";");
            if (st.hasMoreTokens()) {
                String ext = st.nextToken();
                return new File(f.getPath() + "." + ext);
            }
        }
        return f;
    }

    /**
     * @param path The complete path for a file.
     * @param withExtension <code>false</code> to get only the name, or <code>true</code> to get also the
     *            extension
     * @return The filename.
     */
    public static String getFileName(String path, boolean withExtension) {
        String name = new File(path).getName();
        if (!withExtension) {
            name = removeFileExtension(name);
        }
        return name;
    }

	/**
	 * @param name the file name that may have a file extension
	 * @return the file name without extension
	 */
	public static String removeFileExtension(String name) {
		int ie = name.lastIndexOf('.');
		if (ie >= 0) {
		    name = name.substring(0, ie);
		}
		return name;
	}

    /**
     * If it's a relative Path, the absolute path is returned, otherwise nothing is changed. 
     * If file is null then null will be returned.
     * 
     * @param file name of file
     * @return the absolute path
     */
    public static String getAbsolutePath(String file) {
    	return getAbsolutePath(file, getCurrentDir());
    }
    
    /**
     * If it's a relative Path, the absolute path is returned depending on the base directory, otherwise nothing is changed. 
     * If file is null then null will be returned.
     * 
     * @param file name of the file
     * @param baseDir the base directory
     * @return the absolute path
     */
    public static String getAbsolutePath(String file, File baseDir) {
        if (HGBaseTools.hasContent(file) && baseDir != null && baseDir.isDirectory() &&  file.charAt(0) != '/' && file.indexOf(':') < 0) {
            return new File(baseDir, file).getAbsolutePath();
        } else {
            return file;
        }
    }

    /**
     * Checks if it is possible to write to the external file system.
     * 
     * @return true if it possible to write to the external file system
     */
    public static boolean isExternalFileSystemWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }

    /**
     * Checks if it is possible to read from the external file system.
     * 
     * @return true if it possible to read from the external file system
     */
    public static boolean isExternalFileSystemReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) 
        		|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
    
    /**
     * Returns the file object for a file on the internal file system.
     * 
     * @param fileName the file name
     * @return the file object
     */
    public static File getFileForIntern(String fileName) {
    	return new File(getAbsolutePath(fileName));
    }
    
    /**
     * Returns the file object for a file on the external file system.
     * 
     * @param fileName the file name
     * @return the file object
     */
    public static File getFileForExtern(String fileName) {
    	return new File(getAbsolutePath(fileName, getExternalDir()));
    }
    
    /**
     * Opens an input stream for a fully qualified file name.
     * Will return null in case of error and log this error with the given message.
     * 
     * @param file a file object
     * @param errorMessage the error message
     * @return an input stream or null in case of error
     */
    public static InputStream openFileStream(File file, String errorMessage) {
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (IOException e) {
            if (HGBaseTools.hasContent(errorMessage)) {
                HGBaseLog.logError(errorMessage + " " + e.getMessage());
            }
            return null;
        }
    }
    
    /**
     * Opens an input stream for a given file name that is stored in the internal file system of the app.
     * 
     * @param fileName name of the file
     * @return an input stream or null in case of error
     */
    public static InputStream openInternalFileStream(String fileName) {
    	File absoluteFile = new File(getAbsolutePath(fileName));
    	return openFileStream(absoluteFile, "File '" + fileName + "' could not be read from internal file system!");
    }
    
    /**
     * Opens an input stream for a given file name that is stored in the external file system of the app.<p>
     * Requires <b>&lt;uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/&gt;</b> in the manifest file!
     * 
     * @param fileName name of the file
     * @return an input stream or null in case of error
     * @
     */
    public static InputStream openExternalFileStream(String fileName) {
        String errorMessage = "File '" + fileName + "' could not be read from external file system!";
        if (isExternalFileSystemReadable()) {
            File absoluteFile = new File(getAbsolutePath(fileName, getExternalDir()));
            return openFileStream(absoluteFile, errorMessage);
        } else {
            HGBaseLog.logError(errorMessage + " " + "Cannot access external file system!");
            return null;
        }
    }

    /**
     * Updates the resource file name by adding the resource type if it is not already given by the name.<p>
     * This is only necessary when identifying by the general resource object.
     * 
     * @param fileName the name of the resource file
     * @param resourceType the resource type (typically "raw")
     * @return the update file name or the original one if it includes already the resource type
     */
    private static String updateResourceName(String fileName, String resourceType) {
        if (!fileName.startsWith(resourceType + "/")) {
            return ((fileName.startsWith("/")) ? resourceType : resourceType + "/") + fileName;
        } else {
            return fileName;
        }
    }

    /**
     * Opens an input stream for a given file name that is stored in the <b>raw</b> resource directory of the app.
     * 
     * @param fileName name of the file
     * @return an input stream or null in case of error
     */
    public static InputStream openRawResourceFileStream(String fileName) {
    	fileName = updateResourceName(fileName, HGBaseResources.RAW);
    	int resourceId = HGBaseResources.getResourceIdByName(fileName, HGBaseResources.RAW);
    	return openRawResourceFileStream(resourceId);
    }
    
    /**
     * Opens an input stream for a given resource id related to the <b>raw</b> resources of the app.
     * 
     * @param resourceId the id of the raw resource
     * @return an input stream or null in case of error
     */
    public static InputStream openRawResourceFileStream(int resourceId) {
    	return (resourceId == 0)? null : HGBaseAppTools.getContext().getResources().openRawResource(resourceId);
    }
    
	/**
	 * @param filePath a file path, can include sub-directories
	 * @return the file path that will work for the assets directory
	 */
	public static String correctAssetsPath(String filePath) {
		String assetPath = filePath;
		if (assetPath.startsWith("/")) {
			assetPath = assetPath.substring(1, assetPath.length());
		}
		return assetPath;
	}
        
    /**
     * Opens an input stream for a given file name that is stored in the <b>assets</b> resource directory of the app.
     * 
     * @param fileName name of the file
     * @return an input stream or null in case of error
     */
    public static InputStream openAssetsFileStream(String fileName) {
        try {
            return HGBaseAppTools.getContext().getResources().getAssets().open(correctAssetsPath(fileName));
        } catch (IOException e) {
            HGBaseLog.logError("Could not open asset '" + fileName + "'! " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Opens an Uri and returns the input stream or null if path does not exists.
     * 
     * @param uriPath the Uri path
     * @return the input stream or null
     */
    public static InputStream openUriStream(Uri uriPath) {
        try {
			return HGBaseAppTools.getContext().getContentResolver().openInputStream(uriPath);
		} catch (FileNotFoundException e) {
			try {
				return HGBaseAppTools.getContext().getAssets().open(uriPath.getPath());
			} catch (IOException e1) {
				return null;
			}
		}    	
    }
    
    /**
     * Opens a connection to the specified URL and returns an <code>InputStream</code> for reading from that
     * connection.
     * 
     * @param url the <code>String</code> to parse as URL
     * @return an input stream for reading from the URL connection or null in case of error
     */
    public static InputStream openUrlStream(String url) {
        try {
            return openUrlStream(new URL(url));
        } catch (MalformedURLException e) {
            HGBaseLog.logError("Cannot parse URL! " + e.getMessage());
            return null;
        }
    }

    /**
     * Opens a connection to the specified URL and returns an <code>InputStream</code> for reading from that
     * connection.
     * 
     * @param url the URL to open the stream for
     * @return an input stream for reading from the URL connection or null in case of error
     */
    public static InputStream openUrlStream(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            HGBaseLog.logError("Cannot open URL! " + e.getMessage());
            return null;
        }
    }    

    /**
     * Opens an output stream for a fully qualified file name.
     * Will return null in case of error and log this error with the given message.
     * 
     * @param fileName name of the file
     * @param errorMessage the error message
     * @return an output stream or null in case of error
     */
    private static OutputStream createFileOutputStream(String fileName, String errorMessage) {
        try {
            return new FileOutputStream(fileName);
        } catch (IOException e) {
            HGBaseLog.logError(errorMessage + " " + e.getMessage());
            return null;
        }    	
    }
    
    /**
     * Opens an output stream for a given file name that is stored in the internal file system of the app.
     * 
     * @param fileName name of the file
     * @return an input stream or null in case of error
     */
    public static OutputStream createInternalFileOutputStream(String fileName) {
    	String absoluteFileName = getAbsolutePath(fileName);
    	return createFileOutputStream(absoluteFileName, "File '" + fileName + "' could not be saved to internal file system!");
    }
    
    /**
     * Opens an output stream for a given file name that is stored in the external file system of the app.<p>
     * Requires <b>&lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/&gt;</b> in the manifest file when writing data!
     * 
     * @param fileName name of the file
     * @return an input stream or null in case of error
     */
    public static OutputStream createExternalFileOutputStream(String fileName) {
        String errorMessage = "File '" + fileName + "' could not be saved to external file system!";
        if (isExternalFileSystemWritable()) {
            String absoluteFileName = getAbsolutePath(fileName, getExternalDir());
            return createFileOutputStream(absoluteFileName, errorMessage);
        } else {
            HGBaseLog.logError(errorMessage + " " + "Cannot access external file system!");
            return null;
        }
    }

    /**
     * Copy an input stream to an output stream.
     * 
     * @param in the input stream
     * @param out the output stream
     * @param withClose true to close both streams, false to keep them open
     * @throws IOException error when copying streams
     */
    public static void copyStream(InputStream in, OutputStream out, boolean withClose) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        try {
            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
            }
        } finally {
            if (withClose) {
                closeStream(in, true);
                closeStream(out, true);
            }
        }
    }

    /**
     * Transfers <code>count</code> bytes from the specified input stream to the file output stream.
     * 
     * @param in The stream from which bytes are to be read.
     * @param out The stream to which bytes are to be written.
     * @param position The position within the file at which the transfer is to begin; must be non-negative.
     * @param count The maximum number of bytes to be transferred; must be non-negative.
     * @param withClose true to close both streams, false to keep them open
     * @throws IOException If some other I/O error occurs.
     */
    public static void copyStream(InputStream in, FileOutputStream out, long position, long count,
            boolean withClose) throws IOException {
        try {
            ReadableByteChannel rbc = Channels.newChannel(in);
            out.getChannel().transferFrom(rbc, position, count);
        } finally {
            if (withClose) {
                closeStream(in, true);
                closeStream(out, true);
            }
        }
    }

    /**
     * Returns the string for a given input stream.
     * 
     * @param in input stream.
     * @return string or "".
     */
    public static String getString(InputStream in) {
    	if (in == null) {
    		return "";
    	}
    	HGBaseStringBuilder sb = new HGBaseStringBuilder("\n");
        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, getEncoding()));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            // NOCHECK: ignore exceptions, empty string will be returned
        } finally {
        	closeStream(in, true);
        }
        return sb.toString();        	
        //return "";*/
    }

    /**
     * Close the given stream safely, exceptions can be logged.
     * 
     * @param stream the stream to close
     * @param withLogging <code>true</code> for logging exception as errors, <code>false</code> to ignore
     *            exceptions
     */
    public static void closeStream(Closeable stream, boolean withLogging) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                if (withLogging) {
                    HGBaseLog.logError(e.getMessage());
                }
            }
        }
    }

    /**
     * Close the given stream safely, exceptions will be ignored.
     * 
     * @param stream the stream to close
     */
    public static void closeStream(Closeable stream) {
        closeStream(stream, false);
    }

    /**
     * Copies the source file to the destination file or folder. If the destination is a folder, then a file
     * with the same name as the source will be created.
     * 
     * @param sourceFile The source file, must not be a folder.
     * @param destFile The destination file, may be a folder.
     * @return <code>true</code> if the file was copied successfully or <code>false</code> otherwise.
     */
    public static boolean copyFile(File sourceFile, File destFile) {
        if (destFile.isDirectory()) {
            destFile = new File(destFile, sourceFile.getName());
        }
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(sourceFile);
            os = new FileOutputStream(destFile);
            copyStream(is, os, 0, Long.MAX_VALUE, true);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            closeStream(is);
            closeStream(os);
        }
    }

    /**
     * Copies files from the {@code sourceFolder} to the {@code destFolder}. Sub directories will not be
     * copied.
     * 
     * @param sourceFolder The source folder.
     * @param destFolder The destination folder.
     * @param filter filter out whether a file shall be copied, may be null to copy all files
     * @return The number of copied files or 0.
     */
    public static int copyFiles(File sourceFolder, File destFolder, FileFilter filter) {
        if ((!sourceFolder.isDirectory()) || (!destFolder.isDirectory()) || (sourceFolder.equals(destFolder))) {
            return 0;
        }
        int count = 0;
        for (File file : sourceFolder.listFiles()) {
            if ((file.isFile()) && ((filter == null) ? true : filter.accept(file))) {
                copyFile(file, destFolder);
            }
        }
        return count;
    }

    /**
     * Searches for a file in a directory. If {@code recursive} is {@code true} all sub directories will be
     * searched, too.<br>
     * The first appearance will be taken.<br>
     * The search is not case sensitive.
     * 
     * @param startFolder The folder where to start.
     * @param filename The name of the file to search.
     * @param recursive If {@code true} all sub directories will be searched, too.
     * @return The absolute path to the found file or {@code null} if the file was not found.
     */
    public static String searchFile(File startFolder, String filename, boolean recursive) {
        if (startFolder != null && startFolder.isDirectory()) {
            Set<File> folders = new HashSet<>();
            for (File file : startFolder.listFiles()) {
                if (file.isFile()) {
                    if (file.getName().equalsIgnoreCase(filename)) {
                        return file.getAbsolutePath();
                    }
                } else {
                    folders.add(file);
                }
            }
            if (recursive) {
                for (File folder : folders) {
                    String found = searchFile(folder, filename, recursive);
                    if (HGBaseTools.hasContent(found)) {
                        return found;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Deletes the given file or folder, if that did not work it shall be deleted on exit.
     * 
     * @param file the file or folder to delete
     */
    public static void deleteFileNowOrOnExit(File file) {
        if (!file.delete()) {
            file.deleteOnExit();
        }
    }
    
    /**
     * Deletes a folder recursively, i.e., if folder is not empty all files in the folder will be also deleted.
     * 
     * @param folder the folder to be deleted, must not be null
     * @return true if deletion was successful, otherwise false
     */
    public static boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                if ((f.isDirectory() && !deleteFolder(f)) || f.isFile() && !f.delete()) {
                    return false;
                }
            }
            return folder.delete();
        } else {
            return false;
        }
    }

    /**
     * Just return the default encoding of Android.
     *
     * @return The xml encoding.
     */
    public static String getEncoding() {
        return "UTF-8";
    }
    
    /**
     * Saves the given bitmap to the default picture folder or the full path.
     * 
     * @param bitmap the bitmap to save
     * @param fileName the file name, must not be empty (if it starts not with '/' the file will be saved into the picture folder)
     * @return the file where the bitmap was saved or null if saving was not successful
     */
    public static File saveBitmapToPictureFolder(Bitmap bitmap, String fileName) {
        String imageDir = (fileName.charAt(0) == '/')
        				? "" 
        				: Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File imageFile = new File(imageDir, fileName);
        OutputStream outStream = HGBaseFileTools.createExternalFileOutputStream(imageFile.getAbsolutePath());
        if (outStream != null) {
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
	        HGBaseFileTools.closeStream(outStream);
	        return imageFile;
        } else {
        	return null;
        }
    }
    
}
