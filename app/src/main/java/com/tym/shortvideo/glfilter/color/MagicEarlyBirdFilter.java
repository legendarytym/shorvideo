package com.tym.shortvideo.glfilter.color;

import android.opengl.GLES20;

import com.tym.shortvideo.R;
import com.tym.shortvideo.filter.base.GPUImageFilter;
import com.tym.shortvideo.filter.helper.OpenGlUtils;
import com.tym.shortvideo.filter.helper.type.GlUtil;
import com.tym.shortvideo.recodrender.ParamsManager;

/**
 * @Author Jliuer
 * @Date 2018/3/27/23:02
 * @Email Jliuer@aliyun.com
 * @Description 晨鸟滤镜
 */
public class MagicEarlyBirdFilter extends GPUImageFilter {
	private int[] inputTextureHandles = {-1,-1,-1,-1,-1};
	private int[] inputTextureUniformLocations = {-1,-1,-1,-1,-1};
	protected int mGLStrengthLocation;

	public MagicEarlyBirdFilter(){
		super(NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.earlybird));
	}
	
	protected void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(inputTextureHandles.length, inputTextureHandles, 0);
        for(int i = 0; i < inputTextureHandles.length; i++)
        	inputTextureHandles[i] = -1;
    }
	
	protected void onDrawArraysAfter(){
		for(int i = 0; i < inputTextureHandles.length
				&& inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i+3));
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		}
	}
	  
	protected void onDrawArraysPre(){
		for(int i = 0; i < inputTextureHandles.length 
				&& inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i+3) );
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
			GLES20.glUniform1i(inputTextureUniformLocations[i], (i+3));
		}
	}
	
	protected void onInit(){
		super.onInit();
		for(int i=0; i < inputTextureUniformLocations.length; i++)
			inputTextureUniformLocations[i] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture"+(2+i));
		mGLStrengthLocation = GLES20.glGetUniformLocation(mGLProgId,
				"strength");
	}
	
	protected void onInitialized(){
		super.onInitialized();
		setFloat(mGLStrengthLocation, 1.0f);
	    runOnDraw(new Runnable(){
		    public void run(){
		    	inputTextureHandles[0] = GlUtil.createTextureFromAssets(ParamsManager.context, "filter/earlybirdcurves.png");
				inputTextureHandles[1] = GlUtil.createTextureFromAssets(ParamsManager.context, "filter/earlybirdoverlaymap_new.png");
				inputTextureHandles[2] = GlUtil.createTextureFromAssets(ParamsManager.context, "filter/vignettemap_new.png");
				inputTextureHandles[3] = GlUtil.createTextureFromAssets(ParamsManager.context, "filter/earlybirdblowout.png");
				inputTextureHandles[4] = GlUtil.createTextureFromAssets(ParamsManager.context, "filter/earlybirdmap.png");
		    }
	    });
	}
}
