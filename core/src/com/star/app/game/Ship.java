package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;

public class Ship {

    protected TextureRegion texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected GameController gc;
    protected Circle hitArea;
    protected Weapon currentWeapon;
    protected Weapon[] weapons;
    protected float angle;
    protected float enginePower;
    protected float fireTimer;
    protected int hpMax;
    protected int hp;
    protected int weaponNum;

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public float getAngle() {
        return angle;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isAlive() {
        return hp > 0;
    }


    public Ship(GameController gc, int hpMax, float enginePower) {
        this.gc = gc;
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.angle = 0.0f;
        this.enginePower = enginePower;
        createWeapons();
        this.currentWeapon = weapons[weaponNum];

    }

    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        float stopKoef = 1.0f - 0.8f * dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);

        checkSpaceBorders();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1,
                1, angle);
    }

    public void takeDamage(float amount) {
        hp -= amount;
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
    }

    public void tryToFire() {
        float wx;
        float wy;
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
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
