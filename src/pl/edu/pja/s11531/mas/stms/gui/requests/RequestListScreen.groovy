package pl.edu.pja.s11531.mas.stms.gui.requests

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen

/**
 * Created by kris on 2/4/17.
 */
class RequestListScreen extends NamedScreen {
    MainWindow window

    RequestListScreen(MainWindow window) {
        super()
        this.window = window
    }

    @Override
    void buildGUI(SwingBuilder builder) {

    }
}
