package com.onesoft.balance_formal;

public abstract class Screen {
	Game game;

	public Screen(Game game) {
		this.game = game;
	}

	public abstract void update(float deltaTime);

	public abstract void render(float deltaTime);

	public abstract void pause();

	public abstract void resize(int width, int height);

	public abstract void resume();

	public abstract void dispose();
}
