package ru.landgrafhomyak.itmo.web.svg_generator

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
    fun check(r: Double, x: Double, y: Double): Boolean {
        when {
            x == 0.0 && y == 0.0 -> return this.topRight != null || this.bottomRight != null || this.bottomLeft != null || this.topLeft != null
            x == 0.0 && y > 0.0 -> return (this.topLeft != null || this.topRight != null) && (if ((this.topLeft?.vSize == true) || (this.topRight?.vSize == true)) y <= r else y <= r / 2)
            x == 0.0 && y < 0.0 -> return (this.bottomLeft != null || this.bottomRight != null) && (if ((this.bottomLeft?.vSize == true) || (this.bottomRight?.vSize == true)) y >= -r else y >= -r / 2)
            x > 0.0 && y == 0.0 -> return (this.bottomRight != null || this.topRight != null) && (if ((this.bottomRight?.vSize == true) || (this.topRight?.vSize == true)) x <= r else x <= r / 2)
            x < 0.0 && y == 0.0 -> return (this.topLeft != null || this.bottomLeft != null) && (if ((this.topLeft?.vSize == true) || (this.bottomLeft?.vSize == true)) x >= -r else x >= -r / 2)
        }
        when {
            x > 0.0 && y > 0.0 -> return this.topRight.checkTopRight(r, x, y)
            x < 0.0 && y > 0.0 -> return this.topRight.checkTopLeft(r, x, y)
            x > 0.0 && y < 0.0 -> return this.topRight.checkBottomRight(r, x, y)
            x < 0.0 && y < 0.0 -> return this.topRight.checkBottomLeft(r, x, y)
        }
        return false
    }

    fun draw(pen: Pen) {
        this.topRight.drawTopRight(pen)
        this.bottomRight.drawBottomRight(pen)
        this.bottomLeft.drawBottomLeft(pen)
        this.topLeft.drawBottomLeft(pen)
    }
}