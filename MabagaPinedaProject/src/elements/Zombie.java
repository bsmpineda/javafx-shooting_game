package elements;

import java.util.Random;
import javafx.scene.image.Image;
import shootingGame.GameStage;

public class Zombie extends Sprite{

	private boolean moveRight;
	private int speed;

	public final static int ZOMBIE_WIDTH = 50;
	public final static Image ZOMBIE_IMAGE = new Image("images/zombie.gif",Zombie.ZOMBIE_WIDTH,Zombie.ZOMBIE_WIDTH,false,false);
	public final static Image ZOMBIE_IMAGE_FLIP = new Image("images/zombie flip.gif",Zombie.ZOMBIE_WIDTH,Zombie.ZOMBIE_WIDTH,false,false);

	public Zombie(int xPos, int yPos) {

		super(xPos, yPos);
		this.loadImage(ZOMBIE_IMAGE);
		Random r = new Random();
		this.speed = r.nextInt(4)+1;

	}

	public void move(){
		this.setDX(this.speed);

		//this check if the object is at the corner of the window
		if(this.x >= (GameStage.WINDOW_WIDTH-Zombie.ZOMBIE_WIDTH)){
			this.moveRight = false;
			this.loadImage(ZOMBIE_IMAGE);
		}
		else if(this.x <= 0){
			this.moveRight = true;
			this.loadImage(ZOMBIE_IMAGE_FLIP);
		}

		//change the position of zombie
		if(this.moveRight){
			this.x += this.dx;
		}
		else{
			this.x -= this.dx;
		}
	}


	public void checkCollision(Player player){
		for	(int i = 0; i < player.getBullets().size(); i++)	{
			if (this.collidesWith(player.getBullets().get(i))){
				player.setScore();
				this.setVisible(false);
				player.getBullets().get(i).setVisible(false);
			}
		}
		if(this.collidesWith(player)){
			this.setVisible(false);

			if(!player.isImmortal()){
				player.collided();
				if(player.getStrength()<=0){
					player.die();
				}
			}

		}
	}

}