/***********************************************************
 	*This power-up add 50 to strength
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

public class Axe extends Powerup{

	public final static Image STAR_IMAGE = new Image("images/Str.gif",Axe.POWERUP_WIDTH,Axe.POWERUP_WIDTH,false,false);
	public Axe(int xPos, int yPos) {
		super(xPos, yPos);
		this.loadImage(STAR_IMAGE);
		// TODO Auto-generated constructor stub
	}

	public void checkCollision(Player player){
		if(this.collidesWith(player)){
			System.out.println(player.getName() + " has collected an axe!");
			player.beastmode();
			player.collidedStr(); //add str to player
			this.setVisible(false);
		}
	}

}
