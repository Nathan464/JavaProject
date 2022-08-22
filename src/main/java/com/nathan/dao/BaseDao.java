package com.nathan.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static final String driver;
    private static final String url;
    private static final String username;
    private static final String password;

    static {
        Properties properties = new Properties();
        InputStream inputStream =
                BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static ResultSet execute(Connection connection, String sql,
                                    Object[] params, ResultSet resultSet,
                                    PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static int execute(Connection connection, String sql, Object[] params,
                              PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeUpdate();
    }

    public static boolean closeResource(Connection connection, PreparedStatement preparedStatement,
                                        ResultSet resultSet) {
        boolean flag = true;
        // 关闭资源后将资源设置为空方便垃圾回收
        if (resultSet != null) {
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
