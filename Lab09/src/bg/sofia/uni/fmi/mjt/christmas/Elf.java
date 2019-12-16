package bg.sofia.uni.fmi.mjt.christmas;

public class Elf implements Runnable {
    private int id;
    private Workshop workshop;
    private int totalGiftsCrafted;

    public Elf(int id, Workshop workshop) {
        this.id = id;
        this.workshop = workshop;
        totalGiftsCrafted = 0;
    }

    /**
     * Gets a wish from the backlog and creates the wanted gift.
     **/
    public void craftGift() throws InterruptedException {
        Gift giftToCraft = workshop.nextGift();
        
        if (giftToCraft != null) {
            Thread.sleep(giftToCraft.getCraftTime());
            totalGiftsCrafted++;
        }
    }

    /**
     * Returns the total number of gifts that the given elf has crafted.
     **/
    public int getTotalGiftsCrafted() {
        return totalGiftsCrafted;
    }

    @Override
    public void run() {
        while (!workshop.getIsChristmasTime() || workshop.getGiftsToMakeCount() != 0) {
            try {
                craftGift();
            } catch (InterruptedException e) {
                System.out.println("Elf " + id + " was interrupted!");
            }
        }

        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Elf-" + id + " crafted " + totalGiftsCrafted + " gifts.";
    }
}
