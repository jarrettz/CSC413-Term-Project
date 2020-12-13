package edu.csc413.tankgame;

import edu.csc413.tankgame.model.GameState;

import edu.csc413.tankgame.model.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener {

    private final GameState gameState;

    public GameKeyListener(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void keyTyped(KeyEvent event) {
        // Useless
    }
    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            gameState.upPressed(true);
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            gameState.escPressed(true);
        } else if (keyCode == KeyEvent.VK_S) {
            gameState.downPressed(true);
        } else if (keyCode == KeyEvent.VK_A) {
            gameState.leftPressed(true);
        } else if (keyCode == KeyEvent.VK_D) {
            gameState.rightPressed(true);
        } else if (keyCode == KeyEvent.VK_SPACE) {
            gameState.shootPressed(true);
        }
    }
    @Override
    public void keyReleased(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            gameState.upPressed(false);
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
        } else if (keyCode == KeyEvent.VK_S) {
            gameState.downPressed(false);
        } else if (keyCode == KeyEvent.VK_A) {
            gameState.leftPressed(false);
        } else if (keyCode == KeyEvent.VK_D) {
            gameState.rightPressed(false);
        } else if (keyCode == KeyEvent.VK_SPACE) {
            gameState.shootPressed(false);
        }
    }
}
