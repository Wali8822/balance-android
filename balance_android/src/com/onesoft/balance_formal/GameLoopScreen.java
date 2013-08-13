package com.onesoft.balance_formal;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class GameLoopScreen extends Screen {
	private AbstractLevel m_theLevel = null;

	private SensorManager m_theSensorManager = null;
	private Sensor m_theSensor = null;

	protected GameLoopWidget m_theWidget = null;
	protected boolean m_bReady = false;

	protected int m_CurrentLevel = 0;

	protected String m_strLevelDescribe = "";

	protected long m_time = 0;
	protected byte m_index = 0;

	public GameLoopScreen(int which, Game game) {
		super(game);

		m_theSensorManager = (SensorManager) Resources.m_theApp
				.getSystemService(Context.SENSOR_SERVICE);
		m_theSensor = m_theSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		m_CurrentLevel = which;
		chooseLevel(which);

		m_theSensorManager.registerListener(m_theLevel, m_theSensor,
				SensorManager.SENSOR_DELAY_NORMAL);

		StartWidget w = new StartWidget(this);
		w.setLabelText(m_strLevelDescribe);
		Resources.m_theUI.setWidget(w);

		m_theWidget = w;
	}

	@Override
	public void update(float deltaTime) {
		m_theLevel.update(deltaTime);

		if (Settings.B_GAME_OVER) {
			if ((System.currentTimeMillis() - m_time) > 1000) {
				m_time = System.currentTimeMillis();
				++m_index;
			}

			if (m_index >= 2) {
				Settings.B_GAME_OVER = false;
				this.gameOver();
				m_index = 0;
				m_time = 0;
			}
		}
	}

	protected void chooseLevel(int which) {
		if (which <= 0 || which > Settings.LEVEL_COUNT) {
			return;
		}

		if (m_theLevel != null) {
			m_theLevel.dispose();
			m_theLevel = null;
		}

		switch (which) {
		case 1:
			m_theLevel = new Level1(this);
			m_strLevelDescribe = Resources.m_theApp.getString(R.string.level1);
			break;
		case 2:
			m_theLevel = new Level2(this);
			break;
		case 3:
			m_theLevel = new Level3(this);
			break;
		case 4:
			m_theLevel = new Level4(this);
			break;
		case 5:
			m_theLevel = new Level5(this);
			break;
		case 6:
			m_theLevel = new Level6(this);
			break;
		default:
			assert (false);
		}
	}

	@Override
	public void render(float deltaTime) {
		m_theLevel.render(deltaTime);

		if (!m_bReady) {
			Resources.m_theUI.render();
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		m_theSensorManager.unregisterListener(m_theLevel);

		m_theLevel.dispose();
	}

	public boolean isReady() {
		return m_bReady;
	}

	public void onBack() {
		game.setScreen(new MenuScreen(game));
	}

	public void onOk() {
		m_bReady = true;
		Settings.B_START = true;
	}

	public void retry() {
		m_theLevel.restart();

		onOk();
	}

	public void stop() {
		if (!m_bReady) {
			return;
		}

		if (m_theWidget != null) {
			m_theWidget.destroy();
			m_theWidget = null;
		}

		StopWidget w = new StopWidget(this);
		Resources.m_theUI.setWidget(w);

		m_theWidget = w;

		m_bReady = false;
		Settings.B_START = false;
	}

	public void gameOver() {
		if (!m_bReady) {
			return;
		}

		if (m_theWidget != null) {
			m_theWidget.destroy();
			m_theWidget = null;
		}

		GameOverWidget w = new GameOverWidget(this);

		Resources.m_theUI.setWidget(w);

		m_theWidget = w;

		m_bReady = false;
		Settings.B_START = false;

		long currentScore = m_theLevel.m_timeEllapse;
		long bestScore = ((Balance_androidActivity) Resources.m_theApp)
				.getValue("" + m_CurrentLevel);
		if (currentScore > bestScore) {
			w.setInfoText("Oh my god!!!You make the new best score:"
					+ currentScore + "s");
			((Balance_androidActivity) Resources.m_theApp).putValue(""
					+ m_CurrentLevel, currentScore);
		} else {
			w.setInfoText("Try again?You may catch the best Score!!!!");
		}
	}

	public void nextLevel() {
		if (m_theWidget != null) {
			m_theWidget.destroy();
			m_theWidget = null;
		}

		chooseLevel(++m_CurrentLevel);

		StartWidget w = new StartWidget(this);
		Resources.m_theUI.setWidget(w);

		m_theWidget = w;

		m_bReady = false;
		Settings.B_START = false;
	}

	static protected class GameLoopWidget extends Widget {
		protected GameLoopScreen m_theScreen = null;

		private float m_alpha = 255.0f;

		public GameLoopWidget(GameLoopScreen s) {
			super();

			m_theScreen = s;

			this.setTheme("/transparent_page");
		}

		@Override
		protected void paint(GUI gui) {
			Pixmap pmap = new Pixmap(32, 32, Format.RGBA8888);

			pmap.setColor(0, 0, 0, m_alpha);
			pmap.fillRectangle(0, 0, 32, 32);

			TextureRegion tr = new TextureRegion(new Texture(pmap));

			Render.m_batch.draw(tr, 0, 0, this.getWidth(), this.getHeight());

			pmap.dispose();

			super.paint(gui);

			if (m_alpha > 128) {
				m_alpha -= 5;
			}
		}
	}

	static protected class StartWidget extends GameLoopWidget {
		protected Button m_btnOk = null;
		protected Button m_btnBack = null;
		protected Label m_lblText = null;

		public StartWidget(GameLoopScreen s) {
			super(s);

			m_btnOk = new Button("   Ok   ");
			m_btnOk.setTheme("/button");
			m_btnOk.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.onOk();
				}
			});
			add(m_btnOk);

			m_btnBack = new Button("  Back  ");
			m_btnBack.setTheme("/button");
			m_btnBack.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.onBack();
				}
			});
			add(m_btnBack);

			m_lblText = new Label("Are you ready?");
			m_lblText.setTheme("/label");
			add(m_lblText);
		}

		public void setLabelText(String text) {
			m_lblText.setText(text);
		}

		public void setLeftText(String text) {
			m_btnOk.setText(text);
		}

		public void setRightText(String text) {
			m_btnBack.setText(text);
		}

		@Override
		protected void layout() {
			super.layout();

			int width = getWidth();
			int height = getHeight();

			m_btnOk.adjustSize();
			m_btnOk.setSize(128, 64);
			int ow = m_btnOk.getWidth();
			int oh = m_btnOk.getHeight();
			m_btnOk.setPosition((width - ow) / 2 - ow, (height - oh) / 2 + 100);

			m_btnBack.adjustSize();
			m_btnBack.setSize(128, 64);
			int bw = m_btnBack.getWidth();
			int bh = m_btnBack.getHeight();
			m_btnBack.setPosition((width - bw) / 2 + bw,
					(height - bh) / 2 + 100);

			m_lblText.adjustSize();
			int lw = m_lblText.getWidth();
			m_lblText.setPosition((width - lw) / 2, height / 4);
		}
	}

	static protected class GameOverWidget extends GameLoopWidget {
		protected Button m_btnNext = null;
		protected Button m_btnRetry = null;
		protected Button m_btnBack = null;
		protected Label m_lblText = null;

		public GameOverWidget(GameLoopScreen s) {
			super(s);

			m_btnNext = new Button(" Next Level ");
			m_btnNext.setTheme("/button");
			m_btnNext.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.nextLevel();
				}
			});
			add(m_btnNext);

			m_btnRetry = new Button("  Retry  ");
			m_btnRetry.setTheme("/button");
			m_btnRetry.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.retry();
				}
			});
			add(m_btnRetry);

			m_btnBack = new Button("  Back  ");
			m_btnBack.setTheme("/button");
			m_btnBack.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.onBack();
				}
			});
			add(m_btnBack);

			m_lblText = new Label("");
			m_lblText.setTheme("/label");
			add(m_lblText);
		}

		public void setInfoText(String str) {
			m_lblText.setText(str);
		}

		@Override
		protected void layout() {
			super.layout();

			int width = getWidth();
			int height = getHeight();

			m_btnRetry.adjustSize();
			m_btnRetry.setSize(128, 64);
			int ow = m_btnRetry.getWidth();
			int oh = m_btnRetry.getHeight();
			m_btnRetry
					.setPosition((width - ow) / 2 - ow * 2, (height - oh) / 2);

			m_btnNext.adjustSize();
			m_btnNext.setSize(128, 64);
			int nw = m_btnNext.getWidth();
			int nh = m_btnNext.getHeight();
			m_btnNext.setPosition((width - nw) / 2, (height - nh) / 2);

			m_btnBack.adjustSize();
			m_btnBack.setSize(128, 64);
			int bw = m_btnBack.getWidth();
			int bh = m_btnBack.getHeight();
			m_btnBack.setPosition((width - bw) / 2 + bw * 2, (height - bh) / 2);

			m_lblText.adjustSize();
			int lw = m_lblText.getWidth();
			m_lblText.setPosition((width - lw) / 2, height / 4);
		}
	}

	static protected class StopWidget extends GameLoopWidget {
		protected Button m_btnNext = null;
		protected Button m_btnResume = null;
		protected Button m_btnBack = null;

		protected Label m_lblText = null;

		public StopWidget(GameLoopScreen s) {
			super(s);

			m_btnNext = new Button(" Next Level ");
			m_btnNext.setTheme("/button");
			m_btnNext.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.nextLevel();
				}
			});
			add(m_btnNext);

			m_btnBack = new Button("   Back  ");
			m_btnBack.setTheme("/button");
			m_btnBack.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.onBack();
				}
			});
			add(m_btnBack);

			m_btnResume = new Button("Resume");
			m_btnResume.setTheme("/button");
			m_btnResume.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.onOk();
				}
			});
			add(m_btnResume);

			m_lblText = new Label("");
			m_lblText.setTheme("/label");
			add(m_lblText);
		}

		@Override
		protected void layout() {
			super.layout();

			int width = getWidth();
			int height = getHeight();

			m_btnResume.adjustSize();
			m_btnResume.setSize(128, 64);
			int ow = m_btnResume.getWidth();
			int oh = m_btnResume.getHeight();
			m_btnResume.setPosition((width - ow) / 2 - ow * 2,
					(height - oh) / 2);

			m_btnNext.adjustSize();
			m_btnNext.setSize(128, 64);
			int nw = m_btnNext.getWidth();
			int nh = m_btnNext.getHeight();
			m_btnNext.setPosition((width - nw) / 2, (height - nh) / 2);

			m_btnBack.adjustSize();
			m_btnBack.setSize(128, 64);
			int bw = m_btnBack.getWidth();
			int bh = m_btnBack.getHeight();
			m_btnBack.setPosition((width - bw) / 2 + bw * 2, (height - bh) / 2);

			m_lblText.adjustSize();
			int lw = m_lblText.getWidth();
			m_lblText.setPosition((width - lw) / 2, height / 4);
		}
	}
}
