package com.eduardo.animation;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Game extends com.badlogic.gdx.Game {


	public static final int SCR_HEIGHT = 480;
	public static final int SCR_WIDTH = 800;
	public SpriteBatch batch;
	public BitmapFont font;

	public boolean game_over = false;
	Skin skin;




	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new AnimationScreen(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void pause(){

	}
	public void resume(){

	}
	public void dispose() {
		batch.dispose();
	}

}
