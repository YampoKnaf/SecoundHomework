package com.yampoknaf.subibattle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class WinLose extends AppCompatActivity {

    public static int[] dyingLoser = new int[]{ R.drawable.die_1 , R.drawable.die_2 , R.drawable.die_3 , R.drawable.die_4 , R.drawable.die_5 , R.drawable.die_6 ,
            R.drawable.die_7 , R.drawable.die_8 , R.drawable.die_9 };

    public static int[] dancerWin = new int[] { R.drawable.dancer_win_1 , R.drawable.dancer_win_2 , R.drawable.dancer_win_3 , R.drawable.dancer_win_4 , R.drawable.dancer_win_5 ,
            R.drawable.dancer_win_6 ,R.drawable.dancer_win_7 , R.drawable.dancer_win_8 , R.drawable.dancer_win_9 ,R.drawable.dancer_win_10,
            R.drawable.dancer_win_11 , R.drawable.dancer_win_12 , R.drawable.dancer_win_13 , R.drawable.dancer_win_14 , R.drawable.dancer_win_15 , R.drawable.dancer_win_16,
            R.drawable.dancer_win_17 , R.drawable.dancer_win_18 , R.drawable.dancer_win_19 , R.drawable.dancer_win_20 , R.drawable.dancer_win_21 , R.drawable.dancer_win_22 ,
            R.drawable.dancer_win_23 , R.drawable.dancer_win_24
    };

    public static ImageView[] show;

    private MyImageView display;


    public static boolean win = false;

    private Bundle bundleWithBoardInformation;
    public static final String KEY_PLAY_AT_ONCE = "playAtOnceAfterGoToNextActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_lose);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                /*try {
                    //Thread.sleep(300); // must be done , ordered glide to kill his cash memory after the game proccess
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                WinLose.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        win = false;
                        Intent intent = getIntent();
                        bundleWithBoardInformation = intent.getBundleExtra(MainActivity.KEY_BUNDLE_TO_CREATE_BOARD);


                        Button btnPlayAgain = (Button)findViewById(R.id.btnStartAgain);
                        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bundleWithBoardInformation.putBoolean(KEY_PLAY_AT_ONCE , true);
                                nextActivity();
                            }
                        });

                        Button btnGoToMenu = (Button)findViewById(R.id.btnGoToMenu);
                        btnGoToMenu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nextActivity();
                            }
                        });

                        ((TextView)findViewById(R.id.lblDifficultyWinLose)).setText(getString(R.string.difficult_lbl) + " " +
                                GameParameters.AvaliableDifficulties.values()[bundleWithBoardInformation.getInt(MainActivity.KEY_BUNDLE_TO_CURRENT_DIFFICULTY)].toString());
                        RelativeLayout relativeLayOut = (RelativeLayout)findViewById(R.id.winLoseOverAllLayer);


                        if(GameManager.EndState.getGameState(bundleWithBoardInformation.getInt(GameProccess.KEY_WIN_LOSE)) == GameManager.EndState.WIN){
                            win = true;
                            initLoges();
                            display = new MyImageView((ImageView)findViewById(R.id.ImageWinnerLoserDisplay) , show.length , WhichPic.WINNER);
                            relativeLayOut.setBackgroundResource(R.drawable.winner);

                            AlertDialog.Builder builder = new AlertDialog.Builder(WinLose.this);
                            builder.setTitle("Please enter your name.");

                            final EditText input = new EditText(WinLose.this);
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            builder.setView(input);

                            builder.setPositiveButton("Insert" , new DialogListener(input));

                            builder.show();

                        }else{
                            initLoges();
                            relativeLayOut.setBackgroundResource(R.drawable.you_lost);
                            display = new MyImageView((ImageView)findViewById(R.id.ImageWinnerLoserDisplay) , show.length , WhichPic.LOSER);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    private void nextActivity(){
        Intent intent = new Intent(WinLose.this , MainActivity.class);
        intent.putExtra(MainActivity.KEY_BUNDLE_TO_CREATE_BOARD, bundleWithBoardInformation);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishMe();
    }


    @Override
    public void onStop(){
        super.onStop();
        System.gc();
        closeLogos();
        keepGoing = false;
        Intent intent = new Intent(this , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);



        finishMe();
    }

    private void finishMe(){
        System.gc();
        Runtime.getRuntime().gc();
        finish();
    }

    private void initLoges(){
        if(win){
            show = new ImageView[dancerWin.length];
            for(int i = 0 ; i < show.length ; i++){
                show[i] = new ImageView(this);
                Glide.with(this).load(dancerWin[i]).override(277, 388).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).fitCenter().into(show[i]);
            }
        }else{
            show = new ImageView[dyingLoser.length];
            for(int i = 0 ; i < show.length ; i++){
                show[i] = new ImageView(this);
                Glide.with(this).load(dyingLoser[i]).override(465, 577).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).fitCenter().into(show[i]);
            }
        }
    }

    private void closeLogos(){
            for(int i = 0 ; i < show.length ; i++){
                Glide.clear(show[i]);
                show[i] = null;
            }
        show = null;
    }

    class DialogListener implements DialogInterface.OnClickListener{

        private EditText mEditText;

        public DialogListener(final EditText editText){
            mEditText = editText;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            boolean storeItem = false;

            String name = null;
            try {
                try {
                    name = mEditText.getText().toString();
                }catch(Exception e){

                }
                if(name == null || name == ""){
                    name = "temp user";
                }
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = lm.getProviders(true);
                Location l = null;

                for (int i = 0; i < providers.size(); i++) {
                    l = lm.getLastKnownLocation(providers.get(i));
                    if (l != null) {
                        double latitude = l.getLatitude();
                        double longitude = l.getLongitude();
                        int numberOfMoves = bundleWithBoardInformation.getInt(GameProccess.KEY_MOVE_NUMBER);
                        HighScoreDbHelper dbHelper = new HighScoreDbHelper(getApplicationContext());
                        dbHelper.insertNewRecord(new DataBaseRowData(name, numberOfMoves, latitude, longitude,
                                GameParameters.AvaliableDifficulties.values()[bundleWithBoardInformation.getInt(MainActivity.KEY_BUNDLE_TO_CURRENT_DIFFICULTY)].toString()));
                        storeItem = true;
                        break;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            if(!storeItem){
                double latitude = 0;
                double longitude = 0;
                int numberOfMoves = bundleWithBoardInformation.getInt(GameProccess.KEY_MOVE_NUMBER);
                HighScoreDbHelper dbHelper = new HighScoreDbHelper(getApplicationContext());
                dbHelper.insertNewRecord(new DataBaseRowData(name, numberOfMoves, latitude, longitude,
                        GameParameters.AvaliableDifficulties.values()[bundleWithBoardInformation.getInt(MainActivity.KEY_BUNDLE_TO_CURRENT_DIFFICULTY)].toString()));
            }
        }
    }


    enum  WhichPic {WINNER , LOSER};
    public static boolean keepGoing;
    class MyImageView {
        private int whichPic = -1;
        private int numberOfBiggerPic;
        private ImageView imageView;
        public MyImageView(ImageView imgv , int lengthOfImages , final WhichPic pic) {
            imageView = imgv;
            numberOfBiggerPic = lengthOfImages;
            keepGoing = true;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (keepGoing) {
                        try {
                            whichPic++;
                            whichPic%= numberOfBiggerPic;
                            WinLose.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        switch(pic){
                                            case WINNER:
                                                nextWinner();
                                                break;
                                            case LOSER:
                                                nextDying();
                                                break;
                                        }
                                    }catch(Exception e){}
                                }
                            });
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    imageView = null;
                }
            });

            thread.start();
        }

        private void nextWinner(){
            imageView.setImageDrawable(show[whichPic].getDrawable());
        }

        private void nextDying(){
            //Log.e("me" , whichPic + "");
            imageView.setImageDrawable(show[whichPic].getDrawable());
        }



    }
}
