package com.csci5308.stocki5.trade;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.csci5308.stocki5.trade.buy.TradeBuy;
import com.csci5308.stocki5.trade.holding.Holding;
import com.csci5308.stocki5.trade.holding.HoldingFetch;
import com.csci5308.stocki5.trade.order.TradeFetch;
import com.csci5308.stocki5.trade.sell.TradeSell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.csci5308.stocki5.stock.Stock;
import com.csci5308.stocki5.stock.StockDb;
import com.csci5308.stocki5.stock.StockFetch;
import com.csci5308.stocki5.user.UserDb;

@Controller
public class TradeController {

	@Autowired
	StockFetch stockFetch;

	@Autowired
	TradeFetch tradeFetch;

	@Autowired
	HoldingFetch holdingFetch;

	@Autowired
	TradeBuy tradeBuy;

	@Autowired
	TradeSell tradeSell;

	@Autowired
	StockDb stockDb;

	@Autowired
	UserDb userDb;

	@Autowired
	TradeDb tradeDb;

	@RequestMapping(value = { "/orders" }, method = RequestMethod.GET)
	public ModelAndView welcomePage(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();

		List<Trade> orders = tradeFetch.fetchUserOrders(principal.getName(), tradeDb);
		model.addObject("orders", orders);
		model.setViewName("orders");

		return model;
	}

	@RequestMapping(value = { "/holdings" }, method = RequestMethod.GET)
	public ModelAndView getHoldings(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();

		List<Holding> orders = holdingFetch.fetchUserHoldings(principal.getName(), tradeDb, stockDb);
		model.addObject("orders", orders);
		model.setViewName("holdings");

		return model;
	}

	@RequestMapping(value = "/buystock", method = RequestMethod.POST)
	public ModelAndView buyStock(HttpServletRequest request, @RequestParam(value = "buystockid") int stockId,
								 @RequestParam(value = "quantity") int quantity) {
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();

		boolean isBought = tradeBuy.buyStock(principal.getName(), stockId, quantity, stockDb, userDb, tradeDb);
		if (isBought) {
			List<Trade> orders = tradeFetch.fetchUserOrders(principal.getName(), tradeDb);
			model.addObject("orders", orders);
			model.setViewName("orders");
			return model;
		}

		List<Stock> stocks = stockFetch.fetchUserStocks(stockDb, userDb, principal.getName());
		List<Stock> top5GainersStocks = stockFetch.fetchTop5GainerStocks(stockDb, userDb, principal.getName());
		List<Stock> top5LosersStocks = stockFetch.fetchTop5LoserStocks(stockDb, userDb, principal.getName());

		model.addObject("stocks", stocks);
		model.addObject("gainers", top5GainersStocks);
		model.addObject("losers", top5LosersStocks);
		model.addObject("error", "Insufficient funds");
		model.setViewName("stocks");
		return model;
	}

	@RequestMapping(value = "/sellstock", method = RequestMethod.POST)
	public ModelAndView sellStock(HttpServletRequest request, @RequestParam(value = "sellstockid") int stockId,
								  @RequestParam(value = "quantity") int quantity, @RequestParam(value = "tradeBuyNumber") String tradeBuyNumber) {
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();

		boolean isSold = tradeSell.sellStock(principal.getName(), stockId, quantity, stockDb, userDb, tradeDb, tradeBuyNumber);
		if (isSold) {
			List<Trade> orders = tradeFetch.fetchUserOrders(principal.getName(), tradeDb);
			model.addObject("orders", orders);
			model.setViewName("orders");
			return model;
		}

		List<Stock> stocks = stockFetch.fetchUserStocks(stockDb, userDb, principal.getName());
		List<Stock> top5GainersStocks = stockFetch.fetchTop5GainerStocks(stockDb, userDb, principal.getName());
		List<Stock> top5LosersStocks = stockFetch.fetchTop5LoserStocks(stockDb, userDb, principal.getName());

		model.addObject("stocks", stocks);
		model.addObject("gainers", top5GainersStocks);
		model.addObject("losers", top5LosersStocks);
		model.addObject("error", "Insufficient funds");
		model.setViewName("stocks");
		return model;
	}

	@RequestMapping(value = "/setbuystock", method = RequestMethod.POST)
	public ModelAndView setBuyStock(HttpServletRequest request, @RequestParam(value = "setbuystockid") int stockId,
									@RequestParam(value = "setquantity") int quantity, @RequestParam(value = "setbuyprice") float buyPrice) {
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();

		boolean isBought = tradeBuy.setBuyPrice(principal.getName(), stockId, quantity, buyPrice, stockDb, userDb,
				tradeDb);
		if (isBought) {
			List<Trade> orders = tradeFetch.fetchUserOrders(principal.getName(), tradeDb);
			model.addObject("orders", orders);
			model.setViewName("orders");
			return model;
		}

		List<Stock> stocks = stockFetch.fetchUserStocks(stockDb, userDb, principal.getName());
		List<Stock> top5GainersStocks = stockFetch.fetchTop5GainerStocks(stockDb, userDb, principal.getName());
		List<Stock> top5LosersStocks = stockFetch.fetchTop5LoserStocks(stockDb, userDb, principal.getName());

		model.addObject("stocks", stocks);
		model.addObject("gainers", top5GainersStocks);
		model.addObject("losers", top5LosersStocks);
		model.addObject("error", "Insufficient funds");
		model.setViewName("stocks");
		return model;
	}

	@RequestMapping(value = "/setsellstock", method = RequestMethod.POST)
	public ModelAndView setBwllStock(HttpServletRequest request, @RequestParam(value = "setsellstockid") int stockId,
									@RequestParam(value = "setquantity") int quantity, @RequestParam(value = "setsellprice") float sellPrice, @RequestParam(value = "tradeBuyNumber") String tradeBuyNumber) {
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();

		boolean isSold = tradeSell.setSellPrice(principal.getName(), stockId, quantity, sellPrice, stockDb, userDb,
				tradeDb, tradeBuyNumber);
		if (isSold) {
			List<Trade> orders = tradeFetch.fetchUserOrders(principal.getName(), tradeDb);
			model.addObject("orders", orders);
			model.setViewName("orders");
			return model;
		}

		List<Stock> stocks = stockFetch.fetchUserStocks(stockDb, userDb, principal.getName());
		List<Stock> top5GainersStocks = stockFetch.fetchTop5GainerStocks(stockDb, userDb, principal.getName());
		List<Stock> top5LosersStocks = stockFetch.fetchTop5LoserStocks(stockDb, userDb, principal.getName());

		model.addObject("stocks", stocks);
		model.addObject("gainers", top5GainersStocks);
		model.addObject("losers", top5LosersStocks);
		model.addObject("error", "Insufficient funds");
		model.setViewName("stocks");
		return model;
	}

}
