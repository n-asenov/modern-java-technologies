package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

public abstract class Card {
	private String name;
	private double amount;
	
	public Card(String name) {
		this(name, 0);
    }
	
	public Card(String name, double amount) {
		this.name = name;
		this.amount = amount;
	}
	
    public abstract boolean executePayment(double cost);

    public String getName() {
    	return name;
    }
	
    public double getAmount() {
    	return amount;
    }
    
    public boolean feed(double amount) {
    	if (amount < 0) {
    		return false;
    	}
    	
    	this.amount += amount;
    	return true;
    }

    public boolean withdraw(double amount) {
    	if (amount < 0 || this.amount - amount < 0) { 
    		return false;
    	}
    	
    	this.amount -= amount;
    	return true;
    }
}
