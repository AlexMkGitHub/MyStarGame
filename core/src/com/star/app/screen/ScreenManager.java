package com.star.app.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.star.app.StarGame;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;

public class ScreenManager {
    public enum ScreenType {
        GAME, MENU, GAMEOVER, GAME_OVER_MY
    }

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HALF_WIDTH = SCREEN_WIDTH / 2;
    public static final int SCREEN_HEIGHT = 720;
    public static final int SCREEN_HALF_HEIGHT = SCREEN_HEIGHT / 2;


    private StarGame game;
    private SpriteBatch batch;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private GameOverScreen gameOverScreen;

    /*-----------Моя реализация окна паузы в игре-----------*/
//        private GameOverScreenMy gameOverScreenMy;
    /*------------------------------------------------------*/

    private Screen targetScreen;
    private Viewport viewport;

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {
    }

    public void init(StarGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch);

        /*-----------Моя реализация окна паузы в игре-----------*/
//                this.gameOverScreenMy = new GameOverScreenMy(batch);
        /*------------------------------------------------------*/

        this.loadingScreen = new LoadingScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public void changeScreen(ScreenType type, Object... args) {
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }

        game.setScreen(loadingScreen);
        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAMEOVER:
                targetScreen = gameOverScreen;
                gameOverScreen.setDefeatedHero((Hero) args[0]);
                Assets.getInstance().loadAssets(ScreenType.GAMEOVER);
                break;

            /*-----------Моя реализация окна паузы в игре-----------*/
//                case GAME_OVER_MY:
//                targetScreen = gameOverScreenMy;
//                Assets.getInstance().loadAssets(ScreenType.GAME_OVER_MY);
//                break;
            /*------------------------------------------------------*/
        }
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }
}