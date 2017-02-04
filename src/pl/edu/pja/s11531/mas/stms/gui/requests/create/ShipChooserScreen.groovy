package pl.edu.pja.s11531.mas.stms.gui.requests.create

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen

import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class ShipChooserScreen extends NamedScreen {
    MainWindow window

    ShipChooserScreen(MainWindow window) {
        super(new BorderLayout())
        this.window = window
    }

    @Override
    void buildGUI(SwingBuilder builder) {

    }
}
