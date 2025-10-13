package org.lwjgl.glfw;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiIntegration {
    
    private static boolean imGuiInitialized = false;
    
    public static void initializeImGuiForWindow(long window) {
        if (imGuiInitialized) return;
        
        // Set up ImGui callbacks that forward events to both ImGui and existing callbacks
        glfwSetKeyCallback(window, ImGuiIntegration::keyCallback);
        glfwSetMouseButtonCallback(window, ImGuiIntegration::mouseButtonCallback);
        glfwSetScrollCallback(window, ImGuiIntegration::scrollCallback);
        glfwSetCharCallback(window, ImGuiIntegration::charCallback);
        glfwSetCursorPosCallback(window, ImGuiIntegration::cursorPosCallback);
        
        imGuiInitialized = true;
    }
    
    private static void keyCallback(long window, int key, int scancode, int action, int mods) {
        // Forward to ImGui first
        forwardToImGuiKeyCallback(window, key, scancode, action, mods);
        
        // Then forward to existing GLFW callback
        if (mGLFWKeyCallback != null) {
            mGLFWKeyCallback.invoke(window, key, scancode, action, mods);
        }
    }
    
    private static void mouseButtonCallback(long window, int button, int action, int mods) {
        // Forward to ImGui first
        forwardToImGuiMouseButtonCallback(window, button, action, mods);
        
        // Then forward to existing GLFW callback
        if (mGLFWMouseButtonCallback != null) {
            mGLFWMouseButtonCallback.invoke(window, button, action, mods);
        }
    }
    
    private static void scrollCallback(long window, double xoffset, double yoffset) {
        // Forward to ImGui first
        forwardToImGuiScrollCallback(window, xoffset, yoffset);
        
        // Then forward to existing GLFW callback
        if (mGLFWScrollCallback != null) {
            mGLFWScrollCallback.invoke(window, xoffset, yoffset);
        }
    }
    
    private static void charCallback(long window, int codepoint) {
        // Forward to ImGui first
        forwardToImGuiCharCallback(window, codepoint);
        
        // Then forward to existing GLFW callback
        if (mGLFWCharCallback != null) {
            mGLFWCharCallback.invoke(window, codepoint);
        }
    }
    
    private static void cursorPosCallback(long window, double xpos, double ypos) {
        // Forward to ImGui first
        forwardToImGuiCursorPosCallback(window, xpos, ypos);
        
        // Then forward to existing GLFW callback
        if (mGLFWCursorPosCallback != null) {
            mGLFWCursorPosCallback.invoke(window, xpos, ypos);
        }
    }
    
    // Native methods that forward to ImGui implementation
    private static native void forwardToImGuiKeyCallback(long window, int key, int scancode, int action, int mods);
    private static native void forwardToImGuiMouseButtonCallback(long window, int button, int action, int mods);
    private static native void forwardToImGuiScrollCallback(long window, double xoffset, double yoffset);
    private static native void forwardToImGuiCharCallback(long window, int codepoint);
    private static native void forwardToImGuiCursorPosCallback(long window, double xpos, double ypos);
    
    public static void updateImGui() {
        // This should be called every frame before ImGui rendering
        updateImGuiFrame();
    }
    
    private static native void updateImGuiFrame();
  }
