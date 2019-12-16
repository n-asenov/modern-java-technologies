package bg.sofia.uni.fmi.mjt.christmas;

import java.util.Random;

public class Kid implements Runnable {
    private static final int MAX_THINKING_TIME = 5;
    
    private Workshop workshop;
    private int timeToChoose;
    private Gift wantedGift;
    
    public Kid(Workshop workshop) {
        this.workshop = workshop;
        timeToChoose = new Random().nextInt(MAX_THINKING_TIME);
        wantedGift = Gift.getGift();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeToChoose);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted!");
        }
        
        workshop.postWish(wantedGift);
    }
}
