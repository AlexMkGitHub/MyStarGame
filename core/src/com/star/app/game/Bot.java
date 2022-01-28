package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2 - 3;
    private boolean active;
    private Vector2 tempVec;
    private int weapon;

    public int getHp() {
        return hp;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public Bot(GameController gc) {
        super(gc, 10 * gc.getLevel(), MathUtils.random(100, 200), MathUtils.random(0, 2));
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.tempVec = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship" + MathUtils.random(4, 9));
        this.hitArea = new Circle(position, 29);
        this.hitArea.setRadius(BASE_RADIUS);
        this.active = false;
        this.ownerType = OwnerType.BOT;
    }

    public void update(float dt) {
        super.update(dt);
        boardControl(dt);

        //Проверка жив бот или нет, если нет, то деактивация
        if (!isAlive()) {
            hp = hpMax;
            deactivate();
        }
    }

    private void boardControl(float dt) {
        tempVec.set(gc.getHero().getPosition()).sub(position).nor();
        angle = tempVec.angleDeg();

        if (gc.getHero().getPosition().dst(position) > 200) {
            accelrrate(dt);
            /*------------Эффект работы двигателя при ускорении---------------*/
            if (velocity.len() > 50.0f) {
                float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
                float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
                for (int i = 0; i < 3; i++) {
                    gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.2f + MathUtils.random(-20, 20), velocity.y * -0.2f + MathUtils.random(-20, 20),
                            0.5f, 1.2f, 0.2f,
                            1.0f, 0.0f, 0.0f, 1,
                            1.0f, 0.5f, 0f, 0);
                }
            }
        }

        if (gc.getHero().getPosition().dst(position) < 300 && gc.getHero().hp > 0) {
            tryToFire();

        }

    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y) {
        position.set(x, y);
        active = true;
    }

    @Override
    public boolean isActive() {
        return active;
    }

}



