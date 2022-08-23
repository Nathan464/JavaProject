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
        InputStream inputStream = BaseDao.class.getClassLoader().
                getResourceAsStream("db.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driver = properties.getProperty("driver");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        url = properties.getProperty("url");
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
                for (int i = 1; i < params.length; i++) {
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
                resultSet = null;  //便于GC
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //关闭sql执行对象
        if(preparedStatement!=null){
            try{
                preparedStatement.close();
                preparedStatement = null; //便于GC
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //关闭数据库连接
        if(connection!=null){
            try{
                connection.close();
                connection = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
