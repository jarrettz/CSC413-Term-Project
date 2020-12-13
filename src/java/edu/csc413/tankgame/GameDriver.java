package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;
import edu.csc413.tankgame.view.StartMenuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

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
    ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent event) {
            String actionCommand = event.getActionCommand();
            if (actionCommand.equals(StartMenuView.START_BUTTON_ACTION_COMMAND)) {
                mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
                runGame();
            } else if (actionCommand.equals(StartMenuView.EXIT_BUTTON_ACTION_COMMAND)) {
                mainView.closeGame();
            }
        }
    };

    public GameDriver() {
        gameState = new GameState();
        gameKeyListener = new GameKeyListener(gameState);
        mainView = new MainView(gameKeyListener, actionListener);
        runGameView = mainView.getRunGameView();
    }

    public void start() {
        mainView.setScreen(MainView.Screen.START_MENU_SCREEN);
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
        Tank turretTank =
                new TurretAiTank(
                        GameState.AI_TANK_2_ID,
                        RunGameView.AI_TANK_2_INITIAL_X,
                        RunGameView.AI_TANK_2_INITIAL_Y,
                        RunGameView.AI_TANK_2_INITIAL_ANGLE
                );
        gameState.addEntity(playerTank);
        gameState.addEntity(aiTank);
        gameState.addEntity(turretTank);

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
        runGameView.addDrawableEntity(
                GameState.AI_TANK_2_ID,
                RunGameView.AI_TANK_IMAGE_FILE,
                turretTank.getX(),
                turretTank.getY(),
                turretTank.getAngle()
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

        for (Entity entity: gameState.getEntities()) {
            if (entity.getId().startsWith("shell")) {
                if (
                        entity.getX() < gameState.getShellXLowerBound()
                                || entity.getX() > gameState.getShellXUpperBound()
                                || entity.getY() < gameState.getShellYLowerBound()
                                || entity.getY() > gameState.getShellYUpperBound()) {
                    // Remembers removable shells
                    gameState.addRemovableShell(entity);
                    runGameView.removeDrawableEntity(entity.getId());
                }
            }
        }
        // Removes all removable shells after iteration to make sure no error occurs
        gameState.removeShells();

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
