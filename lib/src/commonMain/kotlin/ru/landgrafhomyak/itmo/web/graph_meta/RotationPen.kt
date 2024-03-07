package ru.landgrafhomyak.itmo.web.graph_meta

class RotationPen(
    private val original: Pen,
    private val angle: Angle
) : Pen {
    enum class Angle {
        ANGLE_90,
        ANGLE_180,
        ANGLE_270
    }

    override fun moveTo(x: Pen.Coordinate, y: Pen.Coordinate) {
        when (this.angle) {
            Angle.ANGLE_90 -> this.original.moveTo(y, -x)
            Angle.ANGLE_180 -> this.original.moveTo(-x, -y)
            Angle.ANGLE_270 -> this.original.moveTo(-y, x)
        }
    }

    override fun lineTo(x: Pen.Coordinate, y: Pen.Coordinate) {
        when (this.angle) {
            Angle.ANGLE_90 -> this.original.lineTo(y, -x)
            Angle.ANGLE_180 -> this.original.lineTo(-x, -y)
            Angle.ANGLE_270 -> this.original.lineTo(-y, x)
        }
    }

    override fun arcTo(
        rx: Pen.Coordinate, ry: Pen.Coordinate,
        rotation: Int, outerArc: Boolean,
        toX: Pen.Coordinate, toY: Pen.Coordinate
    ) {
        when (this.angle) {
            Angle.ANGLE_90 -> this.original.arcTo(ry, rx, rotation, outerArc, toY, -toX)
            Angle.ANGLE_180 -> this.original.arcTo(rx, ry, rotation, outerArc, -toX, -toY)
            Angle.ANGLE_270 -> this.original.arcTo(ry, rx, rotation, outerArc, -toY, toX)
        }
    }

    override fun closeLine() {
        this.original.closeLine()
    }
}