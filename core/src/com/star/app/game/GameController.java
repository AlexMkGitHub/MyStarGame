package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameController {

    private Background background;
    private BulletController bulletController;
    private Hero hero;
    private AsteroidController asteroidController;

    public Hero getHero() {
        return hero;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public Background getBackground() {
        return background;
    }

    public BulletController getBulletController() {
        return bulletController;
    }


    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
        this.asteroidController = new AsteroidController();
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        //checkCollisions();
        asteroidController.update(dt);
    }


//    private void checkCollisions() {
//        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
//            Bullet b = bulletController.getActiveList().get(i);
//            if (asteroidController.getPosition().dst(b.getPosition())<256.0f){
//                b.deactivate();
//            }
//
//        }
//    }

//    public void checkCollisions(SpriteBatch batch) {
//        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
//            Bullet b = bulletController.getActiveList().get(i);
//            if (asteroidController.getPosition().dst(b.getPosition())<256.0f){
//                Vector2 ps = asteroidController.getPosition();
//                bulletController.boomBullet(ps, batch);
//                b.deactivate();
//            }
//
//        }
//    }
}
