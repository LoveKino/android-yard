package com.freekite.android.yard;

/**
 * Created by yuer on 10/9/16.
 */
public class Yard {
    private String tag = null;
    private Object context = null;

    public Yard(String tag, Object context) {
        this.tag = tag;
        this.context = context;
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
