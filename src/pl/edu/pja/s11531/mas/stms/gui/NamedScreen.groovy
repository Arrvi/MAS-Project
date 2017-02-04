package pl.edu.pja.s11531.mas.stms.gui

import groovy.transform.InheritConstructors

import javax.swing.*
import javax.validation.constraints.NotNull

/**
 * Created by kris on 2/4/17.
 */
@InheritConstructors
abstract class NamedScreen extends JPanel {
    @NotNull
    abstract String getScreenName()
}
