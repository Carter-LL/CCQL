package com.github.gamechampcrafted.CCQL;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Hashtable;
import java.util.Properties;

public class CQL {
    private PreparedStatement PS;
    private boolean bAUTOCLOSE = false;
    private boolean bAUTOEXECUTE = false;
    private Hashtable<Integer, Object> rawData = new Hashtable<Integer, Object>();
    private CQL_STATEMENT CqlSTATEMENT;
    private Connection CQL_DATABASE_CONNECTION;
    private String sCqlTABLE;
    private String[] sCqlVALUES;
    private String sSet;
    private String sSetequalsValue;
    private String sWhere;
    private Object oEqualsWhere;

    public Hashtable<Integer, Hashtable<String, String>> data = new Hashtable<Integer, Hashtable<String, String>>();

    //INSERT
    public CQL(CQL_STATEMENT CqlSTATEMENT, CQL_TABLE CqlTABLE, String...CqlVALUES){
        this.CqlSTATEMENT = CqlSTATEMENT;
        this.sCqlTABLE = CqlTABLE.TABLE.toString();
        this.sCqlVALUES = CqlVALUES;
        CQL_DATABASE_CONNECTION = new CQL_DATABASE().Connect();
    }
    //INSERT & PROPERTIES
    public CQL(CQL_STATEMENT CqlSTATEMENT, CQL_PROPERTY CqlPROPERTIES, CQL_TABLE CqlTABLE, String...CqlVALUES){
        this.CqlSTATEMENT = CqlSTATEMENT;
        this.sCqlTABLE = CqlTABLE.TABLE.toString();
        this.sCqlVALUES = CqlVALUES;
        SETPROPERTIES(CqlPROPERTIES.props);
        CQL_DATABASE_CONNECTION = new CQL_DATABASE().Connect();
    }
    //UPDATE
    public CQL(CQL_STATEMENT CqlSTATEMENT, CQL_TABLE CqlTABLE, String SET, String SET_EQUALS_VALUE, CQL_WHERE WHERE, Object EQUALS_WHERE){
        this.CqlSTATEMENT = CqlSTATEMENT;
        this.sCqlTABLE = CqlTABLE.TABLE.toString();
        this.sSet = SET;
        this.sSetequalsValue = SET_EQUALS_VALUE;
        this.sWhere = WHERE.WHERE.toString();
        this.oEqualsWhere = EQUALS_WHERE;
        CQL_DATABASE_CONNECTION = new CQL_DATABASE().Connect();
    }
    //UPDATE & PROPERTIES
    public CQL(CQL_STATEMENT CqlSTATEMENT, CQL_PROPERTY CqlPROPERTIES, CQL_TABLE CqlTABLE, String SET, String SET_EQUALS_VALUE, CQL_WHERE WHERE, Object EQUALS_WHERE){
        this.CqlSTATEMENT = CqlSTATEMENT;
        this.sCqlTABLE = CqlTABLE.TABLE.toString();
        this.sSet = SET;
        this.sSetequalsValue = SET_EQUALS_VALUE;
        SETPROPERTIES(CqlPROPERTIES.props);
        this.sWhere = WHERE.WHERE.toString();
        this.oEqualsWhere = EQUALS_WHERE;
        CQL_DATABASE_CONNECTION = new CQL_DATABASE().Connect();
    }
    //SELECT
    public CQL(CQL_STATEMENT CqlSTATEMENT, CQL_TABLE CqlTABLE, CQL_WHERE WHERE, CQL_WHERE EQUALS_WHERE, String...CqlVALUES){
        this.CqlSTATEMENT = CqlSTATEMENT;
        this.sCqlTABLE = CqlTABLE.TABLE.toString();
        this.sWhere = WHERE.WHERE.toString();
        this.sCqlVALUES = CqlVALUES;
        this.oEqualsWhere = EQUALS_WHERE.WHERE.toString();
        CQL_DATABASE_CONNECTION = new CQL_DATABASE().Connect();
        PREPARE();
        AUTOEXECUTE();
    }
    //SELECT & PROPERTIES
    public CQL(CQL_STATEMENT CqlSTATEMENT, CQL_PROPERTY CqlPROPERTIES, CQL_TABLE CqlTABLE, CQL_WHERE WHERE, CQL_WHERE EQUALS_WHERE, String...CqlVALUES){
        this.CqlSTATEMENT = CqlSTATEMENT;
        this.sCqlTABLE = CqlTABLE.TABLE.toString();
        this.sWhere = WHERE.WHERE.toString();
        SETPROPERTIES(CqlPROPERTIES.props);
        this.sCqlVALUES = CqlVALUES;
        this.oEqualsWhere = EQUALS_WHERE.WHERE.toString();
        CQL_DATABASE_CONNECTION = new CQL_DATABASE().Connect();
        PREPARE();
        AUTOEXECUTE();
    }


    public void SETPROPERTIES(CQL_PROPERTIES...CqlProperties) {
        for(CQL_PROPERTIES p : CqlProperties) {
            if(p == CQL_PROPERTIES.AUTOCLOSE) {
                this.bAUTOCLOSE = true;
            }
            if(p == CQL_PROPERTIES.AUTOEXECUTE) {
                this.bAUTOEXECUTE = true;
            }
        }
    }

    public void FINDABLE(Object...INSERTIONS){
        PREPARE();
        try{
            int COUNT = 0;
            for(Object o : INSERTIONS){
                COUNT+=1;
                rawData.put(COUNT, o);
            }
            rawData.forEach((k, v) -> {
                Class OBJECTCLASS = v.getClass();
                if(OBJECTCLASS.toString().equals("class java.lang.Integer")) {
                    SETINT(k, (Integer)v);
                }
                if(OBJECTCLASS.toString().equals("class java.lang.String")) {
                    SETSTRING(k,(String)v);
                }
            });
            AUTOEXECUTE();
        }catch (Exception EXCEPTION){
            System.out.println("Unable to FIND OBJECT");
            EXCEPTION.printStackTrace();
        }
    }

    private void SETSTRING(int BASE, String VALUE){
        try{
            PS.setString(BASE, VALUE);
        }catch (SQLException EXCEPTION){
            System.out.println("Unable to set VALUE STRING");
            EXCEPTION.printStackTrace();
        }
    }

    private void SETINT(int BASE, int VALUE) {
        try {
            PS.setInt(BASE, VALUE);
        } catch (SQLException EXCEPTION) {
            System.out.println("Unable to set VALUE INT");
            EXCEPTION.printStackTrace();
        }
    }

    public void CLOSE_CONNECTION(){
        try{
            PS.close();
        }catch (SQLException EXCEPTION){
            System.out.println("Unable to close Connection");
            EXCEPTION.printStackTrace();
        }
    }

    public void AUTOCLOSE_CONNECTION(){
        if(bAUTOCLOSE){
            try{
                PS.close();
            }catch (SQLException EXCEPTION){
                System.out.println("Unable to close Connection");
                EXCEPTION.printStackTrace();
            }
        }
    }

    private void PREPARE(){
        if(CqlSTATEMENT == CQL_STATEMENT.INSERT){
            try{
                String FORMAT = "";
                for(String s : sCqlVALUES){
                    FORMAT += s + ",";
                }
                FORMAT = FORMAT.substring(0, FORMAT.length() - 1);
                String PREPARE = "insert into " + sCqlTABLE + " values (" +  FORMAT + ")";

                PS = CQL_DATABASE_CONNECTION.prepareStatement(PREPARE);
            }catch (SQLException EXCEPTION){
                System.out.println("Unable to INSERT");
                EXCEPTION.printStackTrace();
            }
        }

        if(CqlSTATEMENT == CQL_STATEMENT.UPDATE){
            try{
                String PREPARE = "update " + sCqlTABLE + " set " + sSet + " = " + sSetequalsValue + " where " + sWhere + " = '" + oEqualsWhere + "'";
                PS = CQL_DATABASE_CONNECTION.prepareStatement(PREPARE);
            }catch (SQLException EXCEPTION){
                System.out.println("Unable to UPDATE");
                EXCEPTION.printStackTrace();
            }
        }

        if(CqlSTATEMENT == CqlSTATEMENT.SELECT){
            try{
                String FORMAT = "";
                for(String s : sCqlVALUES){
                    FORMAT += s + ",";
                }
                FORMAT = FORMAT.substring(0, FORMAT.length() - 1);
                String PREPARE = "select " + FORMAT + " from " + sCqlTABLE + " where " + sWhere + " = '" + oEqualsWhere + "'";
                PS = CQL_DATABASE_CONNECTION.prepareStatement(PREPARE);
            }catch (SQLException EXCEPTION){
                System.out.println("Unable to SELECT");
                EXCEPTION.printStackTrace();
            }
        }
    }

    private void AUTOEXECUTE(){
        if(bAUTOEXECUTE){
            EXECUTE();
        }
    }

    public void EXECUTE(){
        if(CqlSTATEMENT == CQL_STATEMENT.INSERT){
            try{
                PS.executeUpdate();
            }catch (SQLException EXCEPTION){
                System.out.println("Unable to EXECUTE INSERT");
                EXCEPTION.printStackTrace();
            }
        }

        if(CqlSTATEMENT == CQL_STATEMENT.UPDATE){
            try{
                PS.executeUpdate();
            }catch (SQLException EXCEPTION){
                System.out.println("Unable to EXECUTE UPDATE");
                EXCEPTION.printStackTrace();
            }
        }

        if(CqlSTATEMENT == CQL_STATEMENT.SELECT){
            try{
                ResultSet RS = PS.executeQuery();
                ResultSetMetaData RSMD = RS.getMetaData();
                int CCOUNT = RSMD.getColumnCount();
                int O = 1;
                while(RS.next()){
                    Hashtable<String, String> rawdatas = new Hashtable<String, String>();
                    for (int i = 1; i <= CCOUNT; i++ ) {
                        for(String s : sCqlVALUES) {
                            String name = RSMD.getColumnName(i);
                            if(RSMD.getColumnClassName(i) == "java.lang.String") {
                                if(name.equals(s)) {
                                    rawdatas.put(name, RS.getString(s));
                                }
                            }
                            if(RSMD.getColumnClassName(i) == "java.lang.Integer") {
                                if(name.equals(s)) {
                                    rawdatas.put(name, String.valueOf(RS.getInt(s)));
                                }
                            }
                            if(RSMD.getColumnClassName(i) == "java.lang.Boolean") {
                                if(name.equals(s)) {
                                    rawdatas.put(name, String.valueOf(RS.getInt(s)));
                                }
                            }
                        }
                    }
                    data.put(O, rawdatas);
                    O++;
                }
            }catch (SQLException EXCEPTION){
                System.out.println("Unable to EXECUTE SELECT");
                EXCEPTION.printStackTrace();
            }
        }
        AUTOCLOSE_CONNECTION();
    }
}
