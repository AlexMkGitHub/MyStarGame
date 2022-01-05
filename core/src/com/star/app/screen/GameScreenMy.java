package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.gamemy.GameControllerMy;
import com.star.app.gamemy.WorldRendererMy;
import com.star.app.screen.utils.Assets;

public class GameScreenMy extends AbstractScreen{
    private SpriteBatch batch;
    private GameControllerMy gc;
    private WorldRendererMy worldRendererMy;

    public GameScreenMy(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gc = new GameControllerMy();
        this.worldRendererMy = new WorldRendererMy(gc, batch);
    }

    @Override
    public void render(float delta) {
        gc.update(delta);
        worldRendererMy.render();
    }
}
