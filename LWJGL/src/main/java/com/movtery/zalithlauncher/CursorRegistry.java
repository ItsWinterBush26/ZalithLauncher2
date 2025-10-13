package org.lwjgl.glfw;

public class CallbackBridge {
    public static final int CLIPBOARD_COPY = 2000;
    public static final int CLIPBOARD_PASTE = 2001;
    
    public static final int EVENT_TYPE_CHAR = 1000;
    public static final int EVENT_TYPE_CHAR_MODS = 1001;
    public static final int EVENT_TYPE_CURSOR_ENTER = 1002;
    public static final int EVENT_TYPE_CURSOR_POS = 1003;
    public static final int EVENT_TYPE_FRAMEBUFFER_SIZE = 1004;
    public static final int EVENT_TYPE_KEY = 1005;
    public static final int EVENT_TYPE_MOUSE_BUTTON = 1006;
    public static final int EVENT_TYPE_SCROLL = 1007;
    public static final int EVENT_TYPE_WINDOW_SIZE = 1008;
    
    public static final int ANDROID_TYPE_GRAB_STATE = 0;
    
    public static final boolean INPUT_DEBUG_ENABLED;
    
    // Cursor state
    private static double cursorX = 0;
    private static double cursorY = 0;
    private static final boolean[] mouseButtons = new boolean[8];
    
    static {
        INPUT_DEBUG_ENABLED = Boolean.parseBoolean(System.getProperty("glfwstub.debugInput", "false"));
    }

    public static void sendData(int type, String data) {
        nativeSendData(false, type, data);
    }
    
    public static native void nativeSendData(boolean isAndroid, int type, String data);
    public static native boolean nativeSetInputReady(boolean ready);
    public static native String nativeClipboard(int action, byte[] copy);
    public static native void nativeSetGrabbing(boolean grab);
    public static native void nativeSetCursorShape(int shape);
    
    // New methods for ImGui integration
    public static void receiveCallback(int eventType, int arg1, int arg2, int arg3, int arg4) {
        if (INPUT_DEBUG_ENABLED) {
            System.out.println("CallbackBridge: Event " + eventType + " - " + arg1 + "," + arg2 + "," + arg3 + "," + arg4);
        }
        
        switch (eventType) {
            case EVENT_TYPE_CURSOR_POS:
                processCursorPos(arg1, arg2);
                break;
            case EVENT_TYPE_MOUSE_BUTTON:
                processMouseButton(arg1, arg2, arg3);
                break;
            case EVENT_TYPE_SCROLL:
                processScroll(arg1, arg2);
                break;
            case EVENT_TYPE_KEY:
                processKeyEvent(arg1, arg2, arg3, arg4);
                break;
            case EVENT_TYPE_CHAR:
                processCharEvent(arg1);
                break;
        }
    }
    
    private static void processCursorPos(int x, int y) {
        cursorX = x;
        cursorY = y;
        ImGuiIntegration.processCursorPosEvent(x, y);
    }
    
    private static void processMouseButton(int button, int action, int mods) {
        if (button >= 0 && button < mouseButtons.length) {
            mouseButtons[button] = (action == GLFW.GLFW_PRESS);
        }
        ImGuiIntegration.processMouseButtonEvent(button, action, mods);
    }
    
    private static void processScroll(double xoffset, double yoffset) {
        ImGuiIntegration.processScrollEvent(xoffset, yoffset);
    }
    
    private static void processKeyEvent(int key, int scancode, int action, int mods) {
        ImGuiIntegration.processKeyEvent(key, scancode, action, mods);
    }
    
    private static void processCharEvent(int codepoint) {
        ImGuiIntegration.processCharEvent(codepoint);
    }
    
    // Methods called from GLFW
    public static double[] getCursorPos() {
        return new double[]{cursorX, cursorY};
    }
    
    public static int getMouseButton(int button) {
        if (button >= 0 && button
