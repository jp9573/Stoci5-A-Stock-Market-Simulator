package com.csci5308.stocki5.trade.sell;

import com.csci5308.stocki5.stock.IStock;
import com.csci5308.stocki5.stock.db.IStockDb;
import com.csci5308.stocki5.stock.db.IStockDbGainersLosers;
import com.csci5308.stocki5.stock.factory.StockAbstractFactory;
import com.csci5308.stocki5.stock.factory.StockFactory;
import com.csci5308.stocki5.stock.fetch.IStockFetch;
import com.csci5308.stocki5.trade.ITrade;
import com.csci5308.stocki5.trade.db.ITradeDb;
import com.csci5308.stocki5.trade.factory.TradeAbstractFactory;
import com.csci5308.stocki5.trade.factory.TradeFactory;
import com.csci5308.stocki5.trade.order.ITradeOrder;
import com.csci5308.stocki5.user.db.IUserDb;
import com.csci5308.stocki5.user.factory.UserAbstractFactory;
import com.csci5308.stocki5.user.factory.UserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class TradeSellController
{
	public static final String SELL_STOCK_ID = "sellstockid";
	public static final String QUANTITY = "quantity";
	public static final String TRADE_BUY_NUMBER = "tradeBuyNumber";
	public static final String ORDERS = "orders";
	public static final String STOCKS = "stocks";
	public static final String GAINERS = "gainers";
	public static final String LOSERS = "losers";
	public static final String SET_QUANTITY = "setquantity";
	public static final String SET_SELL_PRICE = "setsellprice";
	public static final String INSUFFICIENT_FUNDS_ERROR_MESSAGE = "Insufficient funds";

	TradeAbstractFactory tradeFactory = TradeAbstractFactory.instance();
	UserAbstractFactory userFactory = UserFactory.instance();
	StockAbstractFactory stockFactory = StockAbstractFactory.instance();

	IStockDbGainersLosers iStockDbGainersLosers = stockFactory.createStockDbGainersLosers();
	IStockFetch iStockFetch = stockFactory.createStockFetch();
	IStockDb iStockDb = stockFactory.createStockDb();
	ITradeOrder iTradeOrder = tradeFactory.createTradeOrder();
	ITradeSell iTradeSell = tradeFactory.createTradeSell();
	ITradeDb tradeDb = tradeFactory.createTradeDb();
	IUserDb userDb = userFactory.createUserDb();

	@RequestMapping(value = "/sellstock", method = RequestMethod.POST)
	public ModelAndView sellStock(HttpServletRequest request, @RequestParam(value = SELL_STOCK_ID) int stockId, @RequestParam(value = QUANTITY) int quantity, @RequestParam(value = TRADE_BUY_NUMBER) String tradeBuyNumber)
	{
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();

		boolean isSold = iTradeSell.sellStock(principal.getName(), stockId, quantity, iStockDb, userDb, tradeDb, tradeBuyNumber);
		if (isSold)
		{
			List<ITrade> orders = iTradeOrder.fetchUserOrders(principal.getName(), tradeDb);
			model.addObject(ORDERS, orders);
			model.setViewName("orders");
			return model;
		}

		List<IStock> stocks = iStockFetch.fetchUserStocks(iStockDb, userDb, principal.getName());
		List<IStock> top5GainersStocks = iStockFetch.fetchTopGainerStocks(iStockDbGainersLosers, userDb, principal.getName());
		List<IStock> top5LosersStocks = iStockFetch.fetchTopLoserStocks(iStockDbGainersLosers, userDb, principal.getName());

		model.addObject(STOCKS, stocks);
		model.addObject(GAINERS, top5GainersStocks);
		model.addObject(LOSERS, top5LosersStocks);
		model.addObject("error", INSUFFICIENT_FUNDS_ERROR_MESSAGE);
		model.setViewName("stocks");
		return model;
	}

	@RequestMapping(value = "/setsellstock", method = RequestMethod.POST)
	public ModelAndView setBwllStock(HttpServletRequest request, @RequestParam(value = SELL_STOCK_ID) int stockId, @RequestParam(value = SET_QUANTITY) int quantity, @RequestParam(value = SET_SELL_PRICE) float sellPrice, @RequestParam(value = TRADE_BUY_NUMBER) String tradeBuyNumber)
	{
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();

		boolean isSold = iTradeSell.setSellPrice(principal.getName(), stockId, quantity, sellPrice, iStockDb, userDb, tradeDb, tradeBuyNumber);
		if (isSold)
		{
			List<ITrade> orders = iTradeOrder.fetchUserOrders(principal.getName(), tradeDb);
			model.addObject(ORDERS, orders);
			model.setViewName("orders");
			return model;
		}

		List<IStock> stocks = iStockFetch.fetchUserStocks(iStockDb, userDb, principal.getName());
		List<IStock> top5GainersStocks = iStockFetch.fetchTopGainerStocks(iStockDbGainersLosers, userDb, principal.getName());
		List<IStock> top5LosersStocks = iStockFetch.fetchTopLoserStocks(iStockDbGainersLosers, userDb, principal.getName());

		model.addObject(STOCKS, stocks);
		model.addObject(GAINERS, top5GainersStocks);
		model.addObject(LOSERS, top5LosersStocks);
		model.addObject("error", INSUFFICIENT_FUNDS_ERROR_MESSAGE);
		model.setViewName("stocks");
		return model;
	}
}
