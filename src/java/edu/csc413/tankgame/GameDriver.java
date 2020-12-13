package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;

/**
 * GameDriver is the primary controller class for the tank game. The game is launched from GameDriver.main, and
 * GameDriver is responsible for running the game loop while coordinating the views and the data models.
 */
public class GameDriver {
    // TODO: Implement.
    // Add the instance variables, constructors, and other methods needed for this class. GameDriver is the centerpiece
    // for the tank game, and should store and manage the other components (i.e. the views and the models). It also is
    // responsible for running the game loop.
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameState gameState;
    private final GameKeyListener gameKeyListener;

    public GameDriver() {
        gameState = new GameState();
        gameKeyListener = new GameKeyListener(gameState);
        mainView = new MainView(gameKeyListener);
        runGameView = mainView.getRunGameView();
    }

    public void start() {
        // TODO: Implement.
        // This should set the MainView's screen to the start menu screen.
        // mainView.setScreen(MainView.Screen.START_MENU_SCREEN);

        // NOT CORRECT
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        runGame();
    }

    private void runGame() {
        Tank playerTank =
                new PlayerTank(
                        GameState.PLAYER_TANK_ID,
                        RunGameView.PLAYER_TANK_INITIAL_X,
                        RunGameView.PLAYER_TANK_INITIAL_Y,
                        RunGameView.PLAYER_TANK_INITIAL_ANGLE
                );
        Tank aiTank =
                new CushionTank(
                        GameState.AI_TANK_ID,
                        RunGameView.AI_TANK_INITIAL_X,
                        RunGameView.AI_TANK_INITIAL_Y,
                        RunGameView.AI_TANK_INITIAL_ANGLE
                );
        gameState.addEntity(playerTank);
        gameState.addEntity(aiTank);

        runGameView.addDrawableEntity(
                GameState.PLAYER_TANK_ID,
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(),
                playerTank.getY(),
                playerTank.getAngle()
        );
        runGameView.addDrawableEntity(
                GameState.AI_TANK_ID,
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank.getX(),
                aiTank.getY(),
                aiTank.getAngle()
        );

        for (WallImageInfo wallImageInfo: WallImageInfo.readWalls()) {
            Wall newWall = new Wall(wallImageInfo.getX(), wallImageInfo.getY(), 0);
            gameState.getEntities().add(newWall);

            runGameView.addDrawableEntity(
                    newWall.getId(),
                    wallImageInfo.getImageFile(),
                    newWall.getX(),
                    newWall.getY(),
                    0
            );
        }

        Runnable gameRunner = () -> {
            while (update()) {
                runGameView.repaint();
                try {
                    Thread.sleep(8L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
        };
        new Thread(gameRunner).start();
    }

    // TODO: Implement.
    // update should handle one frame of gameplay. All tanks and shells move one step, and all drawn entities
    // should be updated accordingly. It should return true as long as the game continues.
    private boolean update() {
        // Ask all tanks, shells, etc. to move
        for (Entity entity: gameState.getEntities()) {
            entity.move(gameState);
        }

        for (Entity entity: gameState.getEntities()) {
            runGameView.setDrawableEntityLocationAndAngle(
                    entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
        }

        // Ask all tanks, shells, etc. to check bounds

        // Check collisions

        // Ask gameState -- any new shells to draw?
        // If so, call addDrawableEntity
        for (Entity newShell: gameState.getNewShells()) {
            runGameView.addDrawableEntity(
                    newShell.getId(),
                    RunGameView.SHELL_IMAGE_FILE,
                    newShell.getX(),
                    newShell.getY(),
                    newShell.getAngle());
            gameState.addEntity(newShell);
        }

        // Ask gameState -- any shells to remove?
        // If so, call removeDrawableEntity

        for (Entity checkShell: gameState.getEntities()) {
            if (checkShell.getId().startsWith("shell")) {
                if (
                        checkShell.getX() < gameState.getShellXLowerBound()
                        || checkShell.getX() > gameState.getShellXUpperBound()
                        || checkShell.getY() < gameState.getShellYLowerBound()
                        || checkShell.getY() > gameState.getShellYUpperBound()) {
                    //runGameView.removeDrawableEntity(checkShell.getId());
                    //gameState.removeEntity(checkShell);
                }
            }
        }

        return true;
    }

    /*
    private boolean entitiesOverlap(Entity entity1, Entity entity2) {
        return entity1.getX() < entity2.getXBound()
                && entity1.getXBound() > entity2.getX()
                && entity1.getY() < entity2.getYBound()
                && entity1.getYBound() > entity2.getY();
    }
     */

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
