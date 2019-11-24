package bg.sofia.uni.fmi.mjt.shopping.portal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.NoOfferFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.OfferAlreadySubmittedException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.ProductNotFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.Offer;

public class ShoppingDirectoryImpl implements ShoppingDirectory {
    private HashMap<String, Set<Offer>> shoppingDirectory;

    public ShoppingDirectoryImpl() {
        shoppingDirectory = new HashMap<>();
    }

    @Override
    public Collection<Offer> findAllOffers(String productName) throws ProductNotFoundException {
        productName = validateProductName(productName);

        List<Offer> offers = getOffersInTheLast30Days(productName);

        Collections.sort(offers, getComparatorByTotalPrice());

        return offers;
    }

    @Override
    public Offer findBestOffer(String productName)
            throws ProductNotFoundException, NoOfferFoundException {
        productName = validateProductName(productName);
        
        List<Offer> allOffers = getOffersInTheLast30Days(productName);
        
        if (allOffers.isEmpty()) {
            throw new NoOfferFoundException();
        }
        
        Iterator<Offer> iterator = allOffers.iterator();
        Offer bestOffer = iterator.next();
        
        while (iterator.hasNext()) {
            Offer currentOffer = iterator.next();
            if (currentOffer.getTotalPrice() < bestOffer.getTotalPrice()) {
                bestOffer = currentOffer;
            }
        }

        return bestOffer;
    }

    @Override
    public Collection<PriceStatistic> collectProductStatistics(String productName)
            throws ProductNotFoundException {
        productName = validateProductName(productName);

        Map<LocalDate, PriceStatistic> result = new TreeMap<>(Comparator.reverseOrder());

        for (Offer offer : shoppingDirectory.get(productName)) {
            LocalDate offerDate = offer.getDate();

            if (result.containsKey(offerDate)) {
                result.get(offerDate).addPrice(offer.getTotalPrice());
            } else {
                result.put(offerDate, new PriceStatistic(offerDate, offer.getTotalPrice()));
            }
        }

        return result.values();
    }

    @Override
    public void submitOffer(Offer offer) throws OfferAlreadySubmittedException {
        if (offer == null) {
            throw new IllegalArgumentException();
        }

        String productName = offer.getProductName();

        if (!shoppingDirectory.containsKey(productName)) {
            shoppingDirectory.put(productName, new HashSet<>());
        }

        if (!shoppingDirectory.get(productName).add(offer)) {
            throw new OfferAlreadySubmittedException();
        }
    }

    private String validateProductName(String productName) throws ProductNotFoundException {
        if (productName == null) {
            throw new IllegalArgumentException();
        }

        productName = productName.toLowerCase();

        if (!shoppingDirectory.containsKey(productName)) {
            throw new ProductNotFoundException();
        }

        return productName;
    }

    private Comparator<Offer> getComparatorByTotalPrice() {
        return new Comparator<Offer>() {
            @Override
            public int compare(Offer o1, Offer o2) {
                return Double.compare(o1.getTotalPrice(), o2.getTotalPrice());
            }
        };
    }
    
    private List<Offer> getOffersInTheLast30Days(String productName) {
        final long daysToGoBack = 30;
        LocalDate finalDate = LocalDate.now();
        LocalDate startDate = finalDate.minusDays(daysToGoBack);
        List<Offer> allOffers = new ArrayList<>();
        
        for (Offer offer : shoppingDirectory.get(productName)) {
            LocalDate offerDate = offer.getDate();

            if ((offerDate.isAfter(startDate) && offerDate.isBefore(finalDate))
                    || offerDate.isEqual(finalDate)) {
                allOffers.add(offer);
            }
        }
        
        return allOffers;
    }

}
