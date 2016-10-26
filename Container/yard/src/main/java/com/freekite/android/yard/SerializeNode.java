package com.freekite.android.yard;

import android.content.Context;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuer on 10/26/16.
 */

public class SerializeNode {
    /**
     * {
     * domNodeId,
     * node: {
     * attributes: {},
     * index,
     * nodeType,
     * style: {
     * shape,
     * style
     * },
     * tagName,
     * textContent
     * },
     * path: []
     * }
     *
     * @param view
     * @return
     */

    public static JSONObject serialize(View view, boolean excludePath) {
        try {
            JSONObject nodeInfo = new JSONObject();
            nodeInfo.put("tagName", view.getClass().getName());
            if(view != view.getRootView()) {
                nodeInfo.put("index", ((ViewGroup) view.getParent()).indexOfChild(view));
            }

            JSONObject attributesInfo = new JSONObject();
            attributesInfo.put("id", view.getId());
            attributesInfo.put("tag", view.getTag());

            nodeInfo.put("attributes", attributesInfo);

            JSONObject styleWrapperInfo = new JSONObject();
            styleWrapperInfo.put("style", getStyle(view));

            nodeInfo.put("style", styleWrapperInfo);
            nodeInfo.put("shape", getShape(view));

            JSONObject viewInfo = new JSONObject();
            viewInfo.put("node", nodeInfo);
            // path
            if (!excludePath) {
                viewInfo.put("path", getPathInfo(view));
            }
            // TODO textContent

            return viewInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray getPathInfo(View view) {
        JSONArray infos = new JSONArray();
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent == view.getRootView()) {
            infos.put(serialize(parent, true));
            return infos;
        }
        while (parent != view.getRootView()) {
            infos.put(serialize(parent, true));
            parent = (ViewGroup) parent.getParent();
        }

        return infos;
    }

    private static JSONObject getShape(View view) throws JSONException {
        JSONObject shapeInfo = new JSONObject();
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        shapeInfo.put("screenX", locations[0]);
        shapeInfo.put("screenY", locations[1]);
        shapeInfo.put("width", view.getWidth());
        shapeInfo.put("height", view.getHeight());

        Context context = view.getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        display.getSize(size);

        shapeInfo.put("density", metrics.density);
        shapeInfo.put("densityDpi", metrics.densityDpi);
        shapeInfo.put("screenWidthPixels", metrics.widthPixels);
        shapeInfo.put("screenHeightPixels", metrics.heightPixels);
        shapeInfo.put("scaledDensity", metrics.scaledDensity);
        shapeInfo.put("screenXdpi", metrics.xdpi);
        shapeInfo.put("screenYdpi", metrics.ydpi);
        shapeInfo.put("screenSizeX", size.x);
        shapeInfo.put("screenSizeY", size.y);

        return shapeInfo;
    }

    private static JSONObject getStyle(View view) throws JSONException {
        JSONObject styleInfo = new JSONObject();
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            styleInfo.put("accessibilityLiveRegion", view.getAccessibilityLiveRegion());
        }
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP_MR1) {
            styleInfo.put("accessibilityTraversalAfter", view.getAccessibilityTraversalAfter());
        }
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP_MR1) {
            styleInfo.put("accessibilityTraversalBefore", view.getAccessibilityTraversalBefore());
        }

        styleInfo.put("alpha", view.getAlpha());
        styleInfo.put("background", view.getBackground());

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            styleInfo.put("backgroundTintMode", view.getBackgroundTintMode());
        }

        styleInfo.put("clickable", view.isClickable());
        styleInfo.put("contentDescription", view.getContentDescription());
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            styleInfo.put("contextClickable", view.isContextClickable());
        }
        styleInfo.put("drawingCacheQuality", view.getDrawingCacheQuality());
        styleInfo.put("duplicateParentState", view.isDuplicateParentStateEnabled());
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            styleInfo.put("elevation", view.getElevation());
        }
        styleInfo.put("fadeScrollbars", view.isScrollbarFadingEnabled());
        styleInfo.put("fadingEdgeLength", view.getVerticalFadingEdgeLength());
        styleInfo.put("filterTouchesWhenObscured", view.getFilterTouchesWhenObscured());
        styleInfo.put("fitsSystemWindows", view.getFitsSystemWindows());
        styleInfo.put("focusable", view.isFocusable());
        styleInfo.put("focusableInTouchMode", view.isFocusableInTouchMode());
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            styleInfo.put("foreground", view.getForeground());
        }
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            styleInfo.put("foregroundGravity", view.getForegroundGravity());
        }

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            styleInfo.put("foregroundTintMode", view.getForegroundTintMode());
        }
        styleInfo.put("hapticFeedbackEnabled", view.isHapticFeedbackEnabled());
        styleInfo.put("importantForAccessibility", view.getImportantForAccessibility());
        styleInfo.put("isScrollContainer", view.isScrollContainer());
        styleInfo.put("keepScreenOn", view.getKeepScreenOn());
        styleInfo.put("layerType", view.getLayerType());
        styleInfo.put("layoutDirection", view.getLayoutDirection());
        styleInfo.put("longClickable", view.isLongClickable());
        styleInfo.put("minHeight", view.getMinimumHeight());
        styleInfo.put("minWidth", view.getMinimumWidth());
        styleInfo.put("nextFocusDown", view.getNextFocusDownId());
        styleInfo.put("nextFocusForward", view.getNextFocusDownId());
        styleInfo.put("nextFocusLeft", view.getNextFocusLeftId());
        styleInfo.put("nextFocusRight", view.getNextFocusRightId());
        styleInfo.put("nextFocusUp", view.getNextFocusUpId());
        styleInfo.put("paddingBottom", view.getPaddingBottom());
        styleInfo.put("paddingEnd", view.getPaddingEnd());
        styleInfo.put("paddingLeft", view.getPaddingLeft());
        styleInfo.put("paddingRight", view.getPaddingRight());
        styleInfo.put("paddingStart", view.getPaddingStart());
        styleInfo.put("paddingTop", view.getPaddingTop());
        styleInfo.put("rotation", view.getRotation());
        styleInfo.put("rotationX", view.getRotationX());
        styleInfo.put("rotationY", view.getRotationY());
        styleInfo.put("saveEnabled", view.isSaveEnabled());
        styleInfo.put("scaleX", view.getScaleX());
        styleInfo.put("scaleY", view.getScaleY());
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            styleInfo.put("scrollIndicators", view.getScrollIndicators());
        }
        styleInfo.put("scrollX", view.getScrollX());
        styleInfo.put("scrollY", view.getScrollY());
        styleInfo.put("scrollbarDefaultDelayBeforeFade", view.getScrollBarDefaultDelayBeforeFade());
        styleInfo.put("scrollbarFadeDuration", view.getScrollBarFadeDuration());
        styleInfo.put("scrollbarSize", view.getScrollBarSize());
        styleInfo.put("scrollbarStyle", view.getScrollBarStyle());
        styleInfo.put("soundEffectsEnabled", view.isSoundEffectsEnabled());
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            styleInfo.put("stateListAnimator", view.getStateListAnimator());
        }
        styleInfo.put("textAlignment", view.getTextAlignment());
        styleInfo.put("textDirection", view.getTextDirection());
        styleInfo.put("transformPivotX", view.getPivotX());
        styleInfo.put("transformPivotY", view.getPivotY());
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            styleInfo.put("transitionName", view.getTransitionName());
        }
        styleInfo.put("translationX", view.getTranslationX());
        styleInfo.put("translationY", view.getTranslationY());
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            styleInfo.put("translationZ", view.getTranslationZ());
        }
        styleInfo.put("visibility", view.getVisibility());

        return styleInfo;
    }
}