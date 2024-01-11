package org.pjsip;

import java.util.Arrays;
import java.util.List;

import android.util.Range;
import android.util.Size;
import android.util.Log;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraAccessException;

import android.graphics.ImageFormat;
import android.view.SurfaceHolder;
import android.content.Context;

public class PjCameraInfo {
    public static Context mContext;

    public int facing;
    public int orient;
    public int[] supportedSize;        // [w1, h1, w2, h2, ...]
    public int[] supportedFps1000;    // [min1, max1, min2, max2, ...]
    public int[] supportedFormat;    // [fmt1, fmt2, ...]

    // convert Format list {fmt1, fmt2, ...} to [fmt1, fmt2, ...]
    private static int[] IntegerListToIntArray(List<Integer> list) {
        int[] li = new int[list.size()];
        int i = 0;
        for (Integer e : list) {
            li[i++] = e.intValue();
        }
        return li;
    }

    // convert Fps list {[min1, max1], [min2, max2], ...} to
    // [min1, max1, min2, max2, ...]
    private static int[] IntArrayListToIntArray(List<int[]> list) {
        int[] li = new int[list.size() * 2];
        int i = 0;
        for (int[] e : list) {
            li[i++] = e[0];
            li[i++] = e[1];
        }
        return li;
    }

    // convert Fps list {[min1, max1], [min2, max2], ...} to
    // [min1, max1, min2, max2, ...]
    private static int[] IntArrayListToIntArray2(Range[] list) {
        int[] li = new int[list.length * 2];
        int i = 0;
        for (Range<Integer> e : list) {
            li[i++] = e.getLower() * 1000;
            li[i++] = e.getUpper() * 1000;
        }
        return li;
    }

    // convert Size list {{w1, h1}, {w2, h2}, ...} to [w1, h1, w2, h2, ...]
    private static int[] CameraSizeListToIntArray(List<Camera.Size> list) {
        int[] li = new int[list.size() * 2];
        int i = 0;
        for (Camera.Size e : list) {
            li[i++] = e.width;
            li[i++] = e.height;
        }
        return li;
    }

    // convert Size list {{w1, h1}, {w2, h2}, ...} to [w1, h1, w2, h2, ...]
    private static int[] CameraSizeListToIntArray2(Size[] list) {
        int[] li = new int[list.length * 2];
        int i = 0;
        for (Size e : list) {
            li[i++] = e.getWidth();
            li[i++] = e.getHeight();
        }
        return li;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static int GetCameraCount() {
        return Camera.getNumberOfCameras();
    }

    // Get camera info: facing, orientation, supported size/fps/format.
    // Camera must not be opened.
    public static PjCameraInfo GetCameraInfo(int idx) {
        if (idx < 0 || idx >= GetCameraCount())
            return null;

        Camera cam;
        try {
            //cam = Camera.open(idx);
        } catch (Exception e) {
            Log.d("IOException", e.getMessage());
            return null;
        }

        PjCameraInfo pjci = new PjCameraInfo();
        PjCameraInfo pjci2 = new PjCameraInfo();

        CameraInfo ci = new CameraInfo();
        Camera.getCameraInfo(idx, ci);

        pjci.facing = ci.facing;
        pjci.orient = ci.orientation;

        //Camera.Parameters param = cam.getParameters();
        //cam.release();
        //cam = null;

        CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(Integer.toString(idx));
            pjci2.facing = characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT ? CameraInfo.CAMERA_FACING_FRONT : CameraInfo.CAMERA_FACING_BACK;
            pjci2.orient = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            pjci2.supportedFormat = new int[]{ImageFormat.YV12, ImageFormat.NV21};
            pjci2.supportedFps1000 = IntArrayListToIntArray2(characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
            pjci2.supportedSize = CameraSizeListToIntArray2(characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(SurfaceHolder.class));
            Log.d("CameraInfo2 facing", Integer.toString(pjci2.facing));
            Log.d("CameraInfo2 orient", Integer.toString(pjci2.orient));
            Log.d("CameraInfo2 format", Arrays.toString(pjci2.supportedFormat));
            Log.d("CameraInfo2 fps", Arrays.toString(pjci2.supportedFps1000));
            Log.d("CameraInfo2 size", Arrays.toString(pjci2.supportedSize));
        } catch (CameraAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        /*pjci.supportedFormat = IntegerListToIntArray(
                param.getSupportedPreviewFormats());
        pjci.supportedFps1000 = IntArrayListToIntArray(
                param.getSupportedPreviewFpsRange());
        pjci.supportedSize = CameraSizeListToIntArray(
                param.getSupportedPreviewSizes());
        Log.d("CameraInfo1 facing", Integer.toString(pjci.facing));
        Log.d("CameraInfo1 orient", Integer.toString(pjci.orient));
        Log.d("CameraInfo1 format", Arrays.toString(pjci.supportedFormat));
        Log.d("CameraInfo1 fps", Arrays.toString(pjci.supportedFps1000));
        Log.d("CameraInfo1 size", Arrays.toString(pjci.supportedSize));*/
        return pjci2;
    }
}
