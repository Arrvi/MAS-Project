package pl.edu.pja.s11531.mas.stms.gui

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.requests.RequestListScreen
import pl.edu.pja.s11531.mas.stms.gui.requests.create.RequestSummaryScreen
import pl.edu.pja.s11531.mas.stms.gui.requests.create.ShipChooserScreen
import pl.edu.pja.s11531.mas.stms.gui.requests.create.WaypointChooserScreen
import pl.edu.pja.s11531.mas.stms.persistence.PersistenceManager

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
    WaypointChooserScreen waypointChooser
    ShipChooserScreen shipChooser
    RequestSummaryScreen requestSummary
    RequestListScreen requestList

    MainWindow() {
        PersistenceManager.loadConfig()
        buildGUI()
    }

    void buildGUI() {
        builder.edt {
            lookAndFeel UIManager.getSystemLookAndFeelClassName()

            def screens = [
                    menuScreen = new MenuScreen(this),
                    waypointChooser = new WaypointChooserScreen(this),
                    shipChooser = new ShipChooserScreen(this),
                    requestSummary = new RequestSummaryScreen(this),
                    requestList = new RequestListScreen(this)
            ]

            screens*.buildGUI(builder)

            mainFrame = frame(
                    title: 'Space Traffic Management System',
                    size: [1000, 600],
                    locationRelativeTo: null,
                    show: true,
                    defaultCloseOperation: WindowConstants.DISPOSE_ON_CLOSE) {
                cards = cardLayout()

                screens.each { builder.panel(constraints: it.screenName, it) }
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
