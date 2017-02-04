package pl.edu.pja.s11531.mas.stms.gui.requests.create

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.MapPanel
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen

import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class WaypointChooserScreen extends NamedScreen {
    WaypointChooserScreen(MainWindow window) {
        super(new BorderLayout())
        this.window = window
    }

    @Override
    @SuppressWarnings("GroovyAssignabilityCheck")
    void buildGUI(SwingBuilder builder) {
        builder.edt {
            this.add panel {
                label "<html><big>Select waypoints</big>"
            }, BorderLayout.NORTH

            this.add panel(preferredSize: [350, 0]) {
                vbox {
                    label("Start location")
                    label("End location")
                    vglue()
                    hbox {
                        button(text: "Back", actionPerformed: { window.showScreen(window.menuScreen) })
                        button(text: "Continue", actionPerformed: { window.showScreen(window.shipChooser) })
                    }
                }
            }, BorderLayout.EAST

            this.add panel(background: Color.BLACK, foreground: Color.WHITE, new MapPanel()), BorderLayout.CENTER
        }
    }
}
