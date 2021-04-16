package com.word.game.Models;

public class Soru {

    private String soru;
    private String userName;
    private String cevap;
    private int puan;
    private String kategori;
    private String zaman;
    private String soruKey;
    private String soruDurum;
    private String notif;

    public Soru(String soru, String cevap, int puan, String kategori, String userName, String zaman, String soruKey, String soruDurum, String notif) {
        this.soru = soru;
        this.cevap = cevap;
        this.puan = puan;
        this.kategori = kategori;
        this.userName = userName;
        this.zaman = zaman;
        this.soruKey = soruKey;
        this.soruDurum = soruDurum;
        this.notif = notif;

    }

    public String getSoru() {
        return soru;
    }

    public String getNotif() {
        return notif;
    }

    public void setSoru(String soru) {
        this.soru = soru;
    }

    public String getSoruDurum() {
        return soruDurum;
    }

    public void setSoruDurum(String soruDurum) {
        this.soruDurum = soruDurum;
    }

    public String getSoruKey() {
        return soruKey;
    }

    public void setSoruKey(String soruKey) {
        this.soruKey = soruKey;
    }

    public String getCevap() {
        return cevap;
    }

    public void setCevap(String cevap) {
        this.cevap = cevap;
    }

    public int getPuan() {
        return puan;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getZaman() {
        return zaman;
    }

    public void setZaman(String zaman) {
        this.zaman = zaman;
    }
}
