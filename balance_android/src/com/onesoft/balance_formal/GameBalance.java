package com.onesoft.balance_formal;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class GameBalance extends Game {
	public GameBalance(AndroidApplication app) {
		super();

		Resources.m_theApp = app;
	}

	@Override
	public Screen getStartScreen() {
		return new MenuScreen(this);
	}
}
