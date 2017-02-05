package pl.edu.pja.s11531.mas.stms.gui.requests.create

import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen
import pl.edu.pja.s11531.mas.stms.model.Spaceship
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.swing.*
import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class ShipChooserScreen extends NamedScreen {
    private JPanel shipsPanel

    ShipChooserScreen(MainWindow window) {
        super(new BorderLayout())
        this.window = window
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Override
    void buildGUI(SwingBuilder builder) {
        builder.edt {
            this.add panel {
                label "<html><big>Select ships</big>"
            }, BorderLayout.NORTH

            this.add shipsPanel = panel {
                vbox {
                    label("Selected ships")
                    scrollPane {
                        builder.list()
                    }
                }
            }, BorderLayout.WEST

            this.add builder.panel {
                vbox {
                    label("Available ships")
                    scrollPane {
                        builder.list(items: LinkedObject.getExtent(Spaceship.class))
                    }
                    hbox {
                        vbox {
                            hbox {
                                label("Ship name")
                                textField(id: 'shipName')
                            }
                            hbox {
                                label("Cargo")
                                textArea(id: 'cargo')
                            }
                        }
                        vbox {
                            hbox {
                                label("Mass")
                                textField(id: 'shipMass')
                                label("+")
                                textField(id: 'additionalMass')
                            }
                            hbox {
                                label("Captain")
                                textField(id: 'captain')
                            }
                            hbox {
                                label("Owner")
                                textField(id: 'owner')
                            }
                        }
                    }
                    hbox {
                        builder.button(text: "Back", actionPerformed: { window.showScreen(window.waypointChooser) })
                        builder.button(text: "Continue", actionPerformed: { window.showScreen(window.requestSummary) })
                    }
                }
            }, BorderLayout.CENTER
        }
    }

    private class ShipDetailsPanel extends JPanel {

    }
}
