package com.yampoknaf.subibattle;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HighScoreTable extends AppCompatActivity {

    private ArrayList<DataBaseRowData> dataToPut;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_table);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(HighScoreTwoWay.BUNDLE_DELIVER_DIFFICULT_TO_TABLE);
        String difficult = bundle.getString(MainActivity.KEY_BUNDLE_TO_CURRENT_DIFFICULTY);

        HighScoreDbHelper dbHelper = new HighScoreDbHelper(this);
        dataToPut = dbHelper.getUsers(difficult);
        if (dataToPut == null)
            return;

        String allNames[] = new String[dataToPut.size()];

        for(int i = 0 ; i < allNames.length ; i++){
            allNames[i] = dataToPut.get(i).getmName();
        }


        listView = (ListView)findViewById(R.id.lvHighScoreTable);
        MyListAdapter adapter = new MyListAdapter(this , allNames);
        //listView.setScrollBarSize(dataToPut.size());
        listView.setAdapter(adapter);
    }

    class MyListAdapter extends ArrayAdapter<String>{
        private Context context;

        public MyListAdapter(Context c , String name[]){
            super(c , R.layout.row_highscore_list ,name);
            context = c;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewItemToChange holder = null;
            if(row == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_highscore_list ,parent , false);
                holder = new ViewItemToChange(row);
                row.setTag(holder);
            }else{
                holder = (ViewItemToChange)row.getTag();
            }

            TextView title = holder.getTitle();
            TextView description = holder.getDescription();
            ImageView Image = holder.getImage();

            switch(position){
                case 0:
                    Image.setImageResource(R.drawable.place_one_row_highscore);
                    break;
                case 1:
                    Image.setImageResource(R.drawable.place_two_row_highscore);
                    break;
                case 2:
                    Image.setImageResource(R.drawable.place_three_row_highscore);
                    break;
                default:
                    Image.setImageResource(R.drawable.place_forth_row_highscore);
                    break;
            }

            title.setText("Place " + (position + 1) + ". " + dataToPut.get(position).getmName());
            description.setText("       Score is ->" + dataToPut.get(position).getmScore());

            return row;
        }

        class ViewItemToChange{
            private TextView title;
            private TextView description;
            private ImageView image;

            public ViewItemToChange(View view){
                title = (TextView)view.findViewById(R.id.txtTitleHighScoreTable);
                description = (TextView)view.findViewById(R.id.txtDescHighScoreTable);
                image = (ImageView)view.findViewById(R.id.rowImageHighScore);
            }

            public TextView getTitle() {
                return title;
            }

            public TextView getDescription() {
                return description;
            }

            public ImageView getImage() {
                return image;
            }
        }
    }
}

