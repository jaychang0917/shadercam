package com.androidexperiments.shadercam.example;

import android.content.Context;

import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import java.io.InputStream;

public class Utils {

  public static Object3D loadModel(Context context, int filename, float scale) {
    InputStream stream = context.getApplicationContext().getResources().openRawResource(filename);
    Object3D[] model = Loader.load3DS(stream, scale);
    Object3D o3d = new Object3D(0);
    Object3D temp = null;
    for (int i = 0; i < model.length; i++) {
      temp = model[i];
      temp.setCenter(SimpleVector.ORIGIN);
      temp.rotateX((float) (-.5 * Math.PI));
      temp.rotateMesh();
      temp.setRotationMatrix(new Matrix());
      o3d = Object3D.mergeObjects(o3d, temp);
      o3d.build();
    }
    return o3d;
  }

}
