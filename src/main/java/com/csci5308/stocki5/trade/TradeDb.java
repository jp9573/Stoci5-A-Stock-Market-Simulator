package com.csci5308.stocki5.trade;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csci5308.stocki5.config.Stocki5DbConnection;

@Repository
public class TradeDb implements TradeDbInterface {

	@Autowired
	Stocki5DbConnection dbConnection;

	@Override
	public boolean insertTrade(Trade trade, boolean isHolding) {
		Connection connection = dbConnection.createConnection();

		try {
			String insertTradeSql = "INSERT INTO trade VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(insertTradeSql);

			statement.setString(1, trade.getTradeNumber());
			statement.setString(2, trade.getUserCode());
			statement.setInt(3, trade.getStockId());
			statement.setString(4, trade.getSymbol());
			statement.setString(5, trade.getSegment());
			statement.setString(6, String.valueOf(trade.getBuySell()));
			statement.setInt(7, trade.getQuantity());
			statement.setFloat(8, trade.getBuyPrice());
			statement.setFloat(9, trade.getSellPrice());
			statement.setDouble(10, trade.getTotalBuyPrice());
			statement.setDouble(11, trade.getTotalSellPrice());
			statement.setString(12, String.valueOf(trade.getStatus()));
			statement.setBoolean(13, isHolding);
			statement.setDate(14, new Date(System.currentTimeMillis()));
			int tradeCount = statement.executeUpdate();
			if (tradeCount > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			dbConnection.closeConnection(connection);
		}
	}

	@Override
	public Trade getTrade(String tradeNumber) {
		return null;
	}

	@Override
	public List<Trade> getTodaysTradeByUserCode(String userCode) {
		List<Trade> trades = new ArrayList<>();
		Connection connection = dbConnection.createConnection();
		try {
			String selectTradeSql = "SELECT * FROM trade WHERE userCode=? AND tradeDate=? ORDER BY tradeDate DESC";
			PreparedStatement statement = connection.prepareStatement(selectTradeSql);

			statement.setString(1, userCode);
			statement.setDate(2, new Date(System.currentTimeMillis()));
			ResultSet resultSet = statement.executeQuery();

			Trade trade = null;
			while (resultSet.next()) {
				trade = new Trade();
				trade.setTradeNumber(resultSet.getString("tradeNumber"));
				trade.setUserCode(resultSet.getString("userCode"));
				trade.setStockId(resultSet.getInt("stockId"));
				trade.setBuySell(TradeType.valueOf(resultSet.getString("buySell")));
				trade.setSymbol(resultSet.getString("symbol"));
				trade.setSegment(resultSet.getString("segment"));
				trade.setQuantity(resultSet.getInt("quantity"));
				trade.setBuyPrice(resultSet.getFloat("buyPrice"));
				trade.setSellPrice(resultSet.getFloat("sellPrice"));
				trade.setTotalBuyPrice(resultSet.getDouble("totalBuyPrice"));
				trade.setTotalSellPrice(resultSet.getDouble("totalSellPrice"));
				trade.setStatus(TradeStatus.valueOf(resultSet.getString("status")));
				trade.setTradeDate(resultSet.getDate("tradeDate"));
				trades.add(trade);
			}
			return trades;
		} catch (SQLException e) {
			e.printStackTrace();
			return trades;
		} finally {
			dbConnection.closeConnection(connection);
		}
	}

	@Override
	public List<Holding> getHoldingsByUserCode(String userCode) {
		List<Holding> holdings = new ArrayList<>();
		Connection connection = dbConnection.createConnection();
		try {
			String selectTradeSql = "SELECT * FROM trade INNER JOIN stock_data WHERE userCode=? AND tradeDate=? AND isHolding=1 ORDER BY tradeDate DESC";
			PreparedStatement statement = connection.prepareStatement(selectTradeSql);

			statement.setString(1, userCode);
			statement.setDate(2, new Date(System.currentTimeMillis()));
			ResultSet resultSet = statement.executeQuery();

			Holding holding = null;
			while (resultSet.next()) {
				holding = new Holding();
				holding.setTradeNumber(resultSet.getString("tradeNumber"));
				holding.setUserCode(resultSet.getString("userCode"));
				holding.setStockId(resultSet.getInt("stockId"));
				holding.setBuySell(TradeType.valueOf(resultSet.getString("buySell")));
				holding.setSymbol(resultSet.getString("symbol"));
				holding.setSegment(resultSet.getString("segment"));
				holding.setQuantity(resultSet.getInt("quantity"));
				holding.setBuyPrice(resultSet.getFloat("buyPrice"));
				holding.setSellPrice(resultSet.getFloat("price"));
				holding.setTotalBuyPrice(resultSet.getDouble("totalBuyPrice"));
				holding.setTotalSellPrice(resultSet.getDouble("totalSellPrice"));
				holding.setStatus(TradeStatus.valueOf(resultSet.getString("status")));
				holding.setIsHolding(resultSet.getBoolean("isHolding"));
				holding.setTradeDate(resultSet.getDate("tradeDate"));
				holdings.add(holding);
			}
			return holdings;
		} catch (SQLException e) {
			e.printStackTrace();
			return holdings;
		} finally {
			dbConnection.closeConnection(connection);
		}
	}

}
