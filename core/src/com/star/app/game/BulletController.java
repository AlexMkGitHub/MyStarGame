package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.ObjectPool;

public class BulletController extends ObjectPool<Bullet> {
    private Texture bulletTexture;

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public BulletController() {
        this.bulletTexture = new Texture("bullet.png");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            batch.draw(bulletTexture, b.getPosition().x-16, b.getPosition().y-16, 16, 16, 32, 32, 1.0f,
                    1.0f,0.0f, 0, 0, 32, 32, false, false);
        }
    }

    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void boomBullet(Vector2 v, SpriteBatch batch) {
        batch.draw(bulletTexture, v.x-16, v.y-16, 16, 16, 32, 32, 4f,
                4f, 0.0f, 32, 32, 32, 32, false, false);
    }

}
