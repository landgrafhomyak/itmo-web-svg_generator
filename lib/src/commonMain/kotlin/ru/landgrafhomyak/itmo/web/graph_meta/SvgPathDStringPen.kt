package ru.landgrafhomyak.itmo.web.graph_meta

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmStatic

@OptIn(ExperimentalJsExport::class)
@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
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
            this.isFirst = false
        } else {
            this.builder.append(' ')
        }
    }

    @Suppress("FunctionName", "NOTHING_TO_INLINE")
    private inline fun _mapRadius(coord: Pen.Coordinate, r: Double): Double = when (coord) {
        Pen.Coordinate.NEG_FULL -> r
        Pen.Coordinate.NEG_HALF -> r / 2
        Pen.Coordinate.ZERO -> 0.0
        Pen.Coordinate.POS_HALF -> r / 2
        Pen.Coordinate.POS_FULL -> r
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
        this.builder.append("A ${this._mapRadius(rx.abs(), this.rw)} ${this._mapRadius(ry.abs(), this.rh)} $angle 0 ${if (outerArc) "1" else "0"} ${this._mapX(toX)} ${this._mapY(toY)}")
    }

    override fun closeLine() {
        this.addLeadingSeparator()
        this.builder.append("Z")
    }

    fun build(): String = this.builder.toString()

    companion object {
        @OptIn(ExperimentalContracts::class)
        @JvmStatic
        @JsName("_drawScoped")
        inline fun draw(cx: Double, cy: Double, rw: Double, rh: Double, scope: (Pen) -> Unit): String {
            contract {
                callsInPlace(scope, InvocationKind.EXACTLY_ONCE)
            }

            val pen = SvgPathDStringPen(cx, cy, rw, rh)
            scope(pen)
            return pen.build()
        }

        @JvmStatic
        @JsName("draw")
        fun draw(cx: Double, cy: Double, rw: Double, rh: Double, graph: GraphInfo): String {
            val pen = SvgPathDStringPen(cx, cy, rw, rh)
            graph.draw(pen)
            return pen.build()
        }
    }
}