package com.example.ru_kun.model;

import java.io.Serializable;

public class Peminjaman implements Serializable {
    private String idAjuan;
    private String idPinjam;
    private String namaUser;
    private String nim;
    private String namaRuangan;
    private String tanggalPinjam;
    private String waktuMulai;
    private String waktuSelesai;
    private String namaKegiatan;
    private String fileUrl;
    private String image;
    private String namaAdmin;
    private String tanggalDisetujui;
    private String catatan;

    public Peminjaman() {
        // Default constructor required for calls to DataSnapshot.getValue(Peminjaman.class)
    }

    public Peminjaman(String idAjuan, String idPinjam, String namaUser, String nim, String namaRuangan, String tanggalPinjam,
                           String waktuMulai, String waktuSelesai, String namaKegiatan, String fileUrl, String image,
                            String namaAdmin, String tanggalDisetujui, String catatan) {
        this.idAjuan = idAjuan;
        this.idPinjam = idPinjam;
        this.namaUser = namaUser;
        this.nim = nim;
        this.namaRuangan = namaRuangan;
        this.tanggalPinjam = tanggalPinjam;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.namaKegiatan = namaKegiatan;
        this.fileUrl = fileUrl;
        this.image = image;
        this.namaAdmin = namaAdmin;
        this.tanggalDisetujui = tanggalDisetujui;
        this.catatan = catatan;
    }
    public String getIdAjuan() {
        return idAjuan;
    }
    public void setIdAjuan(String idAjuan) {
        this.idAjuan = idAjuan;
    }
    public String getIdPinjam() {
        return idPinjam;
    }
    public void setIdPinjam(String idPinjam) {
        this.idPinjam = idPinjam;
    }
    public String getNamaUser() {
        return namaUser;
    }
    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNamaRuangan() {
        return namaRuangan;
    }

    public void setNamaRuangan(String namaRuangan) {
        this.namaRuangan = namaRuangan;
    }
    public String getTanggalPinjam() {
        return tanggalPinjam;
    }
    public void setTanggalPinjam(String tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
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

    public String getNamaKegiatan() {
        return namaKegiatan;
    }
    public void setNamaKegiatan(String namaKegiatan) {
        this.namaKegiatan = namaKegiatan;
    }
    public String getFileUrl() {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getNamaAdmin() {
        return namaAdmin;
    }
    public void setNamaAdmin(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }
    public String getTanggalDisetujui() {
        return tanggalDisetujui;
    }

    public void setTanggalDisetujui(String tanggalDisetujui) {
        this.tanggalDisetujui = tanggalDisetujui;
    }
    public String getCatatan() {
        return catatan;
    }
    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
