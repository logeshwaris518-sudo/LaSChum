package com.example.littleacechatbot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class BouncingBallView extends View {

    private float ballX, ballY;
    private float velocityY = 8f;
    private float ballRadius = 40f;
    private float rotation = 0f;
    private Paint ballPaint;
    private Paint glowPaint;
    private ValueAnimator rotationAnimator;

    public BouncingBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ballPaint.setColor(0x33D4A857); // translucent gold

        glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        rotationAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotationAnimator.setDuration(6000);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.addUpdateListener(animation -> {
            rotation = (float) animation.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ballX = 150f;
        ballY = 300f;
        rotationAnimator.start();
        startBounce();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        rotationAnimator.cancel();
    }

    private void startBounce() {
        postDelayed(bounceRunnable, 16);
    }

    private final Runnable bounceRunnable = new Runnable() {
        @Override
        public void run() {
            ballY += velocityY;
            if (ballY > getHeight() - ballRadius || ballY < ballRadius) {
                velocityY = -velocityY;
            }
            invalidate();
            postDelayed(this, 16);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        glowPaint.setShader(new RadialGradient(
                ballX, ballY, ballRadius * 3,
                0x22D4A857, 0x00000000,
                Shader.TileMode.CLAMP));
        canvas.drawCircle(ballX, ballY, ballRadius * 3, glowPaint);

        canvas.save();
        canvas.rotate(rotation, ballX, ballY);
        canvas.drawCircle(ballX, ballY, ballRadius, ballPaint);
        canvas.restore();
    }
}