package pl.edu.pja.s11531.mas.stms.gui.requests

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen

import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class RequestListScreen extends NamedScreen {
    RequestListScreen(MainWindow window) {
        super(new BorderLayout())
        this.window = window
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Override
    void buildGUI(SwingBuilder builder) {
        builder.edt {
            this.add panel {
                label "<html><big>Recent requests</big>"
            }, BorderLayout.NORTH

            this.add builder.panel {
                builder.button(text: "Back", actionPerformed: { window.showScreen(window.menuScreen) })
            }, BorderLayout.CENTER
        }
    }
}
