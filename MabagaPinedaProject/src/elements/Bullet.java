package elements;

import javafx.scene.image.Image;

public class Bullet extends Sprite{
	private final static Image BULLET_IMAGE = new Image("images/bullet.png", Bullet.BULLET_WIDTH, Bullet.BULLET_WIDTH, false, false);
	private final static int BULLET_WIDTH = 30;

	public Bullet(int xPos, int yPos) {
		super(xPos, yPos);
		this.loadImage(BULLET_IMAGE);
	}

	//change the position of object
	public void move(){
		this.x += 10;
	}

}
