package com.onesoft.balance_formal;

import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;

public class MenuScreen extends Screen {
	private MainMenuWidget m_theWidget = null;

	public MenuScreen(Game game) {
		super(game);

		m_theWidget = new MainMenuWidget(this);

		Resources.m_theUI.setWidget(m_theWidget);
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public void render(float deltaTime) {
		Resources.m_theUI.render();
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
		m_theWidget.destroy();
	}

	protected void onPlay() {
		game.setScreen(new LevelChooseScreen(game));
	}

	protected void onReset() {
		for(int i = 0;i<Settings.LEVEL_COUNT;++i)
		{
			((Balance_androidActivity)Resources.m_theApp).putValue(""+i, 0);
		}
	}

	protected static class MainMenuWidget extends Widget {
		private MenuScreen m_theScreen = null;

		private Button m_btnMoreGames = null;
		private Button m_btnPlay = null;
		private Button m_btnReset = null;

		private ToggleButton m_btnMetalBall = null;
		private ToggleButton m_btnRubberBall = null;
		private ToggleButton m_btnPingPangBall = null;

		private ToggleButton m_btnMusic = null;
		private ToggleButton m_btnSound = null;

		private float m_theAlpha = 1.0f;

		static final protected int PLAY_BUTTON_WIDTH = Settings.SCREEN_WIDTH / 2 - 16;
		static final protected int PLAY_BUTTON_HEIGHT = 100;
		static final protected int PLAY_BUTTON_X = ((Settings.SCREEN_WIDTH / 2) + PLAY_BUTTON_WIDTH) / 2;
		static final protected int PLAY_BUTTON_Y = 30;

		static final protected int BALL_SELECT_BUTTON_WIDTH = 128;
		static final protected int BALL_SELECT_BUTTON_HEIGHT = 64;

		static final protected int RESET_BUTTON_WIDTH = PLAY_BUTTON_WIDTH;
		static final protected int RESET_BUTTON_HEIGHT = 64;

		static final protected int MUSIC_BUTTON_WIDTH = 64;
		static final protected int MUSIC_BUTTON_HEIGHT = 64;

		static final protected int BUTTON_GAP = 20;

		public MainMenuWidget(MenuScreen s) {
			super();

			m_theScreen = s;

			m_btnMoreGames = new Button("More Game");
			m_btnMoreGames.setTheme("/button");
			add(m_btnMoreGames);

			m_btnPlay = new Button("Play");
			m_btnPlay.setTheme("/button");
			add(m_btnPlay);

			m_btnReset = new Button("Reset Scores");
			m_btnReset.setTheme("/button");
			add(m_btnReset);

			m_btnMetalBall = new ToggleButton();
			m_btnPingPangBall = new ToggleButton();
			m_btnRubberBall = new ToggleButton();

			m_btnMusic = new ToggleButton();
			m_btnSound = new ToggleButton();

			m_btnMetalBall.setTheme("/metal_ball");
			m_btnPingPangBall.setTheme("/pingpang_ball");
			m_btnRubberBall.setTheme("/rubber_ball");

			m_btnMusic.setTheme("/music_button");
			m_btnSound.setTheme("/audio_button");

			switch (Settings.LEVEL) {
			case Settings.LEVEL_METAL_BALL:
				m_btnMetalBall.setActive(true);
				m_btnPingPangBall.setActive(false);
				m_btnRubberBall.setActive(false);
				break;
			case Settings.LEVEL_PINGPANG_BALL:
				m_btnMetalBall.setActive(false);
				m_btnPingPangBall.setActive(true);
				m_btnRubberBall.setActive(false);
				break;
			case Settings.LEVEL_RUBBER_BALL:
				m_btnMetalBall.setActive(false);
				m_btnPingPangBall.setActive(false);
				m_btnRubberBall.setActive(true);
				break;
			}

			initButtonCallbacks();

			add(m_btnMetalBall);
			add(m_btnPingPangBall);
			add(m_btnRubberBall);

			add(m_btnMusic);
			add(m_btnSound);

			setTheme("/menu_background");
		}

		protected void initButtonCallbacks() {
			m_btnMoreGames.addCallback(new Runnable() {
				@Override
				public void run() {
					Uri uri = Uri
							.parse("https://market.android.com/search?q=onesoft");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					Resources.m_theApp.startActivity(intent);
				}
			});

			m_btnPlay.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.onPlay();
				}
			});

			m_btnReset.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.onReset();
				}
			});

			m_btnMetalBall.addCallback(new Runnable() {
				@Override
				public void run() {
					Settings.LEVEL = Settings.LEVEL_METAL_BALL;

					m_btnPingPangBall.setActive(false);
					m_btnRubberBall.setActive(false);
				}
			});

			m_btnRubberBall.addCallback(new Runnable() {
				@Override
				public void run() {
					Settings.LEVEL = Settings.LEVEL_RUBBER_BALL;

					m_btnPingPangBall.setActive(false);
					m_btnMetalBall.setActive(false);
				}
			});

			m_btnPingPangBall.addCallback(new Runnable() {
				@Override
				public void run() {
					Settings.LEVEL = Settings.LEVEL_PINGPANG_BALL;

					m_btnMetalBall.setActive(false);
					m_btnRubberBall.setActive(false);
				}
			});

			m_btnMusic.addCallback(new Runnable() {
				@Override
				public void run() {
					Settings.bSoundEnabled = m_btnMusic.isActive();
				}
			});

			m_btnSound.addCallback(new Runnable() {
				@Override
				public void run() {
					Settings.bVibrateEnable = m_btnSound.isActive();
				}
			});
		}

		@Override
		protected void layout() {
			super.layout();

			m_btnMoreGames.adjustSize();
			m_btnMoreGames.setSize(PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
			m_btnMoreGames.setPosition(8, Settings.SCREEN_HEIGHT
					- PLAY_BUTTON_HEIGHT);

			m_btnPlay.adjustSize();
			m_btnPlay.setSize(PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
			m_btnPlay.setPosition(PLAY_BUTTON_X, PLAY_BUTTON_Y);

			m_btnMetalBall.adjustSize();
			m_btnMetalBall.setSize(BALL_SELECT_BUTTON_WIDTH,
					BALL_SELECT_BUTTON_HEIGHT);
			m_btnMetalBall.setPosition(PLAY_BUTTON_X, PLAY_BUTTON_Y
					+ PLAY_BUTTON_HEIGHT + BUTTON_GAP);

			m_btnPingPangBall.adjustSize();
			m_btnPingPangBall.setSize(BALL_SELECT_BUTTON_WIDTH,
					BALL_SELECT_BUTTON_HEIGHT);
			m_btnPingPangBall.setPosition(PLAY_BUTTON_X
					+ BALL_SELECT_BUTTON_WIDTH, PLAY_BUTTON_Y
					+ PLAY_BUTTON_HEIGHT + BUTTON_GAP);

			m_btnRubberBall.adjustSize();
			m_btnRubberBall.setSize(BALL_SELECT_BUTTON_WIDTH,
					BALL_SELECT_BUTTON_HEIGHT);
			m_btnRubberBall.setPosition(PLAY_BUTTON_X
					+ BALL_SELECT_BUTTON_WIDTH * 2, PLAY_BUTTON_Y
					+ PLAY_BUTTON_HEIGHT + BUTTON_GAP);

			m_btnReset.adjustSize();
			m_btnReset.setSize(RESET_BUTTON_WIDTH, RESET_BUTTON_HEIGHT);
			m_btnReset.setPosition(PLAY_BUTTON_X, PLAY_BUTTON_Y
					+ PLAY_BUTTON_HEIGHT + BALL_SELECT_BUTTON_HEIGHT
					+ BUTTON_GAP * 2);

			m_btnMusic.adjustSize();
			m_btnMusic.setSize(MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_HEIGHT);
			m_btnMusic.setPosition(PLAY_BUTTON_X, Settings.SCREEN_HEIGHT
					- MUSIC_BUTTON_HEIGHT);

			m_btnSound.adjustSize();
			m_btnSound.setSize(MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_HEIGHT);
			m_btnSound.setPosition(PLAY_BUTTON_X + MUSIC_BUTTON_WIDTH
					+ BUTTON_GAP * 2, Settings.SCREEN_HEIGHT
					- MUSIC_BUTTON_HEIGHT);
		}

		@Override
		protected void paint(GUI gui) {
			super.paint(gui);

			if (m_theAlpha >= 0) {
				Pixmap pmap = new Pixmap(32, 32, Format.RGBA8888);

				pmap.setColor(0, 0, 0, m_theAlpha);
				pmap.fillRectangle(0, 0, 32, 32);

				TextureRegion tr = new TextureRegion(new Texture(pmap));

				Render.m_batch
						.draw(tr, 0, 0, this.getWidth(), this.getHeight());

				pmap.dispose();

				m_theAlpha -= Settings.ALPHA_STEP;
			}
		}
	}
}
