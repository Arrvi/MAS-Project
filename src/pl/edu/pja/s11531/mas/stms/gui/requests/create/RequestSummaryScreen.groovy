package pl.edu.pja.s11531.mas.stms.gui.requests.create

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.MapPanel
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen

import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class RequestSummaryScreen extends NamedScreen {
    RequestSummaryScreen(MainWindow window) {
        super(new BorderLayout())
        this.window = window
    }

    @Override
    void buildGUI(SwingBuilder builder) {
        builder.edt {
            this.add builder.panel {
                label "<html><big>Warp request summary</big>"
            }, BorderLayout.NORTH

            this.add builder.panel {
                vbox {
                    panel {
                        gridLayout(cols: 2, rows: 0)
                        label("Start system: ")
                        label(text: bind(source: window.waypointChooser.model, sourceProperty: 'start'))
                        label("End system: ")
                        label(text: bind(source: window.waypointChooser.model, sourceProperty: 'end'))
                    }
                    separator()
                    builder.button(text: "Back", actionPerformed: { window.showScreen(window.shipChooser) })
                    builder.button(text: "Continue", actionPerformed: { window.showScreen(window.menuScreen) })
                }
            }, BorderLayout.EAST

            this.add builder.panel(background: Color.black, foreground: Color.white, new MapPanel()), BorderLayout.CENTER
        }
    }
}
