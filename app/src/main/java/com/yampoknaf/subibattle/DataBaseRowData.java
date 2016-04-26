package com.yampoknaf.subibattle;

/**
 * Created by Orleg on 24/04/2016.
 */
public class DataBaseRowData {

    private String mName;
    private int mScore;
    private double mLat;
    private double mLag;
    private String mDifficult;


    public DataBaseRowData(String name, int score, double lat, double lag,String difficult){
        mName = name;
        mScore = score;
        mLat = lat;
        mLag = lag;
        mDifficult = difficult;

    }


    public String getmName () {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmScore() {
        return mScore;
    }

    public void setmScore(int mScore) {
        this.mScore = mScore;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLag() {
        return mLag;
    }

    public void setmLag(double mLag) {
        this.mLag = mLag;
    }

    public String getmDifficult() {
        return mDifficult;
    }

    public void setmDifficult(String mDifficult) {
        this.mDifficult = mDifficult;
    }


}
