package com.star.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.screen.GameScreenMy;

public class StarGameMy extends Game {
    private SpriteBatch batch;
    private GameScreenMy gameScreenMy;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        gameScreenMy = new GameScreenMy(batch);
        setScreen(gameScreenMy);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
