/*
 * Tencent is pleased to support the open source community by making ncnn available.
 *
 * Copyright (C) 2021 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.yolov8ncnn;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.Surface;

public class Yolov8Ncnn {
    // Load the native library
    static {
        System.loadLibrary("yolov8ncnn");
    }

    // Native methods for JNI calls
    public native boolean loadModel(AssetManager mgr, int modelid, int cpugpu);
    public native boolean openCamera(int facing);
    public native boolean closeCamera();
    public native boolean setOutputWindow(Surface surface);
    public native String detect(Bitmap bitmap);

    /**
     * Method to initialize and load YOLOv8 model.
     *
     * @param assetManager The AssetManager to load model from assets.
     * @param modelId      Model identifier.
     * @param cpuGpu       Flag to specify whether to use CPU or GPU.
     * @return True if model loaded successfully, false otherwise.
     */
    public boolean initializeModel(AssetManager assetManager, int modelId, int cpuGpu) {
        return loadModel(assetManager, modelId, cpuGpu);
    }

    /**
     * Method to perform object detection on a Bitmap.
     *
     * @param bitmap The input Bitmap for object detection.
     * @return String containing the detection results.
     */
    public String performDetection(Bitmap bitmap) {
        return detect(bitmap);
    }
}
