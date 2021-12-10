package com.kirana.samsat.network.model;

public class AntrianModel {
    private int id_antrian, kostumer_id, status;
    private String created_at, nama, alamat, email, no_hp, no_antrian,panggil_antrian;

    public String getPanggil_antrian() {
        return panggil_antrian;
    }

    public void setPanggil_antrian(String panggil_antrian) {
        this.panggil_antrian = panggil_antrian;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId_antrian() {
        return id_antrian;
    }

    public void setId_antrian(int id_antrian) {
        this.id_antrian = id_antrian;
    }

    public int getKostumer_id() {
        return kostumer_id;
    }

    public void setKostumer_id(int kostumer_id) {
        this.kostumer_id = kostumer_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getNo_antrian() {
        return no_antrian;
    }

    public void setNo_antrian(String no_antrian) {
        this.no_antrian = no_antrian;
    }
}
