package ru.landgrafhomyak.itmo.web.graph_meta

import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestQuartCheck {
    @Test
    fun checkRect() =
        assertEquals(true, QuartInfo.Rectangle(true, true).checkTopRight(1.0, 1.0, 2.0))
}