package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

public class GoldenCard extends Card {

	public GoldenCard(String name) {
		super(name, 0);
	}

	public GoldenCard(String name, double amount) {
		super(name, amount);
	}

	@Override
	public boolean executePayment(double cost) {
		return super.withdraw(0.85 * cost);
	}
}
