package com.yampoknaf.subibattle;

import android.provider.BaseColumns;

/**
 * Created by Orleg on 24/04/2016.
 */
public final class HighScoreContract {

    public HighScoreContract(){} //   to prevent someone from accidentally inistantiating the contract class

    public static abstract class HighScoreEntry implements BaseColumns{
        public static final String TABLE_NAME = "HighScore";
        public static final String USER_NAME = "Username";
        public static final String SCORE = "Score";
        public static final String LAT_CORDS = "LatCordinates";
        public static final String LAG_CORDS = "LagCordinates";
        public static final String DIFFICULTY = "Difficulty";
    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER ";
    public static final String DOUBLE_TYPE = " DOUBLE ";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HighScoreEntry.TABLE_NAME + " (" +
                    HighScoreEntry.USER_NAME + TEXT_TYPE + COMMA_SEP +
                    HighScoreEntry.SCORE + INTEGER_TYPE + COMMA_SEP +
                    HighScoreEntry.LAT_CORDS + DOUBLE_TYPE + COMMA_SEP +
                    HighScoreEntry.LAG_CORDS + DOUBLE_TYPE + COMMA_SEP +
                    HighScoreEntry.DIFFICULTY + TEXT_TYPE  +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HighScoreEntry.TABLE_NAME;

}
