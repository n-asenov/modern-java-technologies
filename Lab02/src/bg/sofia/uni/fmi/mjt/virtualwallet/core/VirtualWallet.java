package bg.sofia.uni.fmi.mjt.virtualwallet.core;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.virtualwallet.core.card.Card;
import bg.sofia.uni.fmi.mjt.virtualwallet.core.payment.PaymentInfo;
import bg.sofia.uni.fmi.mjt.virtualwallet.core.payment.Transaction;

public class VirtualWallet implements VirtualWalletAPI {
	private Card[] cards;
	private int numberOfCards;
	private Transaction[] lastTransactions;
	private int numberOfTransactions;

	public VirtualWallet() {
		cards = new Card[5];
		numberOfCards = 0;
		lastTransactions = new Transaction[10];
		numberOfTransactions = 0;
	}

	@Override
	public boolean registerCard(Card card) {
		if (card == null || numberOfCards == 5 || card.getName() == null) {
			return false;
		}

		for (int index = 0; index < numberOfCards; index++) {
			if (cards[index].getName().equals(card.getName())) {
				return false;
			}
		}

		cards[numberOfCards] = card;
		numberOfCards++;
		return true;
	}

	@Override
	public boolean executePayment(Card card, PaymentInfo paymentInfo) {
		if (paymentInfo == null) {
			return false;
		}

		for (int index = 0; index < numberOfCards; index++) {
			if (cards[index] == card && card.executePayment(paymentInfo.getCost())) {
				lastTransactions[numberOfTransactions] = new Transaction(card.getName(), LocalDateTime.now(), paymentInfo);
				numberOfTransactions = (numberOfTransactions + 1) % 10;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean feed(Card card, double amount) {
		for (int index = 0; index < numberOfCards; index++) {
			if (cards[index] == card) {
				return card.feed(amount);
			}
		}

		return false;
	}

	@Override
	public Card getCardByName(String name) {
		for (int index = 0; index < numberOfCards; index++) {
			if (cards[index].getName().equals(name)) {
				return cards[index];
			}
		}

		return null;
	}

	@Override
	public int getTotalNumberOfCards() {
		return numberOfCards;
	}
}
