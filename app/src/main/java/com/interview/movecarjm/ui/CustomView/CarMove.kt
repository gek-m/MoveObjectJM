package com.interview.movecarjm.ui.CustomView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.interview.movecarjm.R

class CarMove(context: Context, attribute: AttributeSet? = null) :
    LinearLayout(context, attribute) {

    companion object {
        private const val ROAD_LINE_WIDTH = 7F
    }

    private val paint = Paint()
    private val road = Path()

    var roadPoints: List<Pair<Float, Float>>? = null
    var carWidth: Int = 0
    var carHeight: Int = 0

    init {
        paint.color = ResourcesCompat.getColor(context.resources, R.color.black, null)
        paint.strokeWidth = ROAD_LINE_WIDTH
        paint.style = Paint.Style.STROKE
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        road.reset()
        val carWidthCenter = carWidth / 2
        val carHeightCenter = carHeight / 2

        roadPoints?.let {
            it.forEachIndexed { index, pair ->
                val xCoordinate = pair.first + carWidthCenter
                val yCoordinate = pair.second + carHeightCenter
                if (index == 0) {
                    road.moveTo(xCoordinate, yCoordinate)
                } else {
                    road.lineTo(xCoordinate, yCoordinate)
                }
                canvas?.drawPath(road, paint)
            }
        }
    }
}