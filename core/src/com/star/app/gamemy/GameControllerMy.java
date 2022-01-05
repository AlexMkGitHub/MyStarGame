package com.star.app.gamemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.Bullet;
import com.star.app.game.BulletController;

public class GameControllerMy {

    private BackgroundMy backgroundMy;
    private BulletController bulletController;
    private HeroMy hero;
    private AsteroidControllerMy asteroidControllerMy;

    public AsteroidControllerMy getAsteroidController() {
        return asteroidControllerMy;
    }

    public BackgroundMy getBackground() {
        return backgroundMy;
    }

    public HeroMy getHero() {
        return hero;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public GameControllerMy() {
        this.backgroundMy = new BackgroundMy(this);
        this.hero = new HeroMy(this);
        this.bulletController = new BulletController();
        this.asteroidControllerMy = new AsteroidControllerMy(this);
    }

    public void update(float dt) {

        backgroundMy.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidControllerMy.update(dt);

    }

    /*--------------Уничтожене астероида и заряда выстрела при попадании-----------------*/
    public void checkCollisions(SpriteBatch batch) {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidControllerMy.getActiveList().size(); j++) {
                AsteroidMy a = asteroidControllerMy.getActiveList().get(j);
                if (a.getPosition().dst(b.getPosition()) < (a.getScale() * 128.0f) || hero.getPosition()
                        .dst(a.getPosition()) < (a.getScale() * 256.0f)) {
                    Vector2 ps = b.getPosition();
                    bulletController.boomBullet(ps, batch);
                    b.deactivate();
                    a.deactivate();
                } else {
                    if (a.getPosition().dst(hero.getPosition()) < (a.getScale()) * 512) {
                        a.deactivate();
                        System.out.println("Boom");
                    }
                }
            }
        }
    }
}



