
public class StockExchange {
	public static int maxProfit(int[] prices) {
		int profit = 0;
		boolean buy = true;

		for (int index = 0; index < prices.length - 1; index++) {
			if (buy && prices[index] < prices[index + 1]) {
				profit -= prices[index];
				buy = false;
			} else if (!buy && prices[index] > prices[index + 1]) {
				profit += prices[index];
				buy = true;
			}
		}
		
		if (!buy) {
			profit += prices[prices.length - 1];
		}

		return profit;
	}
}
