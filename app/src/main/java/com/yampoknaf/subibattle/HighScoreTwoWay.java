package com.yampoknaf.subibattle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

public class HighScoreTwoWay extends AppCompatActivity {

    public static final String BUNDLE_DELIVER_DIFFICULT_TO_TABLE = "BundleToDeliverHighScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_two_way);

        ImageButton tableImageButton = (ImageButton)findViewById(R.id.ImgButnTable);
        tableImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialogBuilder = new AlertDialog.Builder(HighScoreTwoWay.this);
                myDialogBuilder.setTitle(getString(R.string.choose_difficult));
                myDialogBuilder.setItems(GameParameters.getStringOfDifficulties(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putString( MainActivity.KEY_BUNDLE_TO_CURRENT_DIFFICULTY ,GameParameters.AvaliableDifficulties.values()[which].toString());
                        Intent intent = new Intent( HighScoreTwoWay.this , HighScoreTable.class );
                        intent.putExtra(BUNDLE_DELIVER_DIFFICULT_TO_TABLE , bundle);
                        startActivity(intent);
                    }
                });
                myDialogBuilder.create().show();
            }
        });

        ImageButton  mapImageButton = (ImageButton)findViewById(R.id.ImgButnMap);
        mapImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScoreTwoWay.this , HighScoreOnMap.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onStart(){
        super.onStart();
        Runtime.getRuntime().gc();
    }
}
