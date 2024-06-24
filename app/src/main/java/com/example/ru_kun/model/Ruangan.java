package com.example.ru_kun.model;

import java.io.Serializable;

public class Ruangan implements Serializable {
    private String nama;
    private String jenis;
    private String kondisi;
    private String deskripsi;
    private String tanggal;
    private String waktuMulai;
    private String waktuSelesai;
    private String waktuTersedia;
    private String image;

    // Constructors, getters, and setters
    public Ruangan() {
    }

    public Ruangan(String nama,String jenis, String kondisi, String deskripsi, String tanggal, String waktuMulai, String waktuSelesai, String waktuTersedia, String image) {
        this.nama = nama;
        this.jenis = jenis;
        this.kondisi = kondisi;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.waktuTersedia = waktuTersedia;
        this.image = image;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(String waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public String getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(String waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public String getWaktuTersedia() {
        return waktuTersedia;
    }

    public void setWaktuTersedia(String waktuTersedia) {
        this.waktuTersedia = waktuTersedia;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
