/***********************************************************
 	*
 	* @author
 	* * * Apraem Cayle Mabaga
 	* * * Brixter Sien Pineda
 	*
 	* @created_date 2021-12-06 16:30
 	*
 ***********************************************************/

package shootingGame;

import java.util.ArrayList;
import java.util.Random;

import elements.*;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameTimer extends AnimationTimer{

	private Image background = new Image( "images/roadBg.gif", GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT, false, false);
	private long startSpawn, startTimer, powerUpTimer, immortalTimer;
	private GraphicsContext gc;
	private Scene theScene;
	private Player myPlayer;
	private ArrayList<Zombie> zombies;
	private ArrayList<Powerup> powerups;
	private boolean shooting, isAtStart;

	private final static double SPAWN_DELAY = 5;
	private static int GAME_TIMER = 60;
	private static int POWERUP_APPEAR = 10;


	GameTimer(GraphicsContext gc, Scene theScene){
		this.gc = gc;
		this.theScene = theScene;
		this.myPlayer = new Player("Marco",150,250);
		//call method to handle mouse click event
		this.handleKeyPressEvent();
		this.zombies = new ArrayList<Zombie>();
		this.powerups = new ArrayList<Powerup>();
		this.isAtStart = true; //the game is at start, this is for determining the number of zombies to spawn
	}

	@Override
	public void handle(long currentNanoTime) {
		double spawnElapsedTime = (currentNanoTime - this.startSpawn) / 1000000000.0;
		double timerElapsedTime = (currentNanoTime - this.startTimer) / 1000000000.0; //for the time counter
		double powerUPElapsedTime = (currentNanoTime - this.powerUpTimer) / 1000000000.0;
		double immortalElapsedTime = (currentNanoTime - this.immortalTimer) / 1000000000.0;

		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);


     // background image clears canvas
        this.gc.drawImage( background, 0, 0 );

		//call the methods to move the ship
		this.myPlayer.move();

		if(this.myPlayer.isImmortal()){
			this.myPlayer.loadImage(Player.PLAYER_IMMORTAL_IMAGE);
		}else{
			if(shooting) this.myPlayer.loadImage(Player.PLAYER_IMAGEA);
			else this.myPlayer.loadImage(Player.PLAYER_IMAGE);
		}

		this.moveBullets();

		//render the ship
		this.myPlayer.render(this.gc);

		//spawn zombies
		if(spawnElapsedTime > GameTimer.SPAWN_DELAY) {

			this.spawnZombies();

	        this.startSpawn = System.nanoTime();
	    }

		if(GameTimer.GAME_TIMER == 30 && timerElapsedTime > 1){
//			this.spawnPowerup();
			this.spawnBoss();
		}
		this.moveZombies();



		// draw
        for (Zombie zombie : this.zombies )
        	zombie.render( this.gc );

        if(GameTimer.GAME_TIMER < 60 && GameTimer.GAME_TIMER%GameTimer.POWERUP_APPEAR == 0 && timerElapsedTime > 1){
			this.spawnPowerup();
		}
		this.removePowerUp(powerUPElapsedTime); //method that remove the powerup after 5 secs
		this.collectPowerup(immortalElapsedTime);

        for (Bullet b : this.myPlayer.getBullets())
        	b.render( this.gc );

        for (Powerup p : this.powerups)
        	p.render( this.gc );

        if(!(this.myPlayer.isAlive())){
        	this.stop();
        	this.drawGameOver();
        }

        if(GameTimer.GAME_TIMER == 0 && this.myPlayer.isAlive()){
        	this.stop();
        	this.drawWin();
        }

        this.drawTimer(timerElapsedTime);
        this.drawScore();
        this.drawStrength();
	}


	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
            	if(code == KeyCode.SPACE){
            		if(!shooting){

            			shooting = true;
            			shoot();
            			System.out.println(code +" key pressed.");

            		}
            	}
            	else{
            		 moveMyShip(code);
            	}

			}

		});

		theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		            	if(code == KeyCode.SPACE){
		            		shooting = false;
		            	}
		            	else{
		            		stopMyShip(code);
		            	}

		            }
		        });
    }

	//method that will move the ship depending on the key pressed
	private void moveMyShip(KeyCode ke) {
		int pix = 10;
		if(ke==KeyCode.UP) this.myPlayer.setDY(-pix);

		if(ke==KeyCode.LEFT) this.myPlayer.setDX(-pix);

		if(ke==KeyCode.DOWN) this.myPlayer.setDY(pix);

		if(ke==KeyCode.RIGHT) this.myPlayer.setDX(pix);

		System.out.println(ke+" key pressed.");
   	}

	//method that will stop the ship's movement; set the ship's DX and DY to 0
	private void stopMyShip(KeyCode ke){
		this.myPlayer.setDX(0);
		this.myPlayer.setDY(0);
	}

	//create Bullet
	private void shoot(){
		this.myPlayer.shootBullet();
		this.myPlayer.loadImage(Player.PLAYER_IMAGE);
	}

	//change bullet's xPos then render it
	public void moveBullets(){
		ArrayList<Bullet> bList = this.myPlayer.getBullets();
		for(int i=0; i<bList.size(); i++){
			Bullet b = bList.get(i);
			if(b.isVisible()){
				b.move();
			}
			else {
				bList.remove(i);
			}

		}
	}

	//change the position of zombie
	private void moveZombies(){
		for(int i = 0; i < this.zombies.size(); i++){
			Zombie m = this.zombies.get(i);
			if(m.isVisible()){
				m.move();
				m.checkCollision(this.myPlayer); //check if the zombie got hit by a bullet or by the player
			}
			else this.zombies.remove(i);
		}
	}

	//create zombies
	private void spawnZombies(){
		Random r = new Random();
		int yPos, zombieCount;

		//spawn 7 zombies at the start of the game
		if(this.isAtStart){
			zombieCount = 7;
		}
		else{
			zombieCount = 3;
		}

		for(int i=0; i<zombieCount; i++)
		{
			yPos = r.nextInt(GameStage.WINDOW_HEIGHT - Zombie.ZOMBIE_WIDTH); //for the random y position of zombie

			Zombie newZombie = new Zombie(GameStage.WINDOW_WIDTH, yPos);
			this.zombies.add(newZombie);
		}


		this.isAtStart = false;
	}

	//create a boss object, then add in zombies arrayList
	private void spawnBoss(){
		Random r = new Random();
		int yPos = r.nextInt(GameStage.WINDOW_HEIGHT - BossZombie.ZOMBIE_WIDTH);

		BossZombie newBossZombie = new BossZombie(GameStage.WINDOW_WIDTH-(BossZombie.ZOMBIE_WIDTH/2), yPos);
		this.zombies.add(newBossZombie);
	}

	private void collectPowerup(double timer){

		for(int i = 0; i < this.powerups.size(); i++){
			Powerup c = this.powerups.get(i);
			if(c.isVisible()){
				c.checkCollision(this.myPlayer);
				this.immortalTimer = System.nanoTime(); //get the current elapsed time of the game when the player collected the powerup
			}
			else powerups.remove(i);
		}

		if(timer >= 3){ //after 3 seconds, the immortality's effect will be removed

			System.out.println("Not immortal anymore!");
			this.myPlayer.setImmortal(false);
			this.immortalTimer = System.nanoTime();
		}

	}

	//create a powerup
	private void spawnPowerup(){
		Random r = new Random();
		int yPos = r.nextInt(GameStage.WINDOW_HEIGHT - Powerup.POWERUP_WIDTH);
		int xPos = r.nextInt(GameStage.WINDOW_WIDTH/2);
		int type = r.nextInt(2); //for the type of power-up we will create


		if(type == 0){
			Powerup newPowerUp = new Axe(xPos, yPos);

			this.powerups.add(newPowerUp);
		}
		else{
			Powerup newPowerUp = new Shield(xPos, yPos);
			this.powerups.add(newPowerUp);
		}

	}

	//this method remove the powerUp after 5 seconds
	private void removePowerUp(double timer){

		if(timer>=5){ //remove powerup after 5 secs
			for(int i=0; i<this.powerups.size(); i++){
				this.powerups.remove(i);
			}
			this.powerUpTimer = System.nanoTime();
		}


	}



	//draw the score text
	private void drawScore(){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.YELLOW);
		this.gc.fillText("Score:", 20, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(this.myPlayer.getScore()+"", 100, 30);
	}

	private void drawStrength(){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.YELLOW);
		this.gc.fillText("Strength:", 170, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
		this.gc.setFill(Color.WHITE);

		int s = this.myPlayer.getStrength();

		if(s <= 0){ // for the strength text to not show a negative value
			s = 0;
		}

		this.gc.fillText(s + "", 280, 30);
	}


	private void drawTimer(double timer){
		String min, sec;

		min = Integer.toString(GameTimer.GAME_TIMER/60);
		sec = Integer.toString(GameTimer.GAME_TIMER%60);

		if(timer > 1){ //decrement the timer every 1 second
			GameTimer.GAME_TIMER -= 1;
			this.startTimer  = System.nanoTime();
		}

		if(GameTimer.GAME_TIMER == 0){
			min = sec = "00";
		}

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.YELLOW);
		this.gc.fillText("Timer: ", 360, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(min + " : " + sec +"", 440, 30);

	}

	private void drawGameOver(){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		this.gc.setFill(Color.CYAN);
		this.gc.fillText("GAME OVER!", GameStage.WINDOW_WIDTH/3.75, GameStage.WINDOW_HEIGHT/2);
	}


	private void drawWin(){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		this.gc.setFill(Color.CYAN);
		this.gc.fillText("YOU WIN!", GameStage.WINDOW_WIDTH/3.75, GameStage.WINDOW_HEIGHT/2 );
	}

}
