package com.freekite.android.yard.adbcontest2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ddchen.bridge.messchunkpc.Messchunkpc;
import com.ddchen.bridge.messchunkpc.Messchunkpc.Caller;
import com.ddchen.bridge.messchunkpc.Messchunkpc.HandleCallResult;
import com.ddchen.bridge.messchunkpc.Messchunkpc.SandboxFunction;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String channel = "/data/user/0/com.freekite.android.yard.adbcontest2/files/aosp_hook/command.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map sandbox = new HashMap();
        sandbox.put("subtraction", new SandboxFunction() {
            @Override
            public Object apply(Object[] args) {
                double a = Double.parseDouble(args[0].toString());
                double b = Double.parseDouble(args[1].toString());
                return a - b;
            }
        });
        Messchunkpc.pc(this, channel, sandbox);
    }
}
