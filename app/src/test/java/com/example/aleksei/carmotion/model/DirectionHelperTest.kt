package com.example.aleksei.carmotion.model

import org.junit.Assert.*
import org.junit.Test

class DirectionHelperTest {

    private val directionHelper = DirectionHelper()

    @Test
    fun `point is to the left of the vector to the left`() {
        val startPoint = Point(0f, 0f)
        val rotation = 0f
        val finishPoint = Point(2f, 3f)
        val actualDirection = directionHelper.calculateDirection(startPoint, rotation, finishPoint)
        val expectedDirection = Direction.LEFT
        assertEquals(expectedDirection, actualDirection)
    }

    @Test
    fun `point is to the right of the vector to the left`() {
        val startPoint = Point(0f, 0f)
        val rotation = 0f
        val finishPoint = Point(2f, -3f)
        val actualDirection = directionHelper.calculateDirection(startPoint, rotation, finishPoint)
        val expectedDirection = Direction.RIGHT
        assertEquals(expectedDirection, actualDirection)
    }

    @Test
    fun `point is to the right of the vector to the top`() {
        val startPoint = Point(0f, 0f)
        val rotation = (Math.PI / 2).toFloat()
        val finishPoint = Point(2f, 3f)
        val actualDirection = directionHelper.calculateDirection(startPoint, rotation, finishPoint)
        val expectedDirection = Direction.RIGHT
        assertEquals(expectedDirection, actualDirection)
    }

    @Test
    fun `point is straight on the vector to the left`() {
        val startPoint = Point(0f, 0f)
        val rotation = 0f
        val finishPoint = Point(5f, 0f)
        val actualDirection = directionHelper.calculateDirection(startPoint, rotation, finishPoint)
        val expectedDirection = Direction.STRAIGHT
        assertEquals(expectedDirection, actualDirection)
    }
}
