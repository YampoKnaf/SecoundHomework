package com.yampoknaf.subibattle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Orleg on 24/04/2016.
 */
public class HighScoreDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SubiDataBase.db";
    private SQLiteDatabase mDb;

    public HighScoreDbHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        mDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HighScoreContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(HighScoreContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void insertNewRecord(DataBaseRowData newUserData){
        ContentValues values = new ContentValues();
        values.put(HighScoreContract.HighScoreEntry.DIFFICULTY , newUserData.getmDifficult());
        values.put(HighScoreContract.HighScoreEntry.LAG_CORDS  , newUserData.getmLag());
        values.put(HighScoreContract.HighScoreEntry.LAT_CORDS  , newUserData.getmLat());
        values.put(HighScoreContract.HighScoreEntry.SCORE      , newUserData.getmScore());
        values.put(HighScoreContract.HighScoreEntry.USER_NAME, newUserData.getmName());

        mDb.insert(HighScoreContract.HighScoreEntry.TABLE_NAME, "null", values);
    }

    public ArrayList<DataBaseRowData> getUsers(String difficult){

        String[] columnToReturn = {
                HighScoreContract.HighScoreEntry.DIFFICULTY ,
                HighScoreContract.HighScoreEntry.LAG_CORDS  ,
                HighScoreContract.HighScoreEntry.LAT_CORDS  ,
                HighScoreContract.HighScoreEntry.SCORE      ,
                HighScoreContract.HighScoreEntry.USER_NAME
        };
        String selection = HighScoreContract.HighScoreEntry.DIFFICULTY;
        String sortOrder = HighScoreContract.HighScoreEntry.SCORE + " ASC";
        GameParameters.AvaliableDifficulties arr[] = GameParameters.AvaliableDifficulties.values();
        String allPosibility[] = new String[]{arr[0].toString() , arr[1].toString() , arr[2].toString()};
        Cursor cursor = null;
        /*cursor = mDb.query(
                HighScoreContract.HighScoreEntry.TABLE_NAME ,
                columnToReturn ,
                selection ,
                (difficult == null ? allPosibility : new String[]{difficult}) ,
                null ,
                null ,
                sortOrder
        );*/

        cursor = mDb.rawQuery("select " + columnToReturn[0] + " , " + columnToReturn[1] + " , " + columnToReturn[2] + " , " + columnToReturn[3] + " , " + columnToReturn[4] +
                            " from " +  HighScoreContract.HighScoreEntry.TABLE_NAME +
                            (difficult == null ? "" : " where " + HighScoreContract.HighScoreEntry.DIFFICULTY +  " = \"" + difficult + "\"") + " order by " + sortOrder  , null);

        if(cursor.getCount() <= 0)
            return null;

        cursor.moveToFirst();
        ArrayList<DataBaseRowData> toReturn = new ArrayList<>();
        do{
            String diff     = cursor.getString(0);
            double lag   = cursor.getInt(1);
            double lat   = cursor.getInt(2);
            int score = cursor.getInt(3);
            String userName = cursor.getString(4);
            DataBaseRowData temp = new DataBaseRowData(userName , score , lat , lag , diff);
            toReturn.add(temp);
        }while(cursor.moveToNext());

        return toReturn;
    }
}
