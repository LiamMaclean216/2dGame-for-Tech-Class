package com.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.entity.GameObject;
import com.game.entity.player.Player;
import com.game.states.State;

public class Play extends State {
	private SpriteBatch batch;
	private Texture texture;

	private World world;
	private Box2DDebugRenderer renderer;

	private ShaderProgram shader;
	
	private Player player;
	Mesh mesh;	
	public Play() {
		shader = new ShaderProgram(Gdx.files.internal("vertex"), Gdx.files.internal("frag"));

		batch = new SpriteBatch();
		batch.setShader(shader);
		texture = new Texture(Gdx.files.internal("res/download.png"));

		world = new World(new Vector2(0, -9.8f), false);
		renderer = new Box2DDebugRenderer();
		player = new Player(world);
		
		GameObject block = new GameObject(BodyType.DynamicBody, world, Gdx.graphics.getWidth(), 1);
		block.addBodyDef(1, 1, 0.5f, 0.01f, 1, 0.3f);
		block.getBody().setAngularVelocity(-3);

		GameObject floor = new GameObject(BodyType.StaticBody, world, Gdx.graphics.getWidth(), 1);
		floor.addBodyDef(-2,-1, 30, 0.1f, 0.1f, 1, 0);
		
		
		
		mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
		mesh.setVertices(new float[] 
		{-0.5f, -0.5f, 0, 1, 1, 1, 1, 0, 1,
		0.5f, -0.5f, 0, 1, 1, 1, 1, 1, 1,
		0.5f, 0.5f, 0, 1, 1, 1, 1, 1, 0,
		-0.5f, 0.5f, 0, 1, 1, 1, 1, 0, 0});
		mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
	}

	float x = 0, y = 0;

	public void render(OrthographicCamera camera) {

		/*shader.setUniformf("lightpos", new Vector2(x, y));
		shader.setUniformf("lightColor", new Vector3(255,255,255));
		shader.setUniformf("screenHeight", Game.height);
		shader.setUniformf("lightAttenuation", new Vector3(255,255,255));
		//shader.setUniformf("radius",100);
		*/
		batch.begin();
		batch.draw(texture, 0, 0, Game.width, Game.height);
		batch.end();
		//player.render(camera, batch);
		
		renderer.render(world, camera.combined);


		world.step(1 / 60f, 6, 2);

	}

	public void update(OrthographicCamera camera) {
		player.update(camera);

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			x-=0.1f;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			x+=0.1f;
		
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			y-=0.1f;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			y+=0.1f;
	}

	public void onClose() {
		world.dispose();
		batch.dispose();
	}

	public int getID() {
		return 0;
	}

}
