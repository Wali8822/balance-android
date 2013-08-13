package com.onesoft.balance_formal;

import com.badlogic.gdx.math.Rectangle;

public class RectangleEx extends Rectangle {

	private static final long serialVersionUID = 1L;

	public RectangleEx(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	/**
	 * Test if the point(x,y) in the rectangle
	 * 
	 * @param x
	 *            :the x to test if in the rectangle
	 * @param y
	 *            :the y to test if in the rectangle
	 * @return true if the point is in the rectangle
	 */
	public boolean contains(float x, float y) {
		return x >= getX() && y >= getY() && x <= getX() + getWidth()
				&& y <= getY() + getHeight();
	}
}
