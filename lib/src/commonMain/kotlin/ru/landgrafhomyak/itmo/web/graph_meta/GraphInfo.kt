package ru.landgrafhomyak.itmo.web.graph_meta

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
class GraphInfo(
    @Suppress("MemberVisibilityCanBePrivate")
    val topRight: QuartInfo?,
    @Suppress("MemberVisibilityCanBePrivate")
    val bottomRight: QuartInfo?,
    @Suppress("MemberVisibilityCanBePrivate")
    val bottomLeft: QuartInfo?,
    @Suppress("MemberVisibilityCanBePrivate")
    val topLeft: QuartInfo?,
) {
    fun check(x: Double, y: Double, r: Double): Boolean {
        when {
            x == 0.0 && y == 0.0 -> return this.topRight != null || this.bottomRight != null || this.bottomLeft != null || this.topLeft != null
            x == 0.0 && y > 0.0 -> return (this.topLeft != null || this.topRight != null) && (if ((this.topLeft?.vSize == true) || (this.topRight?.vSize == true)) y <= r else y <= r / 2)
            x == 0.0 && y < 0.0 -> return (this.bottomLeft != null || this.bottomRight != null) && (if ((this.bottomLeft?.vSize == true) || (this.bottomRight?.vSize == true)) y >= -r else y >= -r / 2)
            x > 0.0 && y == 0.0 -> return (this.bottomRight != null || this.topRight != null) && (if ((this.bottomRight?.vSize == true) || (this.topRight?.vSize == true)) x <= r else x <= r / 2)
            x < 0.0 && y == 0.0 -> return (this.topLeft != null || this.bottomLeft != null) && (if ((this.topLeft?.vSize == true) || (this.bottomLeft?.vSize == true)) x >= -r else x >= -r / 2)
        }
        when {
            x > 0.0 && y > 0.0 -> return this.topRight.checkTopRight(x, y, r)
            x < 0.0 && y > 0.0 -> return this.topRight.checkTopLeft(x, y, r)
            x > 0.0 && y < 0.0 -> return this.topRight.checkBottomRight(x, y, r)
            x < 0.0 && y < 0.0 -> return this.topRight.checkBottomLeft(x, y, r)
        }
        return false
    }

    fun draw(pen: Pen) {
        if (this.topLeft == null)
            pen.moveTo(Pen.Coordinate.ZERO, Pen.Coordinate.ZERO)
        else {
            if (this.topLeft.vSize)
                pen.moveTo(Pen.Coordinate.ZERO, Pen.Coordinate.POS_FULL)
            else
                pen.moveTo(Pen.Coordinate.ZERO, Pen.Coordinate.POS_HALF)
        }
        this.topRight.drawTopRight(pen)
        this.bottomRight.drawBottomRight(pen)
        this.bottomLeft.drawBottomLeft(pen)
        this.topLeft.drawTopLeft(pen)
        pen.closeLine()
    }
}