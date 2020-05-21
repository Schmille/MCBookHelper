package schmille.mcbookhelper;

import schmille.mcbookhelper.gui.MainGui;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        setLookAndFeel();
        MainGui gui = new MainGui(new Control());
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
    }
}
