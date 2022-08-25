package com.nathan.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static final String driver;
    private static final String username;
    private static final String password;
    private static final String url;

    static {
//
        driver = "com.mysql.cj.jdbc.Driver";
        url = "jdbc:mysql://localhost:3306/smbms?useSSL=true&useUnicode=true&characterEncoding=UTF-8";
        username = "root";
        password = "123456";
    }

    public static Connection getConnection() {
        Connection connection;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static ResultSet execute(Connection connection, PreparedStatement preparedStatement,
                                    String sql, Object[] params,
                                    ResultSet resultSet) throws SQLException{
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (params!=null){
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i+1, params[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public static int execute(Connection connection, PreparedStatement preparedStatement,
                              String sql, Object[] params) throws SQLException{
        int effectRows;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (params!=null){
                for (int i = 1; i <= params.length; i++) {
                    preparedStatement.setObject(i,params[i-1]);
                }
            }
            effectRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return effectRows;
    }

    public static void closeResources(Connection connection, PreparedStatement preparedStatement,
                                      ResultSet resultSet) throws SQLException{
        //关闭结果集
        if(resultSet!=null){
            try{
                resultSet.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //关闭sql执行对象
        if(preparedStatement!=null){
            try{
                preparedStatement.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //关闭数据库连接
        if(connection!=null){
            try{
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
