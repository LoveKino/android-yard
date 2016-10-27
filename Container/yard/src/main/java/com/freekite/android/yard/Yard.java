package com.freekite.android.yard;

import android.content.Context;
import android.view.MotionEvent;

import com.ddchen.bridge.messchunkpc.Messchunkpc;
import com.ddchen.bridge.messchunkpc.Messchunkpc.Caller;
import com.ddchen.bridge.messchunkpc.Messchunkpc.HandleCallResult;
import com.ddchen.bridge.messchunkpc.Messchunkpc.SandboxFunction;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuer on 10/9/16.
 */
public class Yard {
    private Context context = null;
    private String tag = null;
    private Caller caller = null;
    private String recordStatus = "stop";

    /**
     * @param context
     * @param tag
     */
    public Yard(Object context, String tag) {
        this.context = (Context) context;
        this.tag = tag;

        String pkgName = this.context.getPackageName();
        String channel = "/data/user/0/" + pkgName + "/files/aosp_hook/command.json";

        this.caller = Messchunkpc.pc(this.context, channel, getSandbox());
    }

    private Map getSandbox() {
        Map sandbox = new HashMap();
        // exports native apis to sandbox
        sandbox.put("startRecord", new SandboxFunction() {
            @Override
            public Object apply(Object[] args) {
                Yard.this.recordStatus = "start";
                return null;
            }
        });

        sandbox.put("stopRecord", new SandboxFunction() {
            @Override
            public Object apply(Object[] args) {
                Yard.this.recordStatus = "stop";
                return null;
            }
        });

        sandbox.put("playback", new SandboxFunction() {
            @Override
            public Object apply(Object[] args) {
                return null;
            }
        });

        return sandbox;
    }

    /**
     * receive infos from system
     *
     * @param type
     * @param infos
     */
    public void receive(String type, Object[] infos) {
        // get touch event, at this moment, handlers are not executed yet.
        if (type.equals("dispatchTouchEvent:start")) {
            // send current state
            if (Yard.this.recordStatus == "start") {
                MotionEvent ev = (MotionEvent) infos[0];
                this.caller.call("feedEvent", new Object[]{
                        // basic event info
                        SerializeEvent.serialize(ev),
                        SerializeNode.serialize(TargetViewFinder.find(ev), false)
                }, new HandleCallResult() {
                    @Override
                    public void handle(Object json) {
                    }

                    @Override
                    public void handleError(JSONObject errorInfo) {
                        System.out.println(errorInfo);
                    }
                });
            }
        }
    }
}
