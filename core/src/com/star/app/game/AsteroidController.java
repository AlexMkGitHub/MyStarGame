package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.ScreenManager;

public class AsteroidController extends ObjectPool<Asteroid> {
    private Vector2 position;
    private Vector2 velocity;
    private Texture asteroidTexture;
    private boolean active;
    private float angle;
    private float scale;
    private int scaleRnd;
    private int asteroidRun;
    private int rotation;
    private float deltaTime;
    private GameController gc;

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public AsteroidController() {
        this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200), MathUtils.random(-200, ScreenManager.SCREEN_HIEGHT + 200));
//        //this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HIEGHT / 2);
        this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
//        this.angle = 0.0f;
        //setup(position.x, position.y, velocity.x, velocity.y);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = activeList.get(i);
            asteroidTexture = a.getAsteroidTexture();
            position = a.getPosition();
            velocity = a.getVelocity();
            angle = a.getAngle();
            scale = a.getScale();
            batch.draw(asteroidTexture, position.x, position.y, 128 * scale, 128 * scale, 256 * scale, 256 * scale, scale,
                    scale, angle, 0, 0, 256, 256, true, true);
        }
    }

    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }

//    public void update(float dt) {
//        int rnd = MathUtils.random(1, 3);
//        for (int i = 0; i < rnd; i++) {
//            if (activeList.isEmpty()){
//                this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200), MathUtils.random(-200, ScreenManager.SCREEN_HIEGHT + 200));
//                this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
//                setup(position.x, position.y, velocity.x, velocity.y);
//            }
//        }
////        this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200), MathUtils.random(-200, ScreenManager.SCREEN_HIEGHT + 200));
////        //this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HIEGHT / 2);
////        this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
//        for (int i = 0; i < activeList.size(); i++) {
//            activeList.get(i).update(dt);
//        }
//        checkPool();
//    }

    public void update(float dt) {
//        int rnd = MathUtils.random(1, 3);
//            if (activeList.size()<rnd) {
//                //this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200), MathUtils.random(-200, ScreenManager.SCREEN_HIEGHT + 200));
//                //this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
//                this.position = new Vector2(MathUtils.random(-256, ScreenManager.SCREEN_WIDTH + 256), MathUtils.random(-256,
//                        ScreenManager.SCREEN_HIEGHT + 256));
//                this.velocity = new Vector2(MathUtils.random(-50, 10), MathUtils.random(-20, 10));
//                int rndPosition = MathUtils.random(1, 4);
//                switch (rndPosition) {
//                    case 1:
//                        position.x = -256;
//                        break;
//                    case 2:
//                        position.x = ScreenManager.SCREEN_WIDTH + 256;
//                        break;
//                    case 3:
//                        position.y = -256;
//                        break;
//                    case 4:
//                        position.y = ScreenManager.SCREEN_HIEGHT + 256;
//                        break;
//                }
//
//                setup(position.x, position.y, velocity.x, velocity.y);
//            }

        this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200), MathUtils.random(-200, ScreenManager.SCREEN_HIEGHT + 200));
        //this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HIEGHT / 2);
        this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
        if(activeList.isEmpty()){
            setup(position.x, position.y, velocity.x, velocity.y);
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }


}
