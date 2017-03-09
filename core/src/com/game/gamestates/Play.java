package com.game.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.entity.GameObject;
import com.game.entity.Light;
import com.game.entity.player.Player;
import com.game.math.MyMath;
import com.game.states.State;

public class Play extends State {
	private SpriteBatch batch;
	private Texture texture, background;

	private World world;
	private Box2DDebugRenderer renderer;

	private ShaderProgram shader;
	
	private Player player;
	Mesh mesh;	
	Light light;
	ShapeRenderer shapeR;
	public static final int MAX_LIGHTS = 100;
	
	private ArrayList<Light> lights = new ArrayList<Light>();
	public Play() {
		shader = new ShaderProgram(Gdx.files.internal("vertex"), Gdx.files.internal("frag"));

		batch = new SpriteBatch();
		batch.setShader(shader);
		texture = new Texture(Gdx.files.internal("res/download.png"));
		background = new Texture(Gdx.files.internal("res/background.png"));

		world = new World(new Vector2(0, -9.8f), false);
		renderer = new Box2DDebugRenderer();
		shapeR = new ShapeRenderer();
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
		light = new Light();
		lights.add(light);
		//lights.add(new Vector2(light.getPos().x,light.getPos().y+0.2f));
	}

	float x = 0, y = 0;
	int att = 10000;
	public void render(OrthographicCamera camera) {
		float[] playerShape = new float[] {100,100,
				100,100+player.getSprite().getHeight(),
				100+player.getSprite().getWidth(),100+player.getSprite().getHeight(),
				100+player.getSprite().getWidth(),100};
		float[] shape = new float[] {100,100,100,200,200,200,200,100};
		
		light.setPos(new Vector2(Gdx.input.getX(),-Gdx.input.getY() + Game.height));
		
		batch.begin();
		//shader.setUniformf("numOfLights", 2);

		//shader.setUniformf("lightPos", new Vector2(x,y));
		///shader.setUniformf("lightColor", new Vector3(255,0,255));
		//shader.setUniformf("lightAttenuation", new Vector3(0,att,att));
		//shader.setUniformf("amountOfLights", lights.size());
		for(int i = 0;i < MAX_LIGHTS && i < lights.size();++i) {
			shader.setUniformf("lightPos[" + i + "]", lights.get(i).getPos());
			shader.setUniformf("lightColor[" + i + "]", lights.get(i).getColor());
			shader.setUniformf("lightAttenuation[" + i + "]", lights.get(i).getAttenuation());
		}
		//shader.setUniformf("dimensions", new Vector2(Game.width,Game.height));
		
		batch.draw(background, 0, 0, Game.width, Game.height);
		//player.render(camera, batch);
		batch.end();
		shapeR.begin(ShapeType.Line);
		shapeR.polygon(shape);
		shapeR.polygon(light.generateShadow(shape));
		float[] middle = MyMath.getMiddlePoint(shape);
		shapeR.circle(middle[0], middle[1], 2);

		shapeR.end();
		
		
		renderer.render(world, camera.combined);


		world.step(1 / 60f, 6, 2);

	}
	int timer = 10;
	public void update(OrthographicCamera camera) {
		player.update(camera);
		if(timer > 0) timer--;
		x= Gdx.input.getX()/(float)Game.width;
		y=(Gdx.input.getY()/(float)Game.height);
		lights.get(lights.size()-1).setPos(x,y);
		System.out.println(timer);
		if(Gdx.input.isButtonPressed(0) && timer == 0) {
			timer = 10;
			Light newLight = new Light();
			newLight.setPos(x,y);
			newLight.setColor(new Vector3((float)Math.random()*255,(float)Math.random()*255,(float)Math.random()*255));
			newLight.setAttenuation(new Vector3(0,att/2+(float)Math.random()*att,att/2+(float)Math.random()*att));
			lights.add(newLight);
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.E))
			att+=10;
		if (Gdx.input.isKeyPressed(Input.Keys.Q))
			att-=10;
	}

	public void onClose() {
		world.dispose();
		batch.dispose();
	}

	public int getID() {
		return 0;
	}

}
