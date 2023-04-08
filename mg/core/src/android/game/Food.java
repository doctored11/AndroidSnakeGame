package android.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Food {

    private int saturation;
    private Color color;
    private boolean isTimed;
    private float timer;
    private Vector2 position;
    private float size;
    private static float MIN_SCALE;
    private static float MAX_SCALE;
    private static float ANIMATION_SPEED;

    private float scale = MIN_SCALE;
    private boolean increasing = true;

    public Food(int saturation, Color color, boolean isTimed, float size) {
        this.MIN_SCALE = size;
        this.MAX_SCALE = 1.3f * size;
        this.ANIMATION_SPEED = 0.4f * size;
        this.saturation = saturation;
        this.color = color;
        this.isTimed = isTimed;
        this.position = new Vector2();
        this.size = size;
    }

    public void spawn(int screenWidth, int screenHeight) {
        float x = MathUtils.random(screenWidth);
        float y = MathUtils.random(screenHeight);
        position.set(x, y);
        if (isTimed) {
            timer = MathUtils.random(1f, 6f); // случайное время до исчезновения в секундах
        }
    }
    public void spawn(int screenWidth, int screenHeight, int saturation) {
        this.saturation=saturation;
        float x = MathUtils.random(screenWidth);
        float y = MathUtils.random(screenHeight);
        position.set(x, y);
        if (isTimed) {
            timer = MathUtils.random(1f, 6f); // случайное время до исчезновения в секундах
        }
    }


    public int getSaturation() {
        return saturation;
    }

    public Color getColor() {
        return color;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isTimed() {
        return isTimed;
    }

    public void update(float delta) {
        if (isTimed) {
            timer -= delta;
        }
    }

    public boolean shouldRemove() {
        return isTimed && timer <= 0;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, size / 2);
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void animate(float delta) {
        // Изменяем масштаб ягоды для создания эффекта пульсации
        if (increasing) {
            scale += delta * ANIMATION_SPEED;
            if (scale >= MAX_SCALE) {
                scale = MAX_SCALE;
                increasing = false;
            }
        } else {
            scale -= delta * ANIMATION_SPEED;
            if (scale <= MIN_SCALE) {
                scale = MIN_SCALE;
                increasing = true;
            }

        }
        this.size = scale;
//        scale = (float) ((Math.random() * (MAX_SCALE -MIN_SCALE)) + MIN_SCALE);
    }
}
