package pl.edu.pja.s11531.mas.stms.gui

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent

/**
 * Created by kris on 2/3/17.
 */
class MenuScreen extends NamedScreen {
    final String screenName = this.class.name

    MainWindow window

    MenuScreen(MainWindow window) {
        super(new BorderLayout())
        this.window = window
        buildGUI()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void buildGUI() {
        window.builder.edt {
            this.add panel(preferredSize: [350, 0]) {
                flowLayout()

                navButton("New warp request") { window.showScreen(window.waypointChooser) }
                navButton("Warp requests")
                separator()
                navButton("Gate management")
                navButton("Messages")
                separator()
                navButton("Exit")
            }, BorderLayout.WEST

            this.add panel(background: Color.black), BorderLayout.CENTER
        }
    }

    def navButton(String text, Closure action = null) {
        window.builder.button(text: text, preferredSize: [300, 60], actionPerformed: { ActionEvent e -> if (action) action(e); })
    }

    def separator() {
        window.builder.separator(orientation: SwingConstants.HORIZONTAL, preferredSize: [300, 3])
    }
}
