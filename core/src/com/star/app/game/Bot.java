package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.GameController;
import com.star.app.game.OwnerType;
import com.star.app.game.Ship;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2 - 3;
    private boolean active;
    private Vector2 tempVec;
    private int rndHeroPresentGift;
    private int scale;

    public int getHp() {
        return hp;
    }

    @Override
    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public Bot(GameController gc) {
        super(gc, 10 * gc.getLevel(), MathUtils.random(100, 200));
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.tempVec = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship" + MathUtils.random(4, 9));
        this.hitArea = new Circle(position, 29);
        this.hitArea.setRadius(BASE_RADIUS);
        this.active = false;
        this.ownerType = OwnerType.BOT;
        this.rndHeroPresentGift = 0;
        if (gc.getLevel() <= 3) {
            scale = 1;
        } else if (gc.getLevel() > 3 && gc.getLevel() <= 8) {
            scale = MathUtils.random(1, 2);
        } else if (gc.getLevel() > 8 && gc.getLevel() <= 15) {
            scale = MathUtils.random(1, 3);
        } else if (gc.getLevel() > 15 && gc.getLevel() <= 25) {
            scale = MathUtils.random(2, 3);
        } else if (gc.getLevel() > 25) {
            scale = MathUtils.random(3, 5);
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        boardControl(dt);
        //Проверка жив бот или нет, если нет, то деактивация
        if (!isAlive()) {
            if (hp <= 0) {
                rndHeroPresentGift = MathUtils.random(1, 100);
            }
            hp = hpMax;
            deactivate();
            gc.getParticleController().getEffectBuilder().destroyEffect(position.x, position.y);
        }
        if (rndHeroPresentGift > 0 && rndHeroPresentGift <= 50) {
            //gc.getHero().botDestroyPresent(MathUtils.random(1, 3));
            gc.getHero().botDestroyPresent(MathUtils.random(2));
            rndHeroPresentGift = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, scale,
                scale, angle);
    }

    private void boardControl(float dt) {
        if (gc.isHeroVisible()) {
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



