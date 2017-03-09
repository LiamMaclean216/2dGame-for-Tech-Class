package com.game.math;

public class MyMath {
	public static float[] getMiddlePoint(float[] vertices) {
		float x=0,y=0;
		for(int i =0;i < vertices.length;i+=2) {
			x+=vertices[i];
			y+= vertices[i+1];
		}
		x/=vertices.length/2;
		y/=vertices.length/2;
		return new float[] {x,y};
	}
	
}
