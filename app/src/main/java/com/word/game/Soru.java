package com.word.game;

public class Soru {

    private String soru;
    private String cevap;
    private int puan;
    private String kategori;

    public Soru(String soru, String cevap, int puan, String kategori) {
        this.soru = soru;
        this.cevap = cevap;
        this.puan = puan;
        this.kategori = kategori;
    }

    public String getSoru() {
        return soru;
    }

    public void setSoru(String soru) {
        this.soru = soru;
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
}
