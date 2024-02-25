package ru.landgrafhomyak.itmo.web.svg_generator

interface Pen {
    @Suppress("RemoveRedundantQualifierName")
    enum class Coordinate {
        NEG_FULL {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.POS_FULL
        },
        NEG_HALF {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.POS_HALF
        },
        ZERO {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.ZERO
        },
        POS_HALF {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.NEG_HALF
        },
        POS_FULL {
            override fun unaryMinus(): Coordinate = Pen.Coordinate.NEG_FULL
        };

        abstract operator fun unaryMinus(): Pen.Coordinate
    }

    fun moveTo(x: Coordinate, y: Coordinate)
    fun lineTo(x: Coordinate, y: Coordinate)
    fun arcTo(rx: Coordinate, ry: Coordinate, angle: Int, outerArc: Boolean, x: Coordinate, y: Coordinate)
}