package bg.sofia.uni.fmi.mjt.shopping.portal;

import java.time.LocalDate;

public class PriceStatistic {
    private LocalDate date;
    private double lowestPrice;
    private double totalPrice;
    private int numberOfPrices;

    public PriceStatistic(LocalDate date, double price) {
        this.date = date;
        lowestPrice = price;
        totalPrice = price;
        numberOfPrices = 1;
    }

    /**
     * Returns the date for which the statistic is collected.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the lowest total price from the offers for this product for the
     * specific date.
     */
    public double getLowestPrice() {
        return lowestPrice;
    }

    /**
     * Return the average total price from the offers for this product for the
     * specific date.
     */
    public double getAveragePrice() {
        return totalPrice / numberOfPrices;
    }

    public void addPrice(double price) {
        if (price < lowestPrice) {
            lowestPrice = price;
        }

        totalPrice += price;
        numberOfPrices++;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        PriceStatistic other = (PriceStatistic) obj;
        
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        
        return true;
    }

}
