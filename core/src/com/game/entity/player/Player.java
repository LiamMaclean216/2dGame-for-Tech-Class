package com.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.entity.Entity;

public class Player extends Entity {
	public float speed = 0.1f;
	public float jump = 1f;
	float pw = 0.3f,ph = 1;;
	public Player(World world) {
		super(BodyType.DynamicBody, world, 16, 32);
		super.addBodyDef(0, -ph / 2, pw, 0.1f, 1, 0);
		super.addBodyDef(0, ph / 2, pw - (2), ph / 2 - (1),1000, 1, 0);
		sprite = new Sprite(new Texture("res/player.png"));
		body.setFixedRotation(true);
		
		//setX(100);
		//setY(100);
	}

	public void render(OrthographicCamera camera, SpriteBatch batch) {
		//sprite.setPosition((body.getPosition().x * Game.scale) - sprite.getWidth() / 2, (body.getPosition().y * Game.scale) - sprite.getHeight() / 2);
		sprite.setPosition(100,100);
		sprite.setSize(100, 100);
		//sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		//sprite.setSize(width * 2, height * 2);
		//sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	public void update(OrthographicCamera camera) {
		x = body.getPosition().x;
		y = body.getPosition().y;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			x += speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			x -= speed;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			body.applyLinearImpulse(new Vector2(0,body.getMass()*jump), body.getWorldCenter(), false);
			
		}
		//body.setLinearVelocity(xm, ym);
		
		
		body.setTransform(new Vector2(x, y),body.getTransform().getRotation());
	}
}
