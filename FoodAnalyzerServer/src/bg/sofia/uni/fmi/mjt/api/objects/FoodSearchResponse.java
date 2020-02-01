package bg.sofia.uni.fmi.mjt.api.objects;

import java.util.List;

public class FoodSearchResponse {
    private long totalHits;
    private int currentPage;
    private int totalPages;
    private List<Food> foods;
    
    public FoodSearchResponse(long totalHits, int currentPage, int totalPages, List<Food> foods) {
        this.totalHits = totalHits;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.foods = foods;
    }

    public long getTotalHits() {
        return totalHits;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
    
    public List<Food> getFoods() {
        return foods;
    }
}
