package com.idroid.quickeventscheduler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xeustechnologies.jtar.TarEntry;
import org.xeustechnologies.jtar.TarInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Created by sunil on 3/30/15.
 */
public class OCRInit extends AsyncTask<String, String, Boolean>{

    private static final String TAG = OCRInit.class.getSimpleName();

    private static final String[] CUBE_FILES = {
            ".cube.bigrams",
            ".cube.fold",
            ".cube.lm",
            ".cube.nn",
            ".cube.params",
            ".cube.word-freq",
            ".tesseract_cube.nn",
            ".traineddata"
    };


    private MainActivity activity;
    private Context context;
    private ProgressDialog progressDialog;
    private String OCRlanguageCode;
    private String OCRlanguageName;
    private int ocrEngineMode;

    public OCRInit(MainActivity activity, ProgressDialog dialog, String languageCode, String languageName,
                     int ocrEngineMode) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.progressDialog = dialog;
        this.OCRlanguageCode = languageCode;
        this.OCRlanguageName = languageName;
        this.ocrEngineMode = ocrEngineMode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Checking for data installation...");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    protected Boolean doInBackground(String... params) {

       // Check whether we need Cube data or Tesseract data. Eg: "tesseract-ocr-3.01.eng.tar"/ "eng.traineddata"

        String destinationFilenameBase = OCRlanguageCode + ".traineddata";
        boolean isCubeSupported = true;

        destinationFilenameBase = "tesseract-ocr-3.02." + OCRlanguageCode + ".tar";

        // Check for, and create if necessary, folder to hold model data
        String destinationDirBase = params[0];

        File tessdataDir = new File(destinationDirBase + File.separator + "tessdata");
        if (!tessdataDir.exists() && !tessdataDir.mkdirs()) {
            Log.e(TAG, "Couldn't make directory " + tessdataDir);
            return false;
        }

        // Create a reference to the file to save the download in
        File downloadFile = new File(tessdataDir, destinationFilenameBase);

        // Check if an incomplete download is present. If a *.download file is there, delete it and
        // any (possibly half-unzipped) Tesseract and Cube data files that may be there.
        File incomplete = new File(tessdataDir, destinationFilenameBase + ".download");
        File tesseractTestFile = new File(tessdataDir, OCRlanguageCode + ".traineddata");
        if (incomplete.exists()) {
            incomplete.delete();
            if (tesseractTestFile.exists()) {
                tesseractTestFile.delete();
            }
            deleteExistingCubeDataFiles(tessdataDir);
        }

        // Check whether all Cube data files have already been installed
        boolean isAllCubeDataInstalled = false;
        if (isCubeSupported) {
            boolean isAFileMissing = false;
            File dataFile;
            for (String s : CUBE_FILES) {
                dataFile = new File(tessdataDir.toString() + File.separator + OCRlanguageCode + s);
                if (!dataFile.exists()) {
                    isAFileMissing = true;
                }
            }
            isAllCubeDataInstalled = !isAFileMissing;
        }

        //Install language data files which are not present
        boolean installSuccess = false;
        if (!tesseractTestFile.exists()
                || (isCubeSupported && !isAllCubeDataInstalled)) {
            Log.d(TAG, "Language data for " + OCRlanguageCode + " not found in " + tessdataDir.toString());
            deleteExistingCubeDataFiles(tessdataDir);

            // Check assets for language data to install. If not present, download from Internet
            try {
                Log.d(TAG, "Checking for language data (" + destinationFilenameBase
                        + ".zip) in application assets...");

                // Check for a file like "eng.traineddata.zip" or "tesseract-ocr-3.01.eng.tar.zip"
                installSuccess = installFromAssets(destinationFilenameBase + ".zip", tessdataDir,
                        downloadFile);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            } catch (Exception e) {
                Log.e(TAG, "Got exception", e);
            }

            if (!installSuccess) {
                // File was not packaged in assets, so download it
                Log.d(TAG, "Downloading " + destinationFilenameBase + ".gz...");
                try {



                    installSuccess = downloadFile(destinationFilenameBase, downloadFile);
                    if (!installSuccess) {
                        Log.e(TAG, "Download failed");
                        return false;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException received in doInBackground. Check network connection");
                    return false;
                }
            }

            String extension = destinationFilenameBase.substring(
                    destinationFilenameBase.lastIndexOf('.'),
                    destinationFilenameBase.length());
            if (extension.equals(".tar")) {
                try {
                    untarFile(new File(tessdataDir.toString() + File.separator + destinationFilenameBase),
                            tessdataDir);
                    installSuccess = true;
                } catch (IOException e) {
                    Log.e(TAG, "Untar failed");
                    return false;
                }
            }

        } else {
            Log.d(TAG, "Language data for " + OCRlanguageCode + " already installed in "
                    + tessdataDir.toString());
            installSuccess = true;
        }

        // If OSD data file is not present, download it
        File osdFile = new File(tessdataDir, activity.OSD_FILENAME_BASE);
        boolean osdInstallSuccess = false;
        if (!osdFile.exists()) {
            // Check assets for language data to install. If not present, download from Internet
            OCRlanguageName = "orientation and script detection";
            try {
                // Check for, and delete, partially-downloaded OSD files
                String[] badFiles = { activity.OSD_FILENAME + ".gz.download",
                        activity.OSD_FILENAME + ".gz", activity.OSD_FILENAME };
                for (String filename : badFiles) {
                    File file = new File(tessdataDir, filename);
                    if (file.exists()) {
                        file.delete();
                    }
                }

                Log.d(TAG, "Checking for OSD data (" + activity.OSD_FILENAME_BASE
                        + ".zip) in application assets...");


                osdInstallSuccess = installFromAssets(activity.OSD_FILENAME_BASE + ".zip",
                        tessdataDir, new File(activity.OSD_FILENAME));
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            } catch (Exception e) {
                Log.e(TAG, "Got exception", e);
            }

            if (!osdInstallSuccess) {
                // File was not packaged in assets, so download it
                Log.d(TAG, "Downloading " + activity.OSD_FILENAME + ".gz...");
                try {
                    osdInstallSuccess = downloadFile(activity.OSD_FILENAME, new File(tessdataDir,
                            activity.OSD_FILENAME));
                    if (!osdInstallSuccess) {
                        Log.e(TAG, "Download failed");
                        return false;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException received in doInBackground. Is a network connection available?");
                    return false;
                }
            }

            // Untar the OSD tar file
            try {
                untarFile(new File(tessdataDir.toString() + File.separator + activity.OSD_FILENAME),
                        tessdataDir);
            } catch (IOException e) {
                Log.e(TAG, "Untar failed");
                return false;
            }

        } else {
            Log.d(TAG, "OSD file already present in " + tessdataDir.toString());
            osdInstallSuccess = true;
        }

        try {
            progressDialog.dismiss();
        } catch (IllegalArgumentException e) {
        }

            return installSuccess && osdInstallSuccess;
    }

    /**
     * Delete any existing data/partially uncompressed files for Cube that are present in the given directory.
     */
    private void deleteExistingCubeDataFiles(File tessdataDirectory) {
        File badFile;
        for (String s : CUBE_FILES) {
            badFile = new File(tessdataDirectory.toString() + File.separator + OCRlanguageCode + s);
            if (badFile.exists()) {
                Log.d(TAG, "Deleting existing file " + badFile.toString());
                badFile.delete();
            }
            badFile = new File(tessdataDirectory.toString() + File.separator + "tesseract-ocr-3.01."
                    + OCRlanguageCode + ".tar");
            if (badFile.exists()) {
                Log.d(TAG, "Deleting existing file " + badFile.toString());
                badFile.delete();
            }
        }
    }

    /**
     * Download a file from the site specified by DOWNLOAD_BASE, and gunzip to the given destination.
     */

    private boolean downloadFile(String sourceFilenameBase, File destinationFile)
            throws IOException {
        try {
            return downloadGzippedFile(new URL(activity.DOWNLOAD_BASE + sourceFilenameBase +
                            ".gz"),
                    destinationFile);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Bad URL string.");
        }
    }

    /**
     * Download a gzipped file using an HttpURLConnection, and gunzip it to the given destination.
     */
    private boolean downloadGzippedFile(URL url, File destinationFile)
            throws IOException {

        publishProgress("Downloading data for " + OCRlanguageName + "...", "0");

        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setAllowUserInteraction(false);
        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            Log.e(TAG, "Did not get HTTP_OK response.");
            Log.e(TAG, "Response code: " + urlConnection.getResponseCode());
            Log.e(TAG, "Response message: " + urlConnection.getResponseMessage().toString());
            return false;
        }

        int fileSize = urlConnection.getContentLength();
        InputStream inputStream = urlConnection.getInputStream();
        File tempFile = new File(destinationFile.toString() + ".gz.download");

        // Stream the file contents to a local file temporarily
        Log.d(TAG, "Streaming download to " + destinationFile.toString() + ".gz.download...");
        final int BUFFER = 8192;
        FileOutputStream fileOutputStream = null;
        Integer percentComplete;
        int percentCompleteLast = 0;

        try {
            fileOutputStream = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception received when opening FileOutputStream.", e);
        }


        int downloaded = 0;
        byte[] buffer = new byte[BUFFER];
        int bufferLength = 0;

        while ((bufferLength = inputStream.read(buffer, 0, BUFFER)) > 0) {
            fileOutputStream.write(buffer, 0, bufferLength);
            downloaded += bufferLength;
            percentComplete = (int) ((downloaded / (float) fileSize) * 100);
            if (percentComplete > percentCompleteLast) {
                publishProgress(
                        "Downloading data for " + OCRlanguageName + "...",
                        percentComplete.toString());
                percentCompleteLast = percentComplete;
            }
        }

        fileOutputStream.close();

        if (urlConnection != null) {
            urlConnection.disconnect();
        }

        // Uncompress the downloaded temporary file into place, and remove the temporary file
        try {
            Log.d(TAG, "Unzipping...");
            gunzip(tempFile,
                    new File(tempFile.toString().replace(".gz.download", "")));
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not available for unzipping.");
        } catch (IOException e) {
            Log.e(TAG, "Problem unzipping file.");
        }
        return false;
    }

    /**
     * Unzips the given Gzipped file to the given destination, and deletes the
     * gzipped file.
     */
    private void gunzip(File zippedFile, File outFilePath)
            throws FileNotFoundException, IOException {
        int uncompressedFileSize = getGzipSizeUncompressed(zippedFile);
        Integer percentComplete;
        int percentCompleteLast = 0;
        int unzippedBytes = 0;
        final Integer progressMin = 0;
        int progressMax = 100 - progressMin;
        publishProgress("Uncompressing data for " + OCRlanguageName + "...",
                progressMin.toString());

        // If the file is a tar file, just show progress to 50%
        String extension = zippedFile.toString().substring(
                zippedFile.toString().length() - 16);
        if (extension.equals(".tar.gz.download")) {
            progressMax = 50;
        }
        GZIPInputStream gzipInputStream = new GZIPInputStream(
                new BufferedInputStream(new FileInputStream(zippedFile)));
        OutputStream outputStream = new FileOutputStream(outFilePath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                outputStream);

        final int BUFFER = 8192;
        byte[] data = new byte[BUFFER];
        int len;
        while ((len = gzipInputStream.read(data, 0, BUFFER)) > 0) {
            bufferedOutputStream.write(data, 0, len);
            unzippedBytes += len;
            percentComplete = (int) ((unzippedBytes / (float) uncompressedFileSize) * progressMax)
                    + progressMin;

            if (percentComplete > percentCompleteLast) {
                publishProgress("Uncompressing data for " + OCRlanguageName
                        + "...", percentComplete.toString());
                percentCompleteLast = percentComplete;
            }
        }
        gzipInputStream.close();
        bufferedOutputStream.flush();
        bufferedOutputStream.close();

        if (zippedFile.exists()) {
            zippedFile.delete();
        }
    }

    /**
     * Returns the uncompressed size for a Gzipped file.
     */
    private int getGzipSizeUncompressed(File zipFile) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(zipFile, "r");
        raf.seek(raf.length() - 4);
        int b4 = raf.read();
        int b3 = raf.read();
        int b2 = raf.read();
        int b1 = raf.read();
        raf.close();
        return (b1 << 24) | (b2 << 16) + (b3 << 8) + b4;
    }

    /**
     * Untar the contents of a tar file into the given directory (Uses jtar: http://code.google.com/p/jtar/),
     * ignoring the relative pathname in the tar file, and delete the tar file.
     */
    private void untarFile(File tarFile, File destinationDir) throws IOException {
        Log.d(TAG, "Untarring...");
        final int uncompressedSize = getTarSizeUncompressed(tarFile);
        Integer percentComplete;
        int percentCompleteLast = 0;
        int unzippedBytes = 0;
        final Integer progressMin = 50;
        final int progressMax = 100 - progressMin;
        publishProgress("Uncompressing data for " + OCRlanguageName + "...",
                progressMin.toString());

        // Extract all the files
        TarInputStream tarInputStream = new TarInputStream(new BufferedInputStream(
                new FileInputStream(tarFile)));
        TarEntry entry;
        while ((entry = tarInputStream.getNextEntry()) != null) {
            int len;
            final int BUFFER = 8192;
            byte data[] = new byte[BUFFER];
            String pathName = entry.getName();
            String fileName = pathName.substring(pathName.lastIndexOf('/'), pathName.length());
            OutputStream outputStream = new FileOutputStream(destinationDir + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            Log.d(TAG, "Writing " + fileName.substring(1, fileName.length()) + "...");
            while ((len = tarInputStream.read(data, 0, BUFFER)) != -1) {
                bufferedOutputStream.write(data, 0, len);
                unzippedBytes += len;
                percentComplete = (int) ((unzippedBytes / (float) uncompressedSize) * progressMax)
                        + progressMin;
                if (percentComplete > percentCompleteLast) {
                    publishProgress("Uncompressing data for " + OCRlanguageName + "...",
                            percentComplete.toString());
                    percentCompleteLast = percentComplete;
                }
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }
        tarInputStream.close();

        if (tarFile.exists()) {
            tarFile.delete();
        }
    }


    // Return the uncompressed size for a Tar file.
    private int getTarSizeUncompressed(File tarFile) throws IOException {
        int size = 0;
        TarInputStream tis = new TarInputStream(new BufferedInputStream(
                new FileInputStream(tarFile)));
        TarEntry entry;
        while ((entry = tis.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                size += entry.getSize();
            }
        }
        tis.close();
        return size;
    }

    // Install a file from application assets to device external storage.

     // @param modelRoot
     // Directory on SD card to install the file to

    private boolean installFromAssets(String sourceFilename, File modelRoot,
                                      File destinationFile) throws IOException {
        String extension = sourceFilename.substring(sourceFilename.lastIndexOf('.'),
                sourceFilename.length());
        try {
            if (extension.equals(".zip")) {
                return installZipFilesFromAssets(sourceFilename, modelRoot, destinationFile);
            } else {
                throw new IllegalArgumentException("Extension " + extension
                        + " is unsupported.");
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Language not available in application assets.");
        }
        return false;
    }

    /**
     * Unzip the given Zip file, located in application assets, into the given
     * destination file.
     */
    private boolean installZipFilesFromAssets(String sourceFilename,
                                         File destinationDir, File destinationFile) throws IOException,
            FileNotFoundException {
        publishProgress("Uncompressing data for " + OCRlanguageName + "...", "0");
        ZipInputStream inputStream = new ZipInputStream(context.getAssets().open(sourceFilename));

        // Loop through all the files and folders in the zip archive (but there should just be one)
        for (ZipEntry entry = inputStream.getNextEntry(); entry != null; entry = inputStream
                .getNextEntry()) {
            destinationFile = new File(destinationDir, entry.getName());

            if (entry.isDirectory()) {
                destinationFile.mkdirs();
            } else {

                long zippedFileSize = entry.getSize();

                // Create a file output stream
                FileOutputStream outputStream = new FileOutputStream(destinationFile);
                final int BUFFER = 8192;

                // Buffer the output to the file
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER);
                int unzippedSize = 0;

                // Write the contents
                int count = 0;
                Integer percentComplete = 0;
                Integer percentCompleteLast = 0;
                byte[] data = new byte[BUFFER];
                while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
                    bufferedOutputStream.write(data, 0, count);
                    unzippedSize += count;
                    percentComplete = (int) ((unzippedSize / (long) zippedFileSize) * 100);
                    if (percentComplete > percentCompleteLast) {
                        publishProgress("Uncompressing data for " + OCRlanguageName + "...",
                                percentComplete.toString(), "0");
                        percentCompleteLast = percentComplete;
                    }
                }
                bufferedOutputStream.close();
            }
            inputStream.closeEntry();
        }
        inputStream.close();
        return true;
    }


    // Update the dialog box with the latest incremental progress.

    @Override
    protected void onProgressUpdate(String... message) {
        super.onProgressUpdate(message);
        int percentComplete = 0;

        percentComplete = Integer.parseInt(message[1]);
        progressDialog.setMessage(message[0]);
        progressDialog.setProgress(percentComplete);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result) {
            // Restart recognition
            activity.captureImage();
           // Intent iCameraActivity = new Intent(

        }
    }

}
