package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    public enum Skill {
        HP_MAX(20, 10), HP(20, 10), WEAPON(100, 1), MAGNET(50, 20);

        int cost;
        int power;

        Skill(int cost) {
            this.cost = cost;
        }

        Skill(int cost, int power) {
            this.cost = cost;
            this.power = power;
        }
    }

    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private int hpMax;
    private int hp;
    private Circle hitArea;
    private Circle magneticHitArea;
    private float fireTimer;
    private int score;
    private int scoreView;
    private StringBuilder sb;
    private Weapon currentWeapon;
    private int money;
    private Shop shop;
    private Weapon[] weapons;
    private int weaponNum;
    private ParticleController particleController;

    /*-----------Моя реализация паузы в игре-----------*/
    //    public static int scorePublic;
    //    public static int moneyPublic;
    //    private boolean pause;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2 - 3;
    private float magneticRadius = 0;

    public int getScore() {
        return score;
    }

    public Shop getShop() {
        return shop;
    }

    public int getHp() {
        return hp;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public int getHpMax() {
        return hpMax;
    }

    public float getAngle() {
        return angle;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Circle getMagneticHitArea() {
        return magneticHitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    /*-----------Моя реализация паузы в игре-----------*/
//    public boolean isPause() {
//        return pause;
//    }

    public void addScore(int amount) {
        score += amount;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public boolean isMoneyEnough(int amount) {
        return money >= amount;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public Hero(GameController gc) {
        /*-----------Моя реализация паузы в игре-----------*/
//        this.pause = false;

        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 240.0f;
        this.hpMax = 100;
        this.hp = hpMax;
        this.hitArea = new Circle(position, BASE_RADIUS);
        this.magneticHitArea = new Circle(position, magneticRadius);
        this.hitArea.setRadius(BASE_RADIUS);
        this.sb = new StringBuilder();
        this.shop = new Shop(this);
        this.money = 1500;
        this.weaponNum = 0;
        this.particleController = new ParticleController();
        createWeapons();

        this.currentWeapon = weapons[weaponNum];
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);
        sb.append("SCORE: ").append(scoreView).append("\n");
        sb.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        sb.append("BULLETS: ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        sb.append("MONEY: ").append(money).append("\n");
        font.draw(batch, sb, 20, 700);

        /*-----------Моя реализация паузы в игре-----------*/
//        moneyPublic = money;
//        scorePublic = scoreView;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1,
                1, angle);
    }

    public void takeDamage(float amount) {
        hp -= amount;
    }

    public void setPause(boolean pause){
        gc.setPause(pause);
    }

    public void update(float dt) {
        fireTimer += dt;
        updateScore(dt);
        boardControl(dt);
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        magneticHitArea.setPosition(position);
        float stopKoef = 1.0f - 0.8f * dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);

        checkSpaceBorders();
    }

    private void boardControl(float dt) {

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tryToFire();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
            /*------------Эффект работы двигателя при ускорении---------------*/
            float bx = position.x + MathUtils.cosDeg(angle + 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-10, 10), velocity.y * 0.1f + MathUtils.random(-10, 10),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
            /*----------------------------------------------------------------------*/
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
            /*------------Эффект работы двигателя при ускорении---------------*/
            float bx = position.x + MathUtils.cosDeg(angle - 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle - 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-10, 10), velocity.y * 0.1f + MathUtils.random(-10, 10),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
            /*----------------------------------------------------------------------*/
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;

            /*------------Эффект работы двигателя при ускорении---------------*/
            if (velocity.len() > 50.0f) {
                float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
                float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
                for (int i = 0; i < 3; i++) {
                    gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.2f + MathUtils.random(-20, 20), velocity.y * -0.2f + MathUtils.random(-20, 20),
                            0.5f, 1.2f, 0.2f, 1.0f, 0.5f, 0, 1, 1, 1, 1, 0
                    );
                }
            }
            /*----------------------------------------------------------------------*/
        }

        /*------------Управление задним ходом корабля-------------------------------------*/
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * enginePower / 2 * dt;
            velocity.y -= MathUtils.sinDeg(angle) * enginePower / 2 * dt;

            /*------------Эффект работы двигателя при ускорении---------------*/
            float bx = position.x + MathUtils.cosDeg(angle + 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
            bx = position.x + MathUtils.cosDeg(angle - 90) * 20;
            by = position.y + MathUtils.sinDeg(angle - 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
            /*----------------------------------------------------------------------*/

        }

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            shop.setVisible(true);
            setPause(true);
        }
    }

    /*-----------Моя реализация паузы в игре-----------*/
//    public void gamePause() {
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            if (pause) {
//                pause = false;
//                return;
//            }
//            pause = true;
//            Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f);
//            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        }
//    }

    public boolean upgrade(Skill skill) {
        switch (skill) {
            case HP_MAX:
                hpMax += Skill.HP_MAX.power;
                return true;

            case HP:
                if (hp < hpMax) {
                    hp += Skill.HP.power;
                    if (hp > hpMax) {
                        hp = hpMax;
                        return true;
                    }
                }
                break;

            case WEAPON:
                if (weaponNum < weapons.length - 1) {
                    weaponNum++;
                    currentWeapon = weapons[weaponNum];
                    return true;
                }
                break;

            case MAGNET:
                magneticHitArea.setRadius(magneticRadius += 25);
                return true;

        }
        return false;
    }

    public void consume(PowerUp p) {
        switch (p.getType()) {
            case MEDKIT:
                hp += p.getPower();
                if (hp > hpMax) {
                    hp = hpMax;
                }
                break;
            case MONEY:
                money += p.getPower();
                break;
            case AMMOS:
                currentWeapon.addAmmos(p.getPower());
                break;
        }
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 2000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    private void tryToFire() {
        float wx;
        float wy;
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();

//                /* Стрельба только с носа корабля
//                wx = position.x + MathUtils.cosDeg(angle) * 20.0f;
//                wy = position.y + MathUtils.sinDeg(angle) * 20.0f;
//                */
//
//            /*---Двойная cтрельба с крыльев корабля---*/
//            //Левое крыло
//            wx = position.x + MathUtils.cosDeg(angle + 90) * 25.0f;
//            wy = position.y + MathUtils.sinDeg(angle + 90) * 25.0f;
//            gc.getBulletController().setup(wx, wy,
//                    MathUtils.cosDeg(angle) * 500.0f + velocity.x,
//                    MathUtils.sinDeg(angle) * 500.0f + velocity.y);
//            //Правое крыло
//            wx = position.x + MathUtils.cosDeg(angle - 90) * 25.0f;
//            wy = position.y + MathUtils.sinDeg(angle - 90) * 25.0f;
//            gc.getBulletController().setup(wx, wy,
//                    MathUtils.cosDeg(angle) * 500.0f + velocity.x,
//                    MathUtils.sinDeg(angle) * 500.0f + velocity.y);
        }
    }

    private void checkSpaceBorders() {
        /*------------Определение границы экрана, чтобы корабль не улетал за его пределы.----------------*/
        if (position.x < 32) {
            position.x = 32;
            velocity.x *= -0.5;
        }
        if (position.y < 32) {
            position.y = 32;
            velocity.y *= -0.5;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32;
            velocity.x *= -0.5;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32;
            velocity.y *= -0.5;
        }

        /*------------Чтобы кораль появлялся в противоположной части экрана если улетел за его границу------------------*/
//        if (position.x < - 32) {
//            position.x = ScreenManager.SCREEN_WIDTH + 32;
//        }else {
//            if (position.x > ScreenManager.SCREEN_WIDTH + 32) {
//                position.x = -32;
//            }
//        }
//
//        if (position.y < -32) {
//            position.y = ScreenManager.SCREEN_HIEGHT + 32;
//        }else{
//            if (position.y > ScreenManager.SCREEN_HIEGHT + 32) {
//                position.y = -32;
//            }
//        }
    }

    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(gc, this, "Laser", 0.2f, 1, 300f, 300,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0),
                        }),
                new Weapon(gc, this, "Laser", 0.2f, 1, 600f, 500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),
                        }),
                new Weapon(gc, this, "Laser", 0.1f, 1, 600f, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),
                        }),
                new Weapon(gc, this, "Laser", 0.1f, 2, 600f, 1000,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0),
                                new Vector3(28, 90, 15),
                                new Vector3(28, -90, -15),
                        }),
                new Weapon(gc, this, "Laser", 0.1f, 3, 600f, 1500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                        }),
        };
    }

}


