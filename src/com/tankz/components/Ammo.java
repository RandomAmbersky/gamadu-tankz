package com.tankz.components;

import com.artemis.Component;

public class Ammo extends Component {
	private float ammo;
	private float maximumAmmo;
	
	public Ammo(float ammo) {
		this(ammo, ammo);
	}

	public Ammo(float ammo, float maximumAmmo) {
		this.ammo = ammo;
		this.maximumAmmo = maximumAmmo;
	}
	
	public float getAmmo() {
		return ammo;
	}

	public void setAmmo(float ammo) {
		this.ammo = ammo;
	}
	
	public float getMaximumAmmo() {
		return maximumAmmo;
	}
	
	public void setMaximumAmmo(float maximumAmmo) {
		this.maximumAmmo = maximumAmmo;
	}
	
	public float getAmmoStatus() {
		return ammo/maximumAmmo;
	}
	
	public boolean hasAmmo(float hasAmmo) {
		return ammo >= hasAmmo;
	}
	
	public void reduceBy(float ammo) {
		this.ammo -= ammo;
	}
	
	public void addAmmo(float ammo) {
		this.ammo += ammo;
		if(this.ammo > maximumAmmo)
			this.ammo = maximumAmmo;
	}

}
