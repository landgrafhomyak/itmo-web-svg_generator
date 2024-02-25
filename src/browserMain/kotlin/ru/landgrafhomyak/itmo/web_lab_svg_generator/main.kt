package ru.landgrafhomyak.itmo.web_lab_svg_generator

import kotlinx.browser.document
import org.w3c.dom.Element

private object Generator {
    fun generateSizeButtons(group: Element, transform: TransformationMatrix2d) {
        val smallButton = document.createElementNS("http://www.w3.org/2000/svg","path")
        smallButton.setAttribute(
            "d", "" +
                    "M ${transform._applyTransformX(0, 200)} ${transform._applyTransformY(0, 200)}" +
                    " L ${transform._applyTransformX(0, 150)} ${transform._applyTransformY(0, 150)}" +
                    " L ${transform._applyTransformX(50, 150)} ${transform._applyTransformY(50, 150)}" +
                    " L ${transform._applyTransformX(50, 200)} ${transform._applyTransformY(50, 200)}" +
                    " Z"
        )
        smallButton.setAttribute("fill", "grey")
        group.appendChild(smallButton)
        val bigButton = document.createElementNS("http://www.w3.org/2000/svg","path")
        bigButton.setAttribute(
            "d", "" +
                    "M ${transform._applyTransformX(50, 200)} ${transform._applyTransformY(50, 200)}" +
                    " L ${transform._applyTransformX(50, 150)} ${transform._applyTransformY(50, 150)}" +
                    " L ${transform._applyTransformX(100, 150)} ${transform._applyTransformY(100, 150)}" +
                    " L ${transform._applyTransformX(150, 200)} ${transform._applyTransformY(150, 200)}" +
                    " Z"
        )
        bigButton.setAttribute("fill", "grey")
        group.appendChild(bigButton)
    }

    fun generateSizeButtonsQuart(group: Element, transform: TransformationMatrix2d) {
        this.generateSizeButtons(group, transform)
        this.generateSizeButtons(group, TransformationMatrix2d.FLIP_VERTICAL.addTransform(transform).addTransform(TransformationMatrix2d.ROTATE_RIGHT))
    }
}

fun main() {
    val sizeButtonsGroup = document.getElementById("size-buttons")!!
    Generator.generateSizeButtonsQuart(sizeButtonsGroup, TransformationMatrix2d.NO_TRANSFORM)
    Generator.generateSizeButtonsQuart(sizeButtonsGroup, TransformationMatrix2d.ROTATE_RIGHT)
    Generator.generateSizeButtonsQuart(sizeButtonsGroup, TransformationMatrix2d.ROTATE_180)
    Generator.generateSizeButtonsQuart(sizeButtonsGroup, TransformationMatrix2d.ROTATE_LEFT)
}