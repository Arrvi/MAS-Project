package pl.edu.pja.s11531.mas.stms.gui.requests

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen
import pl.edu.pja.s11531.mas.stms.gui.util.LinkedObjectDecorator
import pl.edu.pja.s11531.mas.stms.model.WarpRequest
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.swing.JList
import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class RequestListScreen extends NamedScreen {
    JList shipList

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

            this.add builder.scrollPane(preferredSize: [300, 0]) {
                def requestList = builder.list(items: getDecoratedRequests())
                requestList.addListSelectionListener({
                    shipList.listData = getShipsForRequest(requestList.selectedValue?.object)
                })
            }, BorderLayout.WEST
            this.add builder.scrollPane(preferredSize: [300, 0]) {
                shipList = builder.list()
            }, BorderLayout.EAST

            this.add builder.panel {
                builder.button(text: "Back", actionPerformed: { window.showScreen(window.menuScreen) })
            }, BorderLayout.CENTER
        }
    }

    static Object[] getShipsForRequest(WarpRequest request) {
        if (request == null) {
            println "Null request"
            return new LinkedObjectDecorator[0]
        }
        return request.ships.collect { new LinkedObjectDecorator(it) }.toArray()
    }

    def getDecoratedRequests() {
        LinkedObject.getExtent(WarpRequest.class).collect { new WarpRequestDecorator(it) }
    }

    static class WarpRequestDecorator {
        WarpRequest object

        WarpRequestDecorator(WarpRequest object) {
            this.object = object
        }

        @Override
        String toString() {
            return "Request ${object.gates?.first()?.starSystem?.name} -> ${object.gates?.last()?.starSystem.name}"
        }
    }
}
