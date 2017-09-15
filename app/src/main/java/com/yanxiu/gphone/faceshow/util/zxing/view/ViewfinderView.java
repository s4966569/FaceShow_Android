/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yanxiu.gphone.faceshow.util.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.util.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 */
public final class ViewfinderView extends View {
    private static final String TAG = "log";
    /**
     * ˢ�½����ʱ��
     */
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;

    /**
     * �ĸ���ɫ�߽Ƕ�Ӧ�ĳ���
     */
    private int ScreenRate;

    /**
     * �ĸ���ɫ�߽Ƕ�Ӧ�Ŀ��
     */
    private static final int CORNER_WIDTH = 10;
    /**
     * ɨ����е��м��ߵĿ��
     */
    private static final int MIDDLE_LINE_WIDTH = 6;

    /**
     * ɨ����е��м��ߵ���ɨ������ҵļ�϶
     */
    private static final int MIDDLE_LINE_PADDING = 5;

    /**
     * �м�������ÿ��ˢ���ƶ��ľ���
     */
    private static final int SPEEN_DISTANCE = 5;

    /**
     * �ֻ�����Ļ�ܶ�
     */
    private static float density;
    /**
     * �����С
     */
    private static final int TEXT_SIZE = 16;
    /**
     * �������ɨ�������ľ���
     */
    private static final int TEXT_PADDING_TOP = 45;
    /**
     * 每行文字间间距
     */
    private static final int TEXT_LINE_PADDING = 25;

    /**
     * ���ʶ��������
     */
    private Paint paint;

    /**
     * �м们���ߵ����λ��
     */
    private int slideTop;

    /**
     * �м们���ߵ���׶�λ��
     */
    private int slideBottom;

    /**
     * ��ɨ��Ķ�ά��������������û��������ܣ���ʱ������
     */
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;

    private final int resultPointColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    boolean isFirst;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);
        mFirstStr = typedArray.getString(R.styleable.ViewfinderView_firstLineStr);
        mSecondStr = typedArray.getString(R.styleable.ViewfinderView_secondLineStr);
        density = context.getResources().getDisplayMetrics().density;
        //������ת����dp
        ScreenRate = (int) (20 * density);

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);

        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    private String mFirstStr = "";
    private String mSecondStr = "";


    @Override
    public void onDraw(Canvas canvas) {
        //�м��ɨ�����Ҫ�޸�ɨ���Ĵ�С��ȥCameraManager�����޸�
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }

        //��ʼ���м��߻��������ϱߺ����±�
        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top;
            slideBottom = frame.bottom;
        }

        //��ȡ��Ļ�Ŀ�͸�
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setColor(resultBitmap != null ? resultColor : maskColor);

        //����ɨ����������Ӱ���֣����ĸ����֣�ɨ�������浽��Ļ���棬ɨ�������浽��Ļ����
        //ɨ��������浽��Ļ��ߣ�ɨ�����ұߵ���Ļ�ұ�
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);


        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            //��ɨ�����ϵĽǣ��ܹ�8������
            /*paint.setColor(Color.GREEN);
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate,
					frame.left + CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
					frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
					frame.right, frame.bottom, paint);*/

            paint.setColor(Color.GREEN);
            //���м��ʮ�ּ�  绘制中间的图片
//			Rect centerRect = new Rect();
//			int horCenter = (frame.right - frame.left)/2;
//			int virCenter = (frame.bottom - frame.top)/2;
//			centerRect.left = frame.left + horCenter - 18;
//			centerRect.right = frame.left + horCenter + 18;
//			centerRect.top = frame.top  + virCenter - 18;
//			centerRect.bottom = frame.top  + virCenter + 18;
//			canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.drawable.scan_center))).getBitmap(), null, centerRect, paint);

            //�����м����,ÿ��ˢ�½��棬�м���������ƶ�SPEEN_DISTANCE
            slideTop += SPEEN_DISTANCE;
            if (slideTop >= frame.bottom) {
                slideTop = frame.top;
            }
//			canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH/2, frame.right - MIDDLE_LINE_PADDING,slideTop + MIDDLE_LINE_WIDTH/2, paint);
            Rect lineRect = new Rect();
            lineRect.left = frame.left;
            lineRect.right = frame.right;
            lineRect.top = slideTop;
            lineRect.bottom = slideTop + 18;
            canvas.drawBitmap(((BitmapDrawable) (getResources()
                            .getDrawable(R.drawable.qrcode_scan_line))).getBitmap(),
                    null, lineRect, paint);

            paint.setColor(getResources().getColor(R.color.color_ccffffff));
            //���߿���  �����
            float leftStartX = frame.left;
            float leftStopX = frame.left + 1;
            float leftStartY = frame.top;
            float leftStopY = frame.bottom;
            canvas.drawLine(leftStartX, leftStartY, leftStopX, leftStopY, paint);

            //���߿���  �ϱ���
            float topStartX = frame.left;
            float topStopX = frame.right;
            float topStartY = frame.top;
            float topStopY = frame.top + 1;
            canvas.drawLine(topStartX, topStartY, topStopX, topStopY, paint);

            //���߿���  �ұ���
            float rightStartX = frame.right - 1;
            float rightStopX = frame.right;
            float rightStartY = frame.top;
            float rightStopY = frame.bottom;
            canvas.drawLine(rightStartX, rightStartY, rightStopX, rightStopY, paint);

            //���߿���  �±���
            float bottomStartX = frame.left;
            float bottomStopX = frame.right;
            float bottomStartY = frame.bottom - 1;
            float bottomStopY = frame.bottom;
            canvas.drawLine(bottomStartX, bottomStartY, bottomStopX, bottomStopY, paint);

            paint.setColor(Color.GREEN);
            //�����Ͻǵ�ͼ  左上角
            Rect leftTopRect = new Rect();
            leftTopRect.left = frame.left;
            leftTopRect.right = frame.left + 50;
            leftTopRect.top = frame.top;
            leftTopRect.bottom = frame.top + 50;
            canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.drawable.scan_left_top))).getBitmap(), null, leftTopRect, paint);

            //�����Ͻǵ�ͼ  右上角
            Rect rightTopRect = new Rect();
            rightTopRect.left = frame.right - 50;
            rightTopRect.right = frame.right;
            rightTopRect.top = frame.top;
            rightTopRect.bottom = frame.top + 50;
            canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.drawable.scan_right_top))).getBitmap(), null, rightTopRect, paint);

            //�����½ǵ�ͼ 左下角
            Rect leftBottomRect = new Rect();
            leftBottomRect.left = frame.left;
            leftBottomRect.right = frame.left + 50;
            leftBottomRect.top = frame.bottom - 50;
            leftBottomRect.bottom = frame.bottom;
            canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.drawable.scan_left_bottom))).getBitmap(), null, leftBottomRect, paint);

//			//�����½ǵ�ͼ 右下角
            Rect rightBottomRect = new Rect();
            rightBottomRect.left = frame.right - 50;
            rightBottomRect.right = frame.right;
            rightBottomRect.top = frame.bottom - 50;
            rightBottomRect.bottom = frame.bottom;
            canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.drawable.scan_right_bottom))).getBitmap(), null, rightBottomRect, paint);


            //��ɨ����������
            paint.setColor(getResources().getColor(R.color.color_4691a6));
            paint.setTextSize(TEXT_SIZE * density);
            //paint.setAlpha(0x40);
//			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
            canvas.drawText(mFirstStr, frame.left + 20 * density, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density), paint);
            canvas.drawText(mSecondStr, frame.left + 14 * density, (float) (frame.bottom + (float) (TEXT_PADDING_TOP + TEXT_LINE_PADDING) * density), paint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }

            //ֻˢ��ɨ�������ݣ������ط���ˢ��
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);

        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
