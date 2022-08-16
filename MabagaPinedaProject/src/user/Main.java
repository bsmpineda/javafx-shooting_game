/***********************************************************
 	*
 	* @author
 	* * * Apraem Cayle Mabaga
 	* * * Brixter Sien Pineda
 	*
 	* @created_date 2021-12-06 16:30
 	*
 ***********************************************************/

package user;

import javafx.application.Application;
import javafx.stage.Stage;
import shootingGame.GameStage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage){
		GameStage theGameStage = new GameStage();
		theGameStage.setStage(stage);
	}

}
