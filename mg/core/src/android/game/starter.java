package android.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class starter extends ApplicationAdapter {
    private Snake snake;

    private ShapeRenderer shapeRenderer;
    private PersonController controller;
    Berry berry = new Berry();
    private ArrayList<Food> foodList;
    private int score = 0;


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

        berry.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        foodList.add(berry);


    }

    @Override
    public void render() {
        int prevScore = score;
         score = snake.getLength();
         if ( score!=prevScore) scoreChecker(snake);

        // Очистка экрана
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Обновление змеи
        float delta = Gdx.graphics.getDeltaTime();
        this.snake.move(delta);


        // Начало отрисовки
//		shapeRenderer.begin();


        // Отрисовка каждого блока змеи
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
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

            berry.draw(shapeRenderer);
        }
//
        shapeRenderer.setColor(Color.BLACK);
        int i = 0;
        for (Vector2 block : snake.getBody()) {

            Color color = (i++ == 0) ? Color.RED : Color.GREEN;
            color = (i % 2 == 0) ? Color.LIME : color;
            snake.drawBlock(block.x, block.y, color, shapeRenderer);
        }

        // Завершение отрисовки
//		shapeRenderer.end();
        shapeRenderer.end();

        if (snake.checkCollision(foodList)) {

            foodList.clear();

            // Создаем новую ягоду и добавляем ее в список еды
            berry.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), (int) ((Math.random() * ((2 - 1) + 1)) + 1));
            foodList.add(berry);

            // Перезапускаем змею в центре экрана
            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();
            this.snake = new Snake(screenWidth / 2, screenHeight / 2, 32);

            // Устанавливаем новый контроллер для новой змеи
            controller = new PersonController(snake);
            Gdx.input.setInputProcessor(controller);

//


        }

    }

    @Override
    public void dispose() {
        // Освобождение ресурсов
        shapeRenderer.dispose();
    }

    public  void scoreChecker(Snake snake) {



        if (score % 5 == 0 && Math.random() > 0.55) {
            Berry br = new Berry(Color.ORANGE);
            br.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            foodList.add(br);
        }
        if (score % 3 == 0 && Math.random() > 0.75) {
            Berry br = new Berry();
            br.spawn(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            foodList.add(br);
        }

    }
}
