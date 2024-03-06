package ru.landgrafhomyak.itmo.web.graph_meta

interface Pen {
    @Suppress("RemoveRedundantQualifierName")
    enum class Coordinate {
        NEG_FULL {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.POS_FULL
            override fun abs(): Coordinate = Pen.Coordinate.POS_FULL
        },
        NEG_HALF {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.POS_HALF
            override fun abs(): Coordinate = Pen.Coordinate.POS_HALF
        },
        ZERO {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.ZERO
            override fun abs(): Coordinate = Pen.Coordinate.ZERO
        },
        POS_HALF {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.NEG_HALF
            override fun abs(): Coordinate = Pen.Coordinate.POS_HALF
        },
        POS_FULL {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.NEG_FULL
            override fun abs(): Coordinate = Pen.Coordinate.POS_FULL
        };

        abstract operator fun unaryMinus(): Pen.Coordinate

        abstract fun abs(): Pen.Coordinate
    }

    fun moveTo(x: Coordinate, y: Coordinate)
    fun lineTo(x: Coordinate, y: Coordinate)
    fun arcTo(rx: Coordinate, ry: Coordinate, angle: Int, outerArc: Boolean, toX: Coordinate, toY: Coordinate)
    fun closeLine()
}