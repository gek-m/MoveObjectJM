package com.interview.movecarjm.ui.CustomView

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan

data class MovingData(
    var fromX: Float = 0f,
    var fromY: Float = 0f,
    var toX: Float = 0f,
    var toY: Float = 0f,
    var grade: Float = 0f,
    var isLeftTurn: Boolean = true
) {
    constructor(fromXPos: Float, fromYPos: Float, toXPos: Float, toYPos: Float) :
            this(
                fromX = fromXPos,
                fromY = fromYPos,
                toX = toXPos,
                toY = toYPos,
                grade = atan(abs(fromXPos - toXPos) / abs(fromYPos - toYPos)) * 180 / PI.toFloat(),
                isLeftTurn = toXPos < fromXPos
            )
}