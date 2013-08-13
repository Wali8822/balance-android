package com.onesoft.balance_formal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class AbstractLevel implements SensorEventListener {
	private Texture m_texBackground = null;
	private TextureRegion m_region = null;

	protected static final int ANIMATE_FIRE_FRAME_DURATION = 150;
	private Animation m_animaFire = null;
	private Texture[] m_texFires = null;
	private float m_fireFrameDuration = 0.0f;

	protected static final int ANIMATE_BOMB_FRAME_DURATION = 200;
	protected Animation m_animaBomb = null;
	protected Texture m_texBomb = null;
	protected boolean m_bIsBombed = false;
	protected float m_bombFrameDuration = 0.0f;

	protected TextureRegion m_texRegionClock = null;
	protected TextureRegion m_texRegionClockForeground = null;
	protected Texture m_texClock = null;
	protected Texture m_texClockForeground = null;

	protected GameLoopScreen m_theScreen = null;

	protected RectangleEx m_fireArea = null;

	protected long m_timeBomb = 0;
	protected long m_timeFire = 0;
	protected long m_timeEllapse = 0;

	protected long m_time = 0;

	private float x = 0.0f;
	private float y = 0.0f;

	private float m_clockAngle = 0.0f;

	protected int m_currentBestScore = 0;

	public AbstractLevel(String texFile, GameLoopScreen s) {
		m_theScreen = s;

		m_texBackground = new Texture(Gdx.files.internal(texFile));
		m_region = new TextureRegion(m_texBackground);

		m_texClock = new Texture(Gdx.files.internal("data/clock.png"));
		m_texRegionClock = new TextureRegion(m_texClock);
		m_texClockForeground = new Texture(
				Gdx.files.internal("data/clock_foreground.png"));
		m_texRegionClockForeground = new TextureRegion(m_texClockForeground);

		TextureRegion keyFrames[] = new TextureRegion[11];
		m_texFires = new Texture[11];

		for (int i = 1; i <= 11; ++i) {
			m_texFires[i - 1] = new Texture(Gdx.files.internal("data/fire_" + i
					+ ".png"));

			keyFrames[i - 1] = new TextureRegion(m_texFires[i - 1]);
		}

		m_animaFire = new Animation(Settings.FRAME_DURATON, keyFrames);

		m_fireArea = new RectangleEx(Settings.SCENE_WIDTH / 3 - 1.0f, 0,
				Settings.SCENE_WIDTH / 3 + 2.0f, Settings.SCENE_HEIGHT / 4);

		m_texBomb = new Texture(Gdx.files.internal("data/bomb.png"));
		m_animaBomb = new Animation(Settings.FRAME_DURATON, new TextureRegion(
				m_texBomb, 0, 64, 64, 64), new TextureRegion(m_texBomb, 64, 64,
				64, 64), new TextureRegion(m_texBomb, 128, 64, 64, 64),
				new TextureRegion(m_texBomb, 192, 64, 64, 64));

		getBestScore();
	}

	protected abstract void buildScene();

	protected void getBestScore() {
		m_currentBestScore = (int) ((Balance_androidActivity) Resources.m_theApp)
				.getValue("" + m_theScreen.m_CurrentLevel);
	}

	public void restart() {
		m_timeEllapse = 0;
		m_time = 0;

		m_bIsBombed = false;

		m_timeBomb = 0;

		getBestScore();
	}

	public void update(float deltaTime) {
		if (Settings.B_START) {
			Resources.m_theSimulation.update();

			if ((System.currentTimeMillis() - m_timeFire) >= ANIMATE_FIRE_FRAME_DURATION) {
				m_fireFrameDuration += Settings.FRAME_DURATON
						* Render.randInt(11);

				m_timeFire = System.currentTimeMillis();
			}

			if ((System.currentTimeMillis() - m_timeBomb) >= ANIMATE_BOMB_FRAME_DURATION) {
				m_bombFrameDuration += Settings.FRAME_DURATON;

				m_timeBomb = System.currentTimeMillis();

				Log.i("Bomb:", "Anima");
			}

			if ((System.currentTimeMillis() - m_time) >= 1000) {
				m_timeEllapse += 1;
				m_time = System.currentTimeMillis();
			}

			m_clockAngle += 0.1f;
		}
	}

	public void render(float deltaTime) {
		Render.draw(m_region, 0, 0, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);

		TextureRegion t = m_animaFire.getKeyFrame(m_fireFrameDuration, true);

		Render.draw(t, 0, 0, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);

		Render.draw(m_texRegionClock, 0.1f, Settings.SCENE_HEIGHT
				- Settings.CLOCK_HEIGHT, Settings.CLOCK_WIDTH,
				Settings.CLOCK_HEIGHT);
		Render.draw(m_texRegionClockForeground, 0.1f, Settings.SCENE_HEIGHT
				- Settings.CLOCK_HEIGHT, Settings.CLOCK_WIDTH,
				Settings.CLOCK_HEIGHT, m_clockAngle);

		BitmapFont.TextBounds b = Render.draw("" + m_timeEllapse + "s",
				Settings.CLOCK_WIDTH * 100 + 10.0f,
				Settings.SCREEN_HEIGHT - 10.0f);

		Render.draw("Level:" + m_theScreen.m_CurrentLevel + "   " + "Best:"
				+ m_currentBestScore, b.width + 10.0f + 64 + 30,
				Settings.SCREEN_HEIGHT - 10.0f);
	}

	public Vector2 near(float x1, float y1, float x2, float y2, float x3,
			float y3) {
		Vector2 p1 = new Vector2(y1, y1);
		Vector2 p2 = new Vector2(x2, y2);
		Vector2 p3 = new Vector2(x3, y3);

		if (p1.dst(p2) > p1.dst(p3)) {
			return p3;
		} else {
			return p2;
		}
	}

	public void dispose() {
		m_texBackground.dispose();

		for (int i = 0; i < 11; ++i) {
			m_texFires[i].dispose();
		}

		m_texBomb.dispose();

		m_texClock.dispose();
		m_texClockForeground.dispose();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		x = se.values[SensorManager.DATA_X];
		y = se.values[SensorManager.DATA_Y];

		Log.i("Gravity Change", "x=" + x + ";y=" + y);

		Resources.m_theSimulation.setGravity(y, -Math.abs(x));
	}
}
