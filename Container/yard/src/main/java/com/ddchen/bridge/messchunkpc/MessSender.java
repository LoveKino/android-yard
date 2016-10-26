package com.ddchen.bridge.messchunkpc;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by yuer on 10/19/16.
 */

public class MessSender {
    private static final String HOOK_DIR = "aosp_hook";
    private static final String OUTPUT_DIR = "output";

    /**
     * 1. write data into a json file
     * 2. send command to PC
     *
     * @param data
     */
    public static void send(Context context, Object data) {
        String id = UUID.randomUUID().toString();
        String str = data.toString();

        // write data into target file
        String outputDir = context.getFilesDir() + File.separator + HOOK_DIR + File.separator + OUTPUT_DIR;
        new File(outputDir).mkdirs();

        String outputFile = outputDir + File.separator + id;

        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
