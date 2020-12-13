package edu.csc413.tankgame.model;

public class PlayerTank extends Tank {

    public PlayerTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }


    @Override
    public void move(GameState gameState) {
        if (gameState.isUpPressed()) {
            moveForward();
        }
        if (gameState.isDownPressed()) {
            moveBackward();
        }
        if (gameState.isRightPressed()) {
            turnRight();
        }
        if (gameState.isLeftPressed()) {
            turnLeft();
        }
        if (gameState.isShootPressed()) {
            shoot(gameState);
        }
    }
}
