package com.example.openglfog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements Renderer {

    public float xrot, yrot, zrot;
    private int yloop,xloop;

    private float fogColor[] = {0.5f, 0.5f, 0.5f, 1.0f};

    private float[][] boxcol =
            {
                    {1.0f, 0.0f, 0.0f, 1.0f},
                    {1.0f, 0.5f, 0.0f, 1.0f},
                    {1.0f, 1.0f, 0.0f, 1.0f},
                    {0.0f, 1.0f, 0.0f, 1.0f},
                    {0.0f, 1.0f, 1.0f, 1.0f},
            };

    FloatBuffer boxVerticesBuffer;
    FloatBuffer boxNormalsBuffer;

    public GLRender(Context context)
    {

    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        drawLists(gl);
    }

    private void drawLists(GL10 gl)
    {
        gl.glMatrixMode(GL10.GL_MODELVIEW);


        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, boxVerticesBuffer);

        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, boxNormalsBuffer);


        for(yloop=1;yloop<6;yloop++)
        {
            for(xloop=0;xloop<yloop;xloop++)
            {
                gl.glLoadIdentity();
                GLU.gluLookAt(gl,0,-1.0f,3,0,-1.0f,0, 0, 1, 0);

                gl.glTranslatef(0.6f * (1.4f+(xloop*2.8f)-(yloop*1.4f)),
                        0.6f * (6.0f-yloop*2.4f), -5.0f);

                gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
                gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

                setupMaterial(gl, boxcol[yloop-1], boxcol[yloop-1], new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 25.0f);

                gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 36);
            }
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

    }

    private void setupLight(GL10 gl)
    {
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);

        FloatBuffer light0Ambient = GLBufferWrapper.WrapFloat(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, light0Ambient);

        FloatBuffer light0Diffuse = GLBufferWrapper.WrapFloat(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, light0Diffuse);

        FloatBuffer light0Specular = GLBufferWrapper.WrapFloat(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, light0Specular);

        FloatBuffer light0Position = GLBufferWrapper.WrapFloat(new float[]{0.0f, 10.0f, 10.0f, 0.0f});
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Position);

        FloatBuffer light0Direction = GLBufferWrapper.WrapFloat(new float[]{0.0f, 0.0f, -1.0f});
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, light0Direction);

        gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, 45.0f);

    }

    private void setupFog(GL10 gl)
    {
        gl.glFogx(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
        gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
        gl.glFogf(GL10.GL_FOG_DENSITY, 0.35f);
        gl.glHint(GL10.GL_FOG_HINT, GL10.GL_DONT_CARE);
        gl.glFogf(GL10.GL_FOG_START, 1.0f);
        gl.glFogf(GL10.GL_FOG_END, 35.0f);
        gl.glEnable(GL10.GL_FOG);

    }

    private void setupTexture(GL10 gl)
    {
//        gl.glEnable(GL10.GL_TEXTURE_2D);
//        gl.glEnable(GL10.GL_BLEND);
//        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ZERO);
//
//        IntBuffer intBuffer = GLBufferWrapper.WrapInt(new int[]{1});
//        gl.glGenTextures(1, intBuffer);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
//        gl.glActiveTexture(GL10.GL_TEXTURE0);
//
//        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//
//        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmapTexture, 0);
    }

    private void setupMaterial(GL10 gl, float[] ambientColor, float[] diffuseColor, float[] specularColor, float shininess)
    {

        FloatBuffer ambient = GLBufferWrapper.WrapFloat(ambientColor);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambient);

        FloatBuffer diffuse = GLBufferWrapper.WrapFloat(diffuseColor);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, diffuse);

        FloatBuffer specular = GLBufferWrapper.WrapFloat(specularColor);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specular);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, shininess);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        Log.i("GLRender", "onSurfaceChanged:: w: " + width + " h: " + height);
        float ratio = (float)width/height;

        gl.glViewport(0,0,width,height);

        gl.glMatrixMode(GL10.GL_PROJECTION);

        gl.glLoadIdentity();;

        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 15);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.i("GLRender", "opengl version: " + gl10.glGetString(GL10.GL_VERSION));

        gl10.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        gl10.glClearColor(0.5F, 0.5F, 0.5F, 1.0F);      // 不要不写小数点！否则会没用。

        gl10.glEnable(GL10.GL_DEPTH_TEST);

        //gl10.glEnable(GL10.GL_CULL_FACE);

        gl10.glShadeModel(GL10.GL_SMOOTH);
        gl10.glClearDepthf(30.f);
        gl10.glDepthFunc(GL10.GL_LEQUAL);
        //gl10.glEnable(GL10.GL_COLOR_MATERIAL);

        setupLight(gl10);
        buildLists(gl10);
        setupFog(gl10);

    }

    private void buildLists(GL10 gl){
        boxVerticesBuffer = GLBufferWrapper.WrapFloat(new float[]{
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,

                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,

                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
        });

        boxNormalsBuffer = GLBufferWrapper.WrapFloat(new float[]{
                 0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                 0.0f,  0.0f, -1.0f,
                 0.0f,  0.0f, -1.0f,

                 0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                 0.0f,  0.0f,  1.0f,
                 0.0f,  0.0f,  1.0f,

                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,

                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,

                 0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                 0.0f, -1.0f,  0.0f,
                 0.0f, -1.0f,  0.0f,

                 0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                 0.0f,  1.0f,  0.0f,
                 0.0f,  1.0f,  0.0f
        });
    }
}

























