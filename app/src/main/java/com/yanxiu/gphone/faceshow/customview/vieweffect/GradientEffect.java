package com.yanxiu.gphone.faceshow.customview.vieweffect;

/**
 * 渐变效果
 * Created by dyf on 2017/7/27.
 */
public interface GradientEffect {
    /**
     * 初始化数据
     */
    void onInitData();

    /**
     * 获取渐变吸收
     *
     * @return
     */
    void generateRatio();

    /**
     * 渐变回调监听
     *
     * @param l
     */
    void setOnGradientEffectListener(OnGradientEffectListener l);

    public interface OnGradientEffectListener {
        /**
         * 渐变回调
         *
         * @param ratio 渐变系数（最小为0最大为1）
         */
        void onGrade(float ratio);
    }
}
