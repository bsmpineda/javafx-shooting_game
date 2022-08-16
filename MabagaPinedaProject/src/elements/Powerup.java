/***********************************************************
 	*
 	* @author
 	* * * Apraem Cayle Mabaga
 	* * * Brixter Sien Pineda
 	*
 	* @created_date 2021-12-15 00:42
 	*
 ***********************************************************/

package elements;

public abstract class Powerup extends Sprite{
	public final static int POWERUP_WIDTH = 50;

	public Powerup(int xPos, int yPos) {
		super(xPos, yPos);
	}

	public abstract void checkCollision(Player player);
}
