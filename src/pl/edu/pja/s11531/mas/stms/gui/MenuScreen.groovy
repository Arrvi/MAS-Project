package pl.edu.pja.s11531.mas.stms.gui

import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent

/**
 * Created by kris on 2/3/17.
 */
class MenuScreen extends NamedScreen {
    MainWindow window

    MenuScreen(MainWindow window) {
        super(new BorderLayout())
        this.window = window
    }

    @Override
    @SuppressWarnings("GroovyAssignabilityCheck")
    void buildGUI(SwingBuilder builder) {
        builder.edt {
            this.add panel {
                label "<html><big>Action hub</big>"
            }, BorderLayout.NORTH

            this.add panel(preferredSize: [350, 0]) {
                flowLayout()

                navButton("New warp request", window.waypointChooser)
                navButton("Warp requests", window.requestList)
                separator()
                navButton("Gate management")
                navButton("Messages")
                separator()
                navButton("Exit", { window.mainFrame.dispose() })
            }, BorderLayout.WEST

            this.add panel(background: Color.BLACK, foreground: Color.WHITE, new MapPanel()), BorderLayout.CENTER
        }
    }

    def navButton(String text) {
        navButton(text, null as Closure)
    }

    def navButton(String text, NamedScreen screen) {
        navButton(text, { window.showScreen(screen) })
    }

    def navButton(String text, Closure action) {
        window.builder.button(
                text: text,
                preferredSize: [300, 60],
                actionPerformed: { ActionEvent e -> if (action) action(e); },
                enabled: action != null
        )
    }

    def separator() {
        window.builder.separator(orientation: SwingConstants.HORIZONTAL, preferredSize: [300, 3])
    }
}
