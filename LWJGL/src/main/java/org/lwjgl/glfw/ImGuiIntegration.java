package org.lwjgl.glfw;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiIntegration {
    
    private static boolean imGuiInitialized = false;
    private static long currentWindow = 0;
    
    public static void initializeImGuiForWindow(long window) {
        if (imGuiInitialized) return;
        
        currentWindow = window;
        
        // Set up ImGui callbacks that forward events to both ImGui and existing callbacks
        setupCallbacks(window);
        
        imGuiInitialized = true;
    }
    
    private static void setupCallbacks(long window) {
        // Store original callbacks
        GLFWKeyCallback originalKeyCallback = mGLFWKeyCallback;
        GLFWMouseButtonCallback originalMouseCallback = mGLFWMouseButtonCallback;
        GLFWScrollCallback originalScrollCallback = mGLFWScrollCallback;
        GLFWCharCallback originalCharCallback = mGLFWCharCallback;
        GLFWCursorPosCallback originalCursorPosCallback = mGLFWCursorPosCallback;
        
        // Set new callbacks that forward to both ImGui and original callbacks
        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            forwardToImGuiKeyCallback(win, key, scancode, action, mods);
            if (originalKeyCallback != null) {
                originalKeyCallback.invoke(win, key, scancode, action, mods);
            }
        });
        
        glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {
            forwardToImGuiMouseButtonCallback(win, button, action, mods);
            if (originalMouseCallback != null) {
                originalMouseCallback.invoke(win, button, action, mods);
            }
        });
        
        glfwSetScrollCallback(window, (win, xoffset, yoffset) -> {
            forwardToImGuiScrollCallback(win, xoffset, yoffset);
            if (originalScrollCallback != null) {
                originalScrollCallback.invoke(win, xoffset, yoffset);
            }
        });
        
        glfwSetCharCallback(window, (win, codepoint) -> {
            forwardToImGuiCharCallback(win, codepoint);
            if (originalCharCallback != null) {
                originalCharCallback.invoke(win, codepoint);
            }
        });
        
        glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {
            forwardToImGuiCursorPosCallback(win, xpos, ypos);
            if (originalCursorPosCallback != null) {
                originalCursorPosCallback.invoke(win, xpos, ypos);
            }
        });
    }
    
    // Native methods that forward to ImGui implementation
    private static native void forwardToImGuiKeyCallback(long window, int key, int scancode, int action, int mods);
    private static native void forwardToImGuiMouseButtonCallback(long window, int button, int action, int mods);
    private static native void forwardToImGuiScrollCallback(long window, double xoffset, double yoffset);
    private static native void forwardToImGuiCharCallback(long window, int codepoint);
    private static native void forwardToImGuiCursorPosCallback(long window, double xpos, double ypos);
    
    public static void updateImGui() {
        if (imGuiInitialized) {
            updateImGuiFrame();
        }
    }
    
    private static native void updateImGuiFrame();
    
    public static boolean isImGuiInitialized() {
        return imGuiInitialized;
    }
    
    public static long getCurrentWindow() {
        return currentWindow;
    }
}
