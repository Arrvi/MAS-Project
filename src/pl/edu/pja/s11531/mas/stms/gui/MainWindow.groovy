package pl.edu.pja.s11531.mas.stms.gui

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.requests.create.WaypointChooser

import javax.swing.*
import java.awt.*

/**
 * Created by kris on 2/3/17.
 */
class MainWindow {
    SwingBuilder builder = new SwingBuilder()
    JFrame mainFrame
    CardLayout cards

    MenuScreen menuScreen
    WaypointChooser waypointChooser

    MainWindow() {
        buildGUI()
    }

    void buildGUI() {
        builder.edt {
            lookAndFeel UIManager.getSystemLookAndFeelClassName()

            menuScreen = new MenuScreen(this)
            waypointChooser = new WaypointChooser(this)

            mainFrame = frame(
                    title: 'Space Traffic Management System',
                    size: [1000, 600],
                    locationRelativeTo: null,
                    show: true,
                    defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {
                cards = cardLayout()

                builder.panel(constraints: menuScreen.screenName, menuScreen)
                builder.panel(constraints: waypointChooser.screenName, waypointChooser)
            }
        }
    }

    void showScreen(NamedScreen screen) {
        showScreen(screen.screenName)
    }

    void showScreen(String screenName) {
        builder.edt {
            cards.show(mainFrame.contentPane, screenName)
        }
    }
}
