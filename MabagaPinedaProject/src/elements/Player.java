package elements;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import shootingGame.GameStage;

public class Player extends Sprite{
	private String name;
	private int strength;
	private boolean alive;
	private int score;
	private boolean isBeastmode, isImmortal;
	private ArrayList<Bullet> bullets;

	public final static Image PLAYER_IMAGE = new Image("images/marco.gif",Player.PLAYER_WIDTH,Player.PLAYER_WIDTH,false,false);
	public final static Image PLAYER_IMAGEA = new Image("images/marcoA.gif",Player.PLAYER_WIDTH,Player.PLAYER_WIDTH,false,false);
	public final static Image PLAYER_IMMORTAL_IMAGE = new Image("images/Powerup.gif",Player.PLAYER_WIDTH,Player.PLAYER_WIDTH,false,false);
	public final static int PLAYER_WIDTH = 60;

	public Player(String name, int x, int y){
		super(x,y);
		this.name = name;
		Random r = new Random();
		this.strength = r.nextInt(51)+100;
		this.alive = true;
		this.score = 0;
		bullets = new ArrayList<Bullet>();
		this.isBeastmode = false;
		this.isImmortal = false;

		this.loadImage(Player.PLAYER_IMAGE);
	}
	//getters
	public void collided_Boss(){
		this.strength = this.strength-50;
	}
	public void collided(){
		this.strength = this.strength-30;
	}
	public void collidedStr(){
		this.strength = this.strength+50;
	}
	public void setScore(){
		this.score = this.score+1;
	}
	public int getScore(){
		return this.score;
	}
	public int getStrength(){
		return this.strength;
	}
	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	}
	public boolean isBeastmode(){
		if(this.isBeastmode) return true;
		return false;
	}
	public void beastmode(){
    	this.isBeastmode = true;
    }

	public boolean isImmortal(){
		if(this.isImmortal) return true;
		return false;
	}
	public void setImmortal(Boolean i){
    	this.isImmortal = i;
    }

	public String getName(){
		return this.name;
	}

	public void die(){
    	this.alive = false;
    }

	public void move() {

		//if player is at the edge/sides of the window
		if(this.x +this.dx >= 0 && this.x +this.dx <= GameStage.WINDOW_WIDTH - this.width && this.y +this.dy >= 0 && this.y +this.dy <= GameStage.WINDOW_HEIGHT - this.height ){
			this.x += this.dx;
			this.y += this.dy;

		}
	}

	//create bullet, add to the arraylist
	public void shootBullet(){
		Bullet newBullet = new Bullet(this.x + (Player.PLAYER_WIDTH/2), this.y + (Player.PLAYER_WIDTH/3));
		this.bullets.add(newBullet);

	}

	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}

}


