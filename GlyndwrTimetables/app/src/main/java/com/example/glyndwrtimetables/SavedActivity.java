package com.example.glyndwrtimetables;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class SavedActivity extends AppCompatActivity
{
    // Database Variables
    OpenDatabase sqh;
    SQLiteDatabase db;
    // Dialog
    Dialog dialog;
    Context context = this;
    // Interface
    Button backButton;
    ListView listview;
    ArrayList<String> list;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        // Initialise Database
        InitDataBase();
        // Setup Controls
        setupControls();

    }   //  protected void onCreate(Bundle savedInstanceState)

    protected void setupControls()
    {
        // Back Button
        backButton = findViewById(R.id.saved_back_button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        }); //  backButton.setOnClickListener(new View.OnClickListener()

        // List View
        listview = findViewById(R.id.saved_listview);
        list = new ArrayList<String>();
        // Construct the list
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        // populate this ArrayList
        list.addAll(sqh.ListAllSavedRecords(db));
        // Link the ArrayAdapter to the list view
        listview.setAdapter( adapter );

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.saved_dialog_layout);

                // Dialog Interface
                String itemValue = (String)listview.getItemAtPosition( position );
                String[] DialogDetails = itemValue.split(",");
                final String Name = DialogDetails[0];
                final String Dept = DialogDetails[1];
                final String Type = DialogDetails[2];
                final String PDF_FILEPATH = DialogDetails[3].replace(" ", "");

                // Dialog Title
                TextView saveDialogTitle = dialog.findViewById(R.id.saveDialogTitle);
                saveDialogTitle.setText(Name);
                // Dialog Info
                TextView saveDialogInfo = dialog.findViewById(R.id.saveDialogInfo);
                saveDialogInfo.setText("Department = " + Dept + "\n" + "Type = " + Type + "\n" + "PDF_URL = " + PDF_FILEPATH);

                //Dialog - Delete Button
                Button dialogButton_Delete = dialog.findViewById(R.id.saveDialogButton_Delete);
                dialogButton_Delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        // Delete Saved Record
                        sqh.removeSavedRecord(db, Name);
                        Log.w("RECORD REMOVED", "Saved Record removed = " + Name);
                        // Delete Saved PDF File
                        File PDF_FILE = new File("/data/data/com.example.glyndwrtimetables/timetables/" + PDF_FILEPATH);
                        PDF_FILE.delete();
                        Log.w("DELETED RECORD", "Record = " + Name + ", PDF FILE DELETED");
                        // populate this ArrayList
                        list.addAll(sqh.ListAllSavedRecords(db));
                        adapter.notifyDataSetChanged();
                        // Link the ArrayAdapter to the list view
                        listview.setAdapter( adapter );
                    }
                });

                //Dialog - Open Button
                Button dialogButton_Open = dialog.findViewById(R.id.saveDialogButton_Open);
                dialogButton_Open.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final String TOTAL_PDF_FILEPATH = "/data/data/com.example.glyndwrtimetables/timetables/" + PDF_FILEPATH;
                        Log.w("FILEPATH", TOTAL_PDF_FILEPATH);
                        // Opens PDF Renderer Activity
                        Intent intent = new Intent(getBaseContext(), PDF_Renderer.class);
                        // Also send PDF detail for load
                        intent.putExtra("PDF_FILEPATH", TOTAL_PDF_FILEPATH);
                        startActivity(intent);

                    }
                });

                //Dialog - Close Button
                Button dialogButton_Close = dialog.findViewById(R.id.saveDialogButton_Close);
                dialogButton_Close.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                list.clear();
                list.addAll(sqh.ListAllSavedRecords(db));
                listview.setAdapter( adapter );
                adapter.notifyDataSetChanged();
                listview.refreshDrawableState();
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


}   //  public class SavedActivity extends AppCompatActivity
