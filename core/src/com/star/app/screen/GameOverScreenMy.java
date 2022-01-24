package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.GameController;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;


public class GameOverScreenMy extends AbstractScreen {
    private BitmapFont font84;
    private BitmapFont font24;
    private BitmapFont font32;
    private GameController gc;


    private Stage stage;

    public GameOverScreenMy(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font84 = Assets.getInstance().getAssetManager().get("fonts/font84.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnNewGame = new TextButton("New Game", textButtonStyle);
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle);
        btnNewGame.setPosition(480, 210);
        btnExitGame.setPosition(480, 110);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(btnNewGame);
        stage.addActor(btnExitGame);
        skin.dispose();


    }

    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        font84.draw(batch, "Game over!!!", 0, 600, 1280, Align.center, false);

        /*-----------Моя реализация паузы в игре-----------*/
//        font32.draw(batch, "You score: " + Hero.scorePublic, 0, 450, 1280, Align.center, false);
//        font32.draw(batch, "You money: " + Hero.moneyPublic, 0, 400, 1280, Align.center, false);

        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
