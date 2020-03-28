package pacmangame;

import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class PacmanMain extends JWindow{
    static int gameDifficulty=2;  
    
	public static void main(String[] args) {
                GameMenu gm = new GameMenu();
                gm.setVisible(true);
                while(gm.isVisible());//{}
                if(gm.getCond()==true){
                    gameDifficulty = gm.getgameDifficulty();
                }
                SplashScreen ss =new SplashScreen();
                ss.setVisible(true);
                while(ss.done!=true){
                    try{
                        Clip clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(new File("./src/sound/intro.wav")));
                        clip.start(); 
                    }catch (Exception exc){ exc.printStackTrace(System.out); }
                    try { Thread.sleep(4000); }catch (InterruptedException e) { e.printStackTrace(); }
                    ss.done=true;
                }
                ss.setVisible(false); 

		JFrame app = new JFrame("Pacman");
		app.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
                
		PacmanLogic gameLogic = new PacmanLogic(gameDifficulty);
		PacmanDesign gameDesign = new PacmanDesign(gameLogic);
            
		app.add(gameDesign);
		app.pack();
		app.setVisible(true);
		
		gameDesign.addKeyListener(new PacKeyListener(gameLogic));
		
		while(!gameLogic.gameOver()){
			gameLogic.gameRun();
			gameDesign.repaint();
			try { Thread.sleep(50); }catch (InterruptedException e) { e.printStackTrace(); }
			
		}
		System.exit(0);
	}

}

class PacKeyListener implements KeyListener {
	PacmanLogic pl;
	public PacKeyListener(PacmanLogic pl) {
		this.pl = pl;
	}

	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		if(code == KeyEvent.VK_LEFT){
			pl.nextpacDir = PacmanLogic.LEFT;
		}else if(code == KeyEvent.VK_RIGHT){
			pl.nextpacDir = PacmanLogic.RIGHT;
		}else if(code == KeyEvent.VK_UP){
			pl.nextpacDir = PacmanLogic.UP;
		}else if(code == KeyEvent.VK_DOWN){
			pl.nextpacDir = PacmanLogic.DOWN;
		}
	}

	public void keyReleased(KeyEvent ke) {}

	public void keyTyped(KeyEvent ke) {}
}
