package ru.landgrafhomyak.itmo.web_lab_svg_generator

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


class TransformationMatrix2d(
    private val aa: Int, private val ab: Int, private val ac: Int,
    private val ba: Int, private val bb: Int, private val bc: Int,
    private val ca: Int, private val cb: Int, private val cc: Int
) {
    companion object {
        val NO_TRANSFORM = TransformationMatrix2d(1, 0, 0, 0, 1, 0, 0, 0, 1)
        val ROTATE_LEFT = TransformationMatrix2d(0, -1, 0, 1, 0, 0, 0, 0, 1)
        val ROTATE_RIGHT = TransformationMatrix2d(0, 1, 0, -1, 0, 0, 0, 0, 1)
        val FLIP_HORIZONTAL = TransformationMatrix2d(-1, 0, 0, 0, 1, 0, 0, 0, 1)
        val FLIP_VERTICAL = TransformationMatrix2d(1, 0, 0, 0, -1, 0, 0, 0, 1)
        val ROTATE_180 = TransformationMatrix2d(-1, 0, 0, 0, 1, 0, 0, 0, 1)
    }

    fun addTransform(other: TransformationMatrix2d) = TransformationMatrix2d(
        other.aa * this.aa + other.ab * this.ba + other.ac * this.ca,
        other.aa * this.ab + other.ab * this.bb + other.ac * this.cb,
        other.aa * this.ac + other.ab * this.bc + other.ac * this.cc,
        other.ba * this.aa + other.bb * this.ba + other.bc * this.ca,
        other.ba * this.ab + other.bb * this.bb + other.bc * this.cb,
        other.ba * this.ac + other.bb * this.bc + other.bc * this.cc,
        other.ca * this.aa + other.cb * this.ba + other.cc * this.ca,
        other.ca * this.ab + other.cb * this.bb + other.cc * this.cb,
        other.ca * this.ac + other.cb * this.bc + other.cc * this.cc,
    )

    @Suppress("FunctionName")
    fun _applyTransformX(x: Int, y: Int): Int {
        return this.aa * x + this.ab * y + this.ac
    }

    @Suppress("FunctionName")
    fun _applyTransformY(x: Int, y: Int): Int {
        return this.ba * x + this.bb * y + this.bc
    }

    @OptIn(ExperimentalContracts::class)
    inline fun <R> applyTransform(x: Int, y: Int, receiver: (x: Int, y: Int) -> R): R {
        contract { callsInPlace(receiver, InvocationKind.EXACTLY_ONCE) }
        return receiver(this._applyTransformX(x, y), this._applyTransformY(x, y))
    }
}