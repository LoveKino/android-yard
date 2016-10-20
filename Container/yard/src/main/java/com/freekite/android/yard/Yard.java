package com.freekite.android.yard;

import android.content.Context;

import com.ddchen.bridge.messchunkpc.Messchunkpc;
import com.ddchen.bridge.messchunkpc.Messchunkpc.Caller;
import com.ddchen.bridge.messchunkpc.Messchunkpc.SandboxFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuer on 10/9/16.
 */
public class Yard {
    private Context context = null;
    private String tag = null;

    /**
     * @param context
     * @param tag
     */
    public Yard(Object context, String tag) {
        this.context = (Context) context;
        this.tag = tag;

        String pkgName = this.context.getPackageName();
        String channel = "/data/user/0/" + pkgName + "/files/aosp_hook/command.json";

        Map sandbox = new HashMap();
        // test
        // exports native apis to sandbox
        sandbox.put("subtraction", new SandboxFunction() {
            @Override
            public Object apply(Object[] args) {
                return 100000;
            }
        });

        Caller caller = Messchunkpc.pc(this.context, channel, sandbox);
        // call freekite apis from caller
    }

    /**
     * receive infos from system
     *
     * @param type
     * @param infos
     */
    public void receive(String type, Object[] infos) {
        System.out.println("-----------------Yard:receive------------------");
        System.out.println(type);
        System.out.println(infos);
        System.out.println("-----------------------------------------------");
    }
}
