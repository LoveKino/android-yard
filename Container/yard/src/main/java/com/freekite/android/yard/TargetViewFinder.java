package com.freekite.android.yard;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by yuer on 10/26/16.
 */

public class TargetViewFinder {
    public static View find(MotionEvent ev) {
        Activity currentActivity = getCurrentActivity();
        ViewGroup root = (ViewGroup) currentActivity.findViewById(android.R.id.content);
        ArrayList<View> views = findPositionViews(root, ev.getRawX(), ev.getRawY());

        if (views.isEmpty()) return null;

        // TODO consider cover problem
        return views.get(0);
    }

    public static ArrayList<View> findPositionViews(View root, float x, float y) {
        ArrayList<View> viewList = new ArrayList<>();
        if (isPointInsideView(x, y, root)) {
            if (root instanceof ViewGroup) {
                ViewGroup rootG = (ViewGroup) root;
                int childCount = rootG.getChildCount();
                boolean childMeet = false;
                for (int i = 0; i < childCount; i++) {
                    View child = rootG.getChildAt(i);
                    ArrayList<View> childList = findPositionViews(child, x, y);
                    if (!childList.isEmpty()) {
                        childMeet = true;
                    }
                    viewList.addAll(childList);
                }
                if (!childMeet) {
                    viewList.add(root);
                }
            } else {
                viewList.add(root);
            }
        }

        return viewList;
    }

    public static boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();

        return (x >= viewX && x <= (viewX + viewWidth)) &&
                (y >= viewY && y <= (viewY + viewHeight));
    }

    public static Activity getCurrentActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if (activities == null)
                return null;

            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }
}
