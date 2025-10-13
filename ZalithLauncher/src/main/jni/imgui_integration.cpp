#include <jni.h>
#include <android/log.h>
#include <unordered_map>

#define LOG_TAG "ImGuiIntegration"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// Simulated ImGui state
struct ImGuiIO {
    bool KeysDown[512] = {false};
    bool MouseDown[5] = {false};
    float MouseWheel = 0.0f;
    float MouseWheelH = 0.0f;
    float MousePosX = 0.0f;
    float MousePosY = 0.0f;
    bool KeyCtrl = false;
    bool KeyShift = false;
    bool KeyAlt = false;
    bool KeySuper = false;
    
    void AddInputCharacter(unsigned int c) {
        // Character input handling
    }
};

static ImGuiIO g_ImGuiIO;

// GLFW key constants
const int GLFW_PRESS = 1;
const int GLFW_RELEASE = 0;
const int GLFW_KEY_LEFT_CONTROL = 341;
const int GLFW_KEY_RIGHT_CONTROL = 345;
const int GLFW_KEY_LEFT_SHIFT = 340;
const int GLFW_KEY_RIGHT_SHIFT = 344;
const int GLFW_KEY_LEFT_ALT = 342;
const int GLFW_KEY_RIGHT_ALT = 346;

extern "C" {

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_ImGuiIntegration_forwardToImGuiKeyCallback(JNIEnv *env, jclass clazz, 
    jlong window, jint key, jint scancode, jint action, jint mods) {
    
    if (key >= 0 && key < 512) {
        g_ImGuiIO.KeysDown[key] = (action == GLFW_PRESS);
    }
    
    // Update modifier keys
    g_ImGuiIO.KeyCtrl = g_ImGuiIO.KeysDown[GLFW_KEY_LEFT_CONTROL] || g_ImGuiIO.KeysDown[GLFW_KEY_RIGHT_CONTROL];
    g_ImGuiIO.KeyShift = g_ImGuiIO.KeysDown[GLFW_KEY_LEFT_SHIFT] || g_ImGuiIO.KeysDown[GLFW_KEY_RIGHT_SHIFT];
    g_ImGuiIO.KeyAlt = g_ImGuiIO.KeysDown[GLFW_KEY_LEFT_ALT] || g_ImGuiIO.KeysDown[GLFW_KEY_RIGHT_ALT];
    
    LOGI("Key event: key=%d, action=%d", key, action);
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_ImGuiIntegration_forwardToImGuiMouseButtonCallback(JNIEnv *env, jclass clazz,
    jlong window, jint button, jint action, jint mods) {
    
    if (button >= 0 && button < 5) {
        g_ImGuiIO.MouseDown[button] = (action == GLFW_PRESS);
    }
    
    LOGI("Mouse button: button=%d, action=%d", button, action);
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_ImGuiIntegration_forwardToImGuiScrollCallback(JNIEnv *env, jclass clazz,
    jlong window, jdouble xoffset, jdouble yoffset) {
    
    g_ImGuiIO.MouseWheelH += (float)xoffset;
    g_ImGuiIO.MouseWheel += (float)yoffset;
    
    LOGI("Scroll event: x=%.2f, y=%.2f", xoffset, yoffset);
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_ImGuiIntegration_forwardToImGuiCharCallback(JNIEnv *env, jclass clazz,
    jlong window, jint codepoint) {
    
    if (codepoint > 0 && codepoint < 0x10000) {
        g_ImGuiIO.AddInputCharacter((unsigned int)codepoint);
    }
    
    LOGI("Char event: codepoint=%d", codepoint);
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_ImGuiIntegration_forwardToImGuiCursorPosCallback(JNIEnv *env, jclass clazz,
    jlong window, jdouble xpos, jdouble ypos) {
    
    g_ImGuiIO.MousePosX = (float)xpos;
    g_ImGuiIO.MousePosY = (float)ypos;
    
    LOGI("Cursor pos: x=%.2f, y=%.2f", xpos, ypos);
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_ImGuiIntegration_updateImGuiFrame(JNIEnv *env, jclass clazz) {
    // Reset scroll values for new frame
    g_ImGuiIO.MouseWheel = 0.0f;
    g_ImGuiIO.MouseWheelH = 0.0f;
}

// Native methods for GLFW
JNIEXPORT jdoubleArray JNICALL
Java_org_lwjgl_glfw_GLFW_nativeGetCursorPos(JNIEnv *env, jclass clazz) {
    jdoubleArray result = env->NewDoubleArray(2);
    jdouble pos[2] = {g_ImGuiIO.MousePosX, g_ImGuiIO.MousePosY};
    env->SetDoubleArrayRegion(result, 0, 2, pos);
    return result;
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_GLFW_nativeSetCursorPos(JNIEnv *env, jclass clazz, jint x, jint y) {
    g_ImGuiIO.MousePosX = (float)x;
    g_ImGuiIO.MousePosY = (float)y;
}

}
