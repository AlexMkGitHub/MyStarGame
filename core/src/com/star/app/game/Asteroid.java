package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

import java.util.Timer;
import java.util.TimerTask;

public class Asteroid implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private Texture asteroidTexture;
    private float angle;
    private float scale;
    private int scaleRnd;
    private int asteroidRun;
    private int rotation;
    private float deltaTime;
    private AsteroidController asteroidController;

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getAsteroidTexture() {
        return asteroidTexture;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public float getScale() {
        return scale;
    }

    public int getScaleRnd() {
        return scaleRnd;
    }

    public Asteroid() {
        this.asteroidTexture = new Texture("asteroid.png");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.scaleRnd = MathUtils.random(1, 3);
        this.asteroidRun = 1;
        this.rotation = MathUtils.random(-2, 2);
        //this.asteroidController.setup(position.x, position.y, velocity.x, velocity.y);
        this.active = false;
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        if (position.x > ScreenManager.SCREEN_WIDTH + 260 || position.y > ScreenManager.SCREEN_HIEGHT + 260 ||
                position.x < -260 || position.y < -260) {
            deactivate();
            asteroidRun = 0;
            position = new Vector2(MathUtils.random(-256, ScreenManager.SCREEN_WIDTH + 256), MathUtils.random(-256,
                    ScreenManager.SCREEN_HIEGHT + 256));
            velocity = new Vector2(MathUtils.random(-50, 10), MathUtils.random(-20, 10));
            int rnd = MathUtils.random(1, 4);
            switch (rnd) {
                case 1:
                    position.x = -256;
                    break;
                case 2:
                    position.x = ScreenManager.SCREEN_WIDTH + 256;
                    break;
                case 3:
                    position.y = -256;
                    break;
                case 4:
                    position.y = ScreenManager.SCREEN_HIEGHT + 256;
                    break;
            }
        }

//        if (asteroidRun == 0) {
////            TimerTask task = new TimerTask() {
////                @Override
////                public void run() {
////                    asteroidRun = 1;
////                }
////            };
////            Timer timer = new Timer();
////            long delay = MathUtils.random(5000, 15000);
////            timer.schedule(task, delay);
////        } else {

            position.x += velocity.x * dt;
            position.y += velocity.y * dt;

            /*----------------Произвольное вращение астероида-----------------------*/
            switch (rotation) {
                case -2:
                    angle -= 1.5f;
                    break;
                case -1:
                    angle -= 0.7f;
                    break;
                case 1:
                    angle += 0.7f;
                    break;
                case 2:
                    angle += 1.5f;
                    break;
            }
            /*---------------Произвольный размер астероида----------------*/
            switch (scaleRnd) {
                case 1:
                    scale = 0.5f;
                    break;
                case 2:
                    scale = 0.75f;
                    break;
                case 3:
                    scale = 1.0f;
                    break;
            }
        }



    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }
}
