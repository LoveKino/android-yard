package com.freekite.android.yard;

import android.content.Context;
import android.os.FileObserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by yuer on 10/12/16.
 */

public class MonitorCommand {
    private static FileObserver observer = null;

    private static final String commandFileName = "command.json";
    private static final String commandDir = "aosp_hook";

    public interface ExecuteCommand {
        void execute(String command);
    }

    public static String getCommandDirPath(Context context){
        return context.getFilesDir() + File.separator + commandDir;
    }

    public static String getCommandPath(Context context) {
        return getCommandDirPath(context) + File.separator + commandFileName;
    }

    public static void monitor(Context context, final ExecuteCommand executeCommand) {
        if (observer != null) return;
        File dir = new File(getCommandDirPath(context));
        dir.mkdirs();
        // init file
        String filePath = getCommandPath(context);
        final File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        observer = new FileObserver(filePath) {
            @Override
            public void onEvent(int event, String f) {
                if (event == FileObserver.CLOSE_WRITE) {
                    try {
                        FileInputStream ins = new FileInputStream(file);
                        byte[] buffer = new byte[(int) file.length()];
                        ins.read(buffer);
                        String command = new String(buffer);
                        executeCommand.execute(command);
                        ins.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        observer.startWatching();
    }
}
