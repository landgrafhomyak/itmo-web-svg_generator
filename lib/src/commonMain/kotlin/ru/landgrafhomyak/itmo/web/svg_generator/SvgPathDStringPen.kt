package ru.landgrafhomyak.itmo.web.svg_generator

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

class SvgPathDStringPen(
    private val cx: Double,
    private val cy: Double,
    private val rw: Double,
    private val rh: Double,
) : Pen {
    private val builder = StringBuilder()
    private var isFirst = true

    private fun addLeadingSeparator() {
        if (this.isFirst) {
            this.builder.append(' ')
            this.isFirst = false
        }
    }

    @Suppress("FunctionName", "NOTHING_TO_INLINE")
    private inline fun __mapCoord(coord: Pen.Coordinate, c: Double, r: Double) = when (coord) {
        Pen.Coordinate.NEG_FULL -> c - r
        Pen.Coordinate.NEG_HALF -> c - r / 2
        Pen.Coordinate.ZERO -> c
        Pen.Coordinate.POS_HALF -> c + r / 2
        Pen.Coordinate.POS_FULL -> c + r
    }


    @Suppress("FunctionName")
    private fun _mapX(coord: Pen.Coordinate): Double =
        this.__mapCoord(coord, this.cx, this.rw)

    @Suppress("FunctionName")
    private fun _mapY(coord: Pen.Coordinate): Double =
        this.__mapCoord(coord, this.cy, this.rh)

    override fun moveTo(x: Pen.Coordinate, y: Pen.Coordinate) {
        this.addLeadingSeparator()
        this.builder.append("M ${this._mapX(x)} ${this._mapY(y)}")
    }

    override fun lineTo(x: Pen.Coordinate, y: Pen.Coordinate) {
        this.addLeadingSeparator()
        this.builder.append("L ${this._mapX(x)} ${this._mapY(y)}")
    }

    override fun arcTo(rx: Pen.Coordinate, ry: Pen.Coordinate, angle: Int, outerArc: Boolean, toX: Pen.Coordinate, toY: Pen.Coordinate) {
        this.addLeadingSeparator()
        this.builder.append("A ${this._mapX(rx)} ${this._mapY(ry)} $angle 0 ${if (outerArc) "1" else "0"} ${this._mapX(toX)} ${this._mapY(toY)}")
    }

    override fun closeLine() {
        this.addLeadingSeparator()
        this.builder.append("Z")
    }

    fun build(): String = this.builder.toString()

    companion object {
        @OptIn(ExperimentalContracts::class)
        @JvmStatic
        inline fun draw(cx: Double, cy: Double, rw: Double, rh: Double, scope: (Pen) -> Unit): String {
            contract {
                callsInPlace(scope, InvocationKind.EXACTLY_ONCE)
            }

            val pen = SvgPathDStringPen(cx, cy, rw, rh)
            scope(pen)
            return pen.build()
        }

        @JvmStatic
        fun draw(cx: Double, cy: Double, rw: Double, rh: Double, graph: GraphInfo): String {
            val pen = SvgPathDStringPen(cx, cy, rw, rh)
            graph.draw(pen)
            return pen.build()
        }
    }
}