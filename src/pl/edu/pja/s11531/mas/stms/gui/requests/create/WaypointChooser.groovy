package pl.edu.pja.s11531.mas.stms.gui.requests.create

import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen

import java.awt.*
import java.awt.event.ActionEvent

/**
 * Created by kris on 2/4/17.
 */
class WaypointChooser extends NamedScreen {
    final String screenName = this.class.name

    MainWindow window

    WaypointChooser(MainWindow window) {
        super(new BorderLayout())
        this.window = window
        buildGUI()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void buildGUI() {
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

            this.add panel(background: Color.BLACK) {
                label("MAP", foreground: Color.white)
            }, BorderLayout.CENTER
        }
    }
}
