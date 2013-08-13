package com.onesoft.balance_formal;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;

public class Simulation implements ContactListener {
	protected World m_theWorld = null;
	protected Body m_theGround = null;

	protected float m_timeStep = 0.0f;
	protected int m_velocityIterations = 0;
	protected int m_positionIterations = 0;

	public Simulation() {
		Vector2 gravity = new Vector2(0.0f, -10.0f);
		m_theWorld = new World(gravity, true);
		m_theWorld.setAutoClearForces(true);
		m_theWorld.setContinuousPhysics(true);

		m_theWorld.setContactListener(this);

		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		bd.type = BodyType.StaticBody;

		PolygonShape ps = new PolygonShape();
		ps.setAsEdge(new Vector2(0, 0), new Vector2(Settings.SCREEN_WIDTH, 0));

		m_theGround = m_theWorld.createBody(bd);

		m_theGround.setUserData("G");

		m_theGround.createFixture(ps, 1.0f);

		m_timeStep = 1.0f / 60.0f;
		m_velocityIterations = 10;
		m_positionIterations = 10;
	}

	public void setGravity(float x, float y) {
		m_theWorld.setGravity(new Vector2(x, y).nor().mul(10.0f));
	}

	public Body addBody(BodyDef bd, FixtureDef fd) {
		Body b = m_theWorld.createBody(bd);
		b.createFixture(fd);

		return b;
	}

	public void removeBody(Body b) {
		m_theWorld.destroyBody(b);
	}

	public Body addBody(BodyDef bd, Shape s, float density) {
		Body b = m_theWorld.createBody(bd);
		b.createFixture(s, density);

		return b;
	}

	public void removeJoint(Joint j) {
		m_theWorld.destroyJoint(j);
	}

	public Joint addJoint(JointDef jd) {
		Joint j = m_theWorld.createJoint(jd);

		return j;
	}

	public void update() {
		m_theWorld.step(m_timeStep, m_velocityIterations, m_velocityIterations);
	}

	public void render() {
		Render.reset();

		Matrix4 projectionMatrix = Render.m_batch.getProjectionMatrix();
		Matrix4 transformMatrix = Render.m_batch.getTransformMatrix();

		// transformMatrix.scl(new Vector3(0.5f, 0.5f, 1.0f));

		GL10 gl = Gdx.gl10;

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMultMatrixf(projectionMatrix.val, 0);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glMultMatrixf(transformMatrix.val, 0);

		Iterator<Body> i = m_theWorld.getBodies();
		while (i.hasNext()) {
			Body b = i.next();

			ArrayList<Fixture> fixtureList = b.getFixtureList();

			for (Fixture f : fixtureList) {
				if (f.getType() == Type.Polygon) {
					PolygonShape shape = (PolygonShape) f.getShape();

					Mesh m = new Mesh(true, 8, 8, new VertexAttribute(
							VertexAttributes.Usage.Position, 3, "a_position"));

					int c = shape.getVertexCount();

					float coords[] = new float[c * 3];
					short indices[] = new short[c];

					for (short e = 0; e < c; ++e) {
						Vector2 v = new Vector2();
						shape.getVertex(e, v);

						indices[e] = e;

						coords[e * 3 + 0] = v.x;
						coords[e * 3 + 1] = v.y;
						coords[e * 3 + 2] = 0;
					}

					m.setIndices(indices);
					m.setVertices(coords);

					gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);

					Vector2 pos = b.getPosition();
					float angle = (float) Math.toDegrees(b.getAngle());

					gl.glPushMatrix();
					gl.glTranslatef(pos.x, pos.y, 0.0f);
					gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
					m.render(GL10.GL_LINE_LOOP);
					m.dispose();
					gl.glPopMatrix();

				}
				if (f.getType() == Type.Circle) {
					CircleShape shape = (CircleShape) f.getShape();

					Mesh m = new Mesh(true, 32, 32, new VertexAttribute(
							VertexAttributes.Usage.Position, 3, "a_position"));

					float vertexes[] = new float[32 * 3];
					short indices[] = new short[32];

					Vector2 cpos = shape.getPosition();
					float radius = shape.getRadius();

					float angle = (float) Math.PI * 2 / 32;
					for (short e = 0; e < 32; ++e) {
						vertexes[e * 3 + 0] = radius
								* (float) Math.sin(angle * e) + cpos.x;
						vertexes[e * 3 + 1] = radius
								* (float) Math.cos(angle * e) + cpos.y;
						vertexes[e * 3 + 2] = 0.0f;

						indices[e] = e;
					}
					m.setVertices(vertexes);
					m.setIndices(indices);

					Vector2 pos = b.getPosition();
					float angle1 = (float) Math.toDegrees(b.getAngle());

					gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);

					gl.glPushMatrix();
					gl.glTranslatef(pos.x, pos.y, 0.0f);
					gl.glRotatef(angle1, 0.0f, 0.0f, 1.0f);
					m.render(GL10.GL_LINE_LOOP);
					m.dispose();
					gl.glPopMatrix();
				}

			}
		}
	}

	public void dispose() {
		m_theWorld.dispose();
	}

	@Override
	public void beginContact(Contact arg0) {
		Fixture fa = arg0.getFixtureA();
		Fixture fb = arg0.getFixtureB();

		Body ba = fa.getBody();
		Body bb = fb.getBody();

		int time = 0;

		switch (Settings.LEVEL) {
		case Settings.LEVEL_METAL_BALL: {
			time = 100;
			break;
		}
		case Settings.LEVEL_PINGPANG_BALL: {
			time = 50;
			break;
		}
		case Settings.LEVEL_RUBBER_BALL: {
			time = 80;
			break;
		}
		}
		if (ba.getUserData().equals("BALL") && bb.getUserData().equals("BOARD")) {
			Resources.vibrate(time);
		} else if (bb.getUserData().equals("BALL")
				&& ba.getUserData().equals("BOARD")) {
			Resources.vibrate(time);
		}

		if (ba.getUserData().equals("G") || bb.getUserData().equals("G")) {
			// game over
			Settings.B_GAME_OVER = true;
		}
	}

	@Override
	public void endContact(Contact arg0) {
	}
}
