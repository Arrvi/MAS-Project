package pl.edu.pja.s11531.mas.stms.gui.requests.create

import groovy.beans.Bindable
import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.MapPanel
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen
import pl.edu.pja.s11531.mas.stms.model.StarSystem
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class WaypointChooserScreen extends NamedScreen {
    Waypoints model = new Waypoints()

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
                    hbox {
                        label("System name")
                        builder.comboBox(id: 'startSystem', items: getDecoratedStarSystems(),
                                selectedItem: bind(target: model, targetProperty: 'start'))
                    }
                    separator()
                    label("End location")
                    hbox {
                        label("System name")
                        builder.comboBox(id: 'endSystem', items: getDecoratedStarSystems(),
                                selectedItem: bind(target: model, targetProperty: 'end'))
                    }
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

    private static getDecoratedStarSystems() {
        LinkedObject.getExtent(StarSystem.class).collect { new StarSystemDecorator(it) }
    }

    static class Waypoints {
        @Bindable
        StarSystemDecorator start
        @Bindable
        StarSystemDecorator end

        StarSystem getStartSystem() {
            start.delegate
        }

        StarSystem getEndSystem() {
            end.delegate
        }
    }

    private static class StarSystemDecorator {
        StarSystem delegate

        StarSystemDecorator(StarSystem delegate) {
            this.delegate = delegate
        }

        @Override
        String toString() {
            return delegate.name
        }
    }
}
