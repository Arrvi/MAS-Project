package pl.edu.pja.s11531.mas.stms.gui.requests.create

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.MapPanel
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen

import java.awt.*
import java.awt.event.ActionEvent

/**
 * Created by kris on 2/4/17.
 */
class WaypointChooserScreen extends NamedScreen {
    MainWindow window

    WaypointChooserScreen(MainWindow window) {
        super(new BorderLayout())
        this.window = window
    }

    @Override
    @SuppressWarnings("GroovyAssignabilityCheck")
    void buildGUI(SwingBuilder builder) {
        window.builder.edt {
            this.add panel(preferredSize: [350, 0]) {
                vbox {
                    label("Start location")
                    label("End location")
                    vglue()
                    hbox {
                        button(text: "Back", actionPerformed: { ActionEvent e -> window.showScreen(window.menuScreen) })
                        button(text: "Continue", actionPerformed: { ActionEvent e -> window.showScreen() })
                    }
                }
            }, BorderLayout.EAST

            this.add panel(background: Color.BLACK, foreground: Color.WHITE, new MapPanel()), BorderLayout.CENTER
        }
    }
}
