package com.yampoknaf.subibattle;

import android.widget.ImageView;

import java.util.ArrayList;

public class Ship {
    private int size;
    private int numberOfPartDemaged;
    private int shipDestroyedInitialize;
    private GameManager.MyDirection directionPlaced;
    private ArrayList<MyImageButton> allButton;
    private ArrayList<ImageView> allImageView;

    public Ship(int sizeOfShip){
        size = sizeOfShip;
        numberOfPartDemaged = 0;
        allButton = new ArrayList<>();
        allImageView = new ArrayList<>();
    }

    public boolean doDemage(){
        numberOfPartDemaged++;
        return shipHasBeenDestroyed();
    }

    public boolean shipHasBeenDestroyed(){
        return size == numberOfPartDemaged;
    }

    public int getSizeOfShip(){
        return size;
    }

    public void setDirectionPlaced(GameManager.MyDirection directionPlaced) {
        this.directionPlaced = directionPlaced;
    }

    public int shipTryEscape(){
        allButton = new ArrayList<>();
        allImageView = new ArrayList<>();
        shipDestroyedInitialize = 0;
        if(size != numberOfPartDemaged && numberOfPartDemaged != 0) {
            numberOfPartDemaged--;
            return 1;
        }
        return 0;
    }

    public boolean needToDestroy() {
        if(shipDestroyedInitialize == numberOfPartDemaged){
            return false;
        }else{
            shipDestroyedInitialize++;
            return true;
        }
    }

    public GameManager.MyDirection getDirectionPlaced() {
        return directionPlaced;
    }

    public void addButtonToShip(MyImageButton but){
        allButton.add(but);
    }

    public ArrayList<MyImageButton> getAllButton(){
        return allButton;
    }

    public void addImageView(ImageView imageView){
        allImageView.add(imageView);
    }

    public ArrayList<ImageView> getAllImageView() {
        return allImageView;
    }
}
