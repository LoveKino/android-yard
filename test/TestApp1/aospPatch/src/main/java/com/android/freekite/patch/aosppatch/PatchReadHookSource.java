package com.android.freekite.patch.aosppatch;

import android.content.Context;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by yuer on 10/10/16.
 * @{hide}
 *
 *
 * important: test this file on original emulator not you'r hooked emulator
 */
public class PatchReadHookSource {
    private static final String HOOK_DIR = "aosp_hook";
    private static final String HOOK_FILE = "yard-dex.jar";
    private static final String CODE_CACHE_DIR = "aosp_hook_code_cache";
    private static final String HOOK_CLASS_NAME = "com.freekite.android.yard.Yard";

    public interface Yard {
        void receive(String type, Object[] infoList);
    }

    private static Yard fakeYard = new Yard() {
        @Override
        public void receive(String type, Object[] infoList) {
        }
    };

    /**
     * @hide
     */
    public static Yard doRead(final Context context, String tag) {
        try {

            System.out.println("start read:********************************");

            // code cache dir
            String cacheDir = context.getFilesDir() + File.separator + CODE_CACHE_DIR;
            new File(cacheDir).mkdirs();

            String hookFile = context.getFilesDir() + File.separator + HOOK_DIR + File.separator + HOOK_FILE;

            DexClassLoader cl = new DexClassLoader(
                    hookFile,
                    cacheDir,
                    null,
                    context.getClassLoader());
            System.out.println("before:********************************");

            final Class Test = cl.loadClass(HOOK_CLASS_NAME);

            System.out.println("********************************");

            System.out.println(Test);

            final Object test = Test.getConstructor(Object.class, String.class).newInstance(context, tag);

            return new Yard() {
                @Override
                public void receive(String type, Object[] infoList) {
                    try {
                        Method receiveMethod = Test.getDeclaredMethod("receive", String.class, Object[].class);
                        receiveMethod.invoke(test, type, infoList);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fakeYard;
    }
}