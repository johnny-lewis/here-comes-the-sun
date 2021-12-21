package au.com.lexicon.herecomesthesun.presentation.component

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import au.com.lexicon.herecomesthesun.domain.model.GraphPoint
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.ABSOLUTE_MASK_OPACITY
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.HOURS_IN_DAY_ZERO_INDEX
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.X_AXIS_END_PADDING
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.X_AXIS_POINT_OFFSET
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.X_AXIS_START_PADDING
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.Y_AXIS_BOTTOM_PADDING
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.Y_AXIS_LINE_HOR_PADDING
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.Y_AXIS_LINE_VERT_PADDING
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.Y_AXIS_START_PADDING
import au.com.lexicon.herecomesthesun.presentation.component.GraphConstants.Y_AXIS_TOP_PADDING
import au.com.lexicon.herecomesthesun.presentation.viewmodel.HomeViewModelContract
import kotlin.math.roundToInt

private data class Point(
    val x: Float,
    val y: Float,
)

private object GraphConstants {
    const val HOURS_IN_DAY_ZERO_INDEX: Float = 23f

    const val X_AXIS_START_PADDING: Float = 82f
    const val X_AXIS_END_PADDING: Float = 48f
    const val X_AXIS_POINT_OFFSET: Float = 0f

    const val Y_AXIS_START_PADDING: Float = 32f
    const val Y_AXIS_TOP_PADDING: Float = 194f
    const val Y_AXIS_BOTTOM_PADDING: Float = 72f

    const val Y_AXIS_LINE_VERT_PADDING: Float = 12f
    const val Y_AXIS_LINE_HOR_PADDING: Float = 46f


    const val ABSOLUTE_MASK_OPACITY: Float = 0.5f
}

@Composable
fun DrawSensorGraph(viewModel: HomeViewModelContract) {
    val dataPerDay by viewModel.graphValuesFlow.collectAsState()
    val yAxisLabels by viewModel.yAxisValuesFlow.collectAsState()
    val selectedDay by viewModel.dayFlow.collectAsState()



    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(top = 8.dp)
    ) {
        val axisTextPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 12.sp.toPx()
            color = Color.Black.toArgb()
            typeface = Typeface.DEFAULT
        }

        val canvasWidth =
            size.width - X_AXIS_START_PADDING - X_AXIS_END_PADDING
        val canvasHeight =
            size.height - Y_AXIS_TOP_PADDING - Y_AXIS_BOTTOM_PADDING

        drawYAxis(
            drawScope = this,
            canvasHeight = canvasHeight,
            yAxisLabels = yAxisLabels,
            axisTextPaint = axisTextPaint
        )

        if (dataPerDay.isNotEmpty()) {
            plotPoints(
                drawScope = this,
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                yAxisLabels = yAxisLabels,
                dataPerDay = dataPerDay,
                incrementsPerPeriod = HOURS_IN_DAY_ZERO_INDEX,
                xStartPadding = X_AXIS_START_PADDING,
                yAxisTopPadding = Y_AXIS_TOP_PADDING,
                yAxisLineVertPadding = Y_AXIS_LINE_VERT_PADDING,
                xValueOffset = X_AXIS_POINT_OFFSET,
                currentIncrement = null,
                absoluteOpacity = ABSOLUTE_MASK_OPACITY,
                lineColour = Color.Black
            )
        }
    }
}

fun plotPoints(
    drawScope: DrawScope,
    canvasWidth: Float,
    canvasHeight: Float,
    yAxisLabels: List<Int>,
    dataPerDay: List<GraphPoint>,
    incrementsPerPeriod: Float,
    xStartPadding: Float,
    yAxisTopPadding: Float,
    yAxisLineVertPadding: Float,
    xValueOffset: Float,
    currentIncrement: Int?,
    absoluteOpacity: Float,
    lineColour: Color
) {
    drawScope.run {
        val pointsInPath: MutableList<Point> = mutableListOf()
        var currentPath = Path()
        var highestPoint: Point? = null

        for (i in dataPerDay.indices) {
            if (currentIncrement != null) {
                if (i > currentIncrement) {
                    break
                }
            }

            val point = Point(
                x = when (i) {
                    0 -> (canvasWidth / incrementsPerPeriod * i) + xStartPadding
                    incrementsPerPeriod.toInt() -> (canvasWidth / incrementsPerPeriod * i) + xStartPadding + xValueOffset * 2
                    else -> (canvasWidth / incrementsPerPeriod * i) + xStartPadding + xValueOffset
                },
                y = determineYPoint(
                    index = i,
                    canvasHeight = canvasHeight,
                    yAxisLabels = yAxisLabels,
                    dataPerDay = dataPerDay,
                    yAxisTopPadding = yAxisTopPadding,
                    yAxisLineVertPadding = yAxisLineVertPadding
                )
            )

            highestPoint = highestPoint?.let { highPoint ->
                // higher points have lower y values on the canvas
                if (point.y < highPoint.y) {
                    point
                } else {
                    highPoint
                }
            } ?: point

            if (currentPath.isEmpty) {
                currentPath.moveTo(point.x, point.y)
                pointsInPath.add(point)
            } else {
                currentPath.lineTo(point.x, point.y)
                pointsInPath.add(point)
            }
        }

        drawPathFromPoints(
            drawScope = this,
            path = currentPath,
            lineColour = lineColour
        )

        completePathAndColour(
            drawScope = this,
            highestY = highestPoint?.y ?: 0f,
            lowestY = calculateYPoint(
                canvasHeight = canvasHeight,
                yAxisLabels = yAxisLabels,
                yValue = yAxisLabels.last(),
                yAxisTopPadding = yAxisTopPadding,
                yAxisLineVertPadding = yAxisLineVertPadding
            ),
            path = currentPath,
            points = pointsInPath,
            absoluteOpacity = absoluteOpacity,
        )
    }
}

private fun determineYPoint(
    index: Int,
    canvasHeight: Float,
    yAxisLabels: List<Int>,
    dataPerDay: List<GraphPoint>,
    yAxisTopPadding: Float,
    yAxisLineVertPadding: Float
): Float {
    val yValue: Int = dataPerDay[index].value

    return calculateYPoint(
        canvasHeight = canvasHeight,
        yAxisLabels = yAxisLabels,
        yValue = yValue,
        yAxisTopPadding = yAxisTopPadding,
        yAxisLineVertPadding = yAxisLineVertPadding
    )
}

private fun calculateYPoint(
    canvasHeight: Float,
    yAxisLabels: List<Int>,
    yValue: Int,
    yAxisTopPadding: Float,
    yAxisLineVertPadding: Float
): Float {
    val relativeYPoint: Float =
        (yAxisLabels.first() - yValue).toFloat() / (yAxisLabels.first() - yAxisLabels.last()).toFloat()
    return (canvasHeight * relativeYPoint) + (yAxisTopPadding - yAxisLineVertPadding)
}

private fun drawPathFromPoints(
    drawScope: DrawScope,
    path: Path,
    lineColour: Color
) {
    drawScope.run {
        drawPath(
            path = path,
            color = lineColour,
            style = Stroke(
                width = 2.dp.toPx(),
                pathEffect = null
            )
        )
    }
}

private fun completePathAndColour(
    drawScope: DrawScope,
    highestY: Float,
    lowestY: Float,
    path: Path,
    points: List<Point>,
    absoluteOpacity: Float
) {
    drawScope.run {
        path.lineTo(points.last().x, lowestY)
        path.lineTo(points.first().x, lowestY)
        clipPath(
            path = path,
            clipOp = ClipOp.Intersect
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(
                            alpha = absoluteOpacity
                        ),
                        Color.Black.copy(
                            alpha = 0f
                        )
                    )
                ),
                topLeft = Offset(
                    x = 0f,
                    y = highestY
                ),
                size = Size(
                    width = size.width,
                    height = size.height - highestY
                )
            )
        }
    }
}


private fun drawYAxis(
    drawScope: DrawScope,
    canvasHeight: Float,
    yAxisLabels: List<Int>,
    axisTextPaint: NativePaint
) {
    drawScope.run {
        for (i in yAxisLabels.indices) {
            val yPos = ((canvasHeight / (yAxisLabels.size - 1)) * i) + Y_AXIS_TOP_PADDING
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    yAxisLabels[i].toString(), // text
                    Y_AXIS_START_PADDING, // x position
                    yPos, // y position
                    axisTextPaint.apply {
                        textAlign = android.graphics.Paint.Align.RIGHT
                    } // paint
                )
                drawYAxisLine(
                    drawScope = this,
                    yPosition = yPos
                )
            }
        }
    }
}

private fun drawYAxisLine(drawScope: DrawScope, yPosition: Float) {
    drawScope.run {
        drawLine(
            start = Offset(
                x = Y_AXIS_START_PADDING + Y_AXIS_LINE_HOR_PADDING,
                y = yPosition - Y_AXIS_LINE_VERT_PADDING
            ),
            end = Offset(
                x = size.width - Y_AXIS_LINE_HOR_PADDING,
                y = yPosition - Y_AXIS_LINE_VERT_PADDING
            ),
            color = Color.Black.copy(
                alpha = 0.2f
            ),
            strokeWidth = 1.5.dp.toPx()
        )
    }
}

private fun checkOffset(offset: Float, endPoint: Float): Boolean {
    return X_AXIS_START_PADDING < offset && offset < endPoint
}