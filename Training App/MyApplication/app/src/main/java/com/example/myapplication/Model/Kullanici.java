package com.example.myapplication.Model;

public class Kullanici {

    private String kullaniciIsmi;
    private String kullaniciEmail;
    private String kullaniciId;
    private long toplam_mesafe=0;
    private int toplam_süre=0;
    private long aktivite_sayisi=0;



    public Kullanici(String kullaniciIsmi, String kullaniciEmail,String kullaniciId,int toplam_mesafe,int toplam_süre,int aktivite_sayisi) {
        this.kullaniciIsmi = kullaniciIsmi;
        this.kullaniciEmail = kullaniciEmail;
        this.kullaniciId=kullaniciId;
        this.toplam_mesafe=toplam_mesafe;
        this.toplam_süre=toplam_süre;
        this.aktivite_sayisi=aktivite_sayisi;
    }

    public Kullanici() {
    }

    public String getKullaniciIsmi() {
        return kullaniciIsmi;
    }

    public void setKullaniciIsmi(String kullaniciIsmi) {
        this.kullaniciIsmi = kullaniciIsmi;
    }

    public String getKullaniciEmail() {
        return kullaniciEmail;
    }

    public void setKullaniciEmail(String kullaniciEmail) {
        this.kullaniciEmail = kullaniciEmail;
    }

    public String getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        this.kullaniciId = kullaniciId;
    }


    public long getToplam_mesafe() {
        return toplam_mesafe;
    }

    public void setToplam_mesafe(long toplam_mesafe) {
        this.toplam_mesafe = toplam_mesafe;
    }

    public int getToplam_süre() {
        return toplam_süre;
    }

    public void setToplam_süre(int toplam_süre) {
        this.toplam_süre = toplam_süre;
    }

    public long getAktivite_sayisi() {
        return aktivite_sayisi;
    }

    public void setAktivite_sayisi(long aktivite_sayisi) {
        this.aktivite_sayisi = aktivite_sayisi;
    }


}
