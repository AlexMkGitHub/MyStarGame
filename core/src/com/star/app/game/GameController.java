package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private BotController botController;
    private Hero hero;
    private Bot bot;
    private Vector2 tempVec;
    private Stage stage;
    private int level;
    private boolean pause;
    private float timer;
    private float timerAsteroidsAdds;
    private Music music;
    private StringBuilder sb;
    private float delta;

    /*-----------Моя реализация проверки жив герой или нет-----------*/
    private boolean crashHero;

    /*-----------Моя реализация появление астероидов-----------*/
//    private float sizeAsteroid;
    /*---------------------------------------------------------*/

    /*---------------Моя реализация уровня игры-------------*/
    //private int gameLevel;
    /*------------------------------------------------------*/

    /*-----------Моя реализация выпадания улучшалок-----------*/
//    private PowerAddController powerAddController;
//
//    public PowerAddController getPowerAddController() {
//        return powerAddController;
//    }
    /*--------------------------------------------------------*/

    /*---------------Моя реализация уровня игры-------------*/
//    public int getGameLevel() {
//        return gameLevel;
//    }
    /*-----------------------------------------------------*/


    public float getTimer() {
        return timer;
    }

    public float getTimerAsteroidsAdds() {
        return timerAsteroidsAdds;
    }

    public int getLevel() {
        return level;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

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

    public BotController getBotController() {
        return botController;
    }

    public Stage getStage() {
        return stage;
    }

    public GameController(SpriteBatch batch) {

        /*-----------Моя реализация выпадания улучшалок-----------*/
//        this.powerAddController = new PowerAddController(this);

        /*-----------Моя реализация проверки жив герой или нет-----------*/
        this.crashHero = false;

        this.powerUpsController = new PowerUpsController(this);
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.botController = new BotController(this, MathUtils.random(20, 50), MathUtils.random(100, 300), MathUtils.random(0, 4));
        this.tempVec = new Vector2();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.infoController = new InfoController();
        this.sb = new StringBuilder();
        this.level = 0;
        this.delta = 1.0f;
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.setVolume(0.1f);
        this.music.play();
        this.timerAsteroidsAdds = 0.0f;
        stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);


        /*-----------Моя реализация появление астероидов-----------*/
//        this.sizeAsteroid = 0;
        /*---------------------------------------------------------*/

    }

    public void update(float dt) {

        /*-----------Моя реализация паузы в игре-----------*/
//        hero.gamePause();
//        if (!hero.isPause()) {
        /*------------------------------------------------*/

        if (pause) {
            return;
        }
        delta = dt;
        timer += dt;
        background.update(dt);
        asteroidController.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        infoController.update(dt);
        botController.update(dt);
        /*-----------Моя реализация выпадания улучшалок-----------*/
        //     powerAddController.update(dt);
        /*--------------------------------------------------------*/

        powerUpsController.update(dt);
        checkCollisions();

        /*----Проверка жив герой или нет + я добавил эффект уничтожения и таймер задержки до появления экрана GameOver--*/
        if (!hero.isAlive()) {
            music.stop();
            if (crashHero) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
            }
            hero.setTexture(Assets.getInstance().getAtlas().findRegion("mini"));
            getParticleController().getEffectBuilder().buildMonsterSplash(hero.getPosition().x, hero.getPosition().y);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    crashHero = true;
                }
            };
            Timer timer = new Timer();
            long delay = 3000;
            timer.schedule(task, delay);
        }
        /*-----------------------------------------------------------------------------------------------------*/

        /*-----------Моя реализация выпадания улучшалок-----------*/
//              addHeroGifts();
        /*--------------------------------------------------------*/


        /*-------------------Моя реализация: астероиды появляются через 3 сек.----------------*/
        if (asteroidController.getActiveList().size() == 0) {
            timerAsteroidsAdds += dt;
            if (timerAsteroidsAdds > 4.0f) {
                level++;
                timer = 0.0f;
                timerAsteroidsAdds = 0.0f;
                generateBigAsteroids(level <= 3 ? level : 3);
                botController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                        MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                        MathUtils.random(-200, 200),
                        MathUtils.random(-200, 200), MathUtils.random(0, 4));
                botController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                        MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                        MathUtils.random(-200, 200),
                        MathUtils.random(-200, 200), MathUtils.random(0, 4));

            }
        }
        /*--------------------------------------------------------------------------------------*/

        stage.act(dt);

        /*-----------Моя реализация появление астероидов-----------*/
//               addAsteroids(level);
        /*---------------------------------------------------------*/

    }

    private void checkCollisions() {

        /*-----------Моя реализация проверки жив герой или нет-----------*/
//        if (crashHero) {
//            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME_OVER_MY);
//        }
        /*----------------------------------------------------------------*/


        /*----------------Проверка столкновения героя и астероида, отталкивание друг от друга если столкнулись-------*/
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
                hero.takeDamage(level * 2);
                sb.setLength(0);
                sb.append("HP -  ").append(level * 2);
                infoController.setup(hero.getPosition().x, hero.getPosition().y, sb.toString(), Color.RED);

            }

            /*---------------Моя реализация увеличения урона при увеличении уровня игры-------------*/
//                                hero.takeDamage(2 * gameLevel);
            /*--------------------------------------------------------------------------------------*/


            /*-----------Моя реализация проверки жив герой или нет-----------*/
//                if (hero.getHp() <= 0) {
//                    hero.setTexture(Assets.getInstance().getAtlas().findRegion("mini"));
//                    getParticleController().getEffectBuilder().shipDestroy(hero.getPosition().x, hero.getPosition().y);
//                    tempVec.set(-1256.0f, -1256.0f);
//                    hero.getPosition().mulAdd(tempVec, 0);
//
//                    TimerTask task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            crashHero = true;
//                        }
//                    };
//                    Timer timer = new Timer();
//                    long delay = 2000;
//                    timer.schedule(task, delay);
//                }
            /*--------------------------------------------------------------------*/

        }

        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            if (bot.getRadiusDetected().contains(hero.getHitArea())) {
                tempVec.set(hero.getPosition()).sub(bot.getPosition()).nor();
                Vector2 botPos = bot.getPosition().cpy();
                tempVec.set(hero.getPosition()).sub(bot.getPosition());
                float af = -botPos.angleDeg(tempVec)+45;
                bot.angle = af;
                bot.tryToFire();
            } else {
                bot.angle = bot.botOldAngel;
            }
        }

        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            bot.botOldAngel = bot.angle;
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (bot.radiusDetected.overlaps(a.getHitArea())) {
                    float dst = bot.getPosition().dst(a.getPosition());
                    float halfOverLen = (bot.radiusDetected.radius + a.getHitArea().radius * 3 - dst) / 150;
                    tempVec.set(a.getPosition()).sub(bot.getPosition()).nor();
                    bot.getPosition().mulAdd(tempVec, -halfOverLen);
                    bot.angle = bot.getVelocity().angleDeg(bot.getPosition());
                } else {
                    //bot.angle = bot.botOldAngel - 90.0f;
                }
            }
        }
        /*-----------------------------------------------------------------------------------------------------------*/

        /*---------------------Проверка попадания пуль в астероид и их уничтожение--------------------------*/
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
        /*-----------------------------------------------------------------------------------------------------*/

        /*----------------Проверка и активация улучшалок при совпадении точек пересечения героя и улучшалок-----------*/
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);

            /*--------------------Магнит--------------------------------*/
            if (hero.getMagneticField().contains(p.getPosition())) {
                tempVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tempVec, 100);
            }
            /*-----------------------------------------------------------*/

            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
            }
        }

        botAndAsteroid();
        /*-----------------------------------------------------------------------------------------------------*/

        /*------------------Моя реализация магнита в игре----------------------*/
//        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
//            PowerUp p = powerUpsController.getActiveList().get(i);
//            if (p.getHitArea().overlaps(hero.getMagneticArea())) {
//                float dst = p.getPosition().dst(hero.getPosition());
//                float halfOverLen = (p.getHitArea().radius + hero.getMagneticArea().radius - dst) / 100;
//                tempVec.set(hero.getPosition()).sub(p.getPosition()).nor();
//                p.getPosition().mulAdd(tempVec, halfOverLen);
//            }
//        }
        /*----------------------------------------------------------------------*/

    }

    private void botAndAsteroid() {
        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (bot.getHitArea().overlaps(a.getHitArea())) {
                    bot.tryToFire();
                    float dst = bot.getPosition().dst(a.getPosition());

                    float halfOverLen = (bot.getHitArea().radius + a.getHitArea().radius - dst);
                    tempVec.set(a.getPosition()).sub(bot.getPosition()).nor();
                    a.getPosition().mulAdd(tempVec, halfOverLen);
                    bot.getPosition().mulAdd(tempVec, -halfOverLen);

                    float sumScl = a.getHitArea().radius + bot.getHitArea().radius;
                    a.getVelocity().mulAdd(tempVec, bot.getHitArea().radius / sumScl * 100);
                    bot.getVelocity().mulAdd(tempVec, -a.getHitArea().radius / sumScl * 100);
                    bot.takeDamage(level * 2);
                    a.takeDamage(level * 2);
                    if (bot.hp <= 0) {
                        //bot.setTexture(Assets.getInstance().getAtlas().findRegion("mini"));
                        getParticleController().getEffectBuilder().buildMonsterSplash(bot.getPosition().x, bot.getPosition().y);
                        bot.deactivate();
                    }
                    if (a.getHp() <= 0) {
                        getParticleController().getEffectBuilder().buildMonsterSplash(a.getPosition().x, a.getPosition().y);
                        a.deactivate();
                    }
                }
            }
        }
    }

    /*---------------------------------Генератор астероидов-------------------------*/
    public void generateBigAsteroids(int n) {
        for (int i = 0; i < n; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200),
                    MathUtils.random(-200, 200), 1.0f);
        }
    }
    /*----------------------------------------------------------------------*/

    /*-----------Моя реализация добавления астероидов в игре-----------*/
//    private void addAsteroids(int astCount) {
//        if (asteroidController.getActiveList().isEmpty()) {
//            sizeAsteroid += 0.25f;
//            if (sizeAsteroid > 1.5f) {
//                sizeAsteroid = 1.5f;
//            }
//            if (astCount > 3) {
//                astCount = 3;
//            }
//            for (int i = 0; i < astCount; i++) {
//                asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
//                        MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
//                        MathUtils.random(-200, 200),
//                        MathUtils.random(-200, 200), MathUtils.random(0.25f, sizeAsteroid));
//            }

    /*---------------Моя реализация уровня игры-------------*/
//            gameLevel += 1;
    /*------------------------------------------------------*/
//
//        }
//    }
    /*---------------------------------------------------------------------*/

    public void dispose() {
        background.dispose();
    }

    /*-----------Моя реализация выпадания улучшалок-----------*/
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
    /*----------------------------------------------------------------------------------*/
}

