package android.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Berry extends Food {

    private static final Color BERRY_COLOR = Color.valueOf("#ed0c79");



    private boolean increasing = true;

    public Berry() {
        super(MathUtils.random(1, 2), BERRY_COLOR, false,32);
    }
    public Berry(Color color) {
        super(MathUtils.random(2, 5), color, true,32);
    }



    public float getScale() {
        return 1;
    }
}
