package edu.csc413.tankgame.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        if(actionCommand.equals(StartMenuView.START_BUTTON_ACTION_COMMAND)) {
            System.out.println("Start button pressed.");
        } else if (actionCommand.equals(StartMenuView.EXIT_BUTTON_ACTION_COMMAND)) {
            System.out.println("Exit button pressed.");
        }
    }
}