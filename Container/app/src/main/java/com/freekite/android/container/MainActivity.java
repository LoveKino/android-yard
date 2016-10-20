package com.freekite.android.container;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.freekite.patch.aosppatch.PatchReadHookSource;
import com.android.freekite.patch.aosppatch.PatchReadHookSource.Yard;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Yard yard = PatchReadHookSource.doRead(this, "com.freekite.android.yard.container.MainActivity");

        System.out.println("----------------------------------");
        System.out.println(yard);
    }
}
