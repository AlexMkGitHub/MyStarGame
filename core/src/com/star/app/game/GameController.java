package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    //private PowerAddController powerAddController;
    private PowerUpsController powerUpsController;
    private Hero hero;
    private Vector2 tempVec;
    private boolean crashHero;
    private float level;
    private float asteroidScale;

    public ParticleController getParticleController() {
        return particleController;
    }

//    public PowerAddController getPowerAddController() {
//        return powerAddController;
//    }


    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public GameController() {
//        this.powerAddController = new PowerAddController(this);
        this.level = 1.0f;
        this.crashHero = false;
        this.powerUpsController = new PowerUpsController(this);
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.tempVec = new Vector2();
        this.asteroidScale = 1.0f;
        this.particleController = new ParticleController();
        for (int i = 0; i < 3; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200),
                    MathUtils.random(-200, 200), asteroidScale * level);
        }
    }

    public void update(float dt) {
        hero.gamePause();
        if (!hero.isPause()) {
            background.update(dt);
            asteroidController.update(dt);
            hero.update(dt);
            bulletController.update(dt);
            particleController.update(dt);
            //     powerAddController.update(dt);
            powerUpsController.update(dt);
            checkCollisions();
//        addHeroGifts();
            addAsteroids();
        }
    }


    private void checkCollisions() {
        if (crashHero) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME_OVER);
        }

        for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
            Asteroid a = asteroidController.getActiveList().get(j);
            if (a.getHitArea().overlaps(hero.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());

                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2;
                tempVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tempVec, halfOverLen);
                a.getPosition().mulAdd(tempVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius + a.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVec, a.getHitArea().radius / sumScl * 100);
                a.getVelocity().mulAdd(tempVec, -hero.getHitArea().radius / sumScl * 100);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 50);
                }
                hero.takeDamage(2 * level);
                if (hero.getHp() <= 0) {
                    hero.setTexture(Assets.getInstance().getAtlas().findRegion("mini"));
                    getParticleController().getEffectBuilder().shipDestroy(hero.getPosition().x, hero.getPosition().y);
                    tempVec.set(-1256.0f, -1256.0f);
                    hero.getPosition().mulAdd(tempVec, 0);

                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            crashHero = true;
                        }
                    };
                    Timer timer = new Timer();
                    long delay = 2000;
                    timer.schedule(task, delay);
                }
            }
        }

        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.1f + MathUtils.random(-20, 20), b.getVelocity().y * -0.1f + MathUtils.random(-20, 20),
                            0.1f, 2.2f, 4.2f,
                            1.0f, 1.0f, 1.0f, 1,
                            0.0f, 0.0f, 1.0f, 0.5f);

                    b.deactivate();
                    if (a.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() * 0.25f);
                        }
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(p.getPosition().x, p.getPosition().y);
                p.deactivate();
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (p.getHitArea().overlaps(hero.getMagneticHitArea())) {
                float dst = p.getPosition().dst(hero.getPosition());
                float halfOverLen = (p.getHitArea().radius + hero.getMagneticHitArea().radius - dst) / 2;
                tempVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getPosition().mulAdd(tempVec, halfOverLen);
            }
        }
    }

    private void addAsteroids() {
        if (asteroidController.getActiveList().isEmpty()) {
            level++;
            for (int i = 0; i < 2; i++) {
                asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                        MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                        MathUtils.random(-200, 200),
                        MathUtils.random(-200, 200), asteroidScale * level);
            }
        }
    }

//    private void addHeroGifts() {
//        int count;
//        for (int j = 0; j < powerAddController.getActiveList().size(); j++) {
//            PowerAdd pa = powerAddController.getActiveList().get(j);
//            int rnd = pa.getRndPower();
//            if (pa.getHitArea().overlaps(hero.getHitArea())) {
//                switch (rnd) {
//                    case 1:
//                        int ammoAddCount = powerAddController.getActiveList().get(j).getCount();
//                        count = hero.getCurrentWeapon().getCurBullets() + ammoAddCount;
//                        if (count > hero.getCurrentWeapon().getMaxBullets()) {
//                            hero.getCurrentWeapon().setCurBullets(hero.getCurrentWeapon().getMaxBullets());
//                        } else {
//                            hero.getCurrentWeapon().setCurBullets(count);
//                        }
//                        particleController.setup(pa.getPosition().x, pa.getPosition().y,
//                                pa.getVelocity().x, pa.getVelocity().y,
//                                0.1f, 3.2f, 7.2f,
//                                1.0f, 1.0f, 1.0f, 1,
//                                1.0f, 1.0f, 0.0f, 0.5f);
//                        pa.deactivate();
//                        powerAddController.getActiveList().remove(j);
//                        break;
//
//                    case 2:
//                        int aidKit = powerAddController.getActiveList().get(j).getCount();
//                        count = hero.getHp() + aidKit;
//                        if (count > hero.getHpMax()) {
//                            hero.setHp(getHero().getHpMax());
//                        } else {
//                            hero.setHp(count);
//                        }
//                        particleController.setup(pa.getPosition().x, pa.getPosition().y,
//                                pa.getVelocity().x, pa.getVelocity().y,
//                                0.1f, 3.2f, 7.2f,
//                                1.0f, 1.0f, 1.0f, 1,
//                                1.0f, 1.0f, 0.0f, 0.5f);
//                        pa.deactivate();
//                        powerAddController.getActiveList().remove(j);
//                        break;
//
//                    case 3:
//                        int moneyCount = powerAddController.getActiveList().get(j).getCount();
//                        count = hero.getMoney() + moneyCount;
//                        hero.setMoney(count);
//                        particleController.setup(pa.getPosition().x, pa.getPosition().y,
//                                pa.getVelocity().x, pa.getVelocity().y,
//                                0.1f, 3.2f, 7.2f,
//                                1.0f, 1.0f, 1.0f, 1,
//                                1.0f, 1.0f, 0.0f, 0.5f);
//                        pa.deactivate();
//                        powerAddController.getActiveList().remove(j);
//                        break;
//                }
//            }
//        }
//    }
}

