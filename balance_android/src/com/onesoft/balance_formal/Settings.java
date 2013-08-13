package com.onesoft.balance_formal;

import com.badlogic.gdx.Gdx;

public class Settings {
	public static boolean bVibrateEnable = false;
	public static boolean bSoundEnabled = false;

	/*
	 * the decrease step of alpha
	 */
	public static float ALPHA_STEP = 0.05f;

	/*
	 * the size of the screen
	 */
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;

	public static float SCENE_WIDTH = 0.0f;
	public static float SCENE_HEIGHT = 0.0f;

	/*
	 * size of the button Ok,Cancel,Back
	 */
	public static int COMMON_BUTTON_WIDTH = 0;
	public static int COMMON_BUTTON_HEIGHT = 0;

	public static final byte LEVEL_METAL_BALL = 0;
	public static final byte LEVEL_PINGPANG_BALL = 1;
	public static final byte LEVEL_RUBBER_BALL = 2;
	/*
	 * The game level ; LEVEL_METAL_BALL : Metal ball LEVEL_PINGPANG_BALL:
	 * Pingpang ball LEVEL_RUBBER_BALL : Rubber ball
	 */
	public static byte LEVEL = LEVEL_METAL_BALL;

	/*
	 * The total count of Levels
	 */
	public static byte LEVEL_COUNT = 10;

	/*
	 * The width and height of the level choose button
	 */
	public static int LEVEL_BUTTON_WIDTH = 100;
	public static int LEVEL_BUTTON_HEIGHT = 100;

	/*
	 * Gap size
	 */
	public static int GAP_SIZE = 20;

	/*
	 * The count of button per line
	 */
	public static int BUTTON_COUNT_PER_LINE = 5;

	/*
	 * If the game begin determine the world step
	 */
	public static boolean B_START = false;

	public static boolean B_GAME_OVER = false;

	public static float FRAME_DURATON = 0.1f;

	/*
	 * The Object's size in the world
	 */
	public static float BALL_RADIUS = 0.2f;
	public static float BOARD_WIDTH = 2.0f;
	public static float BOARD_HEIGHT = 0.2f;

	/*
	 * The size of the clock
	 */
	public static float CLOCK_WIDTH = 0.5f;
	public static float CLOCK_HEIGHT = 0.5f;

	/*
	 * Density and Restitution of the balls
	 */
	public static float DENSITY_METAL = 5.0f;
	public static float RESTITUTION_METAL = 0.2f;

	public static float DENSITY_RUBBER = 2.5f;
	public static float RESTITUTION_RUBBER = 0.45f;

	public static float DENSITY_PINGPANG = 1.0f;
	public static float RESTITUTION_PINGPANG = 0.5f;

	public static void load() {
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();

		COMMON_BUTTON_WIDTH = 100;
		COMMON_BUTTON_HEIGHT = 50;

		SCENE_WIDTH = 8.0f;
		SCENE_HEIGHT = 4.8f;
	}

	public static void dispose() {

	}
}
