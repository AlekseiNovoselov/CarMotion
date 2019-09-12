package com.example.aleksei.carmotion.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout

import com.example.aleksei.carmotion.R

import androidx.fragment.app.Fragment
import com.example.aleksei.carmotion.model.Point
import com.example.aleksei.carmotion.presenter.CarPresenter

class CarFragment : Fragment(), CarView, View.OnTouchListener {

    private lateinit var rootView: FrameLayout
    private lateinit var mPresenter: CarPresenter

    private lateinit var carView: ImageView

    private lateinit var screenHelper: ScreenHelper
    private lateinit var rotationAnimator: ValueAnimator

    override fun setPresenter(presenter: CarPresenter) {
        mPresenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenHelper = ScreenHelper(activity!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.car_fragment, container, false) as FrameLayout
        rootView!!.setOnTouchListener(this)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            view.performClick()
            val x = event.x
            val y = event.y
            mPresenter.startMoveCarToPoint(Point(x, y))
        }
        return true
    }

    override fun getScreenWidth() = screenHelper.getWidth().toFloat()

    override fun getScreenHeight() = screenHelper.getHeight().toFloat()

    override fun getCarRotation() = carView.rotation

    override fun showCar(point: Point) {
        carView = ImageView(context).apply {
            setBackgroundColor(Color.BLUE)
            layoutParams = RelativeLayout.LayoutParams(CAR_WIDTH, CAR_HEIGHT)
            translationX = point.x
            translationY = point.y
        }
        rootView.addView(carView)
    }

    override fun rotateCarOnPointDirection(point: Point, startAngle: Float, finishAngle: Float) {
        val simpleAnimatorListener = object : SimpleAnimatorListener() {

            override fun onAnimationEnd(animation: Animator) {
                mPresenter.onRotationAnimationEnd(Point(carView.x, carView.y))
            }

            override fun onAnimationCancel(animation: Animator) {
                mPresenter.onRotationCancel(point)
            }
        }
        rotationAnimator = ValueAnimator.ofFloat(startAngle, finishAngle).apply {
            duration = ROTATION_SHOW_DURATION
            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                mPresenter.onRotateAnimationUpdate(value, point, startAngle)
            }
            addListener(simpleAnimatorListener)
        }
        rotationAnimator.start()
    }

    override fun cancelRotation() {
        carView.animate().cancel()
        rotationAnimator.cancel()
    }

    override fun moveStraightToPoint(point: Point) {
        AnimatorSet().apply {
            playSequentially(createMoveAnimator(carView, point))
            addListener(createMoveListener(carView))
        }.start()
    }

    private fun createMoveAnimator(carView: ImageView, point: Point): Animator {
        return AnimatorSet().apply {
            duration = STRAIGHT_MOVE_SHOW_DURATION
            playTogether(
                ObjectAnimator.ofFloat(carView, View.TRANSLATION_X, point.x),
                ObjectAnimator.ofFloat(carView, View.TRANSLATION_Y, point.y)
            )
        }
    }

    private fun createMoveListener(carView: ImageView): AnimatorListenerAdapter {
        return object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mPresenter.onAnimationEnd(Point(carView.x, carView.y))
            }
        }
    }

    override fun updateCarPosition(deltaX: Float, deltaY: Float, deltaCourse: Float) {
        if (!java.lang.Float.isNaN(deltaCourse)) {
            carView.rotation = deltaCourse
        }
        carView.translationX = deltaX
        carView.translationY = deltaY
    }

    companion object {
        private const val CAR_HEIGHT = 100
        private const val CAR_WIDTH = 50

        private const val STRAIGHT_MOVE_SHOW_DURATION: Long = 1000
        private const val ROTATION_SHOW_DURATION: Long = 5000

        fun newInstance(): CarFragment {
            return CarFragment()
        }
    }
}
