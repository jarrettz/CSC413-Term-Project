package edu.csc413.tankgame.model;

public class DumbAiTank extends Tank {

    public DumbAiTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameState gameState) {
        moveForward();
        turnRight();

        shoot(gameState);
    }
}
