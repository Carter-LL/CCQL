package com.github.gamechampcrafted.CCQL;

public class CQL_INIT {
    @Deprecated
    protected static String Host = "";
    @Deprecated
    protected static String Port = "";
    @Deprecated
    protected static String Database = "";
    @Deprecated
    protected static String Username = "";
    @Deprecated
    protected static String Password = "";

    public static void INIT(String h, String p, String d, String u, String pw) {
        Host = h;
        Port = p;
        Database = d;
        Username = u;
        Password = pw;
    }
}
