package com.movtery.zalithlauncher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Tools {
    public static final Gson GLOBAL_GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean isValidString(String string) {
        return string != null && !string.isEmpty();
    }

    public static String validOrNullString(String string) {
        if(!isValidString(string)) return null;
        return string;
    }

    public static float dpToPx(float dp) {
        // Better hope for the currentDisplayMetrics to be good
        return dp * currentDisplayMetrics.density;
    }

    public static float pxToDp(float px) {
        // Better hope for the currentDisplayMetrics to be good
        return px / currentDisplayMetrics.density;
    }

    // Note: this should *NOT* be used for positioning and sizing things on the screen
    public static DisplayMetrics currentDisplayMetrics;
}