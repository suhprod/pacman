
package pacmangame;

import java.util.LinkedList;
import java.util.List;

class Data{
    public int x=0;
    public int y=0;
    public int dist=0;
    public Data(){
        x=0; y=0; dist=0;
    }
    public Data(int d, int xx, int yy){
        x=xx; y=yy; dist=d;
    }
}

class Node{
    private int data=0;
    public int x=0;
    public int y=0;
    private int filledChildren=0;
    private Node parent=null;
    private Node childOne=null;
    private Node childTwo=null;
    private Node childThree=null;
    private Node childFour=null;
    // constructors....
    public Node(){
        data=0;
    }
    public Node(int data,int xx,int yy){
        this.data=data;
        this.x=xx;
        this.y=yy;
    }
    // setters....
    public void setParent(Node n){ this.parent=n; }
    public void setChildOne(Node n){ this.childOne=n; filledChildren++; }
    public void setChildTwo(Node n){ this.childTwo=n; filledChildren++; }
    public void setChildThree(Node n){ this.childThree=n; filledChildren++; }
    public void setChildFour(Node n){ this.childFour=n; filledChildren++; }
    public void setData(int d){ this.data=d; }
    // getters....
    public Node setParent(){ return this.parent; }
    public Node getChildOne(){ return this.childOne; }
    public Node getChildTwo(){ return this.childTwo; }
    public Node getChildThree(){ return this.childThree; }
    public Node getChildFour(){ return this.childFour; }
    public int getData(){ return this.data; }
    public int getTotalFilledChildren(){ return this.filledChildren; }
}

public class PacmanTree {
    private Node root=null;
    private Node current=null;
    private int size=0;
    // getters...
    public Node getRoot(){ return root; }
    public int getSize() { return size; }
    
    // tree operations...
    public boolean isLeaf(Node d) {
        if(d.getChildOne()==null && d.getChildTwo()==null && d.getChildThree()==null && d.getChildFour()==null)
            return true;
        return false;
    }
    public void insertInRoot(int data,int x,int y){
        if(root==null){
            root=new Node(data,x,y);
            current=root;
            size++;
        }
    }
    public boolean searchByData(Node n,int data){
        if(n!=null){
            if(n.getData()==data){
                //System.out.println(""+data+" Present!");
                return true;
            }
            searchByData(n.getChildOne(),data);
            searchByData(n.getChildTwo(),data);
            searchByData(n.getChildThree(),data);
            searchByData(n.getChildFour(),data);
        }
        return false;
    }
    
     public Node insertData(Node n,int data,int x,int y,Node p){
        if(root==null){
            root = new Node(data,x,y);
            size++;
            current = root;
            return null;
        }else if(n==null){
            n=new Node(data,x,y);
            size++;
            n.setParent(p);
            //current=n;
            return n;
        }else{
                current=n;
                if(current.getTotalFilledChildren()==0){
                    current.setChildOne(insertData(current.getChildOne(),data,x,y,current));
                }else if(current.getTotalFilledChildren()==1){
                    current.setChildTwo(insertData(current.getChildTwo(),data,x,y,current));
                }
                else if(current.getTotalFilledChildren()==2){
                    current.setChildThree(insertData(current.getChildThree(),data,x,y,current));
                }else if(current.getTotalFilledChildren()==3){
                    current.setChildFour(insertData(current.getChildFour(),data,x,y,current));
                }
                else{
                    if(current.getChildOne()!=null && current.getChildOne().getTotalFilledChildren()<4){
                        insertData(current.getChildOne(),data,x,y,current);
                    }else if(current.getChildTwo()!=null && current.getChildTwo().getTotalFilledChildren()<4){
                        insertData(current.getChildTwo(),data,x,y,current);
                    }else if(current.getChildThree()!=null && current.getChildThree().getTotalFilledChildren()<4){
                        insertData(current.getChildThree(),data,x,y,current);
                    }else if(current.getChildFour()!=null && current.getChildFour().getTotalFilledChildren()<4){
                        insertData(current.getChildFour(),data,x,y,current);
                    }else{
                        insertData(current.getChildOne(),data,x,y,current);
                    }
                }
                return null;
        }
    }

 
    
    public void traverseTree(Node n){
        if(root!=null && n!=null){
            System.out.println(""+n.getData());
            traverseTree(n.getChildOne());
            traverseTree(n.getChildTwo());
            traverseTree(n.getChildThree());
            traverseTree(n.getChildFour());
        }
        else{
            return;
        }
    }
    
    public double distance(int x1,int y1, int x2,int y2){ 
        int dx = x1 - x2;         //horizontal difference 
        int dy = y1 - y2;         //vertical difference 
        double dist = Math.sqrt( dx*dx + dy*dy ); //distance using Pythagoras theorem
        return dist;
    }
    
    public double runHeuristicFunction(Node n,PacmanLogic pl,Ghost g){
        double ans= 0.0;
        int x1 = (int)pl.pacX;
        int y1 = (int)pl.pacY;
        int x2 = (int)g.x;
        int y2 = (int)g.y;
        
        ans = this.distance(x1, y1, x2, y2);
        return ans;
    }
    
    public void traverseTreeForLeafNode(Node n,PacmanLogic pl, Ghost g){
        if(root!=null && n!=null){
            if(this.isLeaf(n)){
                double dist = this.runHeuristicFunction(n, pl, g);
                n.setData((int)dist);
            }
            traverseTreeForLeafNode(n.getChildOne(),pl,g);
            traverseTreeForLeafNode(n.getChildTwo(),pl,g);
            traverseTreeForLeafNode(n.getChildThree(),pl,g);
            traverseTreeForLeafNode(n.getChildFour(),pl,g);
        }
        else{
            return;
        }
    }
    
    public List<Node> generateChildren(Node n) {
        if(this.isLeaf(n) || n==null){
            return null;
        }
        List<Node> children = new LinkedList<Node>();
        if(n.getChildOne()!=null){ children.add(n.getChildOne()); }
        if(n.getChildTwo()!=null){ children.add(n.getChildTwo()); }
        if(n.getChildThree()!=null){ children.add(n.getChildThree()); }
        if(n.getChildFour()!=null){ children.add(n.getChildFour()); }
        return children;
    }
    
    public int applyAlphaBeta(){
        int data = this.alphaBeta(root, this.getHeight(root),-100000,100000);
        
        return data;
    }
    
    /*
        if (currentPlayer == ai) { // ai tries to maximize the score
            for (Node child : children) {
                alpha = Math.max(alpha, alphaBeta(child, depth - 1, alpha, beta));

                if (beta <= alpha) {
                    break; // cutoff
                }
            }
            return alpha;
        } else { // enemy tries to minimize the score
        }
    */
    public Data fData=new Data();
    public int alphaBeta(Node node, int depth, int alpha, int beta) {
        fData.x=node.x; fData.y=node.y; fData.dist=beta;
        if (depth <= 1) {
            fData.x=node.x; fData.y=node.y; fData.dist=node.getData();
            return node.getData();
        }
        
        List<Node> children = this.generateChildren(node); // generates children. also rates them and applies move to copy of field. 
        if(children!=null){
            for (Node child : children) {
                    beta = Math.min(beta, alphaBeta(child, (depth - 1), alpha, beta));
                    if (beta <= alpha) { break; } // cutoff
            }
        }
        fData.x=node.x; fData.y=node.y; fData.dist=beta;
        return beta;
    }
    
    /*
    public Data alphaBeta(Node node, int depth, int alpha, int beta) {
        Data d = new Data(node.getData(),node.x,node.y);
        if (depth <= 1) { return new Data(node.getData(),node.x,node.y); }
        List<Node> children = this.generateChildren(node);
        if(children!=null){
            for (Node child : children) {
                    Data td = alphaBeta(child, (depth - 1), alpha, beta);
                    d.x = td.x; d.y = td.y; d.dist = td.dist; 
                    int bbeta = d.dist;
                    beta = Math.min(beta,bbeta);
                    if (beta <= alpha) { break; } // cutoff
            }
        }
        return d;
    }
     */
    public int getHeight(Node n){
        if (n==null) return 0;
        else {
            int h = getHeight(n.getChildOne());
            return h+1;
        }
    } 
    
    public void printLevelOrder(Node n) {
      int h = getHeight(n);
      int i;
      for(i=1; i<=h; i++){
        printGivenLevel(n, i);
        System.out.println();
      }
    }     

    public void printGivenLevel(Node n, int level) {
      if(n == null)
        return;
      if(level == 1)
        System.out.println(""+n.getData());
      else if (level > 1) {
        printGivenLevel(n.getChildOne(), level-1);
        printGivenLevel(n.getChildTwo(), level-1);
        printGivenLevel(n.getChildThree(), level-1);
        printGivenLevel(n.getChildFour(), level-1);
      }
    }
    
    
    public void traverseTreeForLeafNodeData(Node n){
        
        if(root!=null && n!=null){
            if(this.isLeaf(n)){
                //System.out.println("here!");
                System.out.println("data: "+n.getData());
            }
            traverseTreeForLeafNodeData(n.getChildOne());
            traverseTreeForLeafNodeData(n.getChildTwo());
            traverseTreeForLeafNodeData(n.getChildThree());
            traverseTreeForLeafNodeData(n.getChildFour());
        }
        else{
            return;
        }
    }

    /*
    public static void main(String[] s){
        PacmanTree pt =new PacmanTree();
        //pt.insertInRoot(1);
        for(int i=1;i<29;i++){
            pt.insertData(pt.getRoot(), i,0,0, null);
        }
        
        //pt.traverseTree(pt.getRoot());
        pt.printLevelOrder(pt.getRoot());
        System.out.println();
        System.out.println();
        //pt.traverseTreeForLeafNodeData(pt.getRoot());
        
        //Node n = pt.getRoot().getChildOne();
        //int d = pt.getRoot().getChildOne().getData();
        //System.out.println("\nIs Leaf: : "+pt.isLeaf(n));
        //System.out.println("\nData: "+d);
        int h = pt.getHeight(pt.getRoot());
        System.out.println("\nAlpha-Beta Value: "+(pt.alphaBeta(pt.getRoot(),h,-100000,100000)));
        System.out.println("\fData Dist: "+pt.fData.dist);
        //System.out.println("\nTree Size: "+pt.getSize());
        System.out.println("\nTree Height: "+pt.getHeight(pt.getRoot()));

        System.out.println("\nDone!\n");
    } 
     */

}