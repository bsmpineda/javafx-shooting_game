package elements;

import java.util.Random;

import javafx.scene.image.Image;
import shootingGame.GameStage;

public class BossZombie extends Zombie{
	private boolean moveRight;
	private int speed;
	private int health;
	public final static int ZOMBIE_HEALTH = 3000;
	public final static int ZOMBIE_WIDTH = 200;
	public final static Image BOSS_ZOMBIE_IMAGE = new Image("images/Boss3.gif",BossZombie.ZOMBIE_WIDTH,BossZombie.ZOMBIE_WIDTH,false,false);
	public final static Image BOSS_ZOMBIE_IMAGE_FLIP = new Image("images/Boss3 flip.gif",BossZombie.ZOMBIE_WIDTH,BossZombie.ZOMBIE_WIDTH,false,false);

	public BossZombie(int xPos, int yPos) {
		super(xPos, yPos);
		this.health = ZOMBIE_HEALTH;
		this.loadImage(BOSS_ZOMBIE_IMAGE);
		Random r = new Random();
		this.speed = r.nextInt(4)+1;
	}

	@Override
	public void move(){
		this.setDX(this.speed);

		//this check if the object is at the corner of the window
		if(this.x >= (GameStage.WINDOW_WIDTH-BossZombie.ZOMBIE_WIDTH)){
			this.moveRight = false;
			this.loadImage(BOSS_ZOMBIE_IMAGE);
		}
		else if(this.x <= 0){
			this.moveRight = true;
			this.loadImage(BOSS_ZOMBIE_IMAGE_FLIP);
		}

		//change the position of zombie
		if(this.moveRight){
			this.x += this.dx;
		}
		else{
			this.x -= this.dx;
		}
	}

	@Override
	public void checkCollision(Player player){
		for	(int i = 0; i < player.getBullets().size(); i++)	{
			if (this.collidesWith(player.getBullets().get(i))){
				this.health -= player.getStrength();
				if(this.health <= 0){
				player.setScore();
				this.setVisible(false);
				}
				player.getBullets().get(i).setVisible(false);
			}
		}
		if(this.collidesWith(player)){
			this.setVisible(false);

			if(!player.isImmortal()){
				player.collided_Boss(); //decrease str by 50
				if(player.getStrength()<=0){
					player.die();
				}
			}

		}
	}
}
