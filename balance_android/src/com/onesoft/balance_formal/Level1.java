package com.onesoft.balance_formal;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Level1 extends AbstractLevel {
	private Body m_theFulcrum = null;
	private Body m_theBall = null;
	private Body m_theBoard = null;

	public Level1(GameLoopScreen s) {
		super("data/game_background.png", s);

		buildScene();
	}

	@Override
	protected void buildScene() {
		// define the Fulcrum
		BodyDef bdFulcrum = new BodyDef();
		bdFulcrum.type = BodyType.StaticBody;
		bdFulcrum.position.set(Settings.SCENE_WIDTH / 2,
				Settings.SCENE_HEIGHT / 2);

		PolygonShape shape = new PolygonShape();

		Vector2[] points = new Vector2[3];
		for (int i = 0; i < 3; ++i) {
			points[i] = new Vector2();
		}
		points[0].set(0, 0);
		points[1].set(-0.15f, -0.30f);
		points[2].set(0.15f, -0.30f);
		shape.set(points);

		FixtureDef fdFulcrum = new FixtureDef();
		fdFulcrum.shape = shape;
		fdFulcrum.friction = 1.0f;
		fdFulcrum.density = 1.0f;
		m_theFulcrum = Resources.m_theSimulation.addBody(bdFulcrum, fdFulcrum);
		m_theFulcrum.setUserData("F");

		// define the board
		BodyDef bdBoard = new BodyDef();

		bdBoard.type = BodyType.DynamicBody;
		bdBoard.position.set(Settings.SCENE_WIDTH / 2,
				Settings.SCENE_HEIGHT / 2 + 0.15f);

		PolygonShape psBoard = new PolygonShape();

		psBoard.setAsBox(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT);

		FixtureDef fdBoard = new FixtureDef();
		fdBoard.shape = psBoard;
		fdBoard.density = 2.0f;
		fdBoard.friction = 1.0f;
		m_theBoard = Resources.m_theSimulation.addBody(bdBoard, fdBoard);
		m_theBoard.setUserData("BOARD");

		// define the ball
		CircleShape cs = new CircleShape();
		// cs.setPosition(new
		// Vector2(Settings.SCREEN_WIDTH/2+80,Settings.SCREEN_HEIGHT-20.0f));
		cs.setPosition(new Vector2(0, 0));
		cs.setRadius(Settings.BALL_RADIUS);

		FixtureDef fdBall = new FixtureDef();
		fdBall.shape = cs;

		if (Settings.LEVEL == Settings.LEVEL_METAL_BALL) {
			fdBall.density = Settings.DENSITY_METAL;
			fdBall.restitution = Settings.RESTITUTION_METAL;
		} else if (Settings.LEVEL == Settings.LEVEL_PINGPANG_BALL) {
			fdBall.density = Settings.DENSITY_PINGPANG;
			fdBall.restitution = Settings.RESTITUTION_PINGPANG;
		} else if (Settings.LEVEL == Settings.LEVEL_RUBBER_BALL) {
			fdBall.density = Settings.DENSITY_RUBBER;
			fdBall.restitution = Settings.RESTITUTION_RUBBER;
		}

		BodyDef bdBall = new BodyDef();
		bdBall.type = BodyType.DynamicBody;
		bdBall.position.set(Settings.SCENE_WIDTH / 2 + 0.8f,
				Settings.SCENE_HEIGHT - 0.2f);
		m_theBall = Resources.m_theSimulation.addBody(bdBall, fdBall);

		// m_theBall.setBullet(true);
		m_theBall.setUserData("BALL");
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (!m_bIsBombed) {
			float x = m_theBall.getPosition().x;
			float y = m_theBall.getPosition().y;

			if (m_fireArea.contains(x, y)) {
				m_bIsBombed = true;

				//Vector2 p = near(x, y, 2, 0, 2, 2);
				Vector2 p = m_theBoard.getPosition();
				
				m_theBoard.applyForce(new Vector2((x - p.x) * 1000,
						(y - p.y) * 1000), m_theBoard.getWorldPoint(p));
			}
		}
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		float angle = m_theBoard.getAngle();

		float x = m_theBoard.getPosition().x;
		float y = m_theBoard.getPosition().y;

		TextureRegion texBoard = new TextureRegion(Resources.m_texBoard);

		Render.draw(texBoard, x - 2, y - 0.2f, 2 * 2, 0.2f * 2, angle);

		TextureRegion texBall = null;
		float width = 0.4f;
		float height = 0.4f;

		angle = m_theBall.getAngle();
		x = m_theBall.getPosition().x;
		y = m_theBall.getPosition().y;

		if (m_bIsBombed) {
			texBall = m_animaBomb.getKeyFrame(m_bombFrameDuration, true);
			width = 0.6f;
			height = 0.6f;
		} else {
			if (Settings.LEVEL == Settings.LEVEL_METAL_BALL) {
				texBall = new TextureRegion(Resources.m_texMetal);
			} else if (Settings.LEVEL == Settings.LEVEL_PINGPANG_BALL) {
				texBall = new TextureRegion(Resources.m_texPingpang);
			} else if (Settings.LEVEL == Settings.LEVEL_RUBBER_BALL) {
				texBall = new TextureRegion(Resources.m_texRabber);
			}
		}

		Render.draw(texBall, x - 0.2f, y - 0.2f, width, height, angle);

		TextureRegion texF = new TextureRegion(Resources.m_texFulcrum);
		x = m_theFulcrum.getPosition().x;
		y = m_theFulcrum.getPosition().y;
		Render.draw(texF, x - 0.15f, y - 0.3f, 0.3f, 0.3f);
	}

	@Override
	public void dispose() {
		super.dispose();

		Resources.m_theSimulation.removeBody(m_theBall);
		Resources.m_theSimulation.removeBody(m_theBoard);
		Resources.m_theSimulation.removeBody(m_theFulcrum);
	}

	@Override
	public void restart() {
		super.restart();

		m_bIsBombed = false;
		
		Resources.m_theSimulation.removeBody(m_theBall);
		Resources.m_theSimulation.removeBody(m_theBoard);
		Resources.m_theSimulation.removeBody(m_theFulcrum);

		buildScene();
	}
}
