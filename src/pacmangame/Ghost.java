package pacmangame;

import java.awt.Color;

public class Ghost {
        int id=0;
	float x,y;
        float speed= 0;
	int dir;
	Color color;
	PacmanLogic ww;
	public Ghost(int i,int x, int y, Color c,float s){
                id = i;
		this.x = x;
		this.y = y;
		color = c;
                speed = s;
	}
        
        /*
         *  Ghost = Min
         *  wall = 5
         *  pacman = 1 
         */
        
        /*
         *  Pacman = Max
         *  wall = 5
         *  ghost = 1
         *  food = 9
         */
        
        protected int[] getRightValues(int x, int y, int level){
            int[] arr = {0,0};
            arr[0] = x+level;
            arr[1] = y;
            return arr;
        }
        protected int[] getLeftValues(int x, int y, int level){
            int[] arr = {0,0};
            arr[0] = x-level;
            arr[1] = y;
            return arr;
        }
        protected int[] getUpValues(int x, int y, int level){
            int[] arr = {0,0};
            arr[0] = x;
            arr[1] = y-level;
            return arr;
        }
        protected int[] getDownValues(int x, int y, int level){
            int[] arr = {0,0};
            arr[0] = x;
            arr[1] = y+level;
            return arr;
        }

	public void chooseDir(PacmanLogic w,int difficulty){
            ww=w;
            //0-easy, 1-normal, 2-hard
            if(difficulty==0){
                this.chooseRandomDir(); 
            }else{
                PacmanTree pacTree = new PacmanTree();
                
                pacTree.insertData(pacTree.getRoot(),0,(int)x,(int)y, null);
                
                int level=2;
                int upvals[] = getUpValues((int)x,(int)y,level);
                pacTree.insertData(pacTree.getRoot(),0,upvals[0],upvals[1], null);
                int downvals[] = getDownValues((int)x,(int)y,level);
                pacTree.insertData(pacTree.getRoot(),0,downvals[0],downvals[1], null);
                int leftvals[] = getLeftValues((int)x,(int)y,level);
                pacTree.insertData(pacTree.getRoot(),0,leftvals[0],leftvals[1], null);
                int rightvals[] = getRightValues((int)x,(int)y,level);
                pacTree.insertData(pacTree.getRoot(),0,rightvals[0],rightvals[1], null);
                
                level+=2;

                for(int i=0;i<4;i++){
                    int vals[] = {0,0};
                    if(i==0){ vals = upvals;}
                    else if(i==1){ vals = downvals; }
                    else if(i==2){ vals = leftvals; }
                    else if(i==3){ vals = rightvals; }
                    
                    int upvals2[] = getUpValues(vals[0],vals[1],level);
                    pacTree.insertData(pacTree.getRoot(),0,upvals[0],upvals[1], null);
                    int downvals2[] = getDownValues(vals[0],vals[1],level);
                    pacTree.insertData(pacTree.getRoot(),0,downvals[0],downvals[1], null);
                    int leftvals2[] = getLeftValues(vals[0],vals[1],level);
                    pacTree.insertData(pacTree.getRoot(),0,leftvals[0],leftvals[1], null);
                    int rightvals2[] = getRightValues(vals[0],vals[1],level);
                    pacTree.insertData(pacTree.getRoot(),0,rightvals[0],rightvals[1], null);
                }
                this.startHeuristicFunction(pacTree,difficulty);
            }
	}
        
        public void startHeuristicFunction(PacmanTree pt, int difficulty){
            if(pt.getRoot()==null){
                return;
            }
            pt.traverseTreeForLeafNode(pt.getRoot(), ww, this);
            pt.applyAlphaBeta();
            Data data = pt.fData;
            //System.out.println("Ghost "+id+" Shortest Distance: "+data.dist);
            
            if(difficulty==1 && this.color==Color.PINK){
                chooseRandomDir();
            }
            else if(difficulty==1 && this.color==Color.RED || this.color==Color.CYAN){
                this.chooseDirByPacmanDir(ww,data.x,data.y);
            }
            else if(difficulty==2){
                this.chooseDirByPacmanDir(ww,data.x,data.y);
            }
        }
        
        public void chooseRandomDir(){
            int direction = (int)(Math.random()*4);
            if(direction==0){
		dir = PacmanLogic.UP;
            }else if(direction==1){
		dir = PacmanLogic.RIGHT;
            }else if(direction==2){
		dir = PacmanLogic.DOWN;
            }else if(direction==3){
		dir = PacmanLogic.LEFT;
            }
        }
        public void chooseDirByPacmanDir(PacmanLogic w, int xx, int yy){
        float dx = w.pacX - xx;
            float dy = w.pacY - yy;
            if(Math.abs(dx) > Math.abs(dy)){
                if(dx<0){
                    dir = PacmanLogic.LEFT;
		}else{
                    dir = PacmanLogic.RIGHT;
		}
            }else{
		if(dy<0){
                    dir = PacmanLogic.UP;
		}else{
                    dir = PacmanLogic.DOWN;
		}
            }
        }
        
}