@file:Suppress("LiftReturnOrAssignment")

package ru.landgrafhomyak.itmo.web.graph_meta

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.jvm.JvmName
import kotlin.math.sqrt

@Suppress("ObjectPropertyName")
private val _zeroCoord get() = Pen.Coordinate.ZERO

@Suppress("NON_EXPORTABLE_TYPE")
@OptIn(ExperimentalJsExport::class)
@JsExport
sealed class QuartInfo(
    val hSize: Boolean,
    val vSize: Boolean
) {
    abstract fun checkTopRight(r: Double, x: Double, y: Double): Boolean

    @Suppress("FunctionName")
    protected fun _checkXYPositive(x: Double, y: Double) {
        if (x < 0 || y < 0)
            throw IllegalArgumentException("X and Y must be positive to be checked in top-right quart")
    }

    fun checkTopLeft(r: Double, x: Double, y: Double): Boolean {
        if (x > 0 || y < 0)
            throw IllegalArgumentException("X must be negative and Y must be positive to be checked in top-left quart")
        return this.checkTopRight(r, -x, y)
    }

    fun checkBottomRight(r: Double, x: Double, y: Double): Boolean {
        if (x > 0 || y < 0)
            throw IllegalArgumentException("X must be positive and Y must be negative to be checked in bottom-right quart")
        return this.checkTopRight(r, x, -y)
    }

    fun checkBottomLeft(r: Double, x: Double, y: Double): Boolean {
        if (x > 0 || y < 0)
            throw IllegalArgumentException("X and Y must be negative to be checked in bottom-left quart")
        return this.checkTopRight(r, -x, -y)
    }

    fun drawTopRight(pen: Pen) {
        this._draw(pen, false)
    }

    fun drawBottomRight(pen: Pen) {
        val rotatedPen = RotationPen(pen, RotationPen.Angle.ANGLE_90)
        this._draw(rotatedPen, true)

    }

    fun drawBottomLeft(pen: Pen) {
        val rotatedPen = RotationPen(pen, RotationPen.Angle.ANGLE_180)
        this._draw(rotatedPen, false)
    }


    fun drawTopLeft(pen: Pen) {
        val rotatedPen = RotationPen(pen, RotationPen.Angle.ANGLE_270)
        this._draw(rotatedPen, true)
    }

    @Suppress("FunctionName")
    protected fun _xPenCoord(invertWH: Boolean) = if (if (!invertWH) this.hSize else this.vSize) Pen.Coordinate.POS_FULL else Pen.Coordinate.POS_HALF

    @Suppress("FunctionName")
    protected fun _yPenCoord(invertWH: Boolean) = if (if (!invertWH) this.vSize else this.hSize) Pen.Coordinate.POS_FULL else Pen.Coordinate.POS_HALF

    @Suppress("FunctionName")
    protected abstract fun _draw(pen: Pen, invertWH: Boolean)


    class Rectangle(hSize: Boolean, vSize: Boolean) : QuartInfo(hSize, vSize) {
        override fun checkTopRight(r: Double, x: Double, y: Double): Boolean {
            this._checkXYPositive(x, y)
            if (x > (if (this.hSize) r else r / 2)) return false
            if (y > (if (this.vSize) r else r / 2)) return false
            return true
        }

        override fun _draw(pen: Pen, invertWH: Boolean) {
            pen.lineTo(_zeroCoord, this._yPenCoord(invertWH))
            pen.lineTo(this._xPenCoord(invertWH), this._yPenCoord(invertWH))
            pen.lineTo(this._xPenCoord(invertWH), _zeroCoord)
        }
    }

    class Triangle(hSize: Boolean, vSize: Boolean) : QuartInfo(hSize, vSize) {
        private val k: Double = when {
            this.hSize == this.vSize -> -1.0
            this.hSize && !this.vSize -> -0.5
            !this.hSize && this.vSize -> -2.0
            else -> throw RuntimeException("Unreachable")
        }

        override fun checkTopRight(r: Double, x: Double, y: Double): Boolean {
            this._checkXYPositive(x, y)
            val c = (if (this.vSize) r else r / 2)
            return y <= this.k * x + c
        }


        override fun _draw(pen: Pen, invertWH: Boolean) {
            pen.lineTo(_zeroCoord, this._yPenCoord(invertWH))
            pen.lineTo(this._xPenCoord(invertWH), _zeroCoord)
        }

    }

    class OuterArc(hSize: Boolean, vSize: Boolean) : QuartInfo(hSize, vSize) {
        override fun checkTopRight(r: Double, x: Double, y: Double): Boolean {
            this._checkXYPositive(x, y)
            val xx: Double = x / r
            return y <= r * ((if (this.vSize) 1.0 else 0.5) * sqrt(1 - (if (this.hSize) 1 else 4) * (xx * xx)))
        }

        override fun _draw(pen: Pen, invertWH: Boolean) {
            pen.lineTo(_zeroCoord, this._yPenCoord(invertWH))
            pen.arcTo(
                this._xPenCoord(invertWH).abs(), this._yPenCoord(invertWH).abs(),
                0, true,
                this._xPenCoord(invertWH), _zeroCoord
            )
        }
    }

    class InnerArc(hSize: Boolean, vSize: Boolean) : QuartInfo(hSize, vSize) {
        override fun checkTopRight(r: Double, x: Double, y: Double): Boolean {
            this._checkXYPositive(x, y)
            val xx: Double = if (this.hSize) 1 - x / r else 0.5 - x / r
            val xk: Double = sqrt(1 - (if (this.hSize) 1 else 4) * (xx * xx))
            return y <= r * (if (this.vSize) 1 - xk else 0.5 - 0.5 * xk)
        }

        override fun _draw(pen: Pen, invertWH: Boolean) {
            pen.lineTo(_zeroCoord, this._yPenCoord(invertWH))
            pen.arcTo(
                this._xPenCoord(invertWH).abs(), this._yPenCoord(invertWH).abs(),
                0, false,
                this._xPenCoord(invertWH), _zeroCoord
            )
        }
    }
}

@JvmName("checkTopRightNullable")
fun QuartInfo?.checkTopRight(r: Double, x: Double, y: Double): Boolean {
    if (this == null) return false
    else return this.checkTopRight(r, x, y)
}

@JvmName("checkTopLeftNullable")
fun QuartInfo?.checkTopLeft(r: Double, x: Double, y: Double): Boolean {
    if (this == null) return false
    else return this.checkTopLeft(r, x, y)
}

@JvmName("checkBottomRightNullable")
fun QuartInfo?.checkBottomRight(r: Double, x: Double, y: Double): Boolean {
    if (this == null) return false
    else return this.checkBottomRight(r, x, y)
}

@JvmName("checkBottomLeftNullable")
fun QuartInfo?.checkBottomLeft(r: Double, x: Double, y: Double): Boolean {
    if (this == null) return false
    else return this.checkBottomLeft(r, x, y)
}

@JvmName("drawTopRightNullable")
fun QuartInfo?.drawTopRight(pen: Pen) {
    if (this == null) pen.lineTo(_zeroCoord, _zeroCoord)
    else this.drawTopRight(pen)
}

@JvmName("drawTopLeftNullable")
fun QuartInfo?.drawTopLeft(pen: Pen) {
    if (this == null) pen.lineTo(_zeroCoord, _zeroCoord)
    else this.drawTopLeft(pen)
}

@JvmName("drawBottomRightNullable")
fun QuartInfo?.drawBottomRight(pen: Pen) {
    if (this == null) pen.lineTo(_zeroCoord, _zeroCoord)
    else this.drawBottomRight(pen)
}

@JvmName("drawBottomLeftNullable")
fun QuartInfo?.drawBottomLeft(pen: Pen) {
    if (this == null) pen.lineTo(_zeroCoord, _zeroCoord)
    else this.drawBottomLeft(pen)
}