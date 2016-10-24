package com.freekite.android.yard;

import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuer on 10/20/16.
 * <p>
 * serialize event information
 */

public class SerializeEvent {
    public static JSONObject serialize(MotionEvent ev) {
        JSONObject eventInfo = new JSONObject();
        try {
            eventInfo.put("action", ev.getAction());
            return eventInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
