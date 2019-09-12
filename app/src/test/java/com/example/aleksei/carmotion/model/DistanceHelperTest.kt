package com.example.aleksei.carmotion.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DistanceHelperTest {

    private val distanceHelper = DistanceHelper()

    @Test
    fun `destination point is out of the radius`() {
        val carPoint = Point(0f, 0f)
        val radius = 2f
        val destinationPoint = Point(2f, 3f)

        val actual = distanceHelper.isDestinationPointInCarRadius(carPoint, radius, destinationPoint)
        assertEquals(false, actual)
    }

    @Test
    fun `destination point is in the radius`() {
        val carPoint = Point(0f, 0f)
        val radius = 5f
        val destinationPoint = Point(2f, 3f)

        val actual = distanceHelper.isDestinationPointInCarRadius(carPoint, radius, destinationPoint)
        assertEquals(true, actual)
    }
}
