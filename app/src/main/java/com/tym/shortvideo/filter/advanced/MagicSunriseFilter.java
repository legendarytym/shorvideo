package com.tym.shortvideo.filter.advanced;

import android.opengl.GLES20;

import com.tym.shortvideo.R;
import com.tym.shortvideo.filter.base.GPUImageFilter;
import java.nio.ByteBuffer;
import com.tym.shortvideo.filter.helper.type.GlUtil;
import com.tym.shortvideo.recodrender.ParamsManager;
public class MagicSunriseFilter extends GPUImageFilter {
    private int mMaskGrey1TextureId = -1;
    private int mMaskGrey1UniformLocation;
    private int mMaskGrey2TextureId = -1;
    private int mMaskGrey2UniformLocation;
    private int mMaskGrey3TextureId = -1;
    private int mMaskGrey3UniformLocation;
    private int[] mToneCurveTexture = {-1};
    private int mToneCurveTextureUniformLocation;

    public MagicSunriseFilter() {
        super(R.raw.sunrise);
    }

    protected void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, mToneCurveTexture, 0);
        mToneCurveTexture[0] = -1;
        GLES20.glDeleteTextures(1, new int[]{mMaskGrey1TextureId}, 0);
        mMaskGrey1TextureId = -1;
        GLES20.glDeleteTextures(1, new int[]{mMaskGrey2TextureId}, 0);
        mMaskGrey2TextureId = -1;
        GLES20.glDeleteTextures(1, new int[]{mMaskGrey3TextureId}, 0);
        mMaskGrey3TextureId = -1;
    }

    protected void onDrawArraysAfter() {
        if (mToneCurveTexture[0] != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
        if (mMaskGrey1TextureId != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE4);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
        if (mMaskGrey2TextureId != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE5);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
        if (mMaskGrey3TextureId != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE6);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawArraysPre() {
        if (mToneCurveTexture[0] != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mToneCurveTexture[0]);
            GLES20.glUniform1i(mToneCurveTextureUniformLocation, 3);
        }
        if (mMaskGrey1TextureId != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE4);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mMaskGrey1TextureId);
            GLES20.glUniform1i(mMaskGrey1UniformLocation, 4);
        }
        if (mMaskGrey2TextureId != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE5);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mMaskGrey2TextureId);
            GLES20.glUniform1i(mMaskGrey2UniformLocation, 5);
        }
        if (mMaskGrey3TextureId != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE6);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mMaskGrey3TextureId);
            GLES20.glUniform1i(mMaskGrey3UniformLocation, 6);
        }
    }

    protected void onInit() {
        super.onInit();
        mToneCurveTextureUniformLocation = GLES20.glGetUniformLocation(mGLProgId, "curve");
        mMaskGrey1UniformLocation = GLES20.glGetUniformLocation(getProgram(), "grey1Frame");
        mMaskGrey2UniformLocation = GLES20.glGetUniformLocation(getProgram(), "grey2Frame");
        mMaskGrey3UniformLocation = GLES20.glGetUniformLocation(getProgram(), "grey3Frame");
    }

    public void onInitialized() {
        super.onInitialized();
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
                GLES20.glGenTextures(1, mToneCurveTexture, 0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mToneCurveTexture[0]);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                byte[] arrayOfByte = new byte[2048];
                int[] arrayOfInt1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 25, 26, 27, 28, 30, 31, 32, 34, 35, 36, 38, 39, 41, 42, 44, 45, 47, 49, 50, 52, 54, 55, 57, 59, 61, 63, 65, 67, 69, 71, 73, 75, 77, 79, 81, 83, 85, 87, 89, 92, 94, 96, 98, 101, 103, 105, 107, 110, 111, 113, 115, 118, 120, 122, 124, 126, 129, 131, 133, 135, 137, 139, 141, 143, 145, 147, 149, 150, 152, 154, 156, 158, 159, 161, 162, 164, 166, 167, 169, 170, 172, 173, 174, 176, 177, 178, 180, 181, 182, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 200, 201, 202, 203, 203, 204, 205, 205, 207, 208, 208, 209, 209, 210, 210, 211, 211, 212, 212, 213, 213, 213, 214, 214, 215, 215, 215, 216, 216, 216, 216, 217, 217, 217, 218, 218, 218, 218, 219, 219, 219, 219, 219, 220, 220, 220, 220, 220, 220, 221, 221, 221, 221, 221, 222, 222, 222, 222, 222, 223, 223, 223, 223, 223, 224, 224, 224, 224, 224, 225, 225, 225, 225, 226, 226, 226, 227, 227, 227, 228, 228, 228, 229, 229, 230, 230, 230, 231, 231, 232, 232, 233, 233, 234, 234, 235, 235, 236, 236, 237, 238, 238, 239, 239, 241, 241, 242, 243, 243, 244, 245, 245, 246, 246, 247, 248, 248, 249, 250, 250, 251, 252, 252, 253, 254, 254, 255};
                int[] arrayOfInt2 = {0, 1, 3, 4, 5, 7, 8, 10, 11, 12, 14, 15, 17, 18, 19, 21, 22, 24, 25, 27, 28, 30, 31, 33, 34, 36, 37, 39, 40, 42, 44, 45, 47, 48, 50, 52, 54, 55, 57, 59, 61, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 83, 85, 87, 90, 92, 94, 96, 98, 101, 103, 105, 107, 110, 111, 113, 115, 117, 119, 122, 124, 126, 128, 130, 132, 134, 136, 138, 140, 142, 143, 145, 147, 149, 150, 152, 154, 155, 157, 158, 160, 161, 163, 164, 165, 167, 168, 169, 171, 172, 173, 174, 175, 176, 177, 179, 180, 181, 182, 183, 184, 184, 185, 186, 187, 188, 189, 190, 190, 191, 192, 193, 193, 194, 195, 195, 196, 197, 197, 198, 198, 199, 199, 200, 200, 201, 201, 202, 202, 203, 203, 204, 204, 205, 205, 205, 207, 207, 208, 208, 208, 209, 209, 210, 210, 210, 211, 211, 211, 212, 212, 212, 213, 213, 213, 214, 214, 214, 215, 215, 215, 216, 216, 216, 217, 217, 217, 218, 218, 219, 219, 219, 220, 220, 220, 221, 221, 222, 222, 222, 223, 223, 224, 224, 225, 225, 225, 226, 226, 227, 227, 228, 228, 228, 229, 229, 230, 230, 231, 231, 232, 232, 233, 233, 234, 234, 235, 235, 236, 236, 236, 237, 237, 238, 238, 239, 239, 241, 241, 242, 242, 243, 244, 244, 245, 245, 246, 246, 247, 247, 248, 248, 249, 249, 250, 250, 251, 251, 252, 252, 253, 253, 254, 254, 255};
                int[] arrayOfInt3 = {0, 1, 3, 4, 5, 6, 8, 9, 10, 11, 13, 14, 15, 17, 18, 19, 21, 22, 23, 25, 26, 28, 29, 30, 32, 33, 35, 36, 38, 39, 41, 43, 44, 46, 47, 49, 51, 53, 54, 56, 58, 60, 62, 64, 66, 68, 69, 71, 73, 76, 78, 80, 82, 83, 85, 87, 89, 91, 93, 95, 97, 100, 102, 104, 106, 108, 110, 111, 113, 115, 117, 119, 121, 123, 125, 127, 129, 130, 132, 134, 136, 138, 139, 141, 143, 145, 146, 148, 150, 151, 153, 155, 156, 158, 159, 161, 162, 164, 165, 167, 168, 169, 171, 172, 173, 175, 176, 177, 179, 180, 181, 182, 183, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 200, 201, 202, 203, 204, 204, 205, 207, 207, 208, 209, 209, 210, 210, 211, 212, 212, 213, 213, 214, 214, 215, 215, 215, 216, 216, 217, 217, 217, 218, 218, 218, 219, 219, 219, 220, 220, 220, 220, 221, 221, 221, 221, 222, 222, 222, 222, 223, 223, 223, 223, 224, 224, 224, 224, 224, 225, 225, 225, 225, 225, 226, 226, 226, 226, 227, 227, 227, 227, 228, 228, 228, 228, 229, 229, 229, 230, 230, 230, 231, 231, 231, 232, 232, 233, 233, 233, 234, 234, 235, 235, 235, 236, 236, 237, 237, 238, 238, 239, 239, 241, 241, 242, 242, 243, 243, 244, 244, 245, 245, 246, 247, 247, 248, 248, 249, 249, 250, 250, 251, 252, 252, 253, 253, 254, 254, 255};
                int[] arrayOfInt4 = {0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 8, 8, 8, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 13, 13, 13, 14, 14, 14, 15, 15, 16, 16, 16, 17, 17, 17, 18, 18, 18, 19, 19, 20, 20, 20, 21, 21, 21, 22, 22, 23, 23, 23, 24, 24, 24, 25, 25, 25, 25, 26, 26, 27, 27, 28, 28, 28, 28, 29, 29, 30, 29, 31, 31, 31, 31, 32, 32, 33, 33, 34, 34, 34, 34, 35, 35, 36, 36, 37, 37, 37, 38, 38, 39, 39, 39, 40, 40, 40, 41, 42, 42, 43, 43, 44, 44, 45, 45, 45, 46, 47, 47, 48, 48, 49, 50, 51, 51, 52, 52, 53, 53, 54, 55, 55, 56, 57, 57, 58, 59, 60, 60, 61, 62, 63, 63, 64, 65, 66, 67, 68, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 88, 89, 90, 91, 93, 94, 95, 96, 97, 98, 100, 101, 103, 104, 105, 107, 108, 110, 111, 113, 115, 116, 118, 119, 120, 122, 123, 125, 127, 128, 130, 132, 134, 135, 137, 139, 141, 143, 144, 146, 148, 150, 152, 154, 156, 158, 160, 163, 165, 167, 169, 171, 173, 175, 178, 180, 182, 185, 187, 189, 192, 194, 197, 199, 201, 204, 206, 209, 211, 214, 216, 219, 221, 224, 226, 229, 232, 234, 236, 239, 241, 245, 247, 250, 252, 255};
                for (int i = 0; i < 256; i++) {
                    arrayOfByte[(i * 4)] = ((byte) arrayOfInt1[i]);
                    arrayOfByte[(1 + i * 4)] = ((byte) arrayOfInt2[i]);
                    arrayOfByte[(2 + i * 4)] = ((byte) arrayOfInt3[i]);
                    arrayOfByte[(3 + i * 4)] = ((byte) arrayOfInt4[i]);
                }
                int[] arrayOfInt5 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 34, 35, 36, 37, 38, 39, 41, 42, 43, 44, 45, 46, 48, 49, 50, 51, 52, 53, 54, 56, 57, 58, 59, 60, 61, 63, 64, 65, 66, 67, 68, 70, 71, 72, 73, 74, 75, 77, 78, 79, 80, 81, 82, 83, 85, 86, 87, 88, 89, 90, 92, 93, 94, 95, 96, 97, 99, 100, 101, 102, 103, 104, 105, 107, 108, 109, 110, 111, 112, 114, 115, 116, 117, 118, 119, 121, 122, 123, 124, 125, 126, 128, 129, 130, 131, 132, 133, 134, 136, 137, 138, 139, 140, 141, 143, 144, 145, 146, 147, 148, 150, 151, 152, 153, 154, 155, 156, 158, 159, 160, 161, 162, 163, 165, 166, 167, 168, 169, 170, 172, 173, 174, 175, 176, 177, 178, 180, 181, 182, 183, 184, 185, 187, 188, 189, 190, 191, 192, 194, 195, 196, 197, 198, 199, 201, 202, 203, 204, 205, 206, 207, 209, 210, 211, 212, 213, 214, 216, 217, 218, 219, 220, 221, 223, 224, 225, 226, 227, 228, 230, 231, 232, 233, 234, 235, 236, 238, 239, 240, 241, 242, 243, 245, 246, 247, 248, 249, 250, 252, 253, 254, 255};
                int[] arrayOfInt6 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4, 5, 6, 7, 9, 10, 11, 12, 14, 15, 16, 17, 19, 20, 21, 22, 24, 25, 26, 27, 29, 30, 31, 32, 34, 35, 36, 37, 39, 40, 41, 42, 44, 45, 46, 47, 49, 50, 51, 52, 53, 55, 56, 57, 58, 60, 61, 62, 63, 65, 66, 67, 68, 70, 71, 72, 73, 75, 76, 77, 78, 80, 81, 82, 83, 85, 86, 87, 88, 90, 91, 92, 93, 95, 96, 97, 98, 100, 101, 102, 103, 104, 106, 107, 108, 109, 111, 112, 113, 114, 116, 117, 118, 119, 121, 122, 123, 124, 126, 127, 128, 129, 131, 132, 133, 134, 136, 137, 138, 139, 141, 142, 143, 144, 146, 147, 148, 149, 151, 152, 153, 154, 155, 157, 158, 159, 160, 162, 163, 164, 165, 167, 168, 169, 170, 172, 173, 174, 175, 177, 178, 179, 180, 182, 183, 184, 185, 187, 188, 189, 190, 192, 193, 194, 195, 197, 198, 199, 200, 202, 203, 204, 205, 206, 208, 209, 210, 211, 213, 214, 215, 216, 218, 219, 220, 221, 223, 224, 225, 226, 228, 229, 230, 231, 233, 234, 235, 236, 238, 239, 240, 241, 243, 244, 245, 246, 248, 249, 250, 251, 253, 254, 255};
                int[] arrayOfInt7 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 28, 29, 30, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 49, 51, 52, 53, 54, 55, 56, 57, 59, 60, 61, 62, 63, 64, 65, 67, 68, 69, 70, 71, 72, 74, 75, 76, 77, 78, 79, 80, 82, 83, 84, 85, 86, 87, 88, 90, 91, 92, 93, 94, 95, 96, 98, 99, 100, 101, 102, 103, 105, 106, 107, 108, 109, 110, 111, 113, 114, 115, 116, 117, 118, 119, 121, 122, 123, 124, 125, 126, 128, 129, 130, 131, 132, 133, 134, 136, 137, 138, 139, 140, 141, 142, 144, 145, 146, 147, 148, 149, 150, 152, 153, 154, 155, 156, 157, 159, 160, 161, 162, 163, 164, 165, 167, 168, 169, 170, 171, 172, 173, 175, 176, 177, 178, 179, 180, 181, 183, 184, 185, 186, 187, 188, 190, 191, 192, 193, 194, 195, 196, 198, 199, 200, 201, 202, 203, 204, 206, 207, 208, 209, 210, 211, 213, 214, 215, 216, 217, 218, 219, 221, 222, 223, 224, 225, 226, 227, 229, 230, 231, 232, 233, 234, 235, 237, 238, 239, 240, 241, 242, 244, 245, 246, 247, 248, 249, 250, 252, 253, 254, 255};
                int[] arrayOfInt8 = {0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 12, 12, 13, 14, 14, 15, 15, 16, 17, 17, 18, 19, 19, 20, 21, 22, 22, 23, 24, 24, 25, 26, 27, 27, 28, 29, 30, 31, 31, 32, 33, 34, 34, 35, 36, 37, 38, 39, 39, 40, 41, 42, 43, 44, 44, 45, 46, 47, 48, 49, 50, 51, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 126, 127, 128, 129, 130, 131, 132, 134, 135, 136, 137, 138, 139, 140, 142, 143, 144, 145, 146, 147, 149, 150, 151, 152, 153, 155, 156, 157, 158, 159, 160, 162, 163, 164, 165, 166, 168, 169, 170, 171, 173, 174, 175, 176, 177, 179, 180, 181, 182, 184, 185, 186, 187, 189, 190, 191, 192, 194, 195, 196, 197, 199, 200, 201, 202, 204, 205, 206, 208, 209, 210, 211, 213, 214, 215, 217, 218, 219, 221, 222, 223, 224, 226, 227, 228, 230, 231, 232, 234, 235, 236, 238, 239, 240, 242, 243, 244, 246, 247, 248, 250, 251, 252, 254, 255};
                for (int j = 0; j < 256; j++) {
                    arrayOfByte[(1024 + j * 4)] = ((byte) arrayOfInt5[j]);
                    arrayOfByte[(1 + (1024 + j * 4))] = ((byte) arrayOfInt6[j]);
                    arrayOfByte[(2 + (1024 + j * 4))] = ((byte) arrayOfInt7[j]);
                    arrayOfByte[(3 + (1024 + j * 4))] = ((byte) arrayOfInt8[j]);
                }
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, 256, 2, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ByteBuffer.wrap(arrayOfByte));
                GLES20.glActiveTexture(GLES20.GL_TEXTURE4);
                mMaskGrey1TextureId = GlUtil.createTextureFromAssets(ParamsManager.context, "filter/amaro_mask1.jpg");
                GLES20.glActiveTexture(GLES20.GL_TEXTURE5);
                mMaskGrey2TextureId = GlUtil.createTextureFromAssets(ParamsManager.context, "filter/amaro_mask2.jpg");
                GLES20.glActiveTexture(GLES20.GL_TEXTURE6);
                mMaskGrey3TextureId = GlUtil.createTextureFromAssets(ParamsManager.context, "filter/toy_mask1.jpg");
            }
        });
    }
}
