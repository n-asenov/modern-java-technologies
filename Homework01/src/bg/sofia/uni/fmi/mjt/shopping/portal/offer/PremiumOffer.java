package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.time.LocalDate;

public class PremiumOffer extends RegularOffer {
    private static final int DISCOUNT_OFFSET = 100;
    private double discount;

    public PremiumOffer(String productName, LocalDate date, String description, double price,
            double shippingPrice, double discount) {
        super(productName, date, description, price, shippingPrice);
        this.discount = round(discount);
    }

    public double getDiscount() {
        return discount;
    }

    @Override
    public double getTotalPrice() {
        double totalPrice = super.getTotalPrice();
        double discountPrice = totalPrice * (discount / DISCOUNT_OFFSET);
        
        return totalPrice - discountPrice;
    }
    
    private double round(double price) {
        price *= DISCOUNT_OFFSET;
        price = Math.round(price);
        price /= DISCOUNT_OFFSET;
        
        return price;
    }
}