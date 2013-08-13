package com.onesoft.balance_formal;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

public class LevelChooseScreen extends Screen {

	private LevelChooseWidget m_theWidget = null;

	public LevelChooseScreen(Game game) {
		super(game);

		m_theWidget = new LevelChooseWidget(this);

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

	protected void onBack() {
		game.setScreen(new MenuScreen(game));
	}

	protected void onChooseLevel(int which) {
		game.setScreen(new GameLoopScreen(which, game));
	}

	protected static class OnLevelChoose implements Runnable {
		private int m_whichLevel = 0;
		private LevelChooseScreen m_theScreen = null;

		public OnLevelChoose(int i, LevelChooseScreen ls) {
			m_whichLevel = i;
			m_theScreen = ls;
		}

		@Override
		public void run() {
			m_theScreen.onChooseLevel(m_whichLevel);
		}
	}

	protected static class LevelChooseWidget extends Widget {
		private Button m_back = null;

		private Button[] m_levels = null;

		private Label m_infoLabel = null;

		private float alpha = 1.0f;

		private LevelChooseScreen m_theScreen = null;

		public LevelChooseWidget(LevelChooseScreen ls) {
			super();

			m_theScreen = ls;

			setTheme("/menu_background");

			m_levels = new Button[Settings.LEVEL_COUNT];

			for (int i = 0; i < Settings.LEVEL_COUNT; ++i) {
				m_levels[i] = new Button(i + 1 + "");
				m_levels[i].setTheme("/button");
				m_levels[i].setSize(Settings.LEVEL_BUTTON_WIDTH,
						Settings.LEVEL_BUTTON_HEIGHT);
				add(m_levels[i]);

				m_levels[i].addCallback(new OnLevelChoose(i + 1, m_theScreen));
			}

			m_infoLabel = new Label("Choose Level");
			m_infoLabel.setTheme("/label");
			add(m_infoLabel);

			m_back = new Button("Back");
			m_back.setTheme("/button");
			m_back.setSize(Settings.COMMON_BUTTON_WIDTH,
					Settings.COMMON_BUTTON_HEIGHT);
			m_back.addCallback(new Runnable() {
				@Override
				public void run() {
					m_theScreen.onBack();

					Resources.playButtonClick();
				}
			});

			add(m_back);
		}

		@Override
		protected void layout() {
			super.layout();

			int width = this.getWidth();
			int height = this.getHeight();

			int x = (width - (Settings.LEVEL_BUTTON_WIDTH + Settings.GAP_SIZE)
					* (Settings.BUTTON_COUNT_PER_LINE)) / 2;
			int y = (height - (Settings.LEVEL_BUTTON_HEIGHT + Settings.GAP_SIZE)
					* (Settings.LEVEL_COUNT / Settings.BUTTON_COUNT_PER_LINE)) / 2;

			int startX = x;

			for (int i = 0; i < Settings.LEVEL_COUNT; ++i) {
				m_levels[i].adjustSize();

				m_levels[i].setSize(Settings.LEVEL_BUTTON_WIDTH,
						Settings.LEVEL_BUTTON_HEIGHT);

				m_levels[i].setPosition(x, y);

				x += Settings.LEVEL_BUTTON_WIDTH + Settings.GAP_SIZE;
				if (i + 1 == Settings.BUTTON_COUNT_PER_LINE) {
					x = startX;
					y += Settings.LEVEL_BUTTON_HEIGHT + Settings.GAP_SIZE;
				}
			}

			m_infoLabel.adjustSize();

			int lW = m_infoLabel.getWidth();
			int lH = m_infoLabel.getHeight();

			x = (width - lW) / 2;
			y = lH;

			m_infoLabel.setPosition(x, y);

			m_back.adjustSize();
			m_back.setSize(Settings.COMMON_BUTTON_WIDTH,
					Settings.COMMON_BUTTON_HEIGHT);
			m_back.setPosition(0, height - Settings.COMMON_BUTTON_HEIGHT);
		}

		@Override
		protected void paint(GUI gui) {
			super.paint(gui);

			if (alpha >= 0) {
				Pixmap pmap = new Pixmap(32, 32, Format.RGBA8888);

				pmap.setColor(0, 0, 0, alpha);
				pmap.fillRectangle(0, 0, 32, 32);

				TextureRegion tr = new TextureRegion(new Texture(pmap));

				Render.m_batch
						.draw(tr, 0, 0, this.getWidth(), this.getHeight());

				pmap.dispose();

				alpha -= Settings.ALPHA_STEP;
			}
		}
	}
}
