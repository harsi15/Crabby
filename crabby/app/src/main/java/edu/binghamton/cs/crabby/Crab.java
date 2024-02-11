package edu.binghamton.cs.crabby;

import static edu.binghamton.cs.crabby.GameView.screenRatioX;
import static edu.binghamton.cs.crabby.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Crab {

    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0;
    Bitmap crab1, crab2, dead;
    private GameView gameView;

    Crab(GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        crab1 = BitmapFactory.decodeResource(res, R.drawable.crab);
        crab2 = BitmapFactory.decodeResource(res, R.drawable.crab);

        width = crab1.getWidth();
        height = crab1.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        crab1 = Bitmap.createScaledBitmap(crab1, width, height, false);
        crab2 = Bitmap.createScaledBitmap(crab2, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.crab);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        y = screenY / 2;
        x= 960;

    }

    Bitmap getCrab () {

        if (wingCounter == 0) {
            wingCounter++;
            return crab1;
        }
        wingCounter--;

        return crab2;
    }

    public void setGoingUp(boolean goingUp) {
        isGoingUp = goingUp;
    }

    Circ getCollisionShape () {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int radius = Math.min(width, height) / 2;

        return new Circ(centerX, centerY, radius);
    }

    Bitmap getDead () {
        return dead;
    }

}
