package pl.edu.pja.s11531.mas.stms.gui.requests.create

import groovy.beans.Bindable
import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.MapPanel
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen
import pl.edu.pja.s11531.mas.stms.gui.util.LinkedObjectDecorator
import pl.edu.pja.s11531.mas.stms.model.StarSystem
import pl.edu.pja.s11531.mas.stms.model.WarpGate
import pl.edu.pja.s11531.mas.stms.model.WarpRequest
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject
import pl.edu.pja.s11531.mas.stms.persistence.PersistenceManager

import javax.swing.*
import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class RequestSummaryScreen extends NamedScreen {
    @Bindable
    int totalCost
    @Bindable
    int shipCount
    @Bindable
    double totalMass
    @Bindable
    boolean saved

    java.util.List<WarpGate> route

    JList routeList

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
                        label("Number of ships:")
                        label(text: bind(source: this, 'shipCount'))
                        label("Total mass: ")
                        label(text: bind(source: this, 'totalMass'))
                        label("Total cost: ")
                        label(text: bind(source: this, 'totalCost'))
                    }
                    label("Route:")
                    scrollPane(preferredSize: [0, 300]) {
                        routeList = builder.list()
                    }
                    builder.button(text: "Save request", enabled: bind(source: this, 'saved', converter: {
                        !it
                    }), actionPerformed: {
                        saveRequest()
                    })
                    separator()
                    hbox {
                        builder.button(text: "Back", actionPerformed: { window.showScreen(window.shipChooser) })
                        builder.button(text: "Continue", enabled: bind(source: this, 'saved'), actionPerformed: {
                            window.showScreen(window.menuScreen)
                        })
                    }
                }
            }, BorderLayout.EAST

            this.add builder.panel(background: Color.black, foreground: Color.white, new MapPanel()), BorderLayout.CENTER
        }
    }

    void saveRequest() {
        try {
            def request = new WarpRequest(status: WarpRequest.Status.RECEIVED, gates: route)
            window.shipChooser.selectedShips*.link(request)
            PersistenceManager.saveDatabase()
            setSaved true
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    void refreshData() {
        if (route) {
            routeList.listData = (route.collect {
                new LinkedObjectDecorator(it.starSystem)
            } + [new LinkedObjectDecorator(window.waypointChooser.model.endSystem)]).toArray()
        } else {
            routeList.listData = ['- no path -']
        }
    }

    void calculateRequest() {
        def systems = LinkedObject.getExtent(StarSystem.class)
        route = dijkstra(systems, window.waypointChooser.model.startSystem, window.waypointChooser.model.endSystem)
        setShipCount window.shipChooser.selectedShips.size()
        setTotalMass window.shipChooser.selectedShips.collect { it.mass }.sum() as double
        if (route) {
            setTotalCost(route.collect { it.pricePerTon * totalMass }.sum() as int)
        }
        refreshData()
    }

    static java.util.List<WarpGate> dijkstra(Collection<StarSystem> systems, StarSystem source, StarSystem target) {
        def dist = [:]
        def prev = [:]
        Set unvisited = []

        systems.each {
            dist[it] = Integer.MAX_VALUE
            prev[it] = null
            unvisited << it
        }

        dist[source] = 0

        while (!unvisited.empty) {
            StarSystem node = dist.findAll { unvisited.contains(it.key) }.min { it.value }.key as StarSystem
            unvisited.remove(node)

            if (node == target) {
                java.util.List<WarpGate> route = new LinkedList<>()
                while (prev[node] != null) {
                    def prevNode = prev[node] as StarSystem
                    route.addFirst(prevNode.gates.find { it.gate.starSystem == node })
                    node = prev[node] as StarSystem
                }
                return route
            }

            def neighbours = node.gates*.gate?.starSystem?.findAll { unvisited.contains(it) } ?: []
            for (def n in neighbours) {
                def currDist = dist[node] + node.gates.find { it?.gate?.starSystem == n }.pricePerTon
                if (currDist < dist[n]) {
                    dist[n] = currDist
                    prev[n] = node
                }
            }
        }

        return null
    }
}
