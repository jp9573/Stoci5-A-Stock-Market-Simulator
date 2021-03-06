package com.csci5308.stocki5.stock;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockPriceAlgorithm {

	@Autowired
	StockDbInterface stockDbInterface;

	public void generateStockPrice() {
		float newPrice = 0.00f;
		List<Stock> stocks = stockDbInterface.getStocks();
		for (Stock stock : stocks) {
			newPrice = stockPriceAlgorithm(stock.getPrice());
			stock.setPrice(newPrice);
			stock.calculateHighAndLow(newPrice);
		}
		stockDbInterface.updateStockBulk(stocks);
	}

	public float stockPriceAlgorithm(float currentPrice) {
		DecimalFormat df = new DecimalFormat("##.00");
		Random random = new Random();
		float maxPriceRange = currentPrice + 10;
		float minPriceRange = currentPrice - 10;
		if (minPriceRange < 0.0f) {
			minPriceRange = 0.00f;
		}
		if (maxPriceRange < 0.0f) {
			maxPriceRange = 1.00f;
		}
		float newPrice = minPriceRange + random.nextFloat() * (maxPriceRange - minPriceRange);
		float formatedNewPrice = Float.parseFloat(df.format(newPrice));
		return formatedNewPrice;
	}

}
