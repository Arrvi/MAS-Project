package pl.edu.pja.s11531.mas.stms.gui

import javax.swing.*
import java.awt.*

/**
 * Created by kris on 2/4/17.
 */
class MapPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g)

        (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g.color = foreground
        g.drawString "Map", 10, 10 + Math.round(g.getFontMetrics().getLineMetrics("Map", g).height)

        g.color = Color.red
        for (def i = 0; i < 10; i++) {
            g.drawLine(Math.random() * width as int, Math.random() * height as int, Math.random() * width as int, Math.random() * height as int)
        }
    }
}
