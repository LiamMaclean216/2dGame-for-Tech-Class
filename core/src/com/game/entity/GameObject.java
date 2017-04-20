package com.game.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.game.Game;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject extends Entity {

	public static ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private boolean toDestroy = false;
	public GameObject(BodyType type, World world, float width, float height) {
		super(type, world, width, height);
		objects.add(this);
	}

	public GameObject(Sprite sprite, BodyType type, World world, float width, float height) {
		super(type, world, width, height);
		this.sprite = sprite;

		objects.add(this);

	}

	public void render(OrthographicCamera camera, SpriteBatch batch) {
		if (sprite != null) {
			sprite.setPosition(x, y);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			// if (sprite.getX() == body.getPosition().x - sprite.getWidth() / 2
			// && sprite.getY() == body.getPosition().y - sprite.getHeight() /
			// 2)
			sprite.draw(batch);
			if (body != null)
				sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		}
	}

	public void update(OrthographicCamera camera) {
		x = body.getPosition().x;
		y = body.getPosition().y;
	}

	public double[] getVertices() {
		if (sprite != null) {

			double[] verts = new double[] { sprite.getX(), sprite.getY(), sprite.getX() + sprite.getWidth(),
					sprite.getY(), sprite.getX() + sprite.getWidth(), sprite.getY() + sprite.getHeight(), sprite.getX(),
					sprite.getY() + sprite.getHeight() };
			double cos = Math.cos(body.getAngle());
			double sin = Math.sin(body.getAngle());

			double xOffset = sprite.getX() + sprite.getOriginX();
			double yOffset = sprite.getY() + sprite.getOriginY();
			for (int i = 0; i < verts.length; i += 2) {
				double newX = (double) ((cos * (verts[i] - xOffset) - sin * (verts[i + 1] - yOffset)) + xOffset);
				double newY = (double) ((sin * (verts[i] - xOffset) + cos * (verts[i + 1] - yOffset)) + yOffset);

				verts[i] = newX;
				verts[i + 1] = newY;
			}
			for (int i = 0; i < verts.length; i += 2) {
				verts[i] += Game.camera.position.x * Game.scale;
				verts[i + 1] += Game.camera.position.y * Game.scale;
			}
			return verts;

		}
		return new double[0];
	}
	public void destroy() {
		toDestroy = true;
	}

	public boolean isToDestroy() {
		return toDestroy;
	}
	
}
