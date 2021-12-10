package com.kirana.samsat.network.api;

public class URLServer {
    public static final String BASE_URL = "http://antrian-samsat.prozv.net/api/";
    public static final String LOGIN = BASE_URL + "auth/login";
    public static final String LOGOUT = BASE_URL + "auth/logout";
    public static final String REGISTER = BASE_URL + "auth/register";
    public static final String UBAHPASS = BASE_URL + "user/password";
    public static final String UBAHPROFILE = BASE_URL + "user/edit";
    public static final String GETANTRIAN = BASE_URL + "antrian";
    public static final String GETINFORMASI = BASE_URL + "informasi";
    public static final String GETNOANTRIAN = BASE_URL + "noantrian";
    public static final String POSTANTRIAN = BASE_URL + "antrian";
    public static final String GETPAJAK = BASE_URL + "pajak";
}
