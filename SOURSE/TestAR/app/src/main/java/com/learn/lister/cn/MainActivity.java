//================================================================================================================================
//
//  Copyright (c) 2015-2017 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.learn.lister.cn;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.HashMap;

import cn.easyar.engine.EasyAR;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {


  //  private static String key=
           //"GAJLyBZqMY5YpiacXm3vwNmGK29nVXhKpC3QWMFVKXvKakaz6umbj1g1wzKwnjUUllzZwBacTShn8bjxadSfLkbLkkn5SNizsQceZmEnULLCfN2ZdOyn9pVyp5m5nalwmENKMjRewl8m8X0RukQEL5pdYioGqdj4TlDcNBFfikOlF8iZW0Ha8FZ2BrUp0dXd4Fr9iOuR";
     private static  String key=
          "v3qdQiEO5SmUWCOBZiWMLEXaUH7dJmB8tNhq1c3rwfHOOEp7zdtuYIDq4K9Hbmt5alNakkbHmaEJN7aSpMim6osgKY7nPEhWCcWkNdVkm9HvKIUQ8x76E2t7HlgzZenkaJZmCuIEpkcEiLjUvv9i15Uzd99gD0pJXuSbQcl7KoalXyALN1AAlwearyPNoBvcHsOtBb2a";
    private GLView glView;

    /**
     * 用于处理打开 AlertDialog 的消息
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                /**
                 * 获取追踪到的目标的名字
                 */
                Bundle bundle = msg.getData();
                final String fileName = bundle.getString("fileName");
                /**
                 * 显示 SweetAlertDialog
                 */
                if (fileName.equals("xinxixueyuan")) {
                    final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("信息科学与工程学院").setConfirmText("查看详情")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, RedPackageActivity.class);
                                    intent.putExtra("fileName", fileName);
                                    startActivity(intent);
                                }
                            });
                    dialog.show();
                }else if(fileName.equals("first")){
                    final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("学院一楼").setConfirmText("查看详情")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, FirstFloor.class);
                                    intent.putExtra("fileName", fileName);
                                    startActivity(intent);
                                }
                            });
                    dialog.show();

                }else if(fileName.equals("second")){
                    final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("学院二楼").setConfirmText("查看详情")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, SecondFloor.class);
                                    intent.putExtra("fileName", fileName);
                                    startActivity(intent);
                                }
                            });
                    dialog.show();

                }else if(fileName.equals("third")){
                    final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("学院三楼").setConfirmText("查看详情")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, ThirdFloor.class);
                                    intent.putExtra("fileName", fileName);
                                    startActivity(intent);
                                }
                            });
                    dialog.show();

                }else if(fileName.equals("fourth")){
                    final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("学院四楼").setConfirmText("查看详情")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, FourthFloor.class);
                                    intent.putExtra("fileName", fileName);
                                    startActivity(intent);
                                }
                            });
                    dialog.show();

                }else if(fileName.equals("fifth")){
                    final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("学院五楼").setConfirmText("查看详情")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, FifthFloor.class);
                                    intent.putExtra("fileName", fileName);
                                    startActivity(intent);
                                }
                            });
                    dialog.show();

                }
            }
            }
        }

        ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!EasyAR.initialize(this, key)) {
            Log.e("HelloAR", "Initialization Failed.");
        }

        glView = new GLView(this, mHandler);

        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ((ViewGroup) findViewById(R.id.preview))
                        .addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onFailure() {
            }
        });
    }

    /**
     * 申请权限回调接口
     */
    private interface PermissionCallback {
        void onSuccess();
        void onFailure();
    }

    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<>();
    private int permissionRequestCodeSerial = 0;
    @TargetApi(23)
    public void requestCameraPermission(PermissionCallback callback) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (permissionCallbacks.containsKey(requestCode)) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) { glView.onResume(); }
    }

    @Override
    protected void onPause() {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }
}
