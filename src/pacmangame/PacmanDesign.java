package pacmangame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PacmanDesign extends Canvas{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PacmanLogic world;
	private final int sizeControl=40;
	
	public PacmanDesign(PacmanLogic world) {
            this.setEnabled(true);
                this.requestFocus();
		this.world=world;
		setSize(world.getWidth()*sizeControl, world.getHeight()*sizeControl);
	}
	
	public void update(Graphics g){
		Image im = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
		paint(im.getGraphics());
		g.drawImage(im, 0, 0, null);
	}
	
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0,getWidth(), getHeight());
		g.setColor(Color.BLACK);
		
		for(int x=0;x<world.getWidth();x++){
			for(int y=0;y<world.getHeight();y++){
				char c = world.getCell(x,y);
				if(c=='.'){
					if((world.time /8)%4 !=0)
					g.drawOval(x*sizeControl+sizeControl/3,y*sizeControl+sizeControl/3,sizeControl/3,sizeControl/3);
				}
				else if(c=='#'){
					g.fillRect(x*sizeControl,y*sizeControl,sizeControl,sizeControl);
				}
				else if(c=='*'){
					g.fillRect(x*sizeControl,y*sizeControl,sizeControl,sizeControl);
				}
			}
		}
		
		g.setColor(Color.YELLOW);
		g.fillArc((int)world.pacX*sizeControl,(int)world.pacY*sizeControl,sizeControl, sizeControl,30,300);
		//g.setColor(Color.black);
		//g.fillOval((int)(world.pacX*T)+T/3,(int)(world.pacY*T)+T/4,T/10,T/10);
		
		for(Ghost gh: world.ghosts){
			g.setColor(gh.color);
			g.fillOval((int)(gh.x*sizeControl),(int)(gh.y*sizeControl), sizeControl, sizeControl);
			g.fillRect((int)(gh.x*sizeControl),(int)(gh.y*sizeControl)+ sizeControl/2, sizeControl ,sizeControl/2);
			g.setColor(Color.WHITE);
			g.fillOval((int)(gh.x*sizeControl + sizeControl/3-sizeControl/10),(int)(gh.y*sizeControl +sizeControl/2-sizeControl/10), sizeControl/5, sizeControl/5);
			g.fillOval((int)(gh.x*sizeControl + 2*(sizeControl/3)-sizeControl/10),(int)(gh.y*sizeControl +sizeControl/2-sizeControl/10), sizeControl/5, sizeControl/5);
		}
	}
}
