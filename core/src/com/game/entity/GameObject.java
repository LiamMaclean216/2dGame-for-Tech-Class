package com.game.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject extends Entity {

	public GameObject(BodyType type, World world, float width, float height) {
		super(type, world, width, height);
	}

	public GameObject(Sprite sprite, BodyType type, World world, float width, float height) {
		super(type, world, width, height);
		this.sprite = sprite;
	}

	public void render(OrthographicCamera camera, SpriteBatch batch) {
		if (body != null && sprite != null) {
			sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
					body.getPosition().y - sprite.getHeight() / 2);
			sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
			sprite.setSize(width * 2, height * 2);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			if (sprite.getX() == body.getPosition().x - sprite.getWidth() / 2
					&& sprite.getY() == body.getPosition().y - sprite.getHeight() / 2)
				sprite.draw(batch);
		}
	}

	public void update(OrthographicCamera camera) {
		x = body.getPosition().x;
		y = body.getPosition().y;

	}

}
