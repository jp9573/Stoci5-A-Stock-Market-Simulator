package com.csci5308.stocki5.trade.order;

import com.csci5308.stocki5.trade.Trade;
import com.csci5308.stocki5.trade.db.ITradeDb;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeOrder implements ITradeOrder
{

	public List<Trade> fetchUserOrders(String userCode, ITradeDb tradeDbInterface)
	{
		return tradeDbInterface.getTodaysTradeByUserCode(userCode);
	}

}
