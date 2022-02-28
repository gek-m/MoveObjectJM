package com.interview.movecarjm.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import com.interview.movecarjm.toMovingData
import com.interview.movecarjm.ui.CustomView.CarMoveAction
import com.interview.movecarjm.ui.CustomView.MovingData
import kotlin.random.Random

class MainPresenter {

    companion object {
        private const val DURATION_RESET = 100L
        private const val DURATION_FOR_TURN = 200L
        private const val DURATION_FOR_STRAIGHT = 400L
        private const val DURATION_FULL = DURATION_FOR_TURN + DURATION_FOR_STRAIGHT
    }

    private var carWidth: Int = 0
    private var carHeight: Int = 0
    private var carYPosition: Int = 0
    private var carXPosition: Int = 0

    private var areaWidth: Int = 0
    private var areaHeight: Int = 0

    private var roadPoints: MutableList<Pair<Float, Float>> = mutableListOf()
    private var animatorSet: AnimatorSet? = null

    var carMoveAction: CarMoveAction? = null

    fun setCarParameters(viewCar: View) {
        carWidth = viewCar.measuredWidth
        carHeight = viewCar.measuredHeight
    }

    fun setAreaParameters(viewArea: View) {
        areaWidth = viewArea.measuredWidth
        areaHeight = viewArea.measuredHeight
    }

    fun resetCarPosition(viewCar: View) {
        carYPosition = areaHeight - carHeight
        carXPosition = Random.nextInt(areaWidth - carWidth)
        resetAnimationPosition(viewCar)
        carMoveAction?.setCarStartPosition(carXPosition, carYPosition)
    }

    fun showRoad(){
        getRoadRandomPoints()
    }

    fun startMove(carView: View) {
        animatorSet = getAnimatorSet(carView, roadPoints.toMovingData())
        animatorSet?.start()
    }

    private fun getAnimatorSet(carView: View, movingData: List<MovingData>): AnimatorSet {
        val moveAnimatorSet = AnimatorSet()
        var currentX = 0f
        var currentY = 0f
        var currentGrade = 0f

        movingData.forEachIndexed { index, data ->
            val newX = currentX + data.toX - data.fromX
            val newY = currentY + data.toY - data.fromY
            val grade = data.grade * (if (data.isLeftTurn) -1 else 1)
            val newGrade = currentGrade + (grade - currentGrade)

            val animatorX = ObjectAnimator.ofFloat(carView, View.TRANSLATION_X, currentX, newX)
            val animatorY = ObjectAnimator.ofFloat(carView, View.TRANSLATION_Y, currentY, newY)
            val animatorGrade =
                ObjectAnimator.ofFloat(carView, View.ROTATION, currentGrade, newGrade)
            animatorGrade.duration = DURATION_FOR_TURN
            animatorX.duration = DURATION_FOR_STRAIGHT
            animatorY.duration = DURATION_FOR_STRAIGHT

            val delay = DURATION_FULL * index
            moveAnimatorSet.play(animatorGrade).after(delay)
            moveAnimatorSet.play(animatorX).with(animatorY).after(delay + DURATION_FOR_TURN)

            currentX = newX
            currentY = newY
            currentGrade = newGrade
        }

        return moveAnimatorSet
    }

    private fun getRoadRandomPoints() {
        roadPoints = mutableListOf()
        roadPoints.add(Pair(carXPosition.toFloat(), carYPosition.toFloat()))
        var xPosition = carXPosition
        var yPosition = carYPosition

        while (yPosition > 0) {
            xPosition = Random.nextInt(areaWidth - carWidth)
            yPosition -= Random.nextInt(carHeight)
            if (yPosition < 0) yPosition = 0
            roadPoints.add(Pair(xPosition.toFloat(), yPosition.toFloat()))
        }
        carMoveAction?.drawCarRoad(roadPoints, carWidth, carHeight)
    }

    private fun resetAnimationPosition(viewCar: View) {
        animatorSet?.cancel()
        val animatorX = ObjectAnimator.ofFloat(viewCar, View.TRANSLATION_X, 0f)
        val animatorY = ObjectAnimator.ofFloat(viewCar, View.TRANSLATION_Y, 0f)
        val angle = ObjectAnimator.ofFloat(viewCar, View.ROTATION, 0f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorX, animatorY, angle)
        animatorSet.duration = DURATION_RESET
        animatorSet.start()
    }
}