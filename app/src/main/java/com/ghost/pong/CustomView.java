package com.ghost.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {
    public RectF lev1;
    public RectF lev2;
    public Paint paint;
    private boolean initialized = false;
    public float width, height;
    public Ball ball;

    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        if (!initialized) {
            lev1.left = (canvas.getWidth() / 2) - 100;
            lev1.top = 0;
            width = canvas.getWidth();
            height = canvas.getHeight();
            ball.x = canvas.getWidth() / 2;
            ball.y = canvas.getHeight() / 2;
            ball.r = 30;
            lev1.right = lev1.left + 200;
            lev1.bottom = lev1.top + 50;
            lev2.left = (canvas.getWidth() / 2) - 100;
            lev2.top = canvas.getHeight() - 50;
            lev2.right = lev2.left + 200;
            lev2.bottom = lev2.top + 50;
            initialized = true;
        }
        paint.setColor(Color.WHITE);
        canvas.drawRect(lev1, paint);
        canvas.drawRect(lev2, paint);
        canvas.drawCircle(ball.x, ball.y, ball.r, paint);
        checkCollision();
        updateAngle();
        moveBall();
    }

    private void init(@Nullable AttributeSet set) {
        lev1 = new RectF();
        lev2 = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ball = new Ball();
    }

    public void moveRight(RectF r) {
        for (int i = 0; i < 5; i++) {
            if (lev2.right + 10 <= width) {
                r.left += 10;
                r.right = r.left + 200;
                postInvalidate();
            }
            else {
                break;
            }
        }
    }

    public void moveLeft(RectF r) {
        for (int i = 0; i < 5; i++) {
            if (lev2.left - 10 >= 0) {
                r.left -= 10;
                r.right = r.left + 200;
                postInvalidate();
            }
            else {
                break;
            }
        }
    }

    public boolean TopWallCollision() {
        return ball.y - ball.r <= 0;
    }

    public boolean BottomWallCollision() {
        return ball.y + ball.r >= height;
    }

    public boolean TopPaddleCollision() {
        return ball.y - ball.r <= lev1.bottom && ball.x >= lev1.left && ball.x <= lev1.right;
    }

    public boolean BottomPaddleCollision() {
        return (ball.y + ball.r >= lev2.top) && ball.x >= lev2.left && ball.x <= lev2.right;
    }

    public boolean LeftCollision() {
        return ball.x - ball.r <= 0;
    }

    public boolean RightCollision() {
        return ball.x + ball.r >= width;
    }

    public void moveBall() {
        ball.x += ball.speed * Math.cos(Math.toRadians(ball.angle));
        ball.y += ball.speed * Math.sin(Math.toRadians(ball.angle));
        postInvalidate();
    }

    public void Reset() {
        ball = new Ball();
        ball.x = width / 2;
        ball.y = height / 2;
        ball.r = 30;
        postInvalidate();
    }

    public void checkCollision() {
        switch (ball.dir) {
            case "SE":
                if (RightCollision()) {
                    ball.dir = "SW";
                } else if (BottomPaddleCollision()) {
                    ball.dir = "NE";
                } else if (BottomWallCollision()) {
                    //Reset();
                    ball.dir = "NE";
                }
                break;
            case "SW":
                if (LeftCollision()) {
                    ball.dir = "SE";
                } else if (BottomPaddleCollision()) {
                    ball.dir = "NW";
                } else if (BottomWallCollision()) {
                    //Reset();
                    ball.dir = "NW";
                }
                break;
            case "NE":
                if (RightCollision()) {
                    ball.dir = "NW";
                } else if (TopPaddleCollision()) {
                    ball.dir = "SE";
                } else if (TopWallCollision()) {
                    //Reset();
                    ball.dir = "SE";
                }
                break;
            case "NW":
                if (LeftCollision()) {
                    ball.dir = "NE";
                } else if (TopPaddleCollision()) {
                    ball.dir = "SW";
                } else if (TopWallCollision()) {
                    //Reset();
                    ball.dir = "SW";
                }
                break;
        }
    }

    public void updateAngle() {
        switch (ball.dir) {
            case "NE":  ball.angle = -45;
                break;
            case "SE":  ball.angle = 45;
                break;
            case "SW":  ball.angle = 135;
                break;
            case "NW":  ball.angle = -135;
        }
    }

    class Ball {
        float x, y, r, speed, angle;
        String dir;
        Ball() {
            this.speed = 10;
            this.angle = 0;
            this.dir = "SE";
        }
    }
}
