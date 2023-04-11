package android.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class PersonController extends InputAdapter {

    private Snake snake;
    private float screenWidth;


    public PersonController(Snake snake) {
        this.snake = snake;
        this.screenWidth = Gdx.graphics.getWidth();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float touchX = (float) screenX / screenWidth;

        if (touchX < 0.5f) {
            snake.turnLeft();
        } else {
            snake.turnRight();
        }
        if (starter.gameStop)  doubleTap();


        return true;
    }

    private static final float DOUBLE_TAP_TIME = 0.25f;

    private float lastTapTime = 0;

    public void doubleTap() {
        float currentTime = TimeUtils.nanoTime() / 1000000000.0f;
        if ((currentTime - lastTapTime) <= DOUBLE_TAP_TIME) {

            if (starter.gameStop) starter.restartKey = true;

        }
        lastTapTime = currentTime;
    }
}
