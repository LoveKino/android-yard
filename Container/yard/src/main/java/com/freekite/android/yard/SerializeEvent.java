package com.freekite.android.yard;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by yuer on 10/20/16.
 * <p>
 * serialize event information
 */

public class SerializeEvent {
    public static JSONObject serialize(MotionEvent ev) {
        JSONObject eventInfo = new JSONObject();
        JSONObject motionEventConstants = new JSONObject();
        try {
            Field[] declaredFields = MotionEvent.class.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                // get constants
                if (Modifier.isStatic(field.getModifiers())) {
                    Class<?> t = field.getType();
                    if (t == int.class) {
                        motionEventConstants.put(field.getName(), field.getInt(null));
                    } else if (t == double.class) {
                        motionEventConstants.put(field.getName(), field.getDouble(null));
                    } else if (t == float.class) {
                        motionEventConstants.put(field.getName(), field.getFloat(null));
                    } else if (t == boolean.class) {
                        motionEventConstants.put(field.getName(), field.getBoolean(null));
                    } else if (t == short.class) {
                        motionEventConstants.put(field.getName(), field.getShort(null));
                    } else if (t == long.class) {
                        motionEventConstants.put(field.getName(), field.getLong(null));
                    } else if (t == char.class) {
                        motionEventConstants.put(field.getName(), field.getChar(null));
                    } else if (t == String.class) {
                        motionEventConstants.put(field.getName(), field.get(null).toString());
                    }
                }
            }

            eventInfo.put("CONSTANT", motionEventConstants);

            eventInfo.put("action", ev.getAction());
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                eventInfo.put("actionButton", ev.getActionButton());
            }
            eventInfo.put("actionIndex", ev.getActionIndex());
            eventInfo.put("actionMasked", ev.getActionMasked());
            eventInfo.put("buttonState", ev.getButtonState());
            eventInfo.put("deviceId", ev.getDeviceId());
            eventInfo.put("downTime", ev.getDownTime());
            eventInfo.put("edgeFlags", ev.getEdgeFlags());
            eventInfo.put("eventTime", ev.getEventTime());
            eventInfo.put("flags", ev.getFlags());
            eventInfo.put("historySize", ev.getHistorySize());
            eventInfo.put("metaState", ev.getMetaState());
            eventInfo.put("orientation", ev.getOrientation());
            eventInfo.put("pointerCount", ev.getPointerCount());
            eventInfo.put("pressure", ev.getPressure());
            eventInfo.put("rawX", ev.getRawX());
            eventInfo.put("rawY", ev.getRawY());
            eventInfo.put("size", ev.getSize());
            eventInfo.put("source", ev.getSource());
            eventInfo.put("toolMajor", ev.getToolMajor());
            eventInfo.put("toolMinor", ev.getToolMinor());
            eventInfo.put("touchMajor", ev.getTouchMajor());
            eventInfo.put("touchMinor", ev.getTouchMinor());
            eventInfo.put("X", ev.getX());
            eventInfo.put("XPrecision", ev.getXPrecision());
            eventInfo.put("Y", ev.getYPrecision());

            return eventInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
