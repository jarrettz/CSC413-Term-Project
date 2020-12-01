package edu.csc413.tankgame.model;

public class PlayerTank extends Tank {

    public PlayerTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameState gameState) {

    }

    /*
    @Override
    public void move(GameState gameState) {
        if (w is pressed) {
            moveForward();
        }
        if (s is pressed) {
            moveBackward();
        }
        if (d is pressed) {
            turnRight();
        }
        if (a is pressed) {
            turnLeft();
        }
    }
     */

}
