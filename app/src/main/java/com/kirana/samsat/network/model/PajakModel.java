package com.kirana.samsat.network.model;

public class PajakModel {
    private int id_pajak, pkb_pokok, pkb_denda, jr_pokok, jr_denda,pnbp, total, status;
    private String nopol, jenis, merk, tipe, th_buat, masa_pajak, masa_stnk;

    public int getPkb_denda() {
        return pkb_denda;
    }

    public void setPkb_denda(int pkb_denda) {
        this.pkb_denda = pkb_denda;
    }

    public int getPnbp() {
        return pnbp;
    }

    public void setPnbp(int pnbp) {
        this.pnbp = pnbp;
    }

    public int getId_pajak() {
        return id_pajak;
    }

    public void setId_pajak(int id_pajak) {
        this.id_pajak = id_pajak;
    }

    public int getPkb_pokok() {
        return pkb_pokok;
    }

    public void setPkb_pokok(int pkb_pokok) {
        this.pkb_pokok = pkb_pokok;
    }


    public int getJr_pokok() {
        return jr_pokok;
    }

    public void setJr_pokok(int jr_pokok) {
        this.jr_pokok = jr_pokok;
    }

    public int getJr_denda() {
        return jr_denda;
    }

    public void setJr_denda(int jr_denda) {
        this.jr_denda = jr_denda;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNopol() {
        return nopol;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getTh_buat() {
        return th_buat;
    }

    public void setTh_buat(String th_buat) {
        this.th_buat = th_buat;
    }

    public String getMasa_pajak() {
        return masa_pajak;
    }

    public void setMasa_pajak(String masa_pajak) {
        this.masa_pajak = masa_pajak;
    }

    public String getMasa_stnk() {
        return masa_stnk;
    }

    public void setMasa_stnk(String masa_stnk) {
        this.masa_stnk = masa_stnk;
    }
}
