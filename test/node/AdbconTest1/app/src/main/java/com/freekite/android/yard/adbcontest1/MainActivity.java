package com.freekite.android.yard.adbcontest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ddchen.bridge.messchunkpc.Messchunkpc;
import com.ddchen.bridge.messchunkpc.Messchunkpc.Caller;
import com.ddchen.bridge.messchunkpc.Messchunkpc.HandleCallResult;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String channel = "/data/user/0/com.freekite.android.yard.adbcontest1/files/aosp_hook/command.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Caller caller = Messchunkpc.pc(this, channel);

        caller.call("add", new Object[]{1, 2}, new HandleCallResult() {
            @Override
            public void handle(Object json) {
                System.out.println("++++===========================");
                System.out.println(json);
            }

            @Override
            public void handleError(JSONObject errorInfo) {
            }
        });
    }
}
