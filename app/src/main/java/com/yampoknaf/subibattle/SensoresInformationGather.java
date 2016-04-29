package com.yampoknaf.subibattle;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Orleg on 24/04/2016.
 */

/***
 * http://developer.android.com/guide/topics/sensors/sensors_position.html
 *http://developer.android.com/guide/components/bound-services.html
 *
 * ***/
public class SensoresInformationGather extends Service implements SensorEventListener {
    private IBinder mBinder;
    private SensorManager mSensorManager;
    private Sensor mOrientation;

    private float mPitch;
    private float mRoll;
    private float mHeading;
    private float mRotationMatrix[] = new float[16];
    private float mOrientationMat[] = new float[4];
    private boolean mSencoreChanged = false;
    private boolean mFirstTime = true;
    private boolean mChangeMode = false;
    private boolean mInMiddleOfThread = false;

    private GameProccess.LisenerForSensor mListiner;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCreate() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mBinder = new SensorsBinder();
        //Toast.makeText(getApplicationContext(), "create!!", Toast.LENGTH_LONG);
    }

    @Override
    public boolean stopService(Intent name) {
        mSensorManager.unregisterListener(this);
        return super.stopService(name);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!mSencoreChanged){
            mSencoreChanged = true;

            if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){

                SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
                SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_X,
                        SensorManager.AXIS_Z, mRotationMatrix);
                SensorManager.getOrientation(mRotationMatrix, mOrientationMat);

                float pitch = (float)Math.toDegrees(mOrientationMat[1]);

                float magneticHeading = (float)Math.toDegrees(mOrientationMat[0]);
                float heading = Math.abs(magneticHeading%360);

                float roll = (float) Math.toDegrees(mOrientationMat[2]);

                //Log.i("you sucker" , pitch + "  " + heading + "  " + roll );

                if(mFirstTime){
                    mFirstTime = false;
                    mPitch = pitch;
                    mHeading = heading;
                    mRoll = roll;
                }

                boolean movedTooMuch = needToChange(pitch , heading , roll);
                if(mChangeMode){
                    if(!movedTooMuch)
                        stopChanging();
                }else{
                    if(movedTooMuch)
                        startChanging(pitch , heading , roll);
                }
            }

            mSencoreChanged = false;
        }
    }

    private void startChanging(final float pitch, final float heading, final float roll){
        if(!mInMiddleOfThread) {
            //Log.e("you fucker" , pitch + "  " + heading + "  " + roll );
            mChangeMode = true;
            mListiner.goingToChange();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    mInMiddleOfThread = true;
                    try {
                        Thread.sleep(3000);
                        if(mChangeMode) {
                            mPitch = pitch;
                            mHeading = heading;
                            mRoll = roll;
                            mListiner.change();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mChangeMode = false;
                    mInMiddleOfThread = false;
                }
            });
            thread.start();
        }
    }

    private void stopChanging(){
        if(mInMiddleOfThread) {
            mChangeMode = false;
            mListiner.stopTheChange();
        }
    }

    private boolean needToChange(float pitch, float heading, float roll) {
        if(Math.abs(mPitch - pitch) < 20 /*&& Math.abs(mRoll - roll) < 15/* && Math.abs(mHeading - heading) < 60*/)
            return false;
        return true;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    class SensorsBinder extends Binder{
        public void setEventListiner(GameProccess.LisenerForSensor listiner){
            mListiner = listiner;
        }

        public void closeIntent(){
            mSensorManager.unregisterListener(SensoresInformationGather.this);
            stopSelf();
            mSensorManager = null;
            mOrientation = null;
            mBinder = null;
        }
    }

}
