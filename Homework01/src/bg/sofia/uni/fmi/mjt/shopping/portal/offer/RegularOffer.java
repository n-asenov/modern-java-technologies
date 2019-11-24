package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.time.LocalDate;

public class RegularOffer implements Offer {
    private String productName;
    private LocalDate date;
    private String description;
    private double price;
    private double shippingPrice;

    public RegularOffer(String productName, LocalDate date, String description, double price,
            double shippingPrice) {
        this.productName = productName.toLowerCase();
        this.date = date;
        this.description = description;
        this.price = price;
        this.shippingPrice = shippingPrice;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getShippingPrice() {
        return shippingPrice;
    }

    @Override
    public double getTotalPrice() {
        return price + shippingPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        RegularOffer other = (RegularOffer) obj;

        if (productName == null) {
            if (other.productName != null) {
                return false;
            }
        } else if (!productName.equals(other.productName)) {
            return false;
        }

        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }

        if (Double.compare(getTotalPrice(), other.getTotalPrice()) != 0) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((productName == null) ? 0 : productName.hashCode());
        temp = Double.doubleToLongBits(shippingPrice);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
