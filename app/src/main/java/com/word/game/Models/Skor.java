package com.word.game.Models;

public class Skor {

    private int skor;
    private int sonSkor;
    private String zaman;
    private String userID;
    private String userName;


    public Skor(int skor, int sonSkor, String zaman, String userID, String userName) {
        this.skor = skor;
        this.zaman = zaman;
        this.userID = userID;
        this.userName = userName;
        this.sonSkor = sonSkor;

    }

    public String getUserID() {
        return userID;
    }

    public int getSonSkor() {
        return sonSkor;
    }


    public String getUserName() {
        return userName;
    }

    public int getSkor() {
        return skor;
    }


    public String getZaman() {
        return zaman;
    }


}