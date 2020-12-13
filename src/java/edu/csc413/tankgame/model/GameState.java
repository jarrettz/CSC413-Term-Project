package edu.csc413.tankgame.model;

import edu.csc413.tankgame.view.RunGameView;

import java.util.ArrayList;
import java.util.List;

/**
 * GameState represents the state of the game "world." The GameState object tracks all of the moving entities like tanks
 * and shells, and provides the controller of the program (i.e. the GameDriver) access to whatever information it needs
 * to run the game. Essentially, GameState is the "data context" needed for the rest of the program.
 */
public class GameState {
    public static final double TANK_X_LOWER_BOUND = 30.0;
    public static final double TANK_X_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.width - 100.0;
    public static final double TANK_Y_LOWER_BOUND = 30.0;
    public static final double TANK_Y_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.height - 120.0;

    public static final double SHELL_X_LOWER_BOUND = -10.0;
    public static final double SHELL_X_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.width;
    public static final double SHELL_Y_LOWER_BOUND = -10.0;
    public static final double SHELL_Y_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.height;

    public static final String PLAYER_TANK_ID = "player-tank";
    public static final String AI_TANK_ID = "ai-tank";
    // TODO: Feel free to add more tank IDs if you want to support multiple AI tanks! Just make sure they're unique.

    // TODO: Implement.
    // There's a lot of information the GameState will need to store to provide contextual information. Add whatever
    // instance variables, constructors, and methods are needed.

    private final List<Entity> entities = new ArrayList<>();

    private final List<Entity> newShells = new ArrayList<>();

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getNewShells() {
        return newShells;
    }

    public void addNewShell(Entity entity) {
        newShells.add(entity);
    }

    private final List<Entity> removableShells = new ArrayList<>();

    public void addRemovableShell(Entity entity) {
        removableShells.add(entity);
    }

    public void removeShells() {
        entities.removeAll(removableShells);
        removableShells.clear();
    }

    /*
    //Trying this out
    private final List<String> removableShells = new ArrayList<>();

    public void addRemovableShell(String id) {
        removableShells.add(id);
    }

    public List<String> getRemovableShells() {
        return removableShells;
    }

    public void removeShells() {
        for (Entity checkEntity: entities) {
            for (String removableShell: removableShells) {
                if (removableShell == checkEntity.getId()) {
                    removeEntity(checkEntity);
                }
            }
        }
    }
 */

    public Entity getEntity(String id) {
        for (Entity findEntity: entities) {
            if (findEntity.getId() == id) {
                return findEntity;
            }
        }
        return null;
    }

    public double getShellXUpperBound() {
        return SHELL_X_UPPER_BOUND;
    }

    public double getShellYUpperBound() {
        return SHELL_Y_UPPER_BOUND;
    }

    public double getShellXLowerBound() {
        return SHELL_X_LOWER_BOUND;
    }

    public double getShellYLowerBound() {
        return SHELL_Y_LOWER_BOUND;
    }


    private boolean isUpPressed;
    private boolean isDownPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isShootPressed;

    public void upPressed(Boolean bool) {
        isUpPressed = bool;
    }

    public void downPressed(Boolean bool) {
        isDownPressed = bool;
    }

    public void leftPressed(Boolean bool) {
        isLeftPressed = bool;
    }

    public void rightPressed(Boolean bool) {
        isRightPressed = bool;
    }

    public void shootPressed(Boolean bool) {
        isShootPressed = bool;
    }


    public boolean isUpPressed() {
        return isUpPressed;
    }

    public boolean isDownPressed() {
        return isDownPressed;
    }

    public boolean isLeftPressed() {
        return isLeftPressed;
    }

    public boolean isRightPressed() {
        return isRightPressed;
    }

    public boolean isShootPressed() {
        return isShootPressed;
    }

}
