package com.game.entity;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;

public class Bullet extends GameObject {
	private Light light;
	private int life = 100;
	private float x, y;
	private double angle;
	private final float bulletSpeed = 15f;

	public Bullet(World world, float x, float y, double angle) {
		super(BodyType.DynamicBody, world, 1, 1);
		
		this.x = x;
		this.y = y;
		this.angle = Math.toRadians(angle);
		super.addBodyDef(0, 0, 0.01f, 0, 0, 0.3f);
		body.setTransform(x, y, (float) angle);
		light = new Light();
		body.setLinearVelocity((float) Math.sin(angle) * bulletSpeed, (float) Math.cos(angle) * bulletSpeed);
		light.setColor(
				new Vector3(244, 155, 66));
		light.setAttenuation(
				new Vector3(0,50000,50000));

	}

	public void update(OrthographicCamera camera) {
		light.setPos((body.getPosition().x*Game.scale)/Game.width+0.5f,(body.getPosition().y*Game.scale)/Game.height+0.5f);
		life--;
		if (life <= 0)
			destroy();
	}

	public void destroy() {
		light.destroy();
		super.destroy();
	}
}
