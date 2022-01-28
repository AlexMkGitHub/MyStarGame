package com.star.app.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;
import javafx.stage.Screen;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font20;
    private BitmapFont font32;
    private BitmapFont font72;
    private StringBuilder sb;
    private Music fight;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font20 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf", BitmapFont.class);
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf", BitmapFont.class);
        this.fight = Assets.getInstance().getAssetManager().get("audio/fight.mp3");
        this.sb = new StringBuilder();
    }

    public void render() {

        /*-----------Моя реализация паузы в игре-----------*/
        //if (!gc.getHero().isPause()) {
        /*-------------------------------------------------*/

        ScreenUtils.clear(0.0f, 0.1f, 0.5f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getBulletController().render(batch);
        gc.getParticleController().render(batch);
        gc.getPowerUpsController().render(batch);
        gc.getInfoController().render(batch, font20);
        gc.getHero().render(batch);
        gc.getHero().renderGUI(batch, font20);
        gc.getBotController().render(batch);

        /*-------------------Моя реализация: оповещении о поднятии уровня исчезает через 1.5 сек.----------------*/
        if (gc.getAsteroidController().getActiveList().isEmpty()) {
            if (gc.getTimerAsteroidsAdds() > 0.75f && gc.getTimerAsteroidsAdds() < 2.5f) {
                sb.setLength(0);
                sb.append("Level: ").append(gc.getLevel() + 1);
                font72.draw(batch, sb, 0, ScreenManager.SCREEN_HALF_HEIGHT, ScreenManager.SCREEN_WIDTH,
                        Align.center, false);
            }
            if (gc.getTimerAsteroidsAdds() > 2.5f && gc.getTimerAsteroidsAdds() < 3.5f) {
                fight.play();
            }
        }
        /*----------------------------------------------------------------------------------------------------*/

        /*-----------Моя реализация улучшалок-----------*/
//        gc.getPowerAddController().render(batch);
        /*----------------------------------------------*/

        batch.end();
        gc.getStage().draw();

        /*-----------Моя реализация паузы в игре-----------*/
        //}
        /*-------------------------------------------------*/
    }

}

