package edu.csc413.tankgame.model;

public class Wall extends Entity {

    private static final String WALL_ID_PREFIX = "wall-";
    private static long uniqueId = 0L;

    public Wall(double x, double y, double angle) {
        super(getUniqueId(), x, y, angle);
    }

    private static String getUniqueId() {
        return WALL_ID_PREFIX + uniqueId++;
    }

    public double wallHP = 4;

    public void lostHP() {
        wallHP -= 1;
    }

    public boolean noHP() {
        if (wallHP > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public double getXBound() {
        return getX()+32;
    }

    @Override
    public double getYBound() {
        return getY()+32;
    }

    // Walls do not move and use override functions
    @Override
    public void move(GameState gameState) {}
    @Override
    protected void moveForward() {}
    @Override
    protected void moveBackward() {}
    @Override
    protected void turnLeft() {}
    @Override
    protected void turnRight() {}
}
