package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GameController {
    private int heroLife;
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    //private BotControllerMy botControllerMy;
    private BotController botController;
    private Hero hero;
    private BotMy botMy;
    private Vector2 tempVec;
    private Stage stage;
    private int level;
    private boolean pause;
    private float timer;
    private float timerAsteroidsAdds;
    private float timerBots;
    private float addBotsTimer;
    private boolean addBots;
    private float rndTime;
    private Music music;
    private Music hahaha;
    private StringBuilder sb;
    private float delta;
    private float dtTime;
    private float gameOverTimer;
    private boolean heroVisible;

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

    public Hero getHero() {
        return hero;
    }

    public boolean isHeroVisible() {
        return heroVisible;
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


    public Background getBackground() {
        return background;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

//    public BotControllerMy getBotController() {
//        return botControllerMy;
//    }


    public BotController getBotController() {
        return botController;
    }

    public Stage getStage() {
        return stage;
    }

    public int getHeroLife() {
        return heroLife;
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
//        this.botControllerMy = new BotControllerMy(this, MathUtils.random(-50, 50), MathUtils.random(-100, 300), MathUtils.random(0, 4));
        this.botController = new BotController(this);
        this.tempVec = new Vector2();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.infoController = new InfoController();
        this.sb = new StringBuilder();
        this.level = 0;
        this.heroLife = 5;
        this.delta = 1.0f;
        this.rndTime = MathUtils.random(3.0f, 12.0f);
        this.timerBots = 0.0f;
        this.addBotsTimer = 0.0f;
        this.addBots = false;
        this.heroVisible = true;
        this.gameOverTimer = 0.0f;
        this.dtTime = Gdx.graphics.getDeltaTime();
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.hahaha = Assets.getInstance().getAssetManager().get("audio/hahaha.mp3");
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
        //botControllerMy.update(dt);

        /*-----------Моя реализация выпадания улучшалок-----------*/
        //     powerAddController.update(dt);
        /*--------------------------------------------------------*/

        botController.update(dt);
        powerUpsController.update(dt);
        checkCollisions();
//        botRndVelocity(dt);

        /*----Проверка жив герой или нет + я добавил эффект уничтожения и таймер задержки до появления экрана GameOver--*/
//        if (!hero.isAlive()) {
//
//
//            if (crashHero) {
//                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
//            }
//            hero.setTexture(Assets.getInstance().getAtlas().findRegion("mini"));
//            getParticleController().getEffectBuilder().buildMonsterSplash(hero.getPosition().x, hero.getPosition().y);
//            if (gameOverTimer == 15){
//                crashHero = true;
//            }
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    crashHero = true;
//                }
//            };
//            Timer timer = new Timer();
//            long delay = 10000;
//            timer.schedule(task, delay);
//        }
        /*-----------------------------------------------------------------------------------------------------*/

        /*-----------Моя реализация выпадания улучшалок-----------*/
//              addHeroGifts();
        /*--------------------------------------------------------*/


//        if (asteroidController.getActiveList().size() == 0) {
//            timerAsteroidsAdds += dt;
//            if (timerAsteroidsAdds > 4.0f) {
//                level++;
//                timer = 0.0f;
//                timerAsteroidsAdds = 0.0f;
//                generateBigAsteroids(level <= 3 ? level : 3);
//                botController.setup(100, 100);
//                botController.setup(1000, -100);
//
//            }
//        }

        /*-------------------Моя реализация: астероиды появляются через 3 сек.----------------*/
        if (heroVisible) {
            if (asteroidController.getActiveList().isEmpty() && botController.getActiveList().isEmpty()) {
                addBots = false;
                timerAsteroidsAdds += dt;
                if (timerAsteroidsAdds > 4.0f) {
                    level += 1;
                    timer = 0.0f;
                    timerAsteroidsAdds = 0.0f;
                    addBotsTimer = 0.0f;
                    generateBigAsteroids(level <= 3 ? level : 3);
                    addBots = true;
                }
            }
        }
        /*--------------------------------------------------------------------------------------*/

        if (botController.getActiveList().isEmpty() && !asteroidController.getActiveList().isEmpty() && timerBots == 0 && addBots) {
            timerBots = MathUtils.random(3.0f, 10.0f);
        }
        if (botController.getActiveList().isEmpty() && timerBots > 0.0f) {
            addBotsTimer += dt;
            if (addBotsTimer > timerBots) {

                botController.setup(MathUtils.random(-100, ScreenManager.SCREEN_WIDTH + 100), MathUtils.random(-100, ScreenManager.SCREEN_HEIGHT + 100));
                addBotsTimer = 0.0f;
            }
        }

        if (!hero.isAlive()) {
            heroVisible = false;
            music.stop();
            gameOverTimer += dt;
            System.out.println(gameOverTimer);
            hero.setTexture(Assets.getInstance().getAtlas().findRegion("mini"));
            tempVec.set(-1256.0f, -1256.0f);
            hero.getPosition().mulAdd(tempVec, 0);
            if (gameOverTimer < 0.5f) {
                getParticleController().getEffectBuilder().buildMonsterSplash(hero.getPosition().x, hero.getPosition().y);
                hahaha.play();
            }
            if (gameOverTimer>2.5f){
                hahaha.stop();
            }
            if (gameOverTimer > 3.0f) {

                for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
                    Asteroid a = asteroidController.getActiveList().get(i);
                    a.deactivate();
                }
                for (int i = 0; i < botController.getActiveList().size(); i++) {
                    Bot b = botController.getActiveList().get(i);
                    b.deactivate();
                }
            }
            if (gameOverTimer > 4) {
                hero.getPosition().set(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
                hero.setTexture(Assets.getInstance().getAtlas().findRegion("ship10"));
            }
            if (gameOverTimer > 6.0f) {
                hero.hp = hero.hpMax;
                gameOverTimer = 0;
                heroLife -= 1;
                level-=1;
                heroVisible = true;
                music.play();
            }
            if (heroLife <= 0 && gameOverTimer > 2.5f) {
                level += 1;
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
            }
        }

        stage.act(dt);

        /*-----------Моя реализация появление астероидов-----------*/
//               addAsteroids(level);
        /*---------------------------------------------------------*/

    }

    private void checkCollisions() {
        if (heroVisible) {
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
            }

            /*----------------Проверка столкновения бота и астероида, отталкивание друг от друга если столкнулись-------*/
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                for (int i = 0; i < botController.getActiveList().size(); i++) {
                    Bot b = botController.getActiveList().get(i);
                    if (a.getHitArea().overlaps(b.getHitArea())) {
                        float dst = a.getPosition().dst(b.getPosition());
                        float halfOverLen = (a.getHitArea().radius + b.getHitArea().radius - dst) / 2;
                        tempVec.set(b.getPosition()).sub(a.getPosition()).nor();
                        b.getPosition().mulAdd(tempVec, halfOverLen);
                        a.getPosition().mulAdd(tempVec, -halfOverLen);
                        float sumScl = b.getHitArea().radius + a.getHitArea().radius;
                        b.getVelocity().mulAdd(tempVec, a.getHitArea().radius / sumScl * 100);
                        a.getVelocity().mulAdd(tempVec, -b.getHitArea().radius / sumScl * 100);
                    }
                }
            }

            /*---------------Моя реализация увеличения урона при увеличении уровня игры-------------*/
//                                hero.takeDamage(2 * gameLevel);
            /*--------------------------------------------------------------------------------------*/


            /*-----------Моя реализация проверки жив герой или нет-----------*/

            /*--------------------------------------------------------------------*/


//        /*-----------Моя реализация реакции бота при приближении героя-----------*/
//        for (int i = 0; i < botControllerMy.getActiveList().size(); i++) {
//            BotMy botMy = botControllerMy.getActiveList().get(i);
//            botMy.botOldAngel = botMy.angle+180;
//            if (botMy.getRadiusDetected().contains(hero.getHitArea())) {
//                tempVec.set(hero.getPosition()).sub(botMy.getPosition()).nor();
//                Vector2 botPos = botMy.getPosition().cpy().nor();
//                Vector2 botVel = botMy.getVelocity().cpy().nor();
//                tempVec.set(hero.getPosition()).sub(botMy.getPosition());
//                //float af = -botPos.angleDeg(tempVec);
//                float af = tempVec.angleDeg();
//                botMy.angle = af;
//                botMy.tryToFire();
//
//            } else {
//                botMy.angle = botMy.botOldAngel;
//            }
//        }
//        /*---------------------------------------------------------------------------*/
//
//
//        /*-----------Моя реализация реакции бота при приближении астероида-----------*/
//        for (int i = 0; i < botControllerMy.getActiveList().size(); i++) {
//            BotMy botMy = botControllerMy.getActiveList().get(i);
//            botMy.botOldAngel = botMy.angle+180;
//            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
//                Asteroid a = asteroidController.getActiveList().get(j);
//                if (botMy.radiusDetected.overlaps(a.getHitArea())) {
//                    float dst = botMy.getPosition().dst(a.getPosition());
//                    float halfOverLen = (botMy.radiusDetected.radius + a.getHitArea().radius * 3 - dst) / 150;
//                    tempVec.set(a.getPosition()).sub(botMy.getPosition()).nor();
//                    botMy.getPosition().mulAdd(tempVec, -halfOverLen);
//                    botMy.angle = botMy.getVelocity().angleDeg(botMy.getPosition());
//                } else {
//                    botMy.angle = botMy.botOldAngel;
//                }
//            }
//        }
//        /*-----------------------------------------------------------------------------------------------------------*/



            /*---------------------Проверка попадания пуль в астероид и их уничтожение--------------------------*/
            for (int i = 0; i < bulletController.getActiveList().size(); i++) {
                Bullet b = bulletController.getActiveList().get(i);
                for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                    Asteroid a = asteroidController.getActiveList().get(j);
                    if (a.getHitArea().contains(b.getPosition())) {
                        particleController.getEffectBuilder().bulletCollideWithAsteroid(b);
                        b.deactivate();
                        if (a.getHp() <= 0) {
                            for (int d = 0; d < MathUtils.random(1, 100); d++) {
                                if (d <= 1) {
                                    if (botController.getActiveList().size() <= level && botController.getActiveList().size() < 2) {
                                        botController.setup(a.getPosition().x, a.getPosition().y);
                                    }
                                }
                            }
                        }

                        if (a.takeDamage(b.getOwner().getCurrentWeapon().getDamage())) {
                            if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                                hero.addScore(a.getHpMax() * 100);
                                for (int k = 0; k < 3; k++) {
                                    powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() * 0.25f);
                                }
                            }
                        }
                        break;
                    }
                }
            }
            /*-----------------------------------------------------------------------------------------------------*/

            /*---------------------Проверка попадания пуль в ботов и в героя--------------------------*/
            //Проверка попадания пули бота в героя
            for (int i = 0; i < bulletController.getActiveList().size(); i++) {
                Bullet b = bulletController.getActiveList().get(i);

                if (b.getOwner().getOwnerType() == OwnerType.BOT) {
                    if (hero.getHitArea().contains(b.getPosition())) {
                        particleController.getEffectBuilder().bulletCollideWithHero(b);
                        hero.takeDamage(b.getOwner().getCurrentWeapon().getDamage());
                        b.deactivate();
                    }
                }
                //Проверка попадания пули героя в бота
                if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                    for (int j = 0; j < botController.getActiveList().size(); j++) {
                        Bot bot = botController.getActiveList().get(j);
                        if (bot.getHitArea().contains(b.getPosition())) {
                            particleController.getEffectBuilder().bulletCollideWithAsteroid(b);
                            bot.takeDamage(b.getOwner().getCurrentWeapon().getDamage());
                            b.deactivate();
                        }


                    }

                }


            }

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

//        botAndAsteroid();
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
    }

//    private void botAndAsteroid() {
//        for (int i = 0; i < botControllerMy.getActiveList().size(); i++) {
//            BotMy botMy = botControllerMy.getActiveList().get(i);
//            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
//                Asteroid a = asteroidController.getActiveList().get(j);
//                if (botMy.getHitArea().overlaps(a.getHitArea())) {
//                    botMy.tryToFire();
//                    float dst = botMy.getPosition().dst(a.getPosition());
//
//                    float halfOverLen = (botMy.getHitArea().radius + a.getHitArea().radius - dst);
//                    tempVec.set(a.getPosition()).sub(botMy.getPosition()).nor();
//                    a.getPosition().mulAdd(tempVec, halfOverLen);
//                    botMy.getPosition().mulAdd(tempVec, -halfOverLen);
//
//                    float sumScl = a.getHitArea().radius + botMy.getHitArea().radius;
//                    a.getVelocity().mulAdd(tempVec, botMy.getHitArea().radius / sumScl * 100);
//                    botMy.getVelocity().mulAdd(tempVec, -a.getHitArea().radius / sumScl * 100);
//                    botMy.takeDamage(level * 2);
//                    a.takeDamage(level * 2);
//                    if (botMy.hp <= 0) {
//                        //bot.setTexture(Assets.getInstance().getAtlas().findRegion("mini"));
//                        getParticleController().getEffectBuilder().buildMonsterSplash(botMy.getPosition().x, botMy.getPosition().y);
//                        botMy.deactivate();
//                    }
//                    if (a.getHp() <= 0) {
//                        getParticleController().getEffectBuilder().buildMonsterSplash(a.getPosition().x, a.getPosition().y);
//                        a.deactivate();
//                    }
//                }
//            }
//        }
//    }

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

//    private void botRndVelocity(float dt) {
//        timerBots += dt;
//        if (timerBots == rndTime) {
//            for (int i = 0; i < MathUtils.random(0, botControllerMy.getActiveList().size()); i++) {
//                BotMy botMy = botControllerMy.getActiveList().get(i);
//               Vector2 vel = botMy.velocity.set(MathUtils.random(-20, 50), MathUtils.random(-100, 300)).nor();
//               botMy.velocity.set(vel);
//                botMy.getPosition().mulAdd(vel, rndTime);
//            }
//            rndTime = MathUtils.random(3.0f, 10.0f);
//        }
//    }

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

