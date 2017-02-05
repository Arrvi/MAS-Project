package pl.edu.pja.s11531.mas.stms.gui

import pl.edu.pja.s11531.mas.stms.datatypes.Offset
import pl.edu.pja.s11531.mas.stms.datatypes.Position
import pl.edu.pja.s11531.mas.stms.model.StarSystem
import pl.edu.pja.s11531.mas.stms.model.WarpGate
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.swing.*
import java.awt.*
import java.time.LocalDateTime

/**
 * Created by kris on 2/4/17.
 */
class MapPanel extends JPanel {
    static final int STAR_SYSTEM_SIZE = 10
    static final int MARGIN = 30

    private BigDecimal[] _universeBounds
    private Position[] _systemPositions
    private StarSystem[] _systems
    private Position[][] _tunnels

    Color systemColor = null
    Color labelColor = null
    Color tunnelColor = Color.blue

    Position[] getSystemPositions() {
        if (!_systemPositions) {
            _systems = LinkedObject.getExtent(StarSystem.class)
            def time = LocalDateTime.now()
            _systemPositions = _systems*.trajectory*.calculatePosition(time)
        }
        return _systemPositions
    }

    BigDecimal[] getUniverseBounds() {
        if (!_universeBounds) {
            def systems = systemPositions
            def minX = systems.collect { Position pos -> pos.x }.min()
            def maxX = systems.collect { Position pos -> pos.x }.max()
            def minY = systems.collect { Position pos -> pos.y }.min()
            def maxY = systems.collect { Position pos -> pos.y }.max()
            _universeBounds = [minX, maxX, minY, maxY]
        }
        return _universeBounds
    }

    BigDecimal[] getUniverseSize() {
        return [universeBounds[1] - universeBounds[0], universeBounds[3] - universeBounds[2]]
    }

    Position[][] getTunnels() {
        if (!_tunnels) {
            def gates = LinkedObject.getExtent(WarpGate.class)
            Set<Set<StarSystem>> systemPairs = []
            gates.each { systemPairs << new HashSet<>([it.starSystem, it.gate.starSystem]) }
            def time = LocalDateTime.now()
            println systemPairs
            _tunnels = systemPairs
                    .collect { Set it -> it.toArray(new StarSystem[2]) }
                    .collect { StarSystem[] it -> it*.trajectory*.calculatePosition(time).toArray() } as Position[][]
        }
        return _tunnels
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g)

        (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g.color = foreground
        g.drawString "Map", 10, 10 + Math.round(g.getFontMetrics().getLineMetrics("Map", g).height)

        g.color = tunnelColor ?: foreground
        tunnels.collect { it.collect { transformPosition(it) } }.each {
            g.drawLine(it[0][0], it[0][1], it[1][0], it[1][1])
        }

        systemPositions
                .collect { transformPosition it }
                .collect { [(it[0] - STAR_SYSTEM_SIZE / 2).intValue(), (it[1] - STAR_SYSTEM_SIZE / 2).intValue()] }
                .eachWithIndex { it, i ->
            g.color = systemColor ?: foreground
            g.drawArc(it[0], it[1], STAR_SYSTEM_SIZE, STAR_SYSTEM_SIZE, 0, 360)
            g.color = labelColor ?: foreground
            g.drawString(_systems[i].name, it[0] + 15, it[1])
        }

    }

    int[] transformPosition(Offset pos) {
        def x = (width - MARGIN * 2) * (pos.x - universeBounds[0]) / universeSize[0] + MARGIN
        def y = (height - MARGIN * 2) * (pos.y - universeBounds[2]) / universeSize[1] + MARGIN
        return [x.intValue(), y.intValue()]
    }
}
