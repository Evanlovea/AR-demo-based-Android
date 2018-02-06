package com.learn.lister.cn;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by Evan on 2017/8/12.
 */


public class SecondFloor extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_package);
                /*http://www.jianshu.com/p/9ad21e548b69*/
        ButterKnife.bind(this);//绑定事件

        String fileName = getIntent().getStringExtra("fileName");


        // 重新将跳转标志量设为 0
         //Constants.flag = 0;
    }



    //在 Activity 中的 onDestroy() 中给 Handler 发送一个延时消息：
    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelloAR.handler.sendEmptyMessageDelayed(1, 3000);
    }
}
