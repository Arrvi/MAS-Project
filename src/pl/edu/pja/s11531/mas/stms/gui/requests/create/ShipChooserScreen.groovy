package pl.edu.pja.s11531.mas.stms.gui.requests.create

import groovy.beans.Bindable
import groovy.swing.SwingBuilder
import pl.edu.pja.s11531.mas.stms.gui.MainWindow
import pl.edu.pja.s11531.mas.stms.gui.NamedScreen
import pl.edu.pja.s11531.mas.stms.gui.util.LinkedObjectDecorator
import pl.edu.pja.s11531.mas.stms.model.Spaceship
import pl.edu.pja.s11531.mas.stms.model.SpaceshipType
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.swing.*
import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class ShipChooserScreen extends NamedScreen {
    private JPanel shipsPanel
    private JList availShipsList

    ShipModel shipModel = new ShipModel()

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
                        availShipsList = builder.list(id: 'availShips', items: getDecoratedSpaceships())
                        availShipsList.addListSelectionListener({
                            if (availShipsList.selectedIndex >= 0)
                                shipModel.ship = availShipsList.selectedValue?.object
                        })
                    }
                    hbox {
                        panel {
                            gridLayout(cols: 2, rows: 0)
                            label("Type")
                            JComboBox<LinkedObjectDecorator> shipTypeCombo
                            shipTypeCombo = comboBox(id: 'shipType', items: getDecoratedShipTypes(), selectedItem: bind(target: shipModel, 'type', mutual: true,
                                    converter: { it?.object }, reverseConverter: { SpaceshipType type ->
                                (0..shipTypeCombo.itemCount).collect { shipTypeCombo.getItemAt(it) }.find {
                                    it?.object == type
                                }
                            }))
                            label("Ship name")
                            textField(id: 'shipName', text: builder.bind(target: shipModel, 'name', mutual: true))
//                            label("Cargo")
//                            textArea(id: 'cargo', text: bind(target: shipModel, 'cargo', mutual: true))
                        }
                        panel {
                            gridLayout(cols: 2, rows: 0)
                            label("Mass")
                            panel {
                                textField(id: 'shipMass', text: bind(target: shipModel, 'mass', mutual: true))
                                label("+")
                                textField(id: 'additionalMass', text: bind(target: shipModel, 'additionalMass', mutual: true))
                            }
                            label("Captain")
                            textField(id: 'shipCaptain', text: bind(target: shipModel, 'captain', mutual: true))
                            label("Owner")
                            textField(id: 'shipOwner', text: bind(target: shipModel, 'owner', mutual: true))
                            button(text: 'Clear', actionPerformed: { shipModel.reset() })
                            button(text: 'Create ship', actionPerformed: { createShip() })
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

    private void refreshData() {
        availShipsList.listData = getDecoratedSpaceships().toArray()
    }

    private void createShip() {
        try {
            assert shipModel.type != null
            assert shipModel.name
            assert shipModel.captain
            assert shipModel.owner
            assert Double.parseDouble(shipModel.mass) > 0
        } catch (AssertionError | NumberFormatException error) {
            JOptionPane.showMessageDialog(
                    window.mainFrame,
                    error.getMessage(),
                    "Validation error",
                    JOptionPane.WARNING_MESSAGE)
            return;
        }
        new Spaceship(
                name: shipModel.name,
                type: shipModel.type,
                currentCaptain: shipModel.captain,
                currentOwner: shipModel.owner,
                mass: Double.parseDouble(shipModel.mass))
        refreshData()
    }

    private void addShip() {
        shipModel.name = "Add ship"
    }

    private static def getDecoratedShipTypes() {
        [null] + LinkedObject.getExtent(SpaceshipType.class).collect { new LinkedObjectDecorator(it) }
    }

    private static def getDecoratedSpaceships() {
        LinkedObject.getExtent(Spaceship.class).collect { new LinkedObjectDecorator(it) }
    }

    private static class ShipModel {
        @Bindable
        SpaceshipType type
        @Bindable
        String name
        @Bindable
        String[] cargo
        @Bindable
        String mass
        @Bindable
        String additionalMass
        @Bindable
        String captain
        @Bindable
        String owner

        def setShip(Spaceship ship) {
            if (ship == null) {
                reset()
                return
            }
            setType ship.type
            setName ship.name
            setCargo new String[0]
            setMass Double.toString(ship.mass)
            setAdditionalMass "0"
            setCaptain ship.currentCaptain
            setOwner ship.currentOwner
        }

        def reset() {
            setType null
            setName null
            setCargo new String[0]
            setMass "0"
            setAdditionalMass "0"
            setCaptain null
            setOwner null
        }
    }
}
