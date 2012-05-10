package com.tankz.systems.player;

import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.TrigLUT;
import com.tankz.components.Physics;
import com.tankz.components.Tower;
import com.tankz.components.Transform;
import com.tankz.components.TurnFactor;
import com.tankz.components.Velocity;

public class PlayerTankMovementSystem extends EntitySystem implements KeyListener {
	private static final float MAX_VELOCITY = 0.16f;
	private static final float THRUST = 0.00012f;

	private static final float TURN_THRUST = 0.0002f;
	private static final float MAX_TURN_VELOCITY = 0.05f;

	private static float RECOIL_SPEED = 0.2f;
	private static float RECOIL_RECOVER_SPEED = 0.02f;
	private static int RECOIL_TARGET_OFFSET = 20;

	private boolean forward;
	private boolean reverse;
	private boolean turnRight;
	private boolean turnLeft;

	private boolean moving;
	private boolean turning;

	private boolean recoil;
	private boolean shoot;

	private GameContainer container;
	private Input input;
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Velocity> velocityMapper;
	private ComponentMapper<TurnFactor> turnFactorMapper;
	private ComponentMapper<Tower> towerMapper;
	private ComponentMapper<Physics> collidableMapper;
	private Entity player;
	
	private float velocity;

	public PlayerTankMovementSystem(GameContainer container) {
		super();

		this.container = container;
		this.input = container.getInput();

		input.addKeyListener(this);
	}
	
	@Override
	public void initialize() {
		transformMapper = new ComponentMapper<Transform>(Transform.class, world);
		velocityMapper = new ComponentMapper<Velocity>(Velocity.class, world);
		turnFactorMapper = new ComponentMapper<TurnFactor>(TurnFactor.class, world);
		towerMapper = new ComponentMapper<Tower>(Tower.class, world);
		collidableMapper = new ComponentMapper<Physics>(Physics.class, world);
		
		ensurePlayerEntity();
	}
	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		ensurePlayerEntity();

		if (player != null) {
			updatePlayer(player);
		}
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}
	
	private void ensurePlayerEntity() {
		if (player == null || !player.isActive())
			player = world.getTagManager().getEntity("PLAYER");
	}

	protected void updatePlayer(Entity e) {
		Velocity v = velocityMapper.get(e);
		TurnFactor tf = turnFactorMapper.get(e);
		Physics c = collidableMapper.get(e);
		
		updateMoving(e);
		//updateRotating(e);

		//updateMoving(t, v, world.getDelta());
		
		updateTurning(c, v, tf, world.getDelta());
		

		//c.getBody().setPosition(t.getX(), t.getY());
		//wc.getBody().setRotation(t.getRotation());
	}
	

	private void updateMoving(Entity e) {
		Physics physics = collidableMapper.get(e);
		
		if(forward) {
			float ax = (TrigLUT.cosDeg(physics.getRotation()) * world.getDelta());
			float ay = (TrigLUT.sinDeg(physics.getRotation()) * world.getDelta());
			
			velocity += world.getDelta() * 0.005f;
			if(velocity > 1.25f) {
				velocity = 1.25f;
			}
			
			physics.getBody().adjustVelocity(new Vector2f(velocity*ax, velocity*ay));
		} else if(velocity > 0) {
			velocity = 0;
		}
		
		if(reverse) {
			float ax = (TrigLUT.cosDeg(physics.getRotation()) * world.getDelta());
			float ay = (TrigLUT.sinDeg(physics.getRotation()) * world.getDelta());
			
			velocity -= world.getDelta() * 0.0025f;
			if(velocity < -1) {
				velocity = -1;
			}
			
			physics.getBody().adjustVelocity(new Vector2f(velocity*ax, velocity*ay));
		} else if(velocity < 0) {
			velocity += world.getDelta() * 1f;
			if(velocity > 0) {
				velocity = 0;
			}
		}
	}

	private void updateMoving(Transform transform, Velocity v, int delta) {
		float velocity = v.getVelocity();

		if (forward) {
			if (!moving) {
				velocity = 0.025f;
			}
			velocity += delta * THRUST;
			if (velocity > MAX_VELOCITY)
				velocity = MAX_VELOCITY;
			moving = true;
		} else if (reverse) {
			velocity -= delta * THRUST / 4;
			if (velocity < -MAX_VELOCITY / 2)
				velocity = -MAX_VELOCITY / 2;
			moving = true;
		}

		if (!forward && moving && velocity > 0) {
			velocity -= delta * (THRUST / 2f);
			if (velocity < 0) {
				velocity = 0;
				moving = false;
			}
		}

		if (!reverse && moving && velocity < 0) {
			velocity += delta * (THRUST / 2f);
			if (velocity > 0) {
				velocity = 0;
				moving = false;
			}
		}

		v.setVelocity(velocity);
	}

	private void updateTurning(Physics physics, Velocity v, TurnFactor tf, int delta) {
		float turnFactor = tf.getFactor();

		if (turnRight) {
			turnFactor += delta * TURN_THRUST;
			if (turnFactor > MAX_TURN_VELOCITY) {
				turnFactor = MAX_TURN_VELOCITY;
			}
			turning = true;
		} else if (turnLeft) {
			turnFactor -= delta * TURN_THRUST;
			if (turnFactor < -MAX_TURN_VELOCITY) {
				turnFactor = -MAX_TURN_VELOCITY;
			}
			turning = true;
		}

		if (!turnRight && !turnLeft && turning) {
			if (turnFactor > 0) {
				turnFactor -= delta * TURN_THRUST;
				if (turnFactor <= 0) {
					turnFactor = 0;
					turning = false;
				}
			} else {
				turnFactor += delta * TURN_THRUST;
				if (turnFactor >= 0) {
					turnFactor = 0;
					turning = false;
				}
			}
		}

		if (turning) {
			ROVector2f velocity = physics.getBody().getVelocity();
			float lengthSquared = velocity.lengthSquared()/20000f;
			tf.setFactor(tf.getFactor() * (lengthSquared/MAX_VELOCITY));
			
			updateRotating(player);

			//transform.addRotation(delta * (turnFactor * (v.getVelocity() / MAX_VELOCITY)));
		}

		tf.setFactor(turnFactor);
	}

	
	private void updateRotating(Entity e) {
		Physics physics = collidableMapper.get(e);
		
		TurnFactor turnFactor = turnFactorMapper.get(e);

		if(turnRight) {
			physics.getBody().adjustRotation(turnFactor.getFactor() * (reverse?-1:1));
		}
		else if(turnLeft) {
			physics.getBody().adjustRotation(turnFactor.getFactor() * (reverse?-1:1));
		}
	}
	
	
	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_W) {
			forward = true;
		} else if (key == Input.KEY_S) {
			reverse = true;
		} else if (key == Input.KEY_A) {
			turnLeft = true;
		} else if (key == Input.KEY_D) {
			turnRight = true;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_W) {
			forward = false;
		} else if (key == Input.KEY_S) {
			reverse = false;
		} else if (key == Input.KEY_A) {
			turnLeft = false;
		} else if (key == Input.KEY_D) {
			turnRight = false;
		}
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public void inputStarted() {
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input input) {
	}

}
