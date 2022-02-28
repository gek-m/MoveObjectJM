package com.interview.movecarjm.ui.CustomView

interface CarMoveAction {

    fun setCarStartPosition(x: Int, y: Int)

    fun drawCarRoad(roadPoints: List<Pair<Float, Float>>, carWidth: Int, carHeight: Int)
}