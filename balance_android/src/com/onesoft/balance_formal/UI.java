package com.onesoft.balance_formal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.twl.TWL;
import de.matthiasmann.twl.Widget;

public class UI {
	public TWL m_theMainUI = null;

	public static Matrix4 m_projectMatrix = null;
	public static Matrix4 m_transformMatrix = null;

	public UI() {
		m_theMainUI = new TWL(Render.m_batch, "data/balance.xml",
				FileType.Internal);

		Gdx.input.setInputProcessor(m_theMainUI);
	}

	public void setWidget(Widget w) {
		m_theMainUI.clear();

		m_theMainUI.setWidget(w);
	}

	public void clear() {
		m_theMainUI.clear();
	}

	public void render() {
		if (m_projectMatrix != null && m_transformMatrix != null) {
			Render.m_batch.setProjectionMatrix(m_projectMatrix);
			Render.m_batch.setTransformMatrix(m_transformMatrix);
		}

		m_theMainUI.render();

		if (m_projectMatrix == null || m_transformMatrix == null) {
			m_projectMatrix = Render.m_batch.getProjectionMatrix().cpy();
			m_transformMatrix = Render.m_batch.getTransformMatrix().cpy();
		}
	}

	public void dispose() {
		m_theMainUI.dispose();
	}
}
