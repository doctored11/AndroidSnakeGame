package android.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Snake {

    private ArrayList<Vector2> body;
    private int blockSize;
    private Vector2 direction;
    private float speed;
    private float moveTimer = 0;
    private float stepSize;

    public Snake(int x, int y, int blockSize) {
        this.blockSize = blockSize;
        this.direction = new Vector2(1, 0);
        this.body = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.body.add(new Vector2(x - i * blockSize, y));
        }
        this.speed = 0.1f;
        this.stepSize = blockSize;
    }


    public void turnLeft() {

        Vector2 rotatedCounterClockwise = new Vector2(-direction.y, direction.x);
        this.direction.set(rotatedCounterClockwise);
//
    }

    public void turnRight() {
        Vector2 rotatedClockwise = new Vector2(direction.y, -direction.x);
        this.direction.set(rotatedClockwise);
//
    }


    public void move(float delta) {
        moveTimer += delta;
        if (moveTimer > speed) {
            moveTimer = 0;
            Vector2 head = body.get(0).cpy().add(direction.cpy().scl(blockSize));
            if (passThroughBorders()) {
                float maxX = Gdx.graphics.getWidth();
                float maxY = Gdx.graphics.getHeight();
                if (head.x < 0) {
                    head.x = maxX - blockSize;
                } else if (head.x > maxX - blockSize) {
                    head.x = 0;
                }
                if (head.y < 0) {
                    head.y = maxY - blockSize;
                } else if (head.y > maxY - blockSize) {
                    head.y = 0;
                }
            }
            body.add(0, head);
            this.body.remove(this.body.size() - 1);
        }
    }


    int screenWidth = Gdx.graphics.getWidth();
    int screenHeight = Gdx.graphics.getHeight();

    public boolean passThroughBorders() {
        if (this.body.get(0).x >= screenWidth) {
            this.body.get(0).x = 0;
            return true;
        } else if (this.body.get(0).x < 0) {
            this.body.get(0).x = screenWidth - this.blockSize;
            return true;
        } else if (this.body.get(0).y >= screenHeight) {
            this.body.get(0).y = 0;
            return true;
        } else if (this.body.get(0).y < 0) {
            this.body.get(0).y = screenHeight - this.blockSize;
            return true;
        }
        return false;
    }


    public void setDirection(Vector2 newDirection) {
//        if (!this.direction.isOpposite(newDirection)) {
        this.direction = newDirection;
//        }
    }



    public void grow(int n) {
        for (int i = 0; i < n; i++) {
            Vector2 tail = this.body.get(this.body.size() - 1).cpy();
            this.body.add(tail);
        }
    }


    public void drawBlock(float x, float y, Color color, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, blockSize, blockSize);
    }

    public ArrayList<Vector2> getBody() {
        return this.body;
    }

    public Vector2 getDirection() {
        return this.direction;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public int getLength() {
        return this.body.size();
    }

    public Vector2 getPosition(int index) {
        return body.get(index);
    }

    //колизии и взаимодействия

    public boolean checkCollision(ArrayList<Food> foodList) {
        Vector2 head = this.body.get(0);
        if (!passThroughBorders() && (head.x < 0 || head.x > Gdx.graphics.getWidth() || head.y < 0 || head.y > Gdx.graphics.getHeight())) {
            return true;
        }

        for (int i = 1; i < this.body.size(); i++) {
            if (head.equals(this.body.get(i)) && !passThroughBorders()) {
                return true;
            }
        }
        for (int i = 0; i < foodList.size(); i++) {
            Vector2 food = foodList.get(i).getPosition();
            final int RADIUS = 50;
            if (head.dst(food) < RADIUS) {
                int sat =  foodList.get(i).getSaturation();
                this.grow(sat);
                foodList.get(i).spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), (int) ((Math.random() * ((2 - 1) + 1)) + 1),this);

                return false;
            }
        }
        return false;
    }


    public float getSegmentSize() {
        return blockSize;
    }
}
