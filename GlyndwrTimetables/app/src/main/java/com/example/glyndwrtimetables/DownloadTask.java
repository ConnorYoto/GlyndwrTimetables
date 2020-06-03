package com.example.glyndwrtimetables;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask
{
    private static final String TAG = "Download Task";
    private Context context;

    private String downloadURL = "", downloadFileName = "", Condition = "";
    private static final String PDF_FILES = "/data/data/com.example.glyndwrtimetables/timetables/";
    private static final String PDF_FILES2 = "/data/data/com.example.glyndwrtimetables/timetables";
    private static final String PDF_TEMP_FILES = "/data/data/com.example.glyndwrtimetables/temp/";
    private static final String PDF_TEMP_FILES2 = "/data/data/com.example.glyndwrtimetables/temp";
    private ProgressDialog progressDialog;

    public DownloadTask(Context context, String downloadURL, String Condition)
    {
        this.context = context;
        this.downloadURL = downloadURL;
        this.Condition = Condition;

        // Create file name by picking download file name from URL
        downloadFileName = downloadURL.substring(downloadURL.lastIndexOf('/'), downloadURL.length());

        // Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void>
    {
        File outputFile = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result)
        {
            try
            {
                if (outputFile != null)
                {
                    progressDialog.dismiss();
                    ContextThemeWrapper ctw = new ContextThemeWrapper( context, R.style.Theme_AlertDialog);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                    alertDialogBuilder.setTitle("Document  ");
                    alertDialogBuilder.setMessage("Document Saved Successfully ");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {

                        }
                    });
                    alertDialogBuilder.show();
                }
                else
                {
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                        }
                    }, 3000);
                    Log.w(TAG, "Download Failed");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                //Change button text if exception occurs
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {

                    }
                }, 3000);
                Log.w(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
                if (Condition.equals("Save")) // Save Files vs Temporary Files Check
                {
                    try
                    {
                        //Create Download URl
                        URL url = new URL(downloadURL);
                        //Open Url Connection
                        HttpURLConnection c = (HttpURLConnection) url.openConnection();
                        //connect the URL Connection
                        c.connect();

                        Log.w(TAG, "URL Connected");

                        // Assign Filepath for new folder
                        File pdfFOLDER = new File( PDF_FILES2 );

                        //If File is not present create directory
                        if (!pdfFOLDER.exists())
                        {
                            pdfFOLDER.mkdir();
                            Log.w(TAG, "Save Directory Created.");
                        }

                        //Create Output file in Main File
                        outputFile = new File(PDF_FILES, downloadFileName);

                        //Create New File if not present
                        if (!outputFile.exists())
                        {
                            outputFile.createNewFile();
                            Log.w(TAG, "Save File Created");
                        }

                        //Get OutputStream for NewFile Location
                        FileOutputStream fos = new FileOutputStream(outputFile);

                        //Get InputStream for connection
                        InputStream is = c.getInputStream();

                        byte[] buffer = new byte[1024];
                        int len1 = 0;
                        while ((len1 = is.read(buffer)) != -1)
                        {
                            fos.write(buffer, 0, len1);
                        }
                        fos.close();
                        is.close();

                    }
                    catch (Exception e)
                    {

                        //Read exception if something went wrong
                        e.printStackTrace();
                        outputFile = null;
                        Log.e(TAG, "Download Error Exception " + e.getMessage());
                    }
                }
                else if ( Condition.equals("Temp") ) // Handles temp files
                {
                    try
                    {
                        //Create Download URl
                        URL url = new URL(downloadURL);
                        //Open Url Connection
                        HttpURLConnection c = (HttpURLConnection) url.openConnection();
                        //connect the URL Connection
                        c.connect();

                        Log.w(TAG, "URL Connected");

                        // Assign Filepath for new folder
                        File tempDir = new File( PDF_TEMP_FILES2 );

                        //If File is not present create directory
                        if (!tempDir.exists())
                        {

                            tempDir.mkdir();
                            Log.w(TAG, "Temp Directory Created.");
                        }

                        //Create Output file in Main File
                        outputFile = new File(PDF_TEMP_FILES, downloadFileName);

                        //Create New File if not present
                        if (!outputFile.exists())
                        {
                            outputFile.createNewFile();
                            Log.w(TAG, "Temp File Created");
                        }

                        //Get OutputStream for NewFile Location
                        FileOutputStream fos = new FileOutputStream(outputFile);

                        //Get InputStream for connection
                        InputStream is = c.getInputStream();

                        byte[] buffer = new byte[1024];
                        int len1 = 0;
                        while ((len1 = is.read(buffer)) != -1)
                        {
                            fos.write(buffer, 0, len1);
                        }
                        fos.close();
                        is.close();

                    }
                    catch (Exception e)
                    {

                        //Read exception if something went wrong
                        e.printStackTrace();
                        outputFile = null;
                        Log.e(TAG, "Download Error Exception " + e.getMessage());
                    }
                }

            return null;
        }
    }
}