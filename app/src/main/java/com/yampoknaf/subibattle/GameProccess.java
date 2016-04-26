package com.yampoknaf.subibattle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.EventListener;

public class GameProccess extends AppCompatActivity {

    private GameManager gameManager;
    private int screenWidth;
    private int screenHeight;

    private static final float PART_OF_SIDE_OF_SCREEN_FOR_MARGIN_SHIP_DISPLAY = 0.025f;
    private static final float PART_OF_TOP_OR_BOTTOM_FOR_MARGIN_SHIP_DISPLAY = 0.0005f;
    private static final float MAX_SIZE_OF_WIDTH_PICTURE_ON_DISPLAY = 0.13332f;
    private static final float PART_OF_MARGIN_FOR_SHIP_DISPLAY = 0.01f;
    private static final float HEIGHT_OF_ITEMS_IN_SHIP_DESPLAY = 0.025f;
    private static final float PART_OF_MARGIN_FOR_SHIP_BUTTON = 0.5f;
    private static final int OFF_SET_OF_PLAYER_BOARD = 0;
    private static final int OFF_SET_OF_ENEMY_BOTTON = 10;
    public final static String KEY_WIN_LOSE = "winLoseKey";
    public final static String KEY_MOVE_NUMBER = "KeyMoveNumber";
    public final static String MULTIPLAY_DISPLAY_SHIP = " x ";
    public final static String ID_VIEW_DISPLAY_BAR = "viewDisplayBar";

    public String lblWhosTurnKey;
    public String playerTurn;
    public String computerTurn;

    private static int numberOfPixelToSide;
    private static int numberOfPixelToAndBottom;
    private LinearLayout overAllLayout;

    private ArrayList<MyImageButton> allButton = new ArrayList<>();
    private ImageView[][] playerImageMatrix;
    private TextView showWhosTurn;
    private GameParameters.AvaliableDifficulties difficulty;
    private AIEnemy enemy;

    private static int sizeOfplayerImage = 0;
    private static int sizeOfEnemyImage = 0;

    private int[] pictureToDisplayBar;

    private int numberOfMoves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_proccess);

        lblWhosTurnKey = getString(R.string.who_is_there);
        playerTurn = getString(R.string.player_turn);
        computerTurn = getString(R.string.computer_turn);

        TypedArray shipsToDisplay = getResources().obtainTypedArray(R.array.ImageOfDisplay);
        pictureToDisplayBar = new int[shipsToDisplay.length()];
        for (int i = 0; i < shipsToDisplay.length(); i++) {
            pictureToDisplayBar[i] = shipsToDisplay.getResourceId(i, -1);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        overAllLayout = (LinearLayout) findViewById(R.id.gameProccessOverAllLayOut);

        Intent intent = getIntent();
        Bundle bundleWithBoardInformation = intent.getBundleExtra(MainActivity.KEY_BUNDLE_TO_CREATE_BOARD);
        final int widthOfBoard = bundleWithBoardInformation.getInt(MainActivity.KEY_WIDTH_OF_BOARD);
        final int heightOfBoard = bundleWithBoardInformation.getInt(MainActivity.KEY_HEIGHT_OF_BOARD);
        final int[] shipsInformation = bundleWithBoardInformation.getIntArray(MainActivity.KEY_ALL_SHIPS_OF_BOARD);
        difficulty = GameParameters.AvaliableDifficulties.values()[bundleWithBoardInformation.getInt(MainActivity.KEY_BUNDLE_TO_CURRENT_DIFFICULTY)];

        playerImageMatrix = new ImageView[widthOfBoard][heightOfBoard];

        Thread initializeGame = new Thread(new Runnable() {
            @Override
            public void run() {
                gameManager = new GameManager(shipsInformation, widthOfBoard, heightOfBoard);
                enemy = new AIEnemy(gameManager.getBoard(GameManager.MakeMove.PLAYER));
                final Ship[][] playerBoard = gameManager.getBoard(GameManager.MakeMove.PLAYER);
                final Ship[][] enemyBoard = gameManager.getBoard(GameManager.MakeMove.COMPUTER);
                GameProccess.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createTopShipDisplayBar(shipsInformation);
                        createEnemyBoard(enemyBoard);
                        createTurnLable();
                        setwhosTurn(true);
                        createPlayerBoard(playerBoard); // https://www.youtube.com/watch?v=-n1tmwZGyvM
                    }
                });
            }
        });


        initializeGame.start();
    }

    private void createTopShipDisplayBar(int[] shipInformation) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        numberOfPixelToSide = (int) (PART_OF_SIDE_OF_SCREEN_FOR_MARGIN_SHIP_DISPLAY * screenWidth);
        numberOfPixelToAndBottom = (int) (PART_OF_TOP_OR_BOTTOM_FOR_MARGIN_SHIP_DISPLAY * screenHeight);
        linearLayoutParam.setMargins(numberOfPixelToSide, numberOfPixelToAndBottom, numberOfPixelToSide, numberOfPixelToAndBottom);
        linearLayout.setLayoutParams(linearLayoutParam);

        final int sizeOfPictureWidth = (int) (MAX_SIZE_OF_WIDTH_PICTURE_ON_DISPLAY * screenWidth);
        final int sizeOfMarginOverAll = (int) (screenWidth * PART_OF_MARGIN_FOR_SHIP_DISPLAY);
        final int heightOfItems = (int) (screenHeight * HEIGHT_OF_ITEMS_IN_SHIP_DESPLAY);

        for (int i = 0; i < shipInformation.length; i++) {
            ImageView pictureTemp = new ImageView(this);
            setImage(pictureTemp , sizeOfPictureWidth , heightOfItems , pictureToDisplayBar[i]);
            LinearLayout.LayoutParams tempParamImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tempParamImage.setMargins(sizeOfMarginOverAll, sizeOfMarginOverAll, sizeOfMarginOverAll, sizeOfMarginOverAll);
            pictureTemp.setLayoutParams(tempParamImage);
            linearLayout.addView(pictureTemp);

            TextView textTemp = new TextView(this);
            textTemp.setTextColor(Color.WHITE);
            textTemp.setText(MULTIPLAY_DISPLAY_SHIP + shipInformation[i]);
            textTemp.setId(ID_VIEW_DISPLAY_BAR.hashCode()+ i + 1);
            linearLayout.addView(textTemp);
        }

        overAllLayout.addView(linearLayout);
    }

    public void setDownshipDisplayBarViewByOne(int shipSize){
        TextView toChange = (TextView)findViewById(ID_VIEW_DISPLAY_BAR.hashCode() + shipSize);
        String periousText =  toChange.getText().toString();
        int numberOfShip = Integer.parseInt(periousText.charAt(periousText.length() - 1) + "") - 1;
        toChange.setText(MULTIPLAY_DISPLAY_SHIP + numberOfShip);
    }


    /// only run on gui thread
    private void createEnemyBoard(Ship[][] enemyBoard) {
        //final int sizeOfMarginOverAll = (int)(screenWidth*PART_OF_MARGIN_FOR_SHIP_BUTTON);
        final int sizeOfButton = (int) ((screenWidth - (float) screenWidth * PART_OF_MARGIN_FOR_SHIP_BUTTON) / (float) enemyBoard[0].length + OFF_SET_OF_ENEMY_BOTTON);



        GridLayout gridLayout = new GridLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        gridLayout.setRowCount(enemyBoard.length);
        gridLayout.setColumnCount(enemyBoard[0].length);
        sizeOfEnemyImage = sizeOfButton;

        ButtonOfEnemyClickListener listener = new ButtonOfEnemyClickListener();
        for (int i = 0; i < enemyBoard.length; i++) {
            for (int j = 0; j < enemyBoard[i].length; j++) {
                MyImageButton temp = new MyImageButton(this,getApplicationContext(), j, i , sizeOfEnemyImage);
                //setImage(temp , sizeOfButton , sizeOfButton , R.drawable.water);
                temp.setPadding(OFF_SET_OF_ENEMY_BOTTON, OFF_SET_OF_ENEMY_BOTTON, OFF_SET_OF_ENEMY_BOTTON, OFF_SET_OF_ENEMY_BOTTON);
                temp.setOnClickListener(listener);
                Ship ship = enemyBoard[i][j];
                if (ship != null)
                    ship.addButtonToShip(temp);
                allButton.add(temp);
                gridLayout.addView(temp);
            }
        }

        layoutParams.setMarginStart((screenWidth - gridLayout.getWidth()) / 9);
        layoutParams.setMarginEnd((screenWidth - gridLayout.getWidth()) / 9);
        gridLayout.setLayoutParams(layoutParams);
        overAllLayout.addView(gridLayout);
    }

    //only run on gui thread
    private void createTurnLable() {
        TextView textView = showWhosTurn = new TextView(this);
        textView.setText(lblWhosTurnKey);
        textView.setId(lblWhosTurnKey.hashCode());
        textView.setTextColor(Color.RED);
        textView.setTypeface(null, Typeface.BOLD);
        int width = textView.getWidth();
        textView.setPadding((screenWidth - width) / 5, OFF_SET_OF_ENEMY_BOTTON * 4, 0, OFF_SET_OF_ENEMY_BOTTON * 4);
        /*Button button = new Button(getApplication());
        button.setText("change");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ship[][] newBoard = gameManager.newEnemyBoard();
                createNewEnemyBoard(newBoard);
            }
        });*/
        overAllLayout.addView(textView);

    }

    private void setwhosTurn(final boolean playerTurn) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (playerTurn)
                    GameProccess.this.showWhosTurn.setText(GameProccess.this.playerTurn);
                else {
                    GameProccess.this.showWhosTurn.setText(computerTurn);
                }
            }
        });
    }

    /// only run on gui thread
    private void createPlayerBoard(Ship[][] playerBoard) {
        final int sizeOfButton = Math.abs((int) ((screenWidth / 4 - (float) screenWidth * PART_OF_MARGIN_FOR_SHIP_BUTTON) / (float) playerBoard.length));


        GridLayout gridLayout = new GridLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParams.setMargins(sizeOfMarginOverAll,sizeOfMarginOverAll,sizeOfMarginOverAll,sizeOfMarginOverAll); // margin of button
        gridLayout.setRowCount(playerBoard[0].length);
        gridLayout.setColumnCount(playerBoard.length);
        sizeOfplayerImage = sizeOfButton;

        for (int i = 0; i < playerBoard[i].length; i++) {
            for (int j = 0; j < playerBoard.length; j++) {
                ImageView temp = new ImageView(getApplicationContext());
                setImage(temp, sizeOfButton, sizeOfButton, playerBoard[j][i] == null ? R.drawable.water : R.drawable.sub_in_area);
                playerImageMatrix[i][j] = temp;
                Ship ship = gameManager.getShip(GameManager.MakeMove.COMPUTER, j, i);
                if (ship != null)
                    ship.addImageView(temp);
                gridLayout.addView(temp);
            }
        }

        layoutParams.setMargins(0, OFF_SET_OF_PLAYER_BOARD * 3, 0, 0);

        layoutParams.setMarginStart((int) ((screenWidth - gridLayout.getWidth()) / 3));
        layoutParams.setMarginEnd((int) ((screenWidth - gridLayout.getWidth()) / 3));
        gridLayout.setLayoutParams(layoutParams);
        overAllLayout.addView(gridLayout);
    }

    public void setImage(ImageView image , int width , int height , int picSource){
        Glide.with(this).load(picSource).override(width , height).fitCenter().dontAnimate().into(image);
    }

    class ButtonOfEnemyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            numberOfMoves++;
            MyImageButton button = (MyImageButton) v;
            int xIndex = button.getxIndex();
            int yIndex = button.getyIndex();
            GameManager.BombResult result = gameManager.makeMove(yIndex, xIndex, GameManager.MakeMove.PLAYER);
            switch (result) {
                case MISS:
                    //setImage(button, sizeOfEnemyImage, sizeOfEnemyImage, R.drawable.target_miss);
                    button.setMode(AnimationMode.MISS);
                    //enemyActionBoard[yIndex][xIndex] = GameManager.BombResult.MISS;
                    break;
                case HIT:
                    setImage(button, sizeOfEnemyImage, sizeOfEnemyImage, R.drawable.target_hit);
                    button.setMode(AnimationMode.HIT);
                    //enemyActionBoard[yIndex][xIndex] = GameManager.BombResult.HIT;
                    break;
                case DROWN_SHIP:
                    drawDrownShipOnButton(xIndex, yIndex);
                    break;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setwhosTurn(false);
                        setAllButtonStillPlayableToEnable(false);
                        Thread.sleep((int) (Math.random() * 3000));
                        if (enemy == null)
                            return;
                        Point p = enemy.play();
                        final int y = p.y;
                        final int x = p.x;
                        GameProccess.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enemy.setResult(doTheGameOfComputer(y, x));
                            }
                        });
                        setAllButtonStillPlayableToEnable(true);
                        setwhosTurn(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

            button.setEnabled(false);
            button.setDisabled(true);
        }
    }

    private GameManager.BombResult doTheGameOfComputer(int y, int x) {
        GameManager.BombResult result = gameManager.makeMove(y, x, GameManager.MakeMove.COMPUTER);

        switch (result) {
            case MISS:
                setImage( playerImageMatrix[x][y], sizeOfplayerImage , sizeOfplayerImage , R.drawable.target_miss);
                //enemyActionBoard[yIndex][xIndex] = GameManager.BombResult.MISS;
                break;
            case HIT:
                setImage(playerImageMatrix[x][y], sizeOfplayerImage, sizeOfplayerImage, R.drawable.sub_player_hit);
                //enemyActionBoard[yIndex][xIndex] = GameManager.BombResult.HIT;
                break;
            case DROWN_SHIP:
                drawDrownShipOnImage(x, y);
                break;
        }
        return result;
    }

    private void drawDrownShipOnImage(int xIndex, int yIndex) {
        Ship ship = gameManager.getShip(GameManager.MakeMove.COMPUTER, yIndex, xIndex);
        int shipSize = 0;
        GameManager.MyDirection direction = ship.getDirectionPlaced();
        ArrayList<ImageView> allView = ship.getAllImageView();
        if ((shipSize = ship.getSizeOfShip()) == 1) {
            setImage(allView.get(0), sizeOfplayerImage , sizeOfplayerImage ,(getRandomZeroOrOne() == 0 ? R.drawable.ship_player_hit_vertical : R.drawable.ship_player_hit_horizontal));
            checkIfGameIsEnded();
            return;
        }
        for (ImageView imgView : allView) {
            switch (direction) {
                case NORTH:
                case SOUTH:
                    setImage(imgView, sizeOfplayerImage, sizeOfplayerImage,  R.drawable.ship_player_hit_horizontal);
                    break;
                case EAST:
                case WEST:
                    setImage(imgView, sizeOfplayerImage, sizeOfplayerImage, R.drawable.ship_player_hit_vertical);
                    break;
            }
        }
        checkIfGameIsEnded();
    }

    private void setAllButtonStillPlayableToEnable(final boolean enable) {
        GameProccess.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (MyImageButton button : allButton)
                    if(!button.getDisabled())
                        button.setEnabled(enable);
            }
        });
    }

    private void drawDrownShipOnButton(int xIndex, int yIndex) {
        Ship ship = gameManager.getShip(GameManager.MakeMove.PLAYER, yIndex, xIndex);
        int shipSize = 0;
        setDownshipDisplayBarViewByOne(ship.getSizeOfShip());
        GameManager.MyDirection direction = ship.getDirectionPlaced();
        ArrayList<MyImageButton> allButton = ship.getAllButton();
        if ((shipSize = ship.getSizeOfShip()) == 1) {
            //setImage(allButton.get(0), sizeOfEnemyImage, sizeOfEnemyImage, (getRandomZeroOrOne() == 0 ? R.drawable.ship_enemy_hit_horizontal : R.drawable.ship_enemy_hit_vertical));
            allButton.get(0).setMode(AnimationMode.DRAOWN);
            checkIfGameIsEnded();
            return;
        }
        for (MyImageButton button : allButton) {

            /*switch (direction) {
                case NORTH:
                case SOUTH:
                    //setImage(button, sizeOfEnemyImage, sizeOfEnemyImage,  R.drawable.ship_enemy_hit_vertical);
                    break;
                case EAST:
                case WEST:
                    //setImage(button, sizeOfEnemyImage, sizeOfEnemyImage, R.drawable.ship_enemy_hit_horizontal);
                    break;
            }*/
            button.setMode(AnimationMode.DRAOWN);
        }
        checkIfGameIsEnded();
    }

    public int getRandomZeroOrOne() {
        return ((int) (Math.random() * 10000)) % 2;
    }

    public void createNewEnemyBoard(final Ship[][] newBoard){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < newBoard.length ; i++){
                    for(int j = 0 ; j < newBoard[i].length ; j++){

                        int numberOfShip = i * newBoard[i].length + j;
                        MyImageButton shipButton = allButton.get(numberOfShip);
                        Ship ship = newBoard[i][j];
                        if(ship == null){
                            //setImage(shipButton , sizeOfEnemyImage , sizeOfEnemyImage , R.drawable.water);
                            shipButton.setMode(AnimationMode.WATER);
                            shipButton.setEnabled(true);
                            shipButton.setDisabled(false);
                        }else {
                            shipButton.setxIndex(j);
                            shipButton.setyIndex(i);
                            ship.addButtonToShip(shipButton);
                            if(ship.needToDestroy()){
                                shipButton.setEnabled(false);
                                if(ship.shipHasBeenDestroyed()){
                                    shipButton.setMode(AnimationMode.DRAOWN);
                                }else{
                                    shipButton.setMode(AnimationMode.HIT);
                                }
                                shipButton.setDisabled(true);
                            }else{
                                shipButton.setMode(AnimationMode.WATER);
                                shipButton.setEnabled(true);
                                shipButton.setDisabled(false);
                            }
                        }

                    }
                }
            }
        });
    }


    public void checkIfGameIsEnded() {
        final GameManager.EndState endState;
        if ((endState = gameManager.gameEnded()) != null) {
            Thread goToWinLoseIntent = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setAllButtonStillPlayableToEnable(false);
                        Thread.sleep(400);
                        Intent intent = new Intent(GameProccess.this, WinLose.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(KEY_WIN_LOSE, endState.getValue());
                        bundle.putInt(MainActivity.KEY_BUNDLE_TO_CURRENT_DIFFICULTY, difficulty.getValue());
                        bundle.putInt(KEY_MOVE_NUMBER , numberOfMoves);
                        intent.putExtra(MainActivity.KEY_BUNDLE_TO_CREATE_BOARD, bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();// made lots of problem because of the memory image took

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            goToWinLoseIntent.start();
        }
    }

    @Override
    public void onDestroy() { // deal with memory lick problem of bitmap
        super.onDestroy();
        enemy.endLevel();
        enemy = null;
        gameManager.endLevel();
        gameManager = null;
        overAllLayout.destroyDrawingCache();
        allButton = null;
        playerImageMatrix = null;
        difficulty = null;
        showWhosTurn = null;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SensoresInformationGather.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBinder.closeIntent();
            mBound = false;
        }
        MyImageButton.setInitializeImages(false);
        System.gc();
    }

    SensoresInformationGather.SensorsBinder mBinder;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            mBinder = (SensoresInformationGather.SensorsBinder)service;
            mBinder.setEventListiner(new LisenerForSensor());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    class LisenerForSensor implements EventListener {
        public void goingToChange(){
            Toast.makeText(getApplicationContext(), "stop moving the phone or the ship will escape!!", Toast.LENGTH_SHORT).show();
        }

        public void change(){
            GameProccess.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Enemy Managed To Escape!", Toast.LENGTH_LONG).show();

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Ship[][] newBoard = gameManager.newEnemyBoard();
                            createNewEnemyBoard(newBoard);
                        }
                    });
                    thread.start();
                }
            });
        }

        public void stopTheChange(){
            Toast.makeText(getApplicationContext() , "Nothing happand be more carful!!" , Toast.LENGTH_LONG).show();
        }
    }
}
