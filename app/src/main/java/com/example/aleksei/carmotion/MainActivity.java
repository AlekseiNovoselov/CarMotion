package com.example.aleksei.carmotion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aleksei.carmotion.car.CarFragment;
import com.example.aleksei.carmotion.car.CarPresenter;
import com.example.aleksei.carmotion.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CarFragment carFragment =
                (CarFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (carFragment == null) {
            carFragment = CarFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), carFragment, R.id.contentFrame);
        }

        CarPresenter mCarPresenter = new CarPresenter(carFragment);

    }
}
