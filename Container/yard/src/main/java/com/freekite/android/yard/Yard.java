package com.freekite.android.yard;

import android.app.Activity;

/**
 * Created by yuer on 10/9/16.
 */
public class Yard {
    private Activity context = null;
    private String tag = null;

    /**
     *
     * @param context
     * @param tag
     *
     */
    public Yard(Object context, String tag) {
        this.context = (Activity) context;
        System.out.println(Activity.class.getName());
        this.tag = tag;
    }

    /**
     * receive infos
     *
     * @param type
     * @param infos
     */
    public void receive(String type, Object[] infos) {
        // TODO
        System.out.println("-----------------Yard:receive------------------");
        System.out.println(type);
        System.out.println(infos);
        System.out.println("-----------------------------------------------");
    }
}

