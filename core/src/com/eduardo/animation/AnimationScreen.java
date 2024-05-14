package com.eduardo.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class AnimationScreen implements Screen {

	final Game game;

	OrthographicCamera camera;

	// Constant rows and columns of the sprite sheet
	private static final int FRAME_COLS = 5, FRAME_ROWS = 2;

	// Objects used
	Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet, background;
	TextureRegion bgRegion;

	// A variable for tracking elapsed time for the animation
	float stateTime;

	Rectangle up, down, left, right, fire;
	final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;

	int posx, posy, scaleX;

	public AnimationScreen(final Game game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.SCR_WIDTH, game.SCR_HEIGHT	);
	}

	@Override
	public void show() {
// Load the sprite sheet as a Texture
		walkSheet = new Texture(Gdx.files.internal("spriteSheet2.png"));

		// facilities per calcular el "touch"
		up = new Rectangle(0, game.SCR_HEIGHT*2/3, game.SCR_WIDTH, game.SCR_HEIGHT/3);
		down = new Rectangle(0, 0, game.SCR_WIDTH, game.SCR_HEIGHT/3);
		left = new Rectangle(0, 0, game.SCR_WIDTH/3, game.SCR_HEIGHT);
		right = new Rectangle(game.SCR_WIDTH*2/3, 0, game.SCR_WIDTH/3, game.SCR_HEIGHT);

		posx = 0;
		posy = 0;
		scaleX = 1;

		// bg
		background = new Texture(Gdx.files.internal("background.png"));
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		bgRegion = new TextureRegion(background);

		// Use the split utility method to create a 2D array of TextureRegions. This is
		// possible because this sprite sheet contains frames of equal size and they are
		// all aligned.
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);

		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		// Initialize the Animation with the frame interval and array of frames
		walkAnimation = new Animation<TextureRegion>(0.080f, walkFrames);

		// Instantiate a SpriteBatch for drawing and reset the elapsed animation
		// time to 0
		stateTime = 0f;
	}

	@Override
	public void render(float delta) {
		int direction = virtual_joystick_control();

		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, false);

		float bgSpeed = 5f; // Adjust this value to control the speed of background movement

		switch (direction) {
			case UP:
				currentFrame = walkAnimation.getKeyFrame(stateTime, true);
				scaleX = 1;
				posx = posx;
				posy = posy + 10;
				break;
			case DOWN:
				currentFrame = walkAnimation.getKeyFrame(stateTime, true);
				posx = posx;
				posy = posy - 10;
				break;
			case LEFT:
				currentFrame = walkAnimation.getKeyFrame(stateTime, true);
				scaleX = -1;
				posx = posx - 10;
				posy = posy;
				break;
			case RIGHT:
				currentFrame = walkAnimation.getKeyFrame(stateTime, true);
				posx = posx + 10;
				posy = posy;
				break;
			default:
				currentFrame = walkAnimation.getKeyFrame(0); // Default to idle animation
				break;
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
		// Get current frame of animation for the current stateTime
		// br
		bgRegion.setRegion(posx,posy,game.SCR_WIDTH,game.SCR_HEIGHT);
		game.batch.begin();
		game.batch.draw(bgRegion,posx,posy);
		game.batch.draw(currentFrame, posx, posy, 0, 0,
				currentFrame.getRegionWidth(),currentFrame.getRegionHeight(),scaleX,1,0);
		game.batch.end();
	}


	@Override
	public void resize(int width, int height) {

	}


	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
		game.dispose();
		walkSheet.dispose();
	}

	protected int virtual_joystick_control() {
		// iterar per multitouch
		// cada "i" és un possible "touch" d'un dit a la pantalla
		for(int i=0;i<10;i++)
			if (Gdx.input.isTouched(i)) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
				// traducció de coordenades reals (depen del dispositiu) a 800x480
				camera.unproject(touchPos);
				if (up.contains(touchPos.x, touchPos.y)) {
					return UP;
				} else if (down.contains(touchPos.x, touchPos.y)) {
					return DOWN;
				} else if (left.contains(touchPos.x, touchPos.y)) {
					return LEFT;
				} else if (right.contains(touchPos.x, touchPos.y)) {
					return RIGHT;
				}
			}
		return IDLE;
	}
}

