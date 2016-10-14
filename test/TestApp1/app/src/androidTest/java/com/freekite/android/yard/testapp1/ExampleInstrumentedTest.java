package com.freekite.android.yard.testapp1;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.android.freekite.patch.aosppatch.PatchReadHookSource;
import com.android.freekite.patch.aosppatch.PatchReadHookSource.Yard;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void yardTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Yard yard = PatchReadHookSource.doRead(appContext, "com.freekite.android.yard.testapp1.ExampleInstrumentedTest");
        assertNotEquals(yard, null);
    }

    @Test
    public void monitorTest() throws Exception {
        System.out.println("--------------------------------------");
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        PatchReadHookSource.doRead(appContext, "com.freekite.android.yard.testapp1.ExampleInstrumentedTest");

        String commandFilePath = appContext.getFilesDir() + "/aosp_hook/command.json";
        FileOutputStream fos = new FileOutputStream(new File(commandFilePath));
        fos.write("test command".getBytes());
        fos.close();
    }
}
