package com.kirana.samsat.network.model;

public class Informasi {
    private int id;
    private String jadwal_buka;
    private String jadwal_tutup;
    private String jam_buka;
    private String jam_tutup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJadwal_buka() {
        return jadwal_buka;
    }

    public void setJadwal_buka(String jadwal_buka) {
        this.jadwal_buka = jadwal_buka;
    }

    public String getJadwal_tutup() {
        return jadwal_tutup;
    }

    public void setJadwal_tutup(String jadwal_tutup) {
        this.jadwal_tutup = jadwal_tutup;
    }

    public String getJam_buka() {
        return jam_buka;
    }

    public void setJam_buka(String jam_buka) {
        this.jam_buka = jam_buka;
    }

    public String getJam_tutup() {
        return jam_tutup;
    }

    public void setJam_tutup(String jam_tutup) {
        this.jam_tutup = jam_tutup;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private String created_at;
}
