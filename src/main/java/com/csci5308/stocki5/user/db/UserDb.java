package com.csci5308.stocki5.user.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.csci5308.stocki5.user.IUser;
import com.csci5308.stocki5.user.factory.UserAbstractFactory;
import com.csci5308.stocki5.user.factory.UserFactory;
import org.springframework.stereotype.Repository;
import com.csci5308.stocki5.config.Stocki5DbConnection;

@Repository
public class UserDb implements IUserDb {

    Stocki5DbConnection dbConnection = new Stocki5DbConnection();
    UserAbstractFactory userFactory = UserFactory.instance();

    @Override
    public boolean insertUser(IUser user) {
        Connection connection = dbConnection.createConnection();
        try {
            String insertUserSql = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertUserSql);
            statement.setString(1, user.getUserCode());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmailId());
            statement.setString(5, user.getContactNo());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getConfirmPassword());
            statement.setDate(8, new Date(user.getDateOfBirth().getTime()));
            statement.setString(9, user.getGender());
            statement.setString(10, user.getAddress());
            statement.setString(11, user.getProvince());
            statement.setString(12, user.getCountry());
            statement.setInt(13, user.getInternationalStockExchange());
            statement.setInt(14, user.getInternationalDerivativeExchange());
            statement.setInt(15, user.getInternationalDerivativeExchange());
            statement.setInt(16, user.getForeignExchange());
            statement.setDouble(17, user.getFunds());
            statement.setString(18, user.getRole());
            int userCount = statement.executeUpdate();
            return userCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public boolean updateUser(IUser user) {
        Connection connection = dbConnection.createConnection();
        try {
            String updateUserSQL = "UPDATE user SET " + "firstName=?," + "lastName=?," + "emailId=?," + "contactNo=?,"
                    + "dateOfBirth=?," + "gender=?," + "address=?," + "province=?," + "country=?,"
                    + "internationalStockExchange=?," + "internationalDerivativeExchange=?,"
                    + "internationalCommodityExchange=?," + "foreignExchange=? " + "WHERE userCode=?";
            PreparedStatement statement = connection.prepareStatement(updateUserSQL);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmailId());
            statement.setString(4, user.getContactNo());
            statement.setDate(5, new Date(user.getDateOfBirth().getTime()));
            statement.setString(6, user.getGender());
            statement.setString(7, user.getAddress());
            statement.setString(8, user.getProvince());
            statement.setString(9, user.getCountry());
            statement.setInt(10, user.getInternationalStockExchange());
            statement.setInt(11, user.getInternationalDerivativeExchange());
            statement.setInt(12, user.getInternationalCommodityExchange());
            statement.setInt(13, user.getForeignExchange());
            statement.setString(14, user.getUserCode());
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public IUser getUser(String userCode) {
        Connection connection = dbConnection.createConnection();
        try {
            IUser user = userFactory.createUser();
            Statement statement = connection.createStatement();
            String selectUserSql = "SELECT * FROM user WHERE userCode='" + userCode + "'";
            ResultSet resultSet = statement.executeQuery(selectUserSql);
            while (resultSet.next()) {
                setUserValueFromResultSet(user, resultSet);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public IUser getUserByEmail(String email) {
        Connection connection = dbConnection.createConnection();
        try {
            IUser user = userFactory.createUser();
            Statement statement = connection.createStatement();
            String selectSql = "SELECT * FROM user where emailid='" + email + "'";
            ResultSet resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                setUserValueFromResultSet(user, resultSet);
            }
            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public boolean updateUserPassword(IUser user) {
        Connection connection = dbConnection.createConnection();
        try {
            String updateUserSQL = "UPDATE user SET password=?, confirmPassword=? WHERE userCode=?";
            PreparedStatement statement = connection.prepareStatement(updateUserSQL);
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getConfirmPassword());
            statement.setString(3, user.getUserCode());
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public double getUserFunds(String userCode) {
        Connection connection = dbConnection.createConnection();
        try {
            double userFunds = 0;
            Statement statement = connection.createStatement();
            String selectUserFundsSql = "SELECT funds FROM user WHERE userCode='" + userCode + "'";
            ResultSet resultSet = statement.executeQuery(selectUserFundsSql);
            while (resultSet.next()) {
                userFunds = resultSet.getDouble("funds");
            }
            return userFunds;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    @Override
    public boolean updateUserFunds(String userCode, double amount) {
        Connection connection = dbConnection.createConnection();
        try {
            String updateUserFundsSQL = "UPDATE user SET " + "funds=? " + "WHERE userCode=?";
            PreparedStatement statement = connection.prepareStatement(updateUserFundsSQL);
            statement.setDouble(1, amount);
            statement.setString(2, userCode);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    private void setUserValueFromResultSet(IUser user, ResultSet resultSet) throws SQLException {
        user.setUserCode(resultSet.getString("userCode"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setEmailId(resultSet.getString("emailId"));
        user.setContactNo(resultSet.getString("contactNo"));
        user.setGender(resultSet.getString("gender"));
        user.setCountry(resultSet.getString("country"));
        user.setAddress(resultSet.getString("address"));
        user.setProvince(resultSet.getString("province"));
        user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
        user.setFunds(resultSet.getDouble("funds"));
        user.setInternationalStockExchange(resultSet.getInt("internationalStockExchange"));
        user.setInternationalDerivativeExchange(resultSet.getInt("internationalDerivativeExchange"));
        user.setInternationalCommodityExchange(resultSet.getInt("internationalCommodityExchange"));
        user.setForeignExchange(resultSet.getInt("foreignExchange"));
    }
}