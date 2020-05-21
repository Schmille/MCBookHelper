package schmille.mcbookhelper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ActionEventAllocator implements ActionListener {

    private Consumer<ActionEvent> function;

    public ActionEventAllocator(Consumer<ActionEvent> function) {
        this.function = function;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        function.accept(actionEvent);
    }
}
