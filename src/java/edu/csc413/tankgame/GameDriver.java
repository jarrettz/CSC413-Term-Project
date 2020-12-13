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
                //cushion
                new DumbAiTank(
                        GameState.AI_TANK_ID,
                        RunGameView.AI_TANK_INITIAL_X,
                        RunGameView.AI_TANK_INITIAL_Y,
                        RunGameView.AI_TANK_INITIAL_ANGLE
                );
        Tank ai2Tank =
                //turret
                new DumbAiTank(
                        GameState.AI_TANK_2_ID,
                        RunGameView.AI_TANK_2_INITIAL_X,
                        RunGameView.AI_TANK_2_INITIAL_Y,
                        RunGameView.AI_TANK_2_INITIAL_ANGLE
                );
        gameState.addEntity(playerTank);
        gameState.addEntity(aiTank);
        gameState.addEntity(ai2Tank);

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
                ai2Tank.getX(),
                ai2Tank.getY(),
                ai2Tank.getAngle()
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
                    // Changed from 8L
                    Thread.sleep(15L);
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

        // Keeps Tank in Bounds
        for (Entity entity: gameState.getEntities()) {
            if (entity instanceof Tank) {
                if (entity.getX() < gameState.getTankXLowerBound()) {
                    runGameView.setDrawableEntityLocationAndAngle(
                            entity.getId(), gameState.getTankXLowerBound(), entity.getY(), entity.getAngle());
                }
                if (entity.getX() > gameState.getTankXUpperBound()) {
                    runGameView.setDrawableEntityLocationAndAngle(
                            entity.getId(), gameState.getTankXUpperBound(), entity.getY(), entity.getAngle());
                }
                if (entity.getY() < gameState.getTankYLowerBound()) {
                    runGameView.setDrawableEntityLocationAndAngle(
                            entity.getId(), entity.getX(), gameState.getTankYLowerBound(), entity.getAngle());
                }
                if (entity.getY() > gameState.getTankYUpperBound()) {
                    runGameView.setDrawableEntityLocationAndAngle(
                            entity.getId(), entity.getX(), gameState.getTankYUpperBound(), entity.getAngle());

                }
            }
        }


        // Check collisions
        for (int i = 0; i < gameState.getEntities().size(); i++) {
            for (int j = i + 1; j < gameState.getEntities().size(); j++) {
                Entity entity1 = gameState.getEntities().get(i);
                Entity entity2 = gameState.getEntities().get(j);
                if (entity1.entitiesOverlap(entity1, entity2)) {
                    handleCollision(entity1, entity2);
                }
            }
        }
        gameState.removeEntities();
        gameState.removeShells();


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

        if (gameState.getEntity(GameState.PLAYER_TANK_ID) == null
                || (gameState.getEntity(GameState.AI_TANK_ID)) == null
                && gameState.getEntity(GameState.AI_TANK_2_ID) == null
                || gameState.isEscPressed()) {
            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            runGameView.reset();
            gameState.getEntities().clear();
            gameState.removeShells();
            gameState.removeEntities();
            gameState.resetEsc();
            return false;
        }

        return true;
    }

    public void handleCollision(Entity entity1, Entity entity2) {
        if (entity1 instanceof Tank && entity2 instanceof Tank) {
            gameState.addRemovableEntity(entity1);
            gameState.addRemovableEntity(entity2);
            runGameView.removeDrawableEntity(entity1.getId());
            runGameView.removeDrawableEntity(entity2.getId());

        } else if (entity1 instanceof Tank && entity2 instanceof Shell) {
            ((Tank) entity1).lostHP();
            if (((Tank) entity1).noHP()) {
                gameState.addRemovableEntity(entity1);
                runGameView.removeDrawableEntity(entity1.getId());
            }
            gameState.addRemovableShell(entity2);
            runGameView.removeDrawableEntity(entity2.getId());
        } else if (entity1 instanceof Shell && entity2 instanceof Tank) {
            ((Tank) entity2).lostHP();
            if (((Tank) entity2).noHP()) {
                gameState.addRemovableEntity(entity2);
                runGameView.removeDrawableEntity(entity2.getId());
            }
            gameState.addRemovableShell(entity1);
            runGameView.removeDrawableEntity(entity1.getId());
        } else if (entity1 instanceof Wall && entity2 instanceof Shell) {
            ((Wall) entity1).lostHP();
            if (((Wall) entity1).noHP()) {
                gameState.addRemovableEntity(entity1);
                runGameView.removeDrawableEntity(entity1.getId());
            }
            gameState.addRemovableShell(entity2);
            runGameView.removeDrawableEntity(entity2.getId());
        } else if (entity1 instanceof Shell && entity2 instanceof Wall) {
            ((Wall) entity2).lostHP();
            if (((Wall) entity2).noHP()) {
                gameState.addRemovableEntity(entity2);
                runGameView.removeDrawableEntity(entity2.getId());
            }
            gameState.addRemovableShell(entity1);
            runGameView.removeDrawableEntity(entity1.getId());
        } else if (entity1 instanceof Tank && entity2 instanceof Wall) {
            tankWallInteraction(entity1, entity2);
        } else if (entity1 instanceof Wall && entity2 instanceof Tank) {
            tankWallInteraction(entity2, entity1);
        }
    }

    public void tankWallInteraction(Entity tank, Entity wall) {
        double smallestX;
        double smallestY;

        boolean right = false;
        boolean left = false;
        boolean up = false;
        boolean down = false;

        double x1 = tank.getXBound() - wall.getX();
        double x2 = wall.getXBound() - tank.getX();

        double y1 = tank.getYBound() - wall.getY();
        double y2 = wall.getYBound() - tank.getY();

        if (x1 < x2) {
            smallestX = x1;
            left = true;
        } else {
            smallestX = x2;
            right = true;
        }

        if (y1 < y2) {
            smallestY = y1;
            up = true;
        } else {
            smallestY = y2;
            down = true;
        }

        if (smallestX < smallestY) {
            if (right) {
                runGameView.setDrawableEntityLocationAndAngle(
                        tank.getId(),
                        tank.getX() + smallestX,
                        tank.getY(),
                        tank.getAngle());

            } else {
                runGameView.setDrawableEntityLocationAndAngle(
                        tank.getId(),
                        tank.getX() - smallestX,
                        tank.getY(),
                        tank.getAngle());
            }
        } else {
            if (up) {
                runGameView.setDrawableEntityLocationAndAngle(
                        tank.getId(),
                        tank.getX(),
                        tank.getY() + smallestY,
                        tank.getAngle());
            } else  {
                runGameView.setDrawableEntityLocationAndAngle(
                        tank.getId(),
                        tank.getX(),
                        tank.getY() - smallestY,
                        tank.getAngle());
            }
        }
    }
    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
