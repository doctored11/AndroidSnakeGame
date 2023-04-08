package android.game;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;

import java.util.ArrayList;


public class Mouse extends Food {
    int screenWidth= Gdx.graphics.getWidth();
    int screenHeight=Gdx.graphics.getHeight();
    private float speed = 0.5f;

    private static final int MIN_DIRECTION_CHANGE_DELAY = 2;
    private static final int MAX_DIRECTION_CHANGE_DELAY = 5;

    private int directionChangeDelay;
    private int directionChangeCounter;
    private Vector2 direction;
    private List<Food> foodList;


    public Mouse() {

        super(random(3, 8), Color.GRAY, false, random.nextInt(36 - 30 + 1) + 30);

        this.directionChangeDelay = random(MIN_DIRECTION_CHANGE_DELAY, MAX_DIRECTION_CHANGE_DELAY);


        this.directionChangeCounter = 0;
        this.direction = new Vector2(getRandomFloatInRange(), getRandomFloatInRange()).nor();
    }

    //    @Override
//    public void animate(float delta) {
//        // Двигаем мышь в текущем направлении
//        Vector2 newPosition = getPosition().add(direction.scl(delta));
//        setPosition((int) newPosition.x, (int) newPosition.y);
//
//        // Уменьшаем счетчик задержки до смены направления
//        directionChangeCounter--;
//
//        // Если счетчик дошел до нуля, меняем направление
//        if (directionChangeCounter <= 0) {
//            directionChangeCounter = directionChangeDelay;
//            directionChangeDelay = random(MIN_DIRECTION_CHANGE_DELAY, MAX_DIRECTION_CHANGE_DELAY);
//            direction.set(getRandomFloatInRange(), getRandomFloatInRange());
//        }
//    }
//    @Override
    private float moveTimer = 0;

    public void animate(float delta) {
        moveTimer += delta;
        int stepCount = getRandomIntInMyRange(5, 11);

        // Выбираем случайное направление
        int direction = MathUtils.random(0, 3); // 0 - вверх, 1 - вниз, 2 - вправо, 3 - влево





        switch (direction) {
            case 0: // вверх
                for (int i = 0; i < stepCount; ++i)
                    if (moveTimer > speed) {
                        moveTimer = 0;
                        setPosition((int) getPosition().x, (int) (getPosition().y + this.getSize()));
                    }
                break;
            case 1: // вниз
                for (int i = 0; i < stepCount; ++i)
                    if (moveTimer > speed) {
                        moveTimer = 0;
                        setPosition((int) getPosition().x, (int) (getPosition().y - this.getSize()));
                    }
                break;
            case 2: // вправо
                for (int i = 0; i < stepCount; ++i)
                    if (moveTimer > speed) {
                        moveTimer = 0;
                        setPosition((int) (getPosition().x + this.getSize()), (int) getPosition().y);
                    }
                break;
            case 3: // влево
                for (int i = 0; i < stepCount; ++i)
                    if (moveTimer > speed) {
                        moveTimer = 0;
                        setPosition((int) (getPosition().x - this.getSize()), (int) getPosition().y);
                    }
                break;

        }

    }


    public static float getRandomFloatInRange() {
        return MathUtils.random(0f, 1f) * 2f - 1f;
    }

    public static int getRandomIntInMyRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
