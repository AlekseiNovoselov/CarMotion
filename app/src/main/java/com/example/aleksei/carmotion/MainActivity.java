package com.example.aleksei.carmotion;

import android.os.Bundle;

import com.example.aleksei.carmotion.car.CarFragment;
import com.example.aleksei.carmotion.car.CarPresenter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CarFragment carFragment =
                (CarFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (carFragment == null) {
            carFragment = CarFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentFrame, carFragment)
                    .commit();
        }
        new CarPresenter(carFragment);
    }
}
