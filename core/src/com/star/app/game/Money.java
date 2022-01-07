package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Money implements Poolable {
    private GameController gc;
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int moneyCount;
    private Circle hitArea;
    private float scale;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    @Override
    public boolean isActive() {
        return active;
    }

    public int getMoneyCount() {
        return moneyCount;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Money(GameController gc) {
        this.gc = gc;
        this.texture = new Texture("images/ammo.png");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.moneyCount = 50;
        this.active = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1,
                0, 0, 0, 64, 64, false, false);
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < -(BASE_RADIUS * scale) ||
                position.x > ScreenManager.SCREEN_WIDTH + (BASE_RADIUS * scale) ||
                position.y < -(BASE_RADIUS * scale) ||
                position.y > ScreenManager.SCREEN_HEIGHT + (BASE_RADIUS * scale)) {
            deactivate();
        }
        hitArea.setPosition(position);
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        this.scale = scale;
        hitArea.setPosition(position);
        hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }
}
