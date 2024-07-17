package com.example.openglfog;

import android.annotation.SuppressLint;
import android.opengl.GLSurfaceView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private float mLastX = 0;
    private float mLastY = 0;
    private float mSensitivity = 0.2f;

    private GLSurfaceView mGLSurfaceView;

    private GLRender mGLRenderer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("Activity", "onCreate");

        // 实例化GLSurfaceView (EGL处理opengles 与窗口系统）
        mGLSurfaceView = new GLSurfaceView(this);

        mGLRenderer = new GLRender(this);
        // 设置渲染器
        mGLSurfaceView.setRenderer(mGLRenderer);

        setContentView(mGLSurfaceView);

        mGLSurfaceView.setOnTouchListener((v, event) -> {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    // 当按下时的处理
                    this.mLastX = event.getRawX();
                    this.mLastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 当滑动时的处理
                {
                    float deltaX = mSensitivity * (event.getRawX() - mLastX);
                    float deltaY = mSensitivity * (event.getRawY() - mLastY);
                    mGLRenderer.yrot += deltaX;
                    mGLRenderer.xrot += deltaY;

                    mLastX = event.getRawX();
                    mLastY = event.getRawY();
                }
                    break;
                case MotionEvent.ACTION_UP:
                    // 当抬起时的处理
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}




























