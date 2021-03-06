package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.stargame.gameobject.gamearea.Background;
import ru.geekbrains.stargame.template.ButtonLayout;
import ru.geekbrains.stargame.gameobject.Star;
import ru.geekbrains.stargame.gameobject.gamearea.StarEmulator;
import ru.geekbrains.stargame.gameobject.buttons.ExitButton;
import ru.geekbrains.stargame.gameobject.buttons.PlayButton;
import ru.geekbrains.stargame.template.MyScreen;
import ru.geekbrains.stargame.template.Rect;

public class GameMenu extends MyScreen {
    private Background background;
    private TextureAtlas atlas;
    private ButtonLayout buttonLayout;
    private StarEmulator starEmulator;

    public GameMenu(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        this.background = new Background(new TextureRegion(new Texture("textures/background.png")));
        this.atlas = new TextureAtlas("textures/button.pack");
        this.buttonLayout = new ButtonLayout();
        this.buttonLayout.addButton(new ExitButton(atlas));
        this.buttonLayout.addButton(new PlayButton(atlas, this.game));
        this.buttonLayout.setSize(100,25);

        this.starEmulator = new StarEmulator(new Star(0.005f, this.atlas.findRegion("star")), 25);
    }

    @Override
    public void update(float dTime) {
        super.update(dTime);
        this.starEmulator.update(dTime);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        this.background.draw(this.spriteBatch);
        this.starEmulator.draw(this.spriteBatch);
        this.buttonLayout.draw(this.spriteBatch);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.atlas.dispose();
    }

    @Override
    public void resize(Rect world) {
        this.background.resize(world);
        this.buttonLayout.resize(world);
        this.starEmulator.resize(world);
    }

    @Override
    public void touchDown(Vector2 vector2, int pointer, int button) {
        this.buttonLayout.touchDown(vector2, pointer, button);
    }

    @Override
    public void touchUp(Vector2 vector2, int pointer, int button) {
        this.buttonLayout.touchUp(vector2, pointer, button);
    }
}
