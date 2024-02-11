package edu.binghamton.cs.crabby;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static edu.binghamton.cs.crabby.GameView.screenRatioX;
import static edu.binghamton.cs.crabby.GameView.screenRatioY;

public class Bird {

    public int speed = 20;
    int x = 0, y, width, height, birdCounter = 1;
    Bitmap bird1, bird2;
    boolean isScored = false;

    private final Object lock = new Object();

    Bird (Resources res) {

        bird1 = BitmapFactory.decodeResource(res, R.drawable.eagle3);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.eagle3);

        width = bird1.getWidth();
        height = bird1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);

        y = -height;
    }

    Bitmap getBird () {
        synchronized (lock) {
            if (birdCounter == 1) {
                birdCounter++;
                return bird1;
            }

            if (birdCounter == 2) {
                birdCounter++;
                return bird2;
            }
            birdCounter = 1;
            return bird2;
        }
    }

    Circ getCollisionShape () {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int radius = Math.min(width, height) / 2;

        return new Circ(centerX, centerY, radius);
    }

}
