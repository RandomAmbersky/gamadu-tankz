package com.tankz.systems.rendering.spatials;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Utils;
import com.tankz.components.Physics;
import com.tankz.components.Tower;
import com.tankz.components.Transform;
import com.tankz.components.TurnFactor;
import com.tankz.components.Velocity;
import com.tankz.managers.Player;
import com.tankz.managers.PlayerManager;

public class MammothTank extends Spatial {
	private static Image shadow = null;
	private static Image rearTracks = null;
	private static Image trackTile;
	private static Image base = null;
	private static Image rightFront = null;
	private static Image leftFront = null;
	private static Image frontTrack = null;
	private static Image barrels;
	private static Image towerImage = null;

	static {
		try {
			shadow = new Image("mammoth/shadow.png");
			trackTile = new Image("mammoth/tracks.png");
			rearTracks = new Image("mammoth/rearTracks.png");
			base = new Image("mammoth/base.png");
			frontTrack = new Image("mammoth/frontTrack.png");
			rightFront = new Image("mammoth/rightFrontTrackCover.png");
			leftFront = new Image("mammoth/leftFrontTrackCover.png");
			barrels = new Image("mammoth/barrels.png");
			towerImage = new Image("mammoth/tower.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private Physics physics;
	private Velocity velocity;
	private TurnFactor turnFactor;

	private int trackTileWidth;

	private float previousX;
	private float previousY;
	private float trackOffset;
	private Tower tower;
	private Color color;

	public MammothTank(World world, Entity owner) {
		super(world, owner);
	}

	@Override
	public void initalize() {
		physics = owner.getComponent(Physics.class);
		velocity = owner.getComponent(Velocity.class);
		turnFactor = owner.getComponent(TurnFactor.class);
		tower = owner.getComponent(Tower.class);
		Player player = world.getManager(PlayerManager.class).getPlayer(owner);
		color = player.getColor();

		trackTileWidth = trackTile.getWidth();

		previousX = 0f;
		previousY = 0f;
	}

	@Override
	public void render(Graphics g) {
		if(velocity.getVelocity() > 0) {
			trackOffset -= Utils.euclideanDistance(physics.getX(), physics.getY(), previousX, previousY);
		} else {
			trackOffset += Utils.euclideanDistance(physics.getX(), physics.getY(), previousX, previousY);
		}

		previousX = physics.getX();
		previousY = physics.getY();

		float turnFactorAngle = turnFactor.getFactor()*255f;
		
		g.rotate(physics.getX(), physics.getY(), physics.getRotation());
		{
			// Draw shadow underneath the tank.
			shadow.drawCentered(physics.getX(), physics.getY());

			// Draw the background under the rear tracks.
			rearTracks.draw(physics.getX()-rearTracks.getCenterOfRotationX(), physics.getY()-rearTracks.getCenterOfRotationY(), color);

			// Draw rear track tiles.
			g.fillRect(physics.getX() - 74, physics.getY() - 45, 62, 11, trackTile, -trackOffset % trackTileWidth, 0);
			g.fillRect(physics.getX() - 74, physics.getY() - 45, 62, 11, trackTile, (-trackOffset % trackTileWidth) + trackTileWidth, 0);
			g.fillRect(physics.getX() - 74, physics.getY() + 34, 62, 11, trackTile, -trackOffset % trackTileWidth, 0);
			g.fillRect(physics.getX() - 74, physics.getY() + 34, 62, 11, trackTile, (-trackOffset % trackTileWidth) + trackTileWidth, 0);

			// Draw base.
			base.draw(physics.getX()-base.getCenterOfRotationX(), physics.getY()-base.getCenterOfRotationY(), color);
			
			// Draw left front tracks.
			g.rotate(physics.getX()+43, physics.getY()-40, turnFactorAngle);
			{
				frontTrack.draw(physics.getX()-frontTrack.getCenterOfRotationX()+41, physics.getY()-frontTrack.getCenterOfRotationY()-39, color);
				g.fillRect(physics.getX() + 22, physics.getY() - 44, 38, 11, trackTile, -trackOffset % trackTileWidth, 0);
				g.fillRect(physics.getX() + 22, physics.getY() - 44, 38, 11, trackTile, (-trackOffset % trackTileWidth) + trackTileWidth, 0);
				leftFront.draw(physics.getX()-leftFront.getCenterOfRotationX()+41, physics.getY()-leftFront.getCenterOfRotationY()-39, color);
			}
			g.rotate(physics.getX()+43, physics.getY()-40, -turnFactorAngle);

			// Draw right front tracks.
			g.rotate(physics.getX()+43, physics.getY()+40, turnFactorAngle);
			{
				frontTrack.draw(physics.getX()-frontTrack.getCenterOfRotationX()+41, physics.getY()-frontTrack.getCenterOfRotationY()+39, color);
				g.fillRect(physics.getX() + 22, physics.getY() + 34, 38, 11, trackTile, -trackOffset % trackTileWidth, 0);
				g.fillRect(physics.getX() + 22, physics.getY() + 34, 38, 11, trackTile, (-trackOffset % trackTileWidth) + trackTileWidth, 0);
				rightFront.draw(physics.getX()-rightFront.getCenterOfRotationX()+41, physics.getY()-rightFront.getCenterOfRotationY()+39, color);
			}
			g.rotate(physics.getX()+43, physics.getY()+40, -turnFactorAngle);
		
		
		}
		g.rotate(physics.getX(), physics.getY(), -physics.getRotation());

		
		// Draw turret.
		g.rotate(physics.getX(), physics.getY(), tower.getRotation());
		{
			barrels.draw(physics.getX()-tower.getRecoil()-barrels.getCenterOfRotationX(), physics.getY()-barrels.getCenterOfRotationY(), color);
			towerImage.draw(physics.getX()-towerImage.getCenterOfRotationX(), physics.getY()-towerImage.getCenterOfRotationY(), color);
		}
		g.rotate(physics.getX(), physics.getY(), -tower.getRotation());
	}

}
