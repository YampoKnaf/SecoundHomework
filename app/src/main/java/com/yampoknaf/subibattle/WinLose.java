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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class WinLose extends AppCompatActivity {


    private Bundle bundleWithBoardInformation;
    public static final String KEY_PLAY_AT_ONCE = "playAtOnceAfterGoToNextActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_lose);

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
            relativeLayOut.setBackgroundResource(R.drawable.winner);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter your name.");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Insert" , new DialogListener(input));

            builder.show();

        }else{
            relativeLayOut.setBackgroundResource(R.drawable.you_lost);
        }


    }

    private void nextActivity(){
        Intent intent = new Intent(WinLose.this , MainActivity.class);
        intent.putExtra(MainActivity.KEY_BUNDLE_TO_CREATE_BOARD , bundleWithBoardInformation);
        startActivity(intent);
        finish();
    }


    @Override
    public void onStop(){
        super.onStop();
        System.gc();
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
}
