package com.github.gamechampcrafted.CCQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CQL_DATABASE {
    @Deprecated
    protected Connection Connect(){
        try{
            Properties PROPERTIES = new Properties();
            PROPERTIES.put("useSSL", "false");
            Class.forName("com.mysql.jdbc.Driver");
            String DATABASE_CONNECTION = "jdbc:mysql://"+ CQL_INIT.Host+":"+CQL_INIT.Port+"/"+ CQL_INIT.Database+"?user="+CQL_INIT.Username+"&password="+CQL_INIT.Password;;
            return DriverManager.getConnection(DATABASE_CONNECTION, PROPERTIES);
        }catch (SQLException | ClassNotFoundException EXCEPTION){
            System.out.println("CQL Connection: null");
            EXCEPTION.printStackTrace();
            return null;
        }
    }
}
