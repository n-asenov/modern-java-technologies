package bg.sofia.uni.fmi.mjt.virtualwallet.core.payment;

import java.time.LocalDateTime;

public class Transaction {
	private String cardName;
	private LocalDateTime date;
	private PaymentInfo paymentInfo;
	
	public Transaction(String cardName, LocalDateTime date, PaymentInfo paymentInfo) {
		this.cardName = cardName;
		this.date = date;
		this.paymentInfo = paymentInfo;
	}

	public String getCardName() {
		return cardName;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}
}
