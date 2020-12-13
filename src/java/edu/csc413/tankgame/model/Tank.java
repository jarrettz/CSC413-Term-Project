package edu.csc413.tankgame.model;

/**
 * Model class representing a tank in the game. A tank has a position and an angle. It has a movement speed and a turn
 * speed, both represented below as constants.
 */
// TODO: Notice that Tank has a lot in common with Shell. For full credit, you will need to find a way to share code
// between the two classes so that the logic for e.g. moveForward, etc. are not duplicated.
public abstract class Tank extends Entity{
    public static final double MOVEMENT_SPEED = 2.0;
    public static final double TURN_SPEED = Math.toRadians(3.0);

    public Tank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    protected void shoot(GameState gameState) {
        Shell shell = new Shell(getShellX(), getShellY(), getAngle());
        gameState.addNewShell(shell);
    }

    // The following methods will be useful for determining where a shell should be spawned when it
    // is created by this tank. It needs a slight offset so it appears from the front of the tank,
    // even if the tank is rotated. The shell should have the same angle as the tank.

    private double getShellX() {
        return getX() + 30.0 * (Math.cos(getAngle()) + 0.5);
    }

    private double getShellY() {
        return getY() + 30.0 * (Math.sin(getAngle()) + 0.5);
    }

    @Override
    protected void moveForward() {
        setX(getX()+MOVEMENT_SPEED * Math.cos(getAngle()));
        setY(getY()+MOVEMENT_SPEED * Math.sin(getAngle()));
        //x += MOVEMENT_SPEED * Math.cos(getAngle());
        //y += MOVEMENT_SPEED * Math.sin(getAngle());
    }

    @Override
    protected void moveBackward() {
        setX(getX()-MOVEMENT_SPEED * Math.cos(getAngle()));
        setY(getY()-MOVEMENT_SPEED * Math.sin(getAngle()));
        //x -= MOVEMENT_SPEED * Math.cos(getAngle());
        //y -= MOVEMENT_SPEED * Math.sin(getAngle());
    }

    @Override
    protected void turnLeft() {
        setAngle(getAngle() - TURN_SPEED);
        //angle -= TURN_SPEED;
    }

    @Override
    protected void turnRight() {
        setAngle(getAngle() + TURN_SPEED);
        //angle += TURN_SPEED;
    }
}
