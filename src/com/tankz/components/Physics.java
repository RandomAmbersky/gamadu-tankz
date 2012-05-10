package com.tankz.components;

import net.phys2d.raw.Body;

import com.artemis.Component;

public class Physics extends Component {

	private Body body;
	
	public Physics(Body body){
		this.body = body;
	}
	
	public float getX() {
		return body.getPosition().getX();
	}
	
	public float getY() {
		return body.getPosition().getY();
	}
	
	public float getRotation() {
		return body.getRotation();
	}
	
	public void setLocation(float x, float y) {
		body.setPosition(x, y);
	}
	
	public float getRotationAsRadians() {
		return (float) Math.toRadians(getRotation());
	}
	
	public void addRotation(float angle) {
		body.setRotation((body.getRotation()+angle)%360);
	}
	
	public void setForce(float xf, float yf) {
		body.setForce(xf, yf);
	}
	
	public Body getBody(){
		return body;
	}

}
