package android.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.awt.Canvas;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.util.ArrayList;

import sun.font.TrueTypeFont;

public class starter extends ApplicationAdapter {
    private Snake snake;
    private BitmapFont font;
    private SpriteBatch spriteBatch;


//     final int SCREEN_WIDTH = Gdx.graphics.getWidth(); //?? -! не работате тут - разобраться
//    final int SCREE_HEIGHT = Gdx.graphics.getHeight();


    private ShapeRenderer shapeRenderer;
    private PersonController controller;
    Berry berry = new Berry();
    private ArrayList<Food> foodList;
    private int score = 0;
    public static boolean restartKey = false;
    public static boolean gameStop = false;
    float tapAlpha = 1.0f;
    float tapScale = 1.0f;
    float tapDuration = 1.0f;
    float tapTimer = 0.0f;
    Color backColor1 = Color.valueOf("FBFFF0");
    Color backColor2 = Color.valueOf("D1FCA1");
    Color backColor3 = Color.valueOf("96FDF8");
    Color backColor4 = Color.valueOf("BDFEFC");


    @Override
    public void create() {


        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();


        // Создание змеи в центре экрана
        this.snake = new Snake(screenWidth / 2, screenHeight / 2, 32);

        // Создание SshapeRenderer для отрисовки
//
        shapeRenderer = new ShapeRenderer();
        // Создание списка для еды
        this.foodList = new ArrayList<>();

        // Создание контроллера персонажа
        controller = new PersonController(snake);

        // Установка контроллера в качестве обработчика ввода
        Gdx.input.setInputProcessor(controller);

        berry.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), snake);
        foodList.add(berry);


    }


    @Override
    public void render() {

        final int SCREEN_WIDTH = Gdx.graphics.getWidth();
        final int SCREE_HEIGHT = Gdx.graphics.getHeight();
        float delta = Gdx.graphics.getDeltaTime();


        int prevScore = score;
        score = snake.getLength();

        if (score != prevScore) scoreChecker(snake);


        // Очистка экрана

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (snake.checkCollision(foodList)) {
            gameStop = true;
            endMenu(delta);
            shapeRenderer.end();


            if (restartKey) restart();
//            restart();
            return;

//
        }


        if (gameStop) {
            if (restartKey) restart();

            endMenu(delta);
            return;
        }
        drawCheckerboard(SCREEN_WIDTH,SCREE_HEIGHT,128, backColor1,backColor2);

        // Обновление змеи

        this.snake.move(delta);


        // Начало отрисовки
//		shapeRenderer.begin();


        // Отрисовка каждого блока змеи

//
        // Обновление и отрисовка каждой ягоды

        for (int i = 0; i < foodList.size(); i++) {
            Food berry = foodList.get(i);
            berry.animate(delta);
            berry.update(delta);
            if (berry.shouldRemove()) {
                foodList.remove(berry);
                scoreChecker(snake);

            }

            if (berry.getPosition().x < -1 || berry.getPosition().x > SCREEN_WIDTH || berry.getPosition().y < -1 || berry.getPosition().y > SCREE_HEIGHT) {
                foodList.remove(this);
            }

            berry.draw(shapeRenderer);
        }
//
        shapeRenderer.setColor(Color.BLACK);
        int i = 0;
        for (Vector2 block : snake.getBody()) {
            if (gameStop) continue;

            Color color = (i++ == 0) ? Color.RED : Color.GREEN;
            color = (i % 2 == 0) ? Color.LIME : color;
            snake.drawBlock(block.x, block.y, color, shapeRenderer);
        }

        // Завершение отрисовки
//		shapeRenderer.end();
        shapeRenderer.end();


    }

    @Override
    public void dispose() {
        // Освобождение ресурсов
        shapeRenderer.dispose();
    }

    public void scoreChecker(Snake snake) {

        if (score % 5 == 0 && Math.random() > 0.35) {
            Berry br = new Berry(Color.ORANGE);
            br.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), snake);
            foodList.add(br);
        }
        if (score % 3 == 0 && Math.random() > 0.55) {
            Berry br = new Berry();
            br.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), snake);
            foodList.add(br);
        }
        if (score % 8 == 0 && Math.random() >= 0.85) {
            Mouse ms = new Mouse();
            ms.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), snake);
            foodList.add(ms);
        }
        if (score % 20 == 0 && Math.random() > 0.75) {
           Food bb = new Food(21,Color.BLUE,true,40);
            bb.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), snake);
            foodList.add(bb);
        }

    }

    public void restart() {
        restartKey = false;
        gameStop = false;
        foodList.clear();

        // Создаем новую ягоду и добавляем ее в список еды
        berry.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), (int) ((Math.random() * ((2 - 1) + 1)) + 1), snake);
        foodList.add(berry);

        // Перезапускаем змею в центре экрана
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        this.snake = new Snake(screenWidth / 2, screenHeight / 2, 32);

        // Устанавливаем новый контроллер для новой змеи
        controller = new PersonController(snake);
        Gdx.input.setInputProcessor(controller);
    }

    public void endMenu(float delta) {
        final int SCREEN_WIDTH = Gdx.graphics.getWidth();
        final int SCREE_HEIGHT = Gdx.graphics.getHeight();

        drawCheckerboard(SCREEN_WIDTH,SCREE_HEIGHT,32, backColor3,backColor4);
        // создаем экземпляр BitmapFont для отображения текста
        BitmapFont font = new BitmapFont();


        // устанавливаем цвет текста (белый цвет в данном случае)
        font.setColor(Color.WHITE);

        // устанавливаем размер шрифта
        font.getData().setScale(6);



        // отображаем текст "SCORE " + score по центру экрана
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, "SCORE " + score);
        float x = Gdx.graphics.getWidth() / 2 - layout.width / 2;
        float y = Gdx.graphics.getHeight() / 2 + layout.height / 2;
        font.draw(batch, layout, x, y);

        aniTxt(delta,batch,y,layout.height);

        batch.end();
    }

    float scale = 1f;
    final float ANIMATION_SPEED = 5f;
    final float MAX_SIZE = 2.5f;
    final float MIN_SIZE = 1.4f;
    boolean increasing = true;


    public void aniTxt(float delta, Batch batch, float y, float offsetY) {
        BitmapFont font2 = new BitmapFont();


        // Изменяем масштаб ягоды для создания эффекта пульсации
        font2.setColor(Color.YELLOW);
        if (increasing) {
            scale += delta * ANIMATION_SPEED;
            if (scale >= MAX_SIZE) {
                scale = MAX_SIZE;
                increasing = false;
            }
        } else {
            scale -= delta * ANIMATION_SPEED;
            if (scale <= MIN_SIZE) {
                scale =MIN_SIZE;
                increasing = true;
            }

        }


        font2.getData().setScale(scale);
        GlyphLayout tapLayout = new GlyphLayout();
        tapLayout.setText(font2, "double click to restart");
        float tapX = Gdx.graphics.getWidth() / 2 - tapLayout.width / 2;
        float tapY = (float) (y - (1.2*offsetY));
        font2.draw(batch, tapLayout, tapX, tapY);
//


    }



    public void drawCheckerboard(int screenWidth, int screenHeight, int cellSize, Color color1, Color color2) {


        int numCellsX = screenWidth / cellSize;
        int numCellsY = screenHeight / cellSize;

        for (int y = 0; y <= numCellsY; y++) {
            for (int x = 0; x <= numCellsX; x++) {

                Color color = ((x + y) % 2 == 0) ? color2 : color1;
                shapeRenderer.setColor(color);
                shapeRenderer.rect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }



}


//



