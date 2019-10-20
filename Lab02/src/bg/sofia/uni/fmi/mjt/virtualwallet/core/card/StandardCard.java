package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

public class StandardCard extends Card {

	public StandardCard(String name) {
		super(name, 0);
	}

	public StandardCard(String name, double amount) {
		super(name, amount);
	}

	@Override
	public boolean executePayment(double cost) {
		return super.withdraw(cost);
	}
}
