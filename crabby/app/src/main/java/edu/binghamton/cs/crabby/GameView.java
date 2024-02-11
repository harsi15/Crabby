package edu.binghamton.cs.crabby;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Bird[] birds;
    private SharedPreferences prefs;
    private Random random;
    private Crab crab;
    private GameActivity activity;
    private Background background1, background2;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        crab = new Crab(this, screenY, getResources());

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        birds = new Bird[2];

        for (int i = 0;i < 2;i++) {
            Bird bird = new Bird(getResources());
            birds[i] = bird;
        }
        random = new Random();

    }

    @Override
    public void run() {

        while (isPlaying) {
            update ();
            draw ();
            sleep ();
        }

    }

    private void update () {

        if (crab.isGoingUp)
            crab.y -= 30 * screenRatioY;
        else
            crab.y += 30 * screenRatioY;

        if (crab.y < 0)
            crab.y = 0;

        if (crab.y >= screenY - crab.height)
            crab.y = screenY - crab.height;

        for (Bird bird : birds) {
            bird.x -= bird.speed;

            if (!bird.isScored && bird.x < crab.x) {
                bird.isScored = true;
                score++; // Increment score only when a bird is successfully passed by the crab
            }

            if (bird.x + bird.width < 0) {
                int bound = (int) (20 * screenRatioX);
                bird.speed = random.nextInt(bound);

                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX);

                bird.x = screenX;
                bird.y = random.nextInt(screenY - bird.height);
                bird.isScored = false; // Reset the scored flag for the new bird position
            }

            if (Circ.intersects(bird.getCollisionShape(), crab.getCollisionShape())) {
                isGameOver = true;
                return;
            }
        }

    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(crab.getDead(), crab.x, crab.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                return;
            }
            canvas.drawBitmap(crab.getCrab(), crab.x, crab.y, paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                crab.setGoingUp(true);
                break;
            case MotionEvent.ACTION_UP:
                crab.setGoingUp(false);
                break;
        }
        return true;
    }

}