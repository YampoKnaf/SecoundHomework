package com.yampoknaf.subibattle;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Orleg on 26/04/2016.
 */

enum AnimationMode {WATER , MISS , HIT , DRAOWN};
class MyImageButton extends ImageButton {

    private static boolean InitializeImages = false;

    private static int allWaterImages[] = new int[]{
            R.drawable.water0 , R.drawable.water1 , R.drawable.water2 , R.drawable.water3 ,
            R.drawable.water4 , R.drawable.water5 , R.drawable.water6 , R.drawable.water7 ,
            R.drawable.water8 , R.drawable.water9 , R.drawable.water10 , R.drawable.water11 ,
            R.drawable.water12 , R.drawable.water13 , R.drawable.water14 , R.drawable.water15
    };
    private static ImageView imagesWaterContainer[] = new ImageView[allWaterImages.length];

    private static int allHitImages[] = new int[]{
            R.drawable.explation00 , R.drawable.explation01 ,R.drawable.explation02 ,R.drawable.explation03 ,R.drawable.explation04 ,R.drawable.explation05 ,
            R.drawable.explation10 , R.drawable.explation11 ,R.drawable.explation12 ,R.drawable.explation13 ,R.drawable.explation14 ,R.drawable.explation15 ,
            R.drawable.explation20 , R.drawable.explation21 ,R.drawable.explation22 ,R.drawable.explation23 ,R.drawable.explation24 ,R.drawable.explation25 ,
            R.drawable.explation30 , R.drawable.explation31 ,R.drawable.explation32 ,R.drawable.explation33 ,R.drawable.explation34 ,R.drawable.explation35 ,
            R.drawable.explation40 , R.drawable.explation41 ,R.drawable.explation42 ,R.drawable.explation43 ,R.drawable.explation44 ,R.drawable.explation45 ,
            R.drawable.explation50 , R.drawable.explation51 ,R.drawable.explation52 ,R.drawable.explation53 ,R.drawable.explation54 ,R.drawable.explation55 ,
    };
    private static ImageView imagesHitContainer[] = new ImageView[allHitImages.length];

    private static int allMissImages[] = new int[]{
            R.drawable.water_sprite00 ,R.drawable.water_sprite01 ,R.drawable.water_sprite02 ,R.drawable.water_sprite03 ,R.drawable.water_sprite04 ,
            R.drawable.water_sprite10 ,R.drawable.water_sprite11 ,R.drawable.water_sprite12 ,R.drawable.water_sprite13 ,R.drawable.water_sprite14 ,
            R.drawable.water_sprite20 ,R.drawable.water_sprite21 ,R.drawable.water_sprite22 ,R.drawable.water_sprite23 ,R.drawable.water_sprite24 ,
            R.drawable.water_sprite30 ,R.drawable.water_sprite31 ,R.drawable.water_sprite32 ,R.drawable.water_sprite33 ,R.drawable.water_sprite34 ,
            R.drawable.water_sprite40 ,R.drawable.water_sprite41 ,R.drawable.water_sprite42 ,R.drawable.water_sprite43 ,R.drawable.water_sprite44 ,
            R.drawable.water_sprite50 ,R.drawable.water_sprite51 ,R.drawable.water_sprite52 ,R.drawable.water_sprite53 ,R.drawable.water_sprite54
    };
    private static ImageView imagesMissContainer[] = new ImageView[allMissImages.length];

    private static int allDrownImages[] = new int[]{
            R.drawable.fire_sprite00 ,R.drawable.fire_sprite01 ,R.drawable.fire_sprite02 ,R.drawable.fire_sprite03 ,
            R.drawable.fire_sprite10 ,R.drawable.fire_sprite11 ,R.drawable.fire_sprite12 ,R.drawable.fire_sprite13 ,
            R.drawable.fire_sprite20 ,R.drawable.fire_sprite21 ,R.drawable.fire_sprite22 ,R.drawable.fire_sprite23 ,
            R.drawable.fire_sprite30 ,R.drawable.fire_sprite31 ,R.drawable.fire_sprite32 ,R.drawable.fire_sprite33
    };
    private static ImageView imagesDrownContainer[] = new ImageView[allDrownImages.length];

    private int xIndex;
    private int yIndex;
    private int numberOfImage;
    private Thread animation;
    private boolean mKeepAnimeRun = true;
    private  AnimationMode mode = AnimationMode.WATER;
    private GameProccess mGameProccess;
    private int sizeOfEnemyImage;
    private boolean disabled = false;
    private static boolean mAllImagesContainers[] = new boolean[imagesWaterContainer.length];

    public MyImageButton(GameProccess game , Context context, int xIndex, int yIndex , int sizeOfButton) {
        super(context);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        mGameProccess = game;
        sizeOfEnemyImage = sizeOfButton;
        setNumberOfImage();
        InitImages();

        nextWaterImage();

        animation = new Thread(new Runnable() {
            @Override
            public void run() {
                while(mKeepAnimeRun){
                    try {
                        Thread.sleep(70);
                        mGameProccess.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //if(MyImageButton.this.mode != AnimationMode.WATER)
                                //Log.i("DAD" , MyImageButton.this.mode.toString());
                                switch (MyImageButton.this.mode) {
                                    case WATER:
                                    default:
                                        MyImageButton.this.nextWaterImage();
                                        break;
                                    case HIT:
                                        MyImageButton.this.nextHitImage();
                                        break;
                                    case MISS:
                                        MyImageButton.this.nextMissImage();
                                        break;
                                    case DRAOWN:
                                        MyImageButton.this.nextDrownImage();
                                        break;
                                }
                            }
                        });
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        animation.start();
    }

    private void setNumberOfImage() {
        numberOfImage = (int)((Math.random()*30000)%allWaterImages.length);
        for(int i = 0 ; i < allWaterImages.length ; i++){
            if(mAllImagesContainers[numberOfImage] == false){
                mAllImagesContainers[numberOfImage] = true;
                return;
            }else{
                numberOfImage = (numberOfImage + 1)%allWaterImages.length;
            }
        }
    }

    public void setMode(AnimationMode animationMode)
    {
        this.mode = animationMode;
        //Log.i("Button", this.xIndex + " , " + this.yIndex);
        if(animationMode != AnimationMode.WATER) {
            numberOfImage = 0;
        }
    }

    public static void setInitializeImages(boolean initializeImages) {
        InitializeImages = initializeImages;
    }

    public void die(){
        mKeepAnimeRun = false;
    }

    public void nextDrownImage()
    {
        numberOfImage++;
        numberOfImage %= imagesDrownContainer.length;
        this.setImageDrawable(imagesDrownContainer[numberOfImage].getDrawable());
    }

    public void nextHitImage()
    {
        numberOfImage++;
        numberOfImage %= imagesHitContainer.length;
        this.setImageDrawable(imagesHitContainer[numberOfImage].getDrawable());
    }

    public void nextMissImage()
    {
        numberOfImage++;
        numberOfImage %= imagesMissContainer.length;
        this.setImageDrawable(imagesMissContainer[numberOfImage].getDrawable());
    }

    public void nextWaterImage(){
        numberOfImage++;
        numberOfImage %= imagesWaterContainer.length;
        this.setImageDrawable(imagesWaterContainer[numberOfImage].getDrawable());
    }

    private void InitImages(){
        //Log.i("Init" , InitializeImages ? "true" : "false");
        if(!InitializeImages){
            InitializeImages = true;
            for(int i = 0 ; i < imagesWaterContainer.length ; i++){
                //imagesWaterContainer[i] = new ImageView(mGameProccess);
                mGameProccess.setImage(imagesWaterContainer[i], sizeOfEnemyImage, sizeOfEnemyImage, allWaterImages[i]);
            }
            for(int i = 0 ; i < imagesHitContainer.length ; i++){
                //imagesHitContainer[i] = new ImageView(mGameProccess);
                mGameProccess.setImage(imagesHitContainer[i], sizeOfEnemyImage, sizeOfEnemyImage, allHitImages[i]);
            }
            for(int i = 0 ; i < imagesMissContainer.length ; i++){
                //imagesMissContainer[i] = new ImageView(mGameProccess);
                mGameProccess.setImage(imagesMissContainer[i], sizeOfEnemyImage, sizeOfEnemyImage, allMissImages[i]);
            }
            for(int i = 0 ; i < imagesDrownContainer.length ; i++){
                //imagesDrownContainer[i] = new ImageView(mGameProccess);
                mGameProccess.setImage(imagesDrownContainer[i], sizeOfEnemyImage, sizeOfEnemyImage, allDrownImages[i]);
            }
        }
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean getDisabled(){
        return disabled;
    }



    public static void InitFromGameActivity(Context con){
            for(int i = 0 ; i < imagesWaterContainer.length ; i++){
                imagesWaterContainer[i] = new ImageView(con);
                Glide.with(con).load(allWaterImages[i]).fitCenter().dontAnimate().into(imagesWaterContainer[i]);
               // mGameProccess.setImage(imagesWaterContainer[i], sizeOfEnemyImage, sizeOfEnemyImage, allWaterImages[i]);
            }
            for(int i = 0 ; i < imagesHitContainer.length ; i++){
                imagesHitContainer[i] = new ImageView(con);
                Glide.with(con).load(allHitImages[i]).fitCenter().dontAnimate().into(imagesHitContainer[i]);
                //mGameProccess.setImage(imagesHitContainer[i], sizeOfEnemyImage, sizeOfEnemyImage, allHitImages[i]);
            }
            for(int i = 0 ; i < imagesMissContainer.length ; i++){
                imagesMissContainer[i] = new ImageView(con);
                Glide.with(con).load(allMissImages[i]).fitCenter().dontAnimate().into(imagesMissContainer[i]);
               // mGameProccess.setImage(imagesMissContainer[i], sizeOfEnemyImage, sizeOfEnemyImage, allMissImages[i]);
            }
            for(int i = 0 ; i < imagesDrownContainer.length ; i++){
                imagesDrownContainer[i] = new ImageView(con);
                Glide.with(con).load(allDrownImages[i]).fitCenter().dontAnimate().into(imagesDrownContainer[i]);
                //mGameProccess.setImage(imagesDrownContainer[i], sizeOfEnemyImage, sizeOfEnemyImage, allDrownImages[i]);
            }
    }
}