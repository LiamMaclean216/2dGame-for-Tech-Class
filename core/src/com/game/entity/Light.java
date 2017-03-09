package com.game.entity;

import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.Game;
import com.game.math.MyMath;

public class Light {
	private Vector2 pos = new Vector2(0,0);
	private Vector3 attenuation = new Vector3(0,50000,50000), color = new Vector3(0,0,0);;

	public Light() {
		
	}
	
	public float[] generateShadow(float[] vertices) {
		float[] middle = MyMath.getMiddlePoint(vertices);
		float dif = (middle[1] - pos.y)/(middle[0] - pos.x);
		float dif1 = (middle[0] - pos.x)/(middle[1] - pos.y);
		//System.out.println("dif : " + dif);

		int farPos = 0, farNeg = 0;
		float farPosVal, farNegVal;
		farPosVal = Float.NEGATIVE_INFINITY;
		farNegVal = Float.POSITIVE_INFINITY;
		for(int i = 0;i < vertices.length;i+=2) {
			float xdif = (vertices[i] - pos.x),ydif = (vertices[i+1] - pos.y);
			float ndif = dif-(ydif/xdif);
			float ndif1 = dif1-(xdif/ydif);
			if(xdif-middle[0] < -49 && xdif-middle[0] > -149)  {
				ndif = ndif1;
			}
			if(ndif > farPosVal) {
				farPos = i;
				farPosVal = ndif;
			} else if (ndif < farNegVal) {
				farNeg = i;
				farNegVal = ndif;
			} 
			
		}
		
		float[] returnValue = new float[8];
		returnValue[0] = vertices[farPos];
		returnValue[1] = vertices[farPos+1];
		returnValue[2] = vertices[farNeg];
		returnValue[3] = vertices[farNeg+1];
		//shadow goes waaayyy off the screen because im lazy
		returnValue[4] =vertices[farNeg]+(vertices[farNeg]-pos.x)*Game.width;
		returnValue[5] =vertices[farNeg+1]+(vertices[farNeg+1]-pos.y)*Game.height;
		returnValue[6] =vertices[farPos]+(vertices[farPos]-pos.x)*Game.width;
		returnValue[7] =vertices[farPos+1]+(vertices[farPos+1]-pos.y)*Game.height;
		return returnValue;
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

}
