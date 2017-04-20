package com.game.entity;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.Game;
import com.game.math.MyMath;

public class Light {
	private Vector2 pos = new Vector2(0,0);
	private Vector3 attenuation = new Vector3(0,50000,50000), color = new Vector3(0,0,0);
	public static ArrayList<Light> lights = new ArrayList<Light>();
	//public Mesh mesh;
	public boolean toDestroy = false;
	public Light() {
		lights.add(this);
	}
	public Light(float x,float y) {
		lights.add(this);
		setPos(x,y);
	}
	
	public double[] generateShadow(double[] vertices,OrthographicCamera camera) {
		double[] middle = MyMath.getMiddlePoint(vertices);
		double dif = (middle[1] - pos.y)/(middle[0] - pos.x) ;
		double dif1 = (middle[0] - pos.x)/(middle[1] - pos.y) ;
		pos.x*=Game.width;
		pos.y*=Game.height;

		int farPos = 0, farNeg = 0;
		double farPosVal, farNegVal;
		farPosVal = Float.NEGATIVE_INFINITY;
		farNegVal = Float.POSITIVE_INFINITY;
		double[] leftRight = MyMath.getLeftRight(vertices);
		boolean onTop = false;
		if(pos.x > leftRight[0] && pos.x < leftRight[1]) onTop = true;
		for(int i = 0;i < vertices.length;i+=2) {
			double xdif = (vertices[i] - pos.x),ydif = (vertices[i+1] - pos.y);
			double ndif = dif-(ydif/xdif);
			if(onTop) {
				ndif = dif1-(xdif/ydif);
			}
			if(ndif > farPosVal) {
				farPos = i;
				farPosVal = ndif;
			} else if (ndif < farNegVal) {
				farNeg = i;
				farNegVal = ndif;
			} 
		}
		//shadow goes waaayyy off the screen because im lazy
		
		double[] returnValue = new double[8];
		returnValue[0] = vertices[farPos]-camera.position.x*Game.scale;
		returnValue[1] = vertices[farPos+1]-camera.position.y*Game.scale;
		returnValue[2] = vertices[farNeg]-camera.position.x*Game.scale;
		returnValue[3] = vertices[farNeg+1]-camera.position.y*Game.scale;
		
		returnValue[4] = vertices[farNeg]+(vertices[farNeg]-pos.x)*Game.width-camera.position.x*Game.scale;
		returnValue[5] = vertices[farNeg+1]+(vertices[farNeg+1]-pos.y)*Game.width-camera.position.y*Game.scale;		
		returnValue[6] = vertices[farPos]+(vertices[farPos]-pos.x)*Game.height-camera.position.x*Game.scale;
		returnValue[7] = vertices[farPos+1]+(vertices[farPos+1]-pos.y)*Game.height-camera.position.y*Game.scale;
		pos.x/=Game.width;
		pos.y/=Game.height;
		return returnValue;
	}
	public Mesh generateShadowGLSL(double[] vertices,OrthographicCamera camera) {
		double[] shape = generateShadow(vertices,camera);
		float[] returnValue = new float[shape.length + (3*shape.length)-2];
        int i = 0;
        Mesh mesh = new Mesh(true, 6, 0,
				new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
	
        //Top Left Vertex Triangle 1
        returnValue[i++] = (float) shape[0];   //X
        returnValue[i++] = (float) shape[1]; //Y
        returnValue[i++] = 0;    //Z
        returnValue[i++] = 0f;   //U
        returnValue[i++] = 0f;   //V

        //Top Right Vertex Triangle 1
        returnValue[i++] = (float) shape[4];
        returnValue[i++] = (float) shape[5];
        returnValue[i++] = 0;
        returnValue[i++] = 1f;
        returnValue[i++] = 0f;

        //Bottom Left Vertex Triangle 1
        returnValue[i++] = (float) shape[2];
        returnValue[i++] = (float) shape[3];
        returnValue[i++] = 0;
        returnValue[i++] = 0f;
        returnValue[i++] = 1f;

        //Top Right Vertex Triangle 2
        returnValue[i++] = (float) shape[4];
        returnValue[i++] = (float) shape[5];
        returnValue[i++] = 0;
        returnValue[i++] = 1f;
        returnValue[i++] = 0f;

        //Bottom Right Vertex Triangle 2
        returnValue[i++] = (float) shape[6];
        returnValue[i++] = (float) shape[7];
        returnValue[i++] = 0;
        returnValue[i++] = 1f;
        returnValue[i++] = 1f;

        //Bottom Left Vertex Triangle 2
        returnValue[i++] = (float) shape[0];
        returnValue[i++] = (float) shape[1];
        returnValue[i++] = 0;
        returnValue[i++] = 0f;
        returnValue[i] = 1f;
        mesh.setVertices(returnValue);
        return mesh;
	}
	
	public void destroy() {
		lights.remove(this);

		toDestroy = true;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	
	public void setPos(float x, float y) {
		pos.set(x, y);
	}
	
	public Vector3 getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Vector3 attenuation) {
		this.attenuation = attenuation;
	}

	public Vector3 getColor() {
		return color;
	}

	public void setColor(Vector3 color) {
		this.color = color;
	}
	public boolean isToDestroy() {
		return toDestroy;
	}

}
