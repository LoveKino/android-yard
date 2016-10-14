package com.freekite.android.yard;

import android.content.Context;

import com.freekite.android.yard.MonitorCommand.ExecuteCommand;

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

        MonitorCommand.monitor(this.context, new ExecuteCommand() {
            @Override
            public void execute(String command) {
                /**
                 * 1. parse command (call command)
                 * 2. execute command
                 * 3. send results
                 */
                System.out.println("----------------------------------");
                System.out.println(command);
            }
        });
    }

    /**
     * receive infos
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
