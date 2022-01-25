package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero extends Ship {
    public enum Skill {
        HP_MAX(20, 10), HP(20, 10), WEAPON(100, 1), MAGNET(50, 10);

        /*-----------Моя реализация магнита в игре-----------*/
//        ,MAGNET(50, 20);
        /*---------------------------------------------------*/

        int cost;
        int power;

        Skill(int cost, int power) {
            this.cost = cost;
            this.power = power;
        }
    }

    /*-----------Моя реализация магнита в игре-----------*/
    //private Circle magneticHitArea;
    /*---------------------------------------------------*/
    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2 - 3;

    private Circle magneticField;
    private int score;
    private int scoreView;
    private StringBuilder sb;
    private int money;
    private Shop shop;

    /*-----------Моя реализация паузы в игре-----------*/
    //    public static int scorePublic;
    //    public static int moneyPublic;
    //    private boolean pause;
    /*-------------------------------------------------*/

    /*-----------Моя реализация магнита в игре-----------*/
    //private float magneticRadius = 0;
    /*---------------------------------------------------*/

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

    public int getHpMax() {
        return hpMax;
    }

    public Circle getMagneticField() {
        return magneticField;
    }

    /*-----------Моя реализация магнита в игре-----------*/
//    public Circle getMagneticHitArea() {
//        return magneticHitArea;
//    }
    /*--------------------------------------------------*/

    /*-----------Моя реализация паузы в игре-----------*/
//    public boolean isPause() {
//        return pause;
//    }
    /*--------------------------------------------------*/

    public Hero(GameController gc) {
        super(gc, 3, 500f);
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.hitArea = new Circle(position, 29);
        this.money = 1500;
        this.sb = new StringBuilder();
        this.shop = new Shop(this);
        this.magneticField = new Circle(position, 100);
        this.hitArea.setRadius(BASE_RADIUS);


        /*-----------Моя реализация паузы в игре-----------*/
//        this.pause = false;
        /*-------------------------------------------------*/

        /*-----------Моя реализация магнита в игре-----------*/
//        this.magneticHitArea = new Circle(position, magneticRadius);
        /*---------------------------------------------------*/

    }

    public void addScore(int amount) {
        score += amount;
    }


    public boolean isMoneyEnough(int amount) {
        return money >= amount;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);

        /*---------------Моя реализация показа уровня игры в верхнем углу-------------*/
//        sb.append("Game Level: ").append(gc.getGameLevel()).append("\n");
        /*----------------------------------------------------------------------------*/

        sb.append("SCORE: ").append(scoreView).append("\n");
        sb.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        sb.append("BULLETS: ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        sb.append("MONEY: ").append(money).append("\n");
        sb.append("MAGNET: ").append((int) magneticField.radius).append("\n");
        font.draw(batch, sb, 20, 700);

        /*-----------Моя реализация паузы в игре-----------*/
//        moneyPublic = money;
//        scorePublic = scoreView;
        /*-------------------------------------------------*/
    }

    public void setPause(boolean pause) {
        gc.setPause(pause);
    }

    public void update(float dt) {
        super.update(dt);
        updateScore(dt);
        boardControl(dt);
        magneticField.setPosition(position);

        /*-----------Моя реализация магнита в игре-----------*/
//        magneticHitArea.setPosition(position);
        /*---------------------------------------------------*/
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
    /*--------------------------------------------------*/

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
                if (magneticField.radius < 500) {
                    magneticField.radius += Skill.MAGNET.power;
                    return true;
                }
                break;

            /*-----------Моя реализация магнита в игре-----------*/
//            case MAGNET:
//                magneticHitArea.setRadius(magneticRadius += 25);
//                return true;
            /*---------------------------------------------------*/

        }
        return false;
    }

    public void consume(PowerUp p) {
        sb.setLength(0);
        switch (p.getType()) {
            case MEDKIT:
                int oldHp = hp;
                hp += p.getPower();
                if (hp > hpMax) {
                    hp = hpMax;
                }
                sb.append("HP + ").append(hp - oldHp);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.GREEN);
                break;
            case MONEY:
                money += p.getPower();
                sb.append("Money + ").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.ORANGE);
                break;
            case AMMOS:
                currentWeapon.addAmmos(p.getPower());
                sb.append("Ammo + ").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.PURPLE);
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



