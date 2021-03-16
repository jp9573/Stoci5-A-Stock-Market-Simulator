package com.csci5308.stocki5.user;

import com.csci5308.stocki5.config.Stocki5DbConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserOtpDb implements UserOtpDbInterface {

    @Autowired
    Stocki5DbConnection dbConnection;

    private boolean executeDelete(String deleteSQL){
        Connection connection = dbConnection.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(deleteSQL);
            int result = statement.executeUpdate();
            if(result > 0){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public boolean insertOtp(UserOtp userOtp) {
        Connection connection = dbConnection.createConnection();
        String insertUserOtpSql = "INSERT INTO user_otp VALUES (?,?,?)";

        try {
            PreparedStatement statement = connection.prepareStatement(insertUserOtpSql);
            statement.setString(1, userOtp.getUserCode());
            statement.setInt(2, userOtp.getOtp());
            statement.setTimestamp(3, Timestamp.valueOf(userOtp.getValidity()));
            int otpCount = statement.executeUpdate();
            if (otpCount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public boolean updateOtp(UserOtp userOtp) {
        return false;
    }

    @Override
    public UserOtp getOtp(int otp) {
        Connection connection = dbConnection.createConnection();

        try {
            UserOtp userOtp = new UserOtp();
            String selectUserSql = "SELECT * FROM user_otp WHERE otp='" + String.valueOf(otp) + "'";
            PreparedStatement statement = connection.prepareStatement(selectUserSql);
            ResultSet resultSet = statement.executeQuery(selectUserSql);
            while (resultSet.next()) {
                userOtp.setUserCode(resultSet.getString("userCode"));
                userOtp.setOtp(resultSet.getInt("otp"));
                userOtp.setValidity(resultSet.getTimestamp("validity").toString());
            }
            return userOtp;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public boolean deleteOtp(int otp) {
        String deleteUserOtpSQL = "DELETE FROM user_otp WHERE otp="+String.valueOf(otp);
        return executeDelete(deleteUserOtpSQL);
    }

    @Override
    public boolean deleteOtpByUserCode(String userCode) {
        String deleteUserOtpSQL = "DELETE FROM user_otp WHERE userCode='"+userCode+"'";
        return executeDelete(deleteUserOtpSQL);
    }
}