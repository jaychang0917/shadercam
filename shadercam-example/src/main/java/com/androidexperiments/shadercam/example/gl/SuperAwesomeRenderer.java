package com.androidexperiments.shadercam.example.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.SystemClock;

import com.androidexperiments.shadercam.example.R;
import com.androidexperiments.shadercam.example.Utils;
import com.androidexperiments.shadercam.gl.CameraRenderer;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

/**
 * Our super awesome shader. It calls its super constructor with the new
 * glsl files we've created for this. Then it overrides {@link #setUniformsAndAttribs()}
 * to pass in our global time uniform
 */
public class SuperAwesomeRenderer extends CameraRenderer {
    private float mTileAmount = 1.f;

    /**
     * 3d model
     */
    private FrameBuffer frameBuffer;
    private World world;
    private Object3D model;
    private Light sun;
    private int fps = 0;
    private long time = System.currentTimeMillis();

    private Context context;
    private int width;
    private int height;

    public SuperAwesomeRenderer(Context context, SurfaceTexture texture, int width, int height) {
        super(context, texture, width, height, "superawesome.frag.glsl", "superawesome.vert.glsl");
        this.context = context;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void setUniformsAndAttribs() {
        //always call super so that the built-in fun stuff can be set first
        super.setUniformsAndAttribs();

        int globalTimeHandle = GLES20.glGetUniformLocation(mCameraShaderProgram, "iGlobalTime");
        GLES20.glUniform1f(globalTimeHandle, SystemClock.currentThreadTimeMillis() / 100.0f);

        int resolutionHandle = GLES20.glGetUniformLocation(mCameraShaderProgram, "iResolution");
        GLES20.glUniform3f(resolutionHandle, mTileAmount, mTileAmount, 1.f);
    }

    public void setTileAmount(float tileAmount) {
        this.mTileAmount = tileAmount;
    }


    @Override
    protected void onSetupComplete() {
        super.onSetupComplete();
        initModel(context, width, height);
    }

    private void initModel(Context context, int width, int height) {
        //set 3d model params
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }
        frameBuffer = new FrameBuffer(width, height);
        if (world == null) {
            world = new World();
            world.setAmbientLight(20, 20, 20);
            sun = new Light(world);
            sun.setIntensity(250, 250, 250);
            Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(R.drawable.monster)), 512, 512));
            TextureManager.getInstance().addTexture("model_texture", texture);
            model = Utils.loadModel(mContext, R.raw.monster, 1);
            model.setTexture("model_texture");
            model.build();
            world.addObject(model);

            com.threed.jpct.Camera cam = world.getCamera();
            cam.moveCamera(com.threed.jpct.Camera.CAMERA_MOVEOUT, 50);
            cam.lookAt(model.getTransformedCenter());

            SimpleVector sv = new SimpleVector();
            sv.set(model.getTransformedCenter());
            sv.y -= 100;
            sv.z -= 100;
            sun.setPosition(sv);
            MemoryHelper.compact();
        }

    }

    @Override
    public void draw() {
        super.draw();

        frameBuffer.clearZBufferOnly();
        world.renderScene(frameBuffer);
        model.rotateAxis(SimpleVector.create(0, 0, 1), 45);
        world.draw(frameBuffer);
        frameBuffer.display();

        if (System.currentTimeMillis() - time >= 1000) {
            System.out.println("fps:" + fps);
            fps = 0;
            time = System.currentTimeMillis();
        }
        fps++;
    }
}
