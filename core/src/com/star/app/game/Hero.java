package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private float fireTimer;
    private int score;
    private int scoreView;

    public int getScoreView() {
        return scoreView;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 240.0f;

    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1,
                1, angle);
    }

    public void update(float dt) {
        fireTimer += dt;
        if (scoreView < score) {
            scoreView += 2000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        float wx;
        float wy;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (fireTimer > 0.2f) {
                fireTimer = 0.0f;
                /* Стрельба только с носа корабля
                wx = position.x + MathUtils.cosDeg(angle) * 20.0f;
                wy = position.y + MathUtils.sinDeg(angle) * 20.0f;
                */

                /*---Двойная cтрельба с крыльев корабля---*/
                //Левое крыло
                wx = position.x + MathUtils.cosDeg(angle + 90) * 25.0f;
                wy = position.y + MathUtils.sinDeg(angle + 90) * 25.0f;
                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500.0f + velocity.x,
                        MathUtils.sinDeg(angle) * 500.0f + velocity.y);
                //Правое крыло
                wx = position.x + MathUtils.cosDeg(angle - 90) * 25.0f;
                wy = position.y + MathUtils.sinDeg(angle - 90) * 25.0f;
                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500.0f + velocity.x,
                        MathUtils.sinDeg(angle) * 500.0f + velocity.y);

            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
        }


        float stopKoef = 1.0f - 0.8f * dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);

        /*------------Управление задним ходом корабля-------------------------------------*/
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * enginePower / 2 * dt;
            velocity.y -= MathUtils.sinDeg(angle) * enginePower / 2 * dt;
        }

        position.mulAdd(velocity, dt);
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
}


