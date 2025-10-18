/*
 * PojavMouseBridge.java
 * 
 * Bridge class that converts PojavLauncher-style mouse events
 * (string-based "mouse_move", "mouse_button", "mouse_scroll")
 * into real GLFW-style callbacks for mods like Flashback.
 */

package org.lwjgl.glfw;

public class PojavMouseBridge {

    /**
     * Translate PojavLauncher mouse events into real GLFW ImGui-compatible callbacks.
     *
     * @param type      The event type: "mouse_move", "mouse_button", "mouse_scroll"
     * @param window    The GLFW window handle (usually from Pojav’s current window)
     * @param x         Mouse X position
     * @param y         Mouse Y position
     * @param button    Mouse button (GLFW_MOUSE_BUTTON_LEFT, etc.)
     * @param action    Action (GLFW_PRESS or GLFW_RELEASE)
     * @param mods      Modifier keys (Shift, Ctrl, etc.)
     * @param scrollX   Scroll offset X
     * @param scrollY   Scroll offset Y
     */
    public static void handlePojavMouseEvent(
            String type, long window,
            double x, double y,
            int button, int action, int mods,
            double scrollX, double scrollY
    ) {
        // Mouse Move
        if ("mouse_move".equals(type)) {
            if (GLFW.mGLFWCursorPosCallback != null) {
                GLFW.mGLFWCursorPosCallback.invoke(window, x, y);
            }

        // Mouse Button (click)
        } else if ("mouse_button".equals(type)) {
            if (GLFW.mGLFWMouseButtonCallback != null) {
                GLFW.mGLFWMouseButtonCallback.invoke(window, button, action, mods);
            }

        // Mouse Scroll (wheel)
        } else if ("mouse_scroll".equals(type)) {
            if (GLFW.mGLFWScrollCallback != null) {
                GLFW.mGLFWScrollCallback.invoke(window, scrollX, scrollY);
            }
        }
    }
}
