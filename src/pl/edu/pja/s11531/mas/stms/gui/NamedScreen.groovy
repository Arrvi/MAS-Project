package pl.edu.pja.s11531.mas.stms.gui

import groovy.swing.SwingBuilder
import groovy.transform.InheritConstructors

import javax.swing.*

/**
 * Created by kris on 2/4/17.
 */
@InheritConstructors
abstract class NamedScreen extends JPanel {
    final String screenName = this.class.name

    abstract void buildGUI(SwingBuilder builder)
}
