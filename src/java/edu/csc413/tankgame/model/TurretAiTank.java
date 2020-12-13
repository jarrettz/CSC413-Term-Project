package edu.csc413.tankgame.model;

public class TurretAiTank extends Tank {

    public TurretAiTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameState gameState) {
        //super.move(gameState);

        Entity playerTank = gameState.getEntity(GameState.PLAYER_TANK_ID);
        double dx = playerTank.getX() - getX();
        double dy = playerTank.getY() - getY();
        double angleToPlayer = Math.atan2(dy, dx);
        double angleDifference = getAngle() - angleToPlayer;
        angleDifference -= Math.floor(angleDifference / Math.toRadians(360.0) + 0.5) * Math.toRadians(360.0);

        if (angleDifference < -TURN_SPEED) {
            turnRight();
        } else if (angleDifference > TURN_SPEED) {
            turnLeft();
        }

        shoot(gameState);
    }

}
