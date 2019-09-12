package com.example.aleksei.carmotion

import android.os.Bundle

import com.example.aleksei.carmotion.view.CarFragment
import com.example.aleksei.carmotion.presenter.CarPresenterImpl

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var carFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as CarFragment?
        if (carFragment == null) {
            carFragment = CarFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.contentFrame, carFragment)
                .commit()
        }
        CarPresenterImpl(carFragment)
    }
}
