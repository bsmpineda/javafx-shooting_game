/***********************************************************
 	*This power-up set immortality to true
 	*
 	*
 	* @author
 	* * * Apraem Cayle Mabaga
 	* * * Brixter Sien Pineda
 	*
 	* @created_date 2021-12-15 00:42
 	*
 ***********************************************************/


package elements;

import javafx.scene.image.Image;

public class Shield extends Powerup{

	public final static Image SHIELD_IMAGE = new Image("images/shield.gif",Shield.POWERUP_WIDTH, Shield.POWERUP_WIDTH,false,false);

	public Shield(int xPos, int yPos) {
		super(xPos, yPos);
		this.loadImage(SHIELD_IMAGE);
	}

	public void checkCollision(Player player){
		if(this.collidesWith(player)){
			System.out.println(player.getName() + " has collected a shield!\nIs now immortal");
			player.setImmortal(true);
			this.setVisible(false);
		}
	}

}
