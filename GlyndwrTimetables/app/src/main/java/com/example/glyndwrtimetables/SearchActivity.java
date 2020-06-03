package com.example.glyndwrtimetables;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity
{
    // Database Variables
    OpenDatabase sqh;
    SQLiteDatabase db;
    // Dialog
    Dialog dialog;
    Context context = this;
    // Interface
    Button backButton;
    Button searchButton;
    EditText searchEditText;
    ListView listview;
    ArrayList<String> list;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialise Database
        InitDataBase();
        // Setup Controls
        setupControls();

    }   //  protected void onCreate(Bundle savedInstanceState)

    protected void setupControls()
    {
        //initialize it in your activity so that the handler is bound to UI thread
        final Handler handlerUI = new Handler();

        // Back button
        backButton = findViewById(R.id.search_back_button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        // List View
        listview = findViewById(R.id.search_listview);
        list = new ArrayList<String>();
        // Construct the list
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        // populate this ArrayList
        list.addAll(sqh.ListAllSearchRecords(db));
        // Link the ArrayAdapter to the list view
        listview.setAdapter( adapter );

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.search_dialog_layout);

                // Dialog Interface
                String itemValue = (String)listview.getItemAtPosition( position );
                String[] DialogDetails = itemValue.split(",");
                final String Name = DialogDetails[0];
                final String Dept = DialogDetails[1];
                final String Type = DialogDetails[2];
                final String PDF_URL = DialogDetails[3].replace(" ", "");

                // Dialog Title
                TextView searchDialogTitle = dialog.findViewById(R.id.searchDialogTitle);
                searchDialogTitle.setText(DialogDetails[0]);
                // Dialog Info
                TextView searchDialogInfo = dialog.findViewById(R.id.searchDialogInfo);
                searchDialogInfo.setText("Department = " + Dept + "\n" + "Type = " + Type + "\n" + "PDF_URL = " + PDF_URL);

                //Dialog - Save Button
                Button dialogButton_Save = dialog.findViewById(R.id.searchDialogButton_Save);
                dialogButton_Save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String Download_URL = "http://timetables.glyndwr.ac.uk/GenericTimetables/" + PDF_URL;
                        String Condition = "Save";
                        new DownloadTask(SearchActivity.this, Download_URL, Condition);

                        sqh.addSavedRecord(db, Name, Dept, Type, PDF_URL);
                    }
                });

                //Dialog - Open Button
                Button dialogButton_Open = dialog.findViewById(R.id.searchDialogButton_Open);
                dialogButton_Open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Also Download PDF for Intent
                        String Download_URL = "http://timetables.glyndwr.ac.uk/GenericTimetables/" + PDF_URL;
                        final String PDF_FILEPATH = "/data/data/com.example.glyndwrtimetables/temp/" + PDF_URL;
                        Log.w("PDF FILEPATH = ",PDF_FILEPATH);
                        String Condition = "Temp";
                        new DownloadTask(SearchActivity.this, Download_URL, Condition);

                        handlerUI.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // Opens PDF Renderer Activity
                                Intent intent = new Intent(getBaseContext(), PDF_Renderer.class);
                                intent.putExtra("PDF_FILEPATH", PDF_FILEPATH);
                                startActivity(intent);
                            }
                        }, 1000);

                    }
                });

                //Dialog - Close Button
                Button dialogButton_Close = dialog.findViewById(R.id.searchDialogButton_Close);
                dialogButton_Close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        // Delete Any Temp Files on close
                        File TempDir = new File("/data/data/com.example.glyndwrtimetables/temp");
                        if (TempDir.isDirectory())
                        {
                            String[] children = TempDir.list();
                            for (int i = 0; i < children.length; i++)
                            {
                                new File(TempDir, children[i]).delete();
                            }
                        }
                        Log.w("TEMP DIRECTORY", "Contents wiped");
                    }
                });

                dialog.show();
            }
        });

        // Search Button + EditText
        searchEditText = findViewById(R.id.search_editText);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                list.clear();
                String searchTerm = searchEditText.getText().toString();
                list.addAll(sqh.SearchAllSearchRecords(db, searchTerm));
                listview.setAdapter( adapter );
            }
        });

    }   //  protected void setupControls()

    public void InitDataBase()
    {
        // Init the SQLite Helper Class
        sqh = new OpenDatabase(this);
        // RETRIEVE A READABLE AND WRITE-ABLE DATABASE
        db = sqh.getWritableDatabase();
    } // public void InitDataBase()

}   //  public class SearchActivity extends AppCompatActivity