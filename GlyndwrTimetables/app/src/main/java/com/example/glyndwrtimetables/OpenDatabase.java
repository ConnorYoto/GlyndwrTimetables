package com.example.glyndwrtimetables;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class OpenDatabase extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "timetable_db.db";

    // TOGGLE THIS NUMBER FOR UPDATING TABLES AND DATABASE
    private static final int DATABASE_VERSION = 1;

    OpenDatabase(Context context)
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    } // OpenDatabase(Context context)

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    } // public void onCreate(SQLiteDatabase db)

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    } // public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)

    //  Method Retrieve Saved Filepath - select PDF FILEPATH FROM saved_records WHERE term

    public ArrayList<String> ListAllSavedRecords(SQLiteDatabase db)  // Saved_records table
    {
        ArrayList<String> list = new ArrayList<String>();
        String str = "";

        Cursor c = db.rawQuery("SELECT * FROM saved_records", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String Name = c.getString(1);
                    str = str + Name + ", ";
                    String Dept = c.getString(2);
                    str = str + Dept + ", ";
                    String Type = c.getString(3);
                    str = str + Type + ", ";
                    String PDF_FILEPATH = c.getString(4);
                    str = str + PDF_FILEPATH;

                    list.add( str );
                    str = "";

                } while (c.moveToNext());
            }
        }
        c.close();
        Log.w("SAVED_RECORDS_REQUEST", "Table successfully loaded");
        return list;
    }   //  public ArrayList<String> ListAllRecordsInSaved(SQLiteDatabase db)

    public ArrayList<String> ListAllSearchRecords(SQLiteDatabase db)    //  Search_records table
    {
        ArrayList<String> list = new ArrayList<String>();
        String str = "";

        Cursor c = db.rawQuery("SELECT * FROM search_records", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String Name = c.getString(1);
                    str = str + Name + ",";
                    String Dept = c.getString(2);
                    str = str + Dept + ",";
                    String Type = c.getString(3);
                    str = str + Type + ",";
                    String PDF_URL = c.getString(4);
                    str = str + PDF_URL;

                    list.add( str );
                    str = "";

                } while (c.moveToNext());
            }
        }
        c.close();
        Log.w("SEARCH_RECORDS_REQUEST", "Table successfully loaded");
        return list;
    }   //  public ArrayList<String> ListAllRecordsInSearch(SQLiteDatabase db)

    public ArrayList<String> SearchAllSearchRecords(SQLiteDatabase db, String searchTerm) //  Search_records table
    {
        ArrayList<String> list = new ArrayList<String>();
        String str = "";

        Cursor c = db.rawQuery("SELECT * FROM search_records WHERE NAME LIKE '%" + searchTerm + "%' OR DEPT LIKE '%" + searchTerm + "%' OR TYPE LIKE '%" + searchTerm + "%'" , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String Name = c.getString(1);
                    str = str + Name + ",";
                    String Dept = c.getString(2);
                    str = str + Dept + ",";
                    String Type = c.getString(3);
                    str = str + Type + ",";
                    String PDF_URL = c.getString(4);
                    str = str + PDF_URL;

                    list.add( str );
                    str = "";

                } while (c.moveToNext());
            }
        }
        c.close();
        Log.w("SEARCH RECORDS", "searchTerm = " + searchTerm);
        return list;
    }   //  public ArrayList<String> SearchAllRecordsArrayListString(SQLiteDatabase db, String searchTerm)

    public void addSearchRecord(SQLiteDatabase db, String Name, String Dept, String Type, String PDF_URL)   // Search_records table
    {
        String addSearchSQL = "INSERT INTO search_records(Name, Dept, Type, PDF_URL) VALUES ('" + Name + "', '" + Dept + "', '" + Type + "', '" + PDF_URL + "');";

        db.execSQL ( addSearchSQL );
    }   //  public void addRecordToSearch(SQLiteDatabase db, String ID, String Name, String Type, String PDF_URL)

    public void addSavedRecord(SQLiteDatabase db, String Name, String Dept, String Type, String PDF_URL) // Saved_records table
    {
        String addSavedSQL = "INSERT INTO saved_records(Name, Dept, Type, PDF_FILEPATH) VALUES ('" + Name + "', '" + Dept + "', '" + Type + "', '" + PDF_URL + "');";

        db.execSQL ( addSavedSQL );

        Log.w("RECORD SAVED", "Record = " + Name);
    }   //  public void addRecordToSaved(SQLiteDatabase db, String ID, String Name, String Type, String PDF_FILEPATH)

    public void removeSavedRecord(SQLiteDatabase db, String Name) // Saved_records table
    {
        String removeSQL = "DELETE FROM saved_records WHERE Name = '" + Name + "';";

        db.execSQL ( removeSQL );

        Log.w("REMOVED FROM SAVED", "Record = " + Name);
    }   //  public void removeRecord(SQLiteDatabase db, String Name)

    public void removeAllRecords(SQLiteDatabase db) // Search_records table
    {
        String removeAllSQL = "DELETE FROM search_records;";

        db.execSQL ( removeAllSQL );

        // Reset AutoIncrement ID
        String resetSequenceSQL = "DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'search_records'";

        db.execSQL( resetSequenceSQL );
    }   //  public void removeAllRecords(SQLiteDatabase db)
}
