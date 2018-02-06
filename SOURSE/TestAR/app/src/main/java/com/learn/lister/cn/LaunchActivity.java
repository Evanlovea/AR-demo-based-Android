package com.learn.lister.cn;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {

    private Button launch_btn_ar;
    private Button launch_btn_anim;
    private Button launch_btn_alert;
/**
 * 实现霓虹灯效果
 */

    private Context mContext = this;
    private String TAG="androidtest1";
    private int currentcolor=0;
    final int[] colors=new int[]{
      R.color.color1, R.color.color2, R.color.color3,
            R.color.color4, R.color.color5, R.color.color6,
    };
    final int[] names=new int[]{
            R.id.view01, R.id.view02, R.id.view03,
            R.id.view04, R.id.view05, R.id.view06
    };
    TextView[] views=new TextView[names.length];
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==0x123){
                for(int i=0;i<names.length;i++){
                    //i+后，每次颜色往后循环一个
                    views[i].setBackgroundResource(colors[(i+currentcolor)%names.length]);
                }
                currentcolor++;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        for(int i=0;i<names.length;i++){
            views[i]=(TextView)findViewById(names[i]);
        }
        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                handler.sendEmptyMessage(0x123);
            }
        },0,500);


        launch_btn_ar = (Button) findViewById(R.id.launch_btn_ar);
       // launch_btn_anim = (Button) findViewById(R.id.launch_btn_anim);
        //launch_btn_alert = (Button) findViewById(R.id.launch_btn_alert);

        /**
         * 对launch_btn_ar设置监听事件
         */
        launch_btn_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
            }
        });

     /*   launch_btn_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, SecondActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
            }
        });

        launch_btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("信息科学与工程学院")
                        .setConfirmText("查看详情")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        }).show();
            }
        });*/
    }
}
