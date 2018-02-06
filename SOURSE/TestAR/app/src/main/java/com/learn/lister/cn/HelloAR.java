//================================================================================================================================
//
//  Copyright (c) 2015-2017 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.learn.lister.cn;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import cn.easyar.CameraCalibration;
import cn.easyar.CameraDevice;
import cn.easyar.CameraDeviceFocusMode;
import cn.easyar.CameraDeviceType;
import cn.easyar.CameraFrameStreamer;
import cn.easyar.Frame;
import cn.easyar.FunctorOfVoidFromPointerOfTargetAndBool;
import cn.easyar.ImageTarget;
import cn.easyar.ImageTracker;
import cn.easyar.Renderer;
import cn.easyar.StorageType;
import cn.easyar.Target;
import cn.easyar.TargetInstance;
import cn.easyar.TargetStatus;
import cn.easyar.Vec2I;
import cn.easyar.Vec4I;

public class HelloAR {

    /**
     * 退出红包界面后 3s 后可以再次抢红包
     */
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Constants.flag = 0;
                removeCallbacksAndMessages(0);
            }
        }
    };

    /**
     * 数据操作的对象
     */
    private DataStore dataStorage;

    /**
     * 上下文，用于跳转
     */
    private Context mContext;

    private Handler mHandler;

    private CameraDevice camera;
    private CameraFrameStreamer streamer;
    private ArrayList<ImageTracker> trackers;
    private Renderer video_renderer;
    private BoxRenderer box_renderer;
    private boolean viewport_changed = false;
    private Vec2I view_size = new Vec2I(0, 0);
    private int rotation = 0;
    private Vec4I viewport = new Vec4I(0, 0, 1280, 720);

    public HelloAR(Context context, Handler handler) {
        trackers = new ArrayList<>();
        mContext = context;
        dataStorage = new DataStore(mContext);
        mHandler = handler;
    }

    private void loadFromImage(ImageTracker tracker, String path) {
        /**
         * ImageTarget 表示平面图像的 target
         * 它可以被 ImageTracker 追踪
         */
        ImageTarget target = new ImageTarget();
        /**
         * jstr: 含有文件路径以及文件名的 json
         */
        String jstr = "{\n"
            + "  \"images\" :\n"
            + "  [\n"
            + "    {\n"
            + "      \"image\" : \"" + path + "\",\n"
            + "      \"name\" : \"" + path.substring(0, path.indexOf(".")) + "\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";
        target.setup(jstr, StorageType.Assets | StorageType.Json, "");
        /**
         * 载入 ImageTarget
         */
        tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
            @Override
            public void invoke(Target target, boolean status) {
                Log.e("HelloAR", "loadFromImage: " +
                        String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
            }
        });
    }

    private void loadFromJsonFile(ImageTracker tracker, String path, String targetname) {
        ImageTarget target = new ImageTarget();
        target.setup(path, StorageType.Assets, targetname);
        //加载一个 Target 进入tracker。Target只有在成功加载进入tracker之后才能被识别和跟踪。
        tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
            @Override
            public void invoke(Target target, boolean status) {
                Log.e("HelloAR", "loadFromJsonFile: " +
                        String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
            }
        });
    }

    /**
     * 加载 asserts 中 json 文件中的所有图片
     */
    private void loadAllFromJsonFile(ImageTracker tracker, String path) {
        for (ImageTarget target : ImageTarget.setupAll(path, StorageType.Assets)) {
            tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
                @Override
                public void invoke(Target target, boolean status) {
                    Log.e("HelloAR", "loadAllFromJsonFile: " +
                            String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
                }
            });
        }
    }

    /**
     * 加载外部文件路径的图片
     */
    private void loadExternalFromJsonFile(ImageTracker tracker, String path) {
        for (ImageTarget target : ImageTarget.setupAll(path, StorageType.Absolute)) {
            tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
                @Override
                public void invoke(Target target, boolean status) {
                    Log.e("HelloAR", "loadAllFromJsonFile: " +
                            String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
                }
            });
        }
    }

    /**
     * 初始化
     */
    public boolean initialize() {

        camera = new CameraDevice();
        streamer = new CameraFrameStreamer();
        streamer.attachCamera(camera);

        boolean status = true;
        status &= camera.open(CameraDeviceType.Default);
        camera.setSize(new Vec2I(1280, 720));

        if (!status) { return status; }
        ImageTracker tracker = new ImageTracker();
        tracker.attachStreamer(streamer);
        /**
         * 将收集到的 json 字符串保存到本地文件
         */
        String jsonFileName = "miao.json";
        String json = "{\n" +
                "  \"images\" :\n" +
                "  [\n" +
                "    {\n" +
                "      \"image\" : \"/storage/emulated/0/arimg/pic1.jpg\",\n" +
                "      \"name\" : \"D.Va\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"image\" : \"/storage/emulated/0/arimg/pic2.jpg\",\n" +
                "      \"name\" : \"猎空\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"image\" : \"/storage/emulated/0/arimg/pic3.jpg\",\n" +
                "      \"name\" : \"黑百合\"\n" +
                "    },\n" +
                "\t{\n" +
                "      \"image\" : \"/storage/emulated/0/arimg/pic4.jpg\",\n" +
                "      \"name\" : \"天使\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        dataStorage.saveJson(json, jsonFileName);

        /**
         * 一共有三种方法去加载 assets 下的图片资源文件
         */
        // loadFromImage(tracker, "namecard.jpg");
        // loadFromJsonFile(tracker, "targets.json", "argame");
        // loadFromJsonFile(tracker, "targets.json", "idback");
        // loadAllFromJsonFile(tracker, "targets2.json");
        loadAllFromJsonFile(tracker, "targets.json");
        /**
         * 获取外部 storage 中 json 的路径，加载这个路径中的所有图片
         */
        String externalPath = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "arimg" + File.separator + jsonFileName;
        loadExternalFromJsonFile(tracker, externalPath);

        trackers.add(tracker);

        return status;
    }


    public void dispose() {
        for (ImageTracker tracker : trackers) {
            tracker.dispose();
        }
        trackers.clear();
        box_renderer = null;
        if (video_renderer != null) {
            video_renderer.dispose();
            video_renderer = null;
        }
        if (streamer != null) {
            streamer.dispose();
            streamer = null;
        }
        if (camera != null) {
            camera.dispose();
            camera = null;
        }
    }

    public boolean start() {
        boolean status = true;
        status &= (camera != null) && camera.start();
        status &= (streamer != null) && streamer.start();
        camera.setFocusMode(CameraDeviceFocusMode.Continousauto);
        for (ImageTracker tracker : trackers) {
            status &= tracker.start();
        }
        return status;
    }

    public boolean stop() {
        boolean status = true;
        for (ImageTracker tracker : trackers) {
            status &= tracker.stop();
        }
        status &= (streamer != null) && streamer.stop();
        status &= (camera != null) && camera.stop();
        return status;
    }

    /**
     * 初始化
     */
    public void initGL() {
        if (video_renderer != null) {
            video_renderer.dispose();
        }
        video_renderer = new Renderer();
        box_renderer = new BoxRenderer();
        box_renderer.init();
    }

    /**
     * 重新绘制区域
     */
    public void resizeGL(int width, int height) {
        view_size = new Vec2I(width, height);
        viewport_changed = true;
    }

    private void updateViewport() {
        CameraCalibration calib = camera != null ? camera.cameraCalibration() : null;
        int rotation = calib != null ? calib.rotation() : 0;

        if (rotation != this.rotation) {
            this.rotation = rotation;
            viewport_changed = true;
        }

        if (viewport_changed) {
            Vec2I size = new Vec2I(1, 1);
            if ((camera != null) && camera.isOpened()) {
                size = camera.size();
            }
            if (rotation == 90 || rotation == 270) {
                size = new Vec2I(size.data[1], size.data[0]);
            }
            float scaleRatio = Math.max((float) view_size.data[0] / (float) size.data[0], (float) view_size.data[1] / (float) size.data[1]);
            Vec2I viewport_size = new Vec2I(Math.round(size.data[0] * scaleRatio), Math.round(size.data[1] * scaleRatio));
            viewport = new Vec4I((view_size.data[0] - viewport_size.data[0]) / 2, (view_size.data[1] - viewport_size.data[1]) / 2, viewport_size.data[0], viewport_size.data[1]);

            if ((camera != null) && camera.isOpened())
                viewport_changed = false;
        }
    }

    /**
     * 绘制区域
     */
    public void render() {
        /**
         * 清除深度缓冲与颜色缓冲
         */
        GLES20.glClearColor(1.f, 1.f, 1.f, 1.f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (video_renderer != null) {
            Vec4I default_viewport = new Vec4I(0, 0, view_size.data[0], view_size.data[1]);
            GLES20.glViewport(default_viewport.data[0], default_viewport.data[1],
                    default_viewport.data[2], default_viewport.data[3]);
            if (video_renderer.renderErrorMessage(default_viewport)) {
                return;
            }
        }

        if (streamer == null) { return; }
        /**
         * Frame 用来存储追踪到的数据
         * 包含当前的 Camera 图像，跟踪到的 Target
         */
        Frame frame = streamer.peek();
        try {
            /**
             * ...
             */
            updateViewport();
            GLES20.glViewport(viewport.data[0], viewport.data[1], viewport.data[2], viewport.data[3]);

            if (video_renderer != null) {
                video_renderer.render(frame, viewport);
            }

            for (TargetInstance targetInstance : frame.targetInstances()) {

                int status = targetInstance.status();
                /**
                 * 如果已经被追踪到
                 */
                if (status == TargetStatus.Tracked && Constants.flag < 1) {
                    /**
                     * 每次追踪到目标，标志量都 +1
                     */
                    Constants.flag ++;
                    /**
                     * 获取目标的信息
                     */
                    Target target = targetInstance.target();
                    ImageTarget imagetarget = target instanceof ImageTarget ? (ImageTarget) (target) : null;
                    /**
                     * 判断标志量，如果是 1 就发消息在主线程显示 AlertDialog
                     */
                    if (Constants.flag == 1) {
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("fileName", target.name());
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }

                    if (imagetarget == null) {
                        continue;
                    }
                    if (box_renderer != null) {
                        box_renderer.render(camera.projectionGL(0.2f, 500.f),
                                targetInstance.poseGL(), imagetarget.size());
                    }
                }
            }
        }
        finally {
            frame.dispose();
        }
    }
}
