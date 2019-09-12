package com.example.aleksei.carmotion.view

import android.util.DisplayMetrics
import androidx.fragment.app.FragmentActivity

class ScreenHelper(activity: FragmentActivity) {

    private val height: Int
    private val width: Int

    init {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels
        height = metrics.heightPixels
    }

    fun getWidth() = width

    fun getHeight() = height
}
