package com.csci5308.stocki5.trade;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class TradeScheduler {
	
	@Autowired
	TradeEod tradeEod;
	
	@Autowired
	TradeDb tradeDb;
	
	@Scheduled(cron = "0 5 18 * * ?")
	public void scheduleFailedBuyOrder() {
		System.out.println("Fail orders called at " + new Date(System.currentTimeMillis()));
		tradeEod.markFailedBuyOrder(tradeDb);
	}

}
