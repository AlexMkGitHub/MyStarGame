package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class BotControllerMy extends ObjectPool<BotMy> {
    private GameController gc;
    private int hpMax;
    private float enginePower;
    private int weapon;

    public BotControllerMy(GameController gc, int xpMax, float enginePower, int weapon) {
        this.gc = gc;
        this.hpMax = xpMax;
        this.enginePower = enginePower;
        this.weapon = weapon;

    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Ship s = activeList.get(i);
            s.render(batch);
        }
    }

    public void setup(float x, float y, float vx, float vy, int weapon) {
        getActiveElement().activate(x, y, vx, vy, weapon);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    @Override
    protected BotMy newObject() {
        return new BotMy(gc, hpMax, enginePower, weapon);
    }
}
