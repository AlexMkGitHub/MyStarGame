package com.star.app.gamemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class WorldRendererMy {
    private GameControllerMy gc;
    private SpriteBatch batch;

    public WorldRendererMy(GameControllerMy gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
    }

    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        gc.getAsteroidController().render(batch);
        gc.checkCollisions(batch);
        batch.end();
    }

}
