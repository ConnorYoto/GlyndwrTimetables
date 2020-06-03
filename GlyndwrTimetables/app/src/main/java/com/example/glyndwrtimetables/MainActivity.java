package com.example.glyndwrtimetables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
// Jsoup Imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// Java IO Imports
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity
{
    // Database Paths + Name + Log Tag
    private static final String DATABASE_PATH = "/data/data/com.example.glyndwrtimetables/databases/";
    private static final String DATABASE_PATH2 = "/data/data/com.example.glyndwrtimetables/databases"; // no / at end of path !!!
    private static final String DATABASE_NAME = "timetable_db.db";
    private static final String LOG_TAG = "TIMETABLE_DB";

    // Database Variables
    Context ctx;
    OpenDatabase sqh;
    SQLiteDatabase db;

    // Interface
    Button searchButton;
    Button savedButton;
    Button helpButton;
    Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Up Database
        setUpDatabase();
        // Initialise Database
        InitDataBase();
        // Jsoup Web-scraping
        scrapeForTimetables();
        // Set up Controls
        setupControls();

    }   //  protected void onCreate(Bundle savedInstanceState)

    protected void scrapeForTimetables()
    {
        // Wipe Records Table before Web-Scraping
        sqh.removeAllRecords(db);
        Log.w("SEARCH_RECORDS_WIPED","Search Records Table wiped for fresh web-scrape");

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final Document doc = Jsoup.connect("http://timetables.glyndwr.ac.uk/GenericTimetables/finder.xml").get();

                    Elements resources = doc.select("resource");
                    Log.w("SCRAPE", "Starting to scrape");
                    for (Element resource : resources)
                    {
                        String check = resource.attr("id");
                        if (check.equals(""))
                        {
                            continue;
                        }
                        else
                        {
                            String Name = resource.select("name").text().replace(",", "").replace("'", "");
                            String Dept = resource.select("dept").text().replace(",", "");
                            String Type = resource.attr("type").replace(",", "");
                            String PDF_URL = resource.select("link.pdf").attr("href");
                            sqh.addSearchRecord(db, Name, Dept, Type, PDF_URL);
                        }
                    }
                    Log.w("SCRAPE SUCCESSFUL", "Scrape is completed");
                }
                catch (IOException e)
                {
                    Log.w("SCRAPING ERROR:", e.getMessage());
                }
            }
        }).start();
    }

    public void setUpDatabase()
    {
        ctx = this.getBaseContext();
        try
        {
            CopyDataBaseFromAsset();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    } // public void setUpDatabase()

    public void CopyDataBaseFromAsset() throws IOException
    {
        // Get the SQLite database in the assets folder
        InputStream in = ctx.getAssets().open(DATABASE_NAME);

        // LOG TAG for LOGCAT - Starting to Copy
        Log.w( LOG_TAG , "Starting copying Database from Assets...");
        String outputFileName = DATABASE_PATH + DATABASE_NAME;
        File databaseFolder = new File( DATABASE_PATH2 );

        // databases folder exists ? No - Create it and copy !!!
        if ( !databaseFolder.exists() )
        {
            databaseFolder.mkdir();
            OutputStream out = new FileOutputStream(outputFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ( (length = in.read(buffer)) > 0 )
            {
                out.write(buffer,0,length);
            } // while ( (length = in.read(buffer)) > 0 )
            out.flush();
            out.close();
            in.close();
            // LOG TAG for LOGCAT - Completed Copy
            Log.w(LOG_TAG, "Completed.");

        } // if ( !databaseFolder.exists() )

    } // public void CopyDataBaseFromAsset() throws IOException

    public void InitDataBase()
    {
        // Init the SQLite Helper Class
        sqh = new OpenDatabase(this);
        // RETRIEVE A READABLE AND WRITE-ABLE DATABASE
        db = sqh.getWritableDatabase();
    } // public void InitDataBase()

    protected void setupControls() // Menu
    {
        // Search Button
        searchButton = findViewById(R.id.search_timetable_button);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Opens Search Activity
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // Saved Button
        savedButton = findViewById(R.id.saved_timetables_button);
        savedButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Opens Saved Activity
                Intent intent = new Intent(getBaseContext(), SavedActivity.class);
                startActivity(intent);
            }
        });

        // Help Button
        helpButton = findViewById(R.id.help_button);
        helpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Opens Help Activity
                Intent intent = new Intent(getBaseContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        // About Button
        aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Opens About Activity
                Intent intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

    }   //  protected void setupControls()

}   //  public class MainActivity extends AppCompatActivity
