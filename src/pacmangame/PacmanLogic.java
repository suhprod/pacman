package pacmangame;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;


public class PacmanLogic {	

       private String startBoard = 
		"********************\n" +
		"*..................*\n" +
		"*.#...########...#.*\n" +
		"*.#..............#.*\n" +
		"*.#..............#.*\n" +
		"*.#...########...#.*\n" +
		"*.....#......#.....*\n" +
		"*.....###..###.....*\n" +
		"*..................*\n" +
		"*..................*\n" +
		"*...#....###....#..*\n" +
		"*...#...........#..*\n" +
		"*...#...........#..*\n" +
		"*...#...........#..*\n" +
		"*...#...........#..*\n" +
		"*...#..##..##...#..*\n" +
		"*..................*\n" +
		"********************";
/*
	  private String startBoard = 
		"********************\n" +
		"*                  *\n" +
		"* #   ########   # *\n" +
		"* #              # *\n" +
		"* #              # *\n" +
		"* #   ########   # *\n" +
		"*     #      #     *\n" +
		"*     ###  ###     *\n" +
		"*                  *\n" +
		"*                  *\n" +
		"*   #    ###    #  *\n" +
		"*   #           #  *\n" +
		"*   #           #  *\n" +
		"*   #           #  *\n" +
		"*   #...........#  *\n" +
		"*   #  ##  ##   #  *\n" +
		"*                  *\n" +
		"********************";
*/
	private ArrayList<ArrayList<Character>> board = new ArrayList<ArrayList<Character>>();
	public int time=0;
	public float pacX = 10;
	public float pacY = 6;
	public final static int STILL=0, UP=1, RIGHT=2, DOWN=3, LEFT=4;
	public int pacDir = STILL;
	public int nextpacDir=pacDir;
	public ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
	private boolean gameOver=false;
	public int difficulty=0; // 0-easy, 1-normal, 2-hard
	public int totalScore=0;
	public int food=0;
	
	public PacmanLogic(int diff){
		difficulty=diff;
		for(String row : startBoard.split("\n")){
			ArrayList<Character> ch = new ArrayList<Character>();
			for(int i=0;i<row.length();i++){
				ch.add(row.charAt(i));
			}
			board.add(ch);
		}
		for(int i=0;i<startBoard.length();i++){
			if(startBoard.charAt(i)=='.'){
				food+=1;
			}
		}
		if(difficulty==0){
                    ghosts.add(new Ghost(1,13,15,Color.CYAN,0.12f));
                    ghosts.add(new Ghost(2,8,4,Color.PINK,0.12f));
                    ghosts.add(new Ghost(3,17,8,Color.RED,0.12f));
                }else if(difficulty==1){
                    ghosts.add(new Ghost(1,3,15,Color.CYAN,0.15f));
                    ghosts.add(new Ghost(2,8,4,Color.PINK,0.15f));
                    ghosts.add(new Ghost(3,17,8,Color.RED,0.15f));
                }else if(difficulty==2){
                    ghosts.add(new Ghost(1,3,15,Color.CYAN,0.16f));
                    ghosts.add(new Ghost(2,8,4,Color.PINK,0.16f));
                    ghosts.add(new Ghost(3,17,8,Color.RED,0.175f));
                    ghosts.add(new Ghost(4,18,16,Color.DARK_GRAY,0.17f));
                }
		
	}
	public int getFood(){
		return food;
	}
	public int getTotalScore(){
		return totalScore;
	}
	public int getHeight() {
		return board.size();
	}

	public int getWidth() {
		return board.get(0).size();
	}

	public char getCell(int x, int y) {
		return board.get(y).get(x);
	}
	public void setCell(int x, int y, char c){
		board.get(y).set(x,' ');
	}
	public void gameRun(){
		time+=1;
		
		if(pacDir==RIGHT && nextpacDir==LEFT){
			pacDir = LEFT;
		}else if(pacDir==LEFT && nextpacDir==RIGHT){
			pacDir = RIGHT;
		}else if(pacDir==UP && nextpacDir==DOWN){
			pacDir = DOWN;
		}else if(pacDir==DOWN && nextpacDir==UP){
			pacDir = UP;
		}else if(Math.abs(pacX - (int)pacX)< 0.5 && Math.abs(pacY - (int)pacY)< 0.5){
			pacDir = nextpacDir;
		}
		
		int x = Math.round(pacX);
		int y = Math.round(pacY);
		
		if(getCell(x,y)=='.'){
			setCell(x,y,' ');
			food--;
			totalScore+=5;
                        try{
                            Clip clip = AudioSystem.getClip();
                            clip.open(AudioSystem.getAudioInputStream(new File("./src/sound/eat.wav")));
                            clip.start();
                        }
                        catch (Exception exc){ exc.printStackTrace(System.out); }   
			if(food==0){
				gameOver=true;
                                try{
                                    Clip clip = AudioSystem.getClip();
                                    clip.open(AudioSystem.getAudioInputStream(new File("./src/sound/won.wav")));
                                    clip.start(); 
                                }catch (Exception exc){ exc.printStackTrace(System.out); }
				JOptionPane.showMessageDialog(null,"YOU WON!!\nTotal Score: "+getTotalScore());
			}
		}
		if(pacDir == RIGHT && (getCell(x+1,y) == '#' || getCell(x+1,y) =='*') && pacX>=x ||
				pacDir == LEFT && (getCell(x-1,y) == '#' || getCell(x-1,y) =='*') && pacX<=x || 
				pacDir == UP && (getCell(x,y-1) == '#' || getCell(x,y-1) =='*') && pacY<=y ||
				pacDir == DOWN && (getCell(x,y+1) == '#' || getCell(x,y+1) =='*') && pacY>=y){
			pacDir = STILL;
			pacX = x;
			pacY = y;
		}
		
		if(pacDir==UP){
			pacY-= .2;
		}else if(pacDir==DOWN){
			pacY+= .2;
		}else if(pacDir==RIGHT){
			pacX+= .2;
		}else if(pacDir==LEFT){
			pacX-= .2;
		}
		
		for(Ghost g: ghosts){
			if(Math.abs(g.x - (int)g.x)< .1 && Math.abs(g.y - (int)g.y)< .1){
                            if(g.color==Color.RED){
				g.chooseDir(this,1);
                            }else{
                                g.chooseDir(this,difficulty);
                            }
		}
			
			if(g.dir == RIGHT && getCell((int)(g.x+1),(int)g.y) == '*'){
                            g.dir = LEFT;
			}else if(g.dir == LEFT && getCell((int)(g.x-1),(int)g.y) == '*'){
                            g.dir = RIGHT;
			}else if(g.dir == UP && getCell((int)g.x,(int)(g.y-1)) == '*'){
                            g.dir = DOWN;
			}else if(g.dir == DOWN && getCell((int)g.x,(int)(g.y+1)) == '*'){
                            g.dir = UP;
			}
			
			if(g.dir==UP){
				//g.y-= .16;
                            g.y-= g.speed;
			}else if(g.dir==DOWN){
                            g.y+=g.speed;
			}else if(g.dir==RIGHT){
                            g.x+=g.speed;
			}else if(g.dir==LEFT){
                            g.x-=g.speed;
			}
		}
		
		for(Ghost g: ghosts){
			if((int)g.x==(int)pacX && (int)g.y==(int)pacY){
                            try{
                                Clip clip = AudioSystem.getClip();
                                clip.open(AudioSystem.getAudioInputStream(new File("./src/sound/death.wav")));
                                clip.start();
                            }
                            catch (Exception exc){ exc.printStackTrace(System.out); }
				gameOver=true;
				JOptionPane.showMessageDialog(null,"GAME OVER!\nTotal Score: "+getTotalScore());
				//System.out.println("GAME OVER!");
			}
		}
		
	}

	public boolean gameOver() {
		return gameOver;
	}
}
