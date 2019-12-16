package bg.sofia.uni.fmi.mjt.christmas;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Workshop {
    private static final int TOTAL_ELVES = 20;
    
    private Queue<Gift> backlog;
    private AtomicInteger wishCount;
    private Elf[] elves;
    private AtomicInteger elfId;
    private boolean isChristmasTime;

    public Workshop() {
        backlog = new ArrayDeque<>();
        wishCount = new AtomicInteger(0);
        elves = new Elf[TOTAL_ELVES];
        elfId = new AtomicInteger(0);
        isChristmasTime = false;
        
        for (int index = 0; index < TOTAL_ELVES; index++) {
            elves[index] = new Elf(elfId.getAndIncrement(), this);
            new Thread(elves[index]).start();
        }
    }
    
    public boolean getIsChristmasTime() {
        return isChristmasTime;
    }
    
    /**
     * Set isChristmasTime flag to true.
     */
    public synchronized void setChristmasTime() {
        isChristmasTime = true;
        this.notifyAll();
    }

    /**
     * Adds a gift to the elves' backlog.
     **/
    public synchronized void postWish(Gift gift) {
        backlog.add(gift);
        wishCount.getAndIncrement();
        this.notifyAll();
    }

    /**
     * Returns an array of the elves working in Santa's workshop.
     **/
    public Elf[] getElves() {
        return elves;
    }

    /**
     * Returns the next gift from the elves' backlog that has to be manufactured.
     * @throws InterruptedException 
     **/
    public synchronized Gift nextGift() throws InterruptedException {
        while (backlog.isEmpty() && !isChristmasTime) {
            this.wait();
        }

        return backlog.poll();
    }
    
    public int getGiftsToMakeCount() {
        return backlog.size();
    }

    /**
     * Returns the total number of wishes sent to Santa's workshop by the kids.
     **/
    public int getWishCount() {
        return wishCount.get();
    }
}
