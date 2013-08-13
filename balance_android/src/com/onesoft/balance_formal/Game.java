package com.onesoft.balance_formal;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public abstract class Game implements ApplicationListener {
	private Screen screen;
	private boolean bRender = true;

	public void setScreen(Screen s) {
		screen.pause();
		screen.dispose();
		screen = s;
	}

	public abstract Screen getStartScreen();

	public Screen getCurrentScreen() {
		return screen;
	}

	@Override
	public void create() {
		Settings.load();
		Render.InitRender();
		Resources.load();

		screen = getStartScreen();
	}

	@Override
	public void resume() {
		screen.resume();

		bRender = true;
	}

	@Override
	public void render() {
		if (bRender) {
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

			screen.update(Gdx.graphics.getDeltaTime());
			screen.render(Gdx.graphics.getDeltaTime());
		}
	}

	@Override
	public void resize(int width, int height) {
		screen.resize(width, height);
	}

	@Override
	public void pause() {
		screen.pause();

		bRender = false;
	}

	@Override
	public void dispose() {
		screen.dispose();
		Settings.dispose();
		Resources.dispose();
		Render.dispose();
	}
}
