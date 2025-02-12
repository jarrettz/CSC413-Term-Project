package edu.csc413.tankgame.model;

/**
 * Model class representing a shell that has been fired by a tank. A shell has a position and an angle, as well as a
 * speed. Shells by default should be unable to turn and only move forward.
 */
// TODO: Notice that Shell has a lot in common with Tank. For full credit, you will need to find a way to share code
// between the two classes so that the logic for e.g. moveForward, etc. are not duplicated.
public class Shell extends Entity {
    private static final String SHELL_ID_PREFIX = "shell-";
    // Adjusted from speed of 4.0
    private static final double MOVEMENT_SPEED = 1.0;

    private static long uniqueId = 0L;

    public Shell(double x, double y, double angle) {
        super(getUniqueId(), x, y, angle);
        //String uniqueId = getUniqueId();
        //super.updateId(getUniqueId());
    }

    private static String getUniqueId() {
        return SHELL_ID_PREFIX + uniqueId++;
    }

    @Override
    public void move(GameState gameState) {
        moveForward();
    }

    @Override
    protected void moveForward() {
        setX(getX()+MOVEMENT_SPEED * Math.cos(getAngle()));
        setY(getY()+MOVEMENT_SPEED * Math.sin(getAngle()));
        //x += MOVEMENT_SPEED * Math.cos(getAngle());
        //y += MOVEMENT_SPEED * Math.sin(getAngle());
    }

    @Override
    public double getXBound() {
        return getX() + 24;
    }

    @Override
    public double getYBound() {
        return getY() + 24;
    }

    // Only implement for homing shells
    @Override
    protected void moveBackward() {}
    @Override
    protected void turnLeft() {}
    @Override
    protected void turnRight() {}
}
