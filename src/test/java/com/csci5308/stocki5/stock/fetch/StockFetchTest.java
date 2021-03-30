package com.csci5308.stocki5.stock.fetch;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.csci5308.stocki5.stock.Stock;
import com.csci5308.stocki5.stock.db.StockDbGainersLosersMock;
import com.csci5308.stocki5.stock.db.StockDbMock;
import com.csci5308.stocki5.user.User;
import com.csci5308.stocki5.user.UserDbMock;

public class StockFetchTest
{
	private UserDbMock userDbMock = null;
	private StockDbGainersLosersMock gainersLosersMock = null;
	private StockDbMock stockDbMock = null;
	private StockFetch stockFetch = null;
	private User user = null;

	@Before
	public void createObjects()
	{
		userDbMock = new UserDbMock();
		gainersLosersMock = new StockDbGainersLosersMock();
		stockDbMock = new StockDbMock();
		stockFetch = new StockFetch();
		user = userDbMock.getUser("AB123456");
	}

	@After
	public void destroyObjects()
	{
		userDbMock = null;
		gainersLosersMock = null;
		stockDbMock = null;
		stockFetch = null;
		user = null;
	}

	@Test
	public void getUserSegmentsTest()
	{
		Assert.assertEquals("'FOREX','IDE','ICE','ISE'", stockFetch.getUserStockSegments(user));
	}

	@Test
	public void getUserSegmentsTestThreeSegments()
	{
		user.setForeignExchange(0);
		Assert.assertEquals("'IDE','ICE','ISE'", stockFetch.getUserStockSegments(user));
	}

	@Test
	public void getUserSegmentsTestTwoSegments()
	{
		user.setForeignExchange(0);
		user.setInternationalDerivativeExchange(0);
		Assert.assertEquals("'ICE','ISE'", stockFetch.getUserStockSegments(user));
	}

	@Test
	public void getUserSegmentsTestOneSegment()
	{
		user.setForeignExchange(0);
		user.setInternationalDerivativeExchange(0);
		user.setInternationalCommodityExchange(0);
		Assert.assertEquals("'ISE'", stockFetch.getUserStockSegments(user));
	}

	@Test
	public void getUserSegmentsTestZeroSegment()
	{
		user.setForeignExchange(0);
		user.setInternationalDerivativeExchange(0);
		user.setInternationalCommodityExchange(0);
		user.setInternationalStockExchange(0);
		Assert.assertEquals("", stockFetch.getUserStockSegments(user));
	}

	@Test
	public void fetchUserStocksTest()
	{
		user.setInternationalDerivativeExchange(0);
		user.setInternationalCommodityExchange(0);
		user.setInternationalStockExchange(0);
		List<Stock> stocks = stockFetch.fetchUserStocks(stockDbMock, userDbMock, "AB123456");
		Assert.assertEquals("FOREX", stocks.get(0).getSegment());
	}

	@Test
	public void fetchTopGainerStocksTest()
	{
		user.setInternationalDerivativeExchange(0);
		user.setInternationalCommodityExchange(0);
		user.setInternationalStockExchange(0);
		List<Stock> topGainersStocks = stockFetch.fetchTopGainerStocks(gainersLosersMock, userDbMock, "AB123456");
		Assert.assertEquals(5, topGainersStocks.size());
	}

	@Test
	public void fetchTopLoserStocksTest()
	{
		user.setInternationalDerivativeExchange(0);
		user.setInternationalCommodityExchange(0);
		user.setInternationalStockExchange(0);
		List<Stock> topLosersStocks = stockFetch.fetchTopLoserStocks(gainersLosersMock, userDbMock, "AB123456");
		Assert.assertEquals(5, topLosersStocks.size());
	}
}
