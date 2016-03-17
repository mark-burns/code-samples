package com.example.helper;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by mburns on 11/12/2015.
 */
public class FileHelper {

    public static final String APP_FILE_DIR = “app”;

    private static final boolean PREFER_EXTERNAL = false;
    private Context context;

    public FileHelper(Context context) {
        this.context = context;
    }

    /**
     * Check if a file indicated by the last path of a url exists in the
     * standard storage location of the application.
     *
     * @param urlString
     * @return
     */
    public boolean doesLocalFileExist(String urlString) {
        return doesLocalFileExist(urlString, APP_FILE_DIR);
    }

    /**
     * Check if a file indicated by the last path of a url exists in the
     * standard storage location of the application.
     *
     * @param urlString
     * @param subdir
     * @return
     */
    public boolean doesLocalFileExist(String urlString, String subdir) {
        if (urlString == null || urlString.isEmpty()) {
            return false;
        }
        File file = getLocalFileForUrl(urlString, subdir, false);
        return file.isFile();
    }

    /**
     * Get the local absolute path where a file would be stored from a url.
     *
     * @param urlString
     * @param mkdirs
     * @return
     */
    public String getLocalPathForUrl(String urlString, boolean mkdirs) {
        return getLocalPathForUrl(urlString, APP_FILE_DIR, mkdirs);
    }

    /**
     * Get the local absolute path where a file would be stored from a url.
     *
     * @param urlString
     * @param subdir
     * @param mkdirs
     * @return
     */
    public String getLocalPathForUrl(String urlString, String subdir, boolean mkdirs) {
        File path = getLocalFileForUrl(urlString, subdir, mkdirs);
        return path.getAbsolutePath();
    }

    /**
     * @param urlString
     * @param mkdirs
     * @return
     */
    public File getLocalFileForUrl(String urlString, boolean mkdirs) {
        return getLocalFileForUrl(urlString, APP_FILE_DIR, mkdirs);
    }

    /**
     * @param urlString
     * @param subdir
     * @param mkdirs
     * @return
     */
    public File getLocalFileForUrl(String urlString, String subdir, boolean mkdirs) {
        // Get the filename
        String filename = getFilenameFromUrl(urlString);
        if (filename == null) {
            return null;
        }

        // Get the storage directory
        File baseDir = getAppStorageDir();
        File dir = new File(baseDir, subdir);
        if (mkdirs) {
            dir.mkdirs();
        }

        return new File(dir, filename);
    }

    /**
     * Returns the last path segment from the uri.
     *
     * @param uriString
     * @return
     */
    public String getFilenameFromUrl(String uriString) {
        if (uriString == null || uriString.isEmpty()) {
            return null;
        }

        Uri uri = Uri.parse(uriString);
        return uri.getLastPathSegment();
    }

    /**
     * Returns the preferred base directory for storing files.
     *
     * @return
     */
    public File getAppStorageDir() {
        if (PREFER_EXTERNAL) {
            return context.getExternalFilesDir(null);
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * Download and save a file asynchronously from a url into the subdir of the
     * apps normal private storage location.
     *
     * @param urlString
     * @param subdir
     */
    public void saveFileAsyncFromUrl(final String urlString, final String subdir) {
        Thread workThread = new Thread() {
            public void run() {
                FileHelper.this.saveFileFromUrl(urlString, subdir);
            }
        };
        workThread.start();
    }

    /**
     * Download and save a file from a url into the subdir of the apps normal
     * private storage location.
     *
     * @param urlString
     * @param subdir
     */
    public void saveFileFromUrl(String urlString, String subdir) {

        if (urlString == null || urlString.isEmpty()) {
            return;
        }

        File outFile = getLocalFileForUrl(urlString, subdir, true);
        if (outFile == null || outFile.exists()) {
            return;
        }

        InputStream is = null;
        OutputStream os = null;

        try {
            URL url = new URL(urlString);
            is = new BufferedInputStream(url.openStream(), 10240);
            os = new FileOutputStream(outFile);
            byte buffer[] = new byte[1024];
            int dataSize;
            while ((dataSize = is.read(buffer)) != -1) {
                os.write(buffer, 0, dataSize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close streams
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e2) {}
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e2) {}
            }
        }

    }
}
