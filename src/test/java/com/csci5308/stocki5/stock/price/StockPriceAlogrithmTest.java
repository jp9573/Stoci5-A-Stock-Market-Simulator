package com.csci5308.stocki5.stock.price;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.csci5308.stocki5.stock.db.StockDbMock;
import com.csci5308.stocki5.stock.history.StockMaintainHistory;
import com.csci5308.stocki5.trade.buy.TradeBuy;
import com.csci5308.stocki5.trade.sell.TradeSell;

public class StockPriceAlogrithmTest
{
	private StockPriceAlgorithm stockPriceAlgorithm = null;
	private StockDbMock stockDbMock = null;
	private TradeBuy tradeBuy = null;
	private TradeSell tradeSell = null;
	private StockMaintainHistory stockMaintainHistory = null;
	private float currentPrice = 0.00f;
	private float previousPrice = 0.00f;
	private int limit = 0;

	@Before
	public void createStock()
	{
		stockPriceAlgorithm = new StockPriceAlgorithm();
		stockDbMock = new StockDbMock();
		tradeBuy = new TradeBuy();
		tradeSell = new TradeSell();
		stockMaintainHistory = new StockMaintainHistory();
		currentPrice = 125.02f;
		previousPrice = 42.63f;
		limit = 10;
	}

	@After
	public void destroyStock()
	{
		stockPriceAlgorithm = null;
		stockDbMock = null;
		tradeBuy = null;
		tradeSell = null;
		stockMaintainHistory = null;
		currentPrice = 0.00f;
		previousPrice = 0.00f;
		limit = 0;
	}

	@Test
	public void stockPriceAlgorithmTest()
	{
		Assert.assertNotNull(stockPriceAlgorithm.stockPriceAlgorithm(currentPrice));
	}

	@Test
	public void stockPriceAlgorithmTestNotMoreThanTenPlusStockPrice()
	{
		float maxStockPrice = currentPrice + limit;
		float newStockPrice = stockPriceAlgorithm.stockPriceAlgorithm(currentPrice);
		Assert.assertTrue(maxStockPrice >= newStockPrice);
	}

	@Test
	public void stockPriceAlgorithmTestNotLessThanTenMinusStockPrice()
	{
		float minStockPrice = currentPrice - limit;
		float newStockPrice = stockPriceAlgorithm.stockPriceAlgorithm(currentPrice);
		Assert.assertTrue(minStockPrice <= newStockPrice);
	}

	@Test
	public void stockPriceAlgorithmTestNegativeNumber()
	{
		currentPrice = -25.00f;
		float newStockPrice = stockPriceAlgorithm.stockPriceAlgorithm(currentPrice);
		Assert.assertTrue(newStockPrice >= 0.00f);
	}

	@Test
	public void stockPriceAlgorithmTestZero()
	{
		currentPrice = 0.00f;
		float newStockPrice = stockPriceAlgorithm.stockPriceAlgorithm(currentPrice);
		Assert.assertTrue(newStockPrice >= 0.00f);
	}

	@Test
	public void stockPricePercentIncreaseDecreaseTest()
	{
		currentPrice = 47.89f;
		float percent = stockPriceAlgorithm.stockPricePercentIncreaseDecrease(currentPrice, previousPrice);
		Assert.assertEquals(percent, 12.34f, 0);
	}

	@Test
	public void generateStockPriceTest()
	{
		boolean isStockPriceGenerated = stockPriceAlgorithm.generateStockPrice(stockDbMock, tradeBuy, tradeSell, stockMaintainHistory);
		Assert.assertEquals(false, isStockPriceGenerated);
	}
}
