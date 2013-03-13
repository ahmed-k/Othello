import javax.swing.*; 
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Othelloagent extends JFrame {
private static int blacks, whites;
private static boolean click=false;
private boolean isBlackTurn = true;
private java.util.List<Integer> pool = new ArrayList();
private OthelloTile[] position = new OthelloTile[64]; 
public static final int BLACK = 10;
public static final int WHITE = 11;
public static final int BLANK = 99;
public static final int POSSIBLE = 12; 

public Othelloagent() {
JPanel board = new JPanel();
board.setBackground(Color.black);
board.setBorder(new LineBorder(Color.black, 2));
board.setLayout(new GridLayout(8,8,1,1)); 
setTitle("Othello");
this.setSize(500,500);
setResizable(false);
add(board); 
setLocationRelativeTo(null);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setVisible(true);
OthelloTile[] compass2;
for (int i=0; i<64; i++) {
compass2 = new OthelloTile[8];
compass2[0] = (i%8==0 || i<8)? null: position[i-9];
compass2[1] = (i<8) ? null : position[i-8];
compass2[2] =  (i%8==7 || i<8)? null: position[i-7]; 
compass2[3] = (i%8==0)? null : position[i-1];
compass2[4] = (i%8==7)? null : position[i+1];
compass2[5] = (i>55 || i%8==0) ? null : position[i+7];
compass2[6] = (i>55) ? null : position[i+8];
compass2[7] = (i>55 || i%8==7) ? null : position[i+9];
board.add(position[i] = new OthelloTile(i,compass2)); 
if (i>0 && i%8!=0) position[i-1].setRight(position[i]);
if (i>8) { if ((i-9)%8==0) position[i-9].setBottomTiles(null, position[i-1], position[i]);
		   if ((i-9)%8==7) position[i-9].setBottomTiles(position[i-2], position[i-1], null);
		   else position[i-9].setBottomTiles(position[i-2], position[i-1], position[i]);
		  }

			
}//for i
board.repaint();
position[27].occupy(11);
position[28].occupy(10);
position[35].occupy(10);
position[36].occupy(11);    
}//constructor



public void survey() {
pool.clear(); 
for (int i=0; i<64; i++) 
{
position[i].clearTargets();
int candidate = position[i].getColor(); 
if (candidate == POSSIBLE) position[i].turnoff();
if (isBlackTurn && candidate==BLACK) pool.add(i);
else if (!isBlackTurn && candidate==WHITE) pool.add(i); 
}//for i
System.out.println("Pool is now: " + pool);
}//survey()

public void calculateMoves() {
while (!pool.isEmpty()) {
int c = pool.remove(0);
java.util.List<java.util.List<Integer>> moves = position[c].lookaround(isBlackTurn); 

ListIterator<java.util.List<Integer>> iterator = moves.listIterator();  
while (iterator.hasNext()) {
java.util.List<Integer> nexter = iterator.next(); 
ListIterator<Integer> subiterator = nexter.listIterator(nexter.size());
int goal = subiterator.previous();
position[goal].possible();  
position[goal].setTargets(nexter); 

}//while iterator

}//while pools

}//calculateMoves()

public static void main(String[] args) {
Othelloagent agent = new Othelloagent();
agent.pack();
agent.survey();
while (blacks+whites<64) {
						System.out.println("blacks: " + blacks + "  whites: " + whites);
						agent.calculateMoves(); 
						 if (click) {click=false; agent.survey();}
						 }
						 }//main 


class OthelloTile extends JPanel {
private int indicator;
private int color;
OthelloTile[] compass; 
java.util.Set<Integer> targets = new HashSet<Integer>(); 


public OthelloTile(int indicator, OthelloTile[] compass2) {
this.compass = compass2;
JLabel label = new JLabel("" + indicator);
this.indicator = indicator;
add(label);
this.setBackground(Color.green);
this.setBorder(new LineBorder(Color.black));
this.color = BLANK; 
this.addMouseListener(new MouseListener() {
 public void mouseClicked(MouseEvent e) {
   if (color!=POSSIBLE) System.out.println("Invalid move");

   else {
				if (isBlackTurn) occupy(BLACK); 
				else occupy(WHITE); 
				
				for (Integer victim: targets) 
				position[victim].invert();
				isBlackTurn = !isBlackTurn;
				click=true;
				
		}//else   
  
 
  }//mouseClicked
  public void mouseExited(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {
  System.out.println(targets);
  /*
  String[] words = {"UPPERLEFT", "UP", "UPPERIGHT", "RIGHT", "LEFT", "BOTTOMLEFT", "BOTTOM", "BOTTOMRIGHT"}; 
  for (int i=0; i<compass.length; i++) {
	System.out.println(words[i] + ": " + compass[i]);
  
  }
  System.out.println();
  */
  }
  public void mouseReleased(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}	  
}); //ActionListener
 
}//constructor 

public void setTargets(java.util.List<Integer> update) {
ListIterator<Integer> iterator = update.listIterator();
while (iterator.hasNext()) targets.add(iterator.next());
targets.remove(indicator);
}//updateTargets

public void invert() {
if (color==BLACK) {color=WHITE; blacks--;}
else if (color==WHITE) {color=BLACK; whites--;}
repaint();
}

public int getIndicator() { return indicator;} 
public void clearTargets() {targets.clear();}
public int getColor() { return color;}



public java.util.List<java.util.List<Integer>> lookaround(boolean turn) {
int goal;
java.util.List<Integer> vector;
java.util.List<java.util.List<Integer>> trajectory = new ArrayList<java.util.List<Integer>>();
				if (color == BLACK) goal=WHITE; 
				else goal=BLACK;
				for (int i=0; i<compass.length; i++) {
				if (compass[i]!= null && compass[i].getColor()==goal){
														vector = new ArrayList<Integer>(); 
														vector.add(compass[i].lookextend(vector, goal, i));
														System.out.println("Vector is " + vector);
														int tail = vector.get(vector.size()-1); 
														if (tail != 99) trajectory.add(vector); 
														}
														

														}//for i 

														return trajectory; 
																	}//lookaround

public Integer lookextend (java.util.List<Integer> traj, int goal, int i) {
traj.add(indicator); 
if (compass[i] == null) return new Integer(99); 
if (compass[i].getColor() == BLANK || compass[i].getColor() == POSSIBLE) return compass[i].getIndicator();
if (compass[i].getColor() == goal) 	return compass[i].lookextend(traj, goal,i);
return new Integer(99); 
} //lookextend 

public void setRight(OthelloTile tile) {
compass[4] = tile;
}

public void setBottomTiles(OthelloTile tile1, OthelloTile tile2, OthelloTile tile3) {
compass[5] = tile1;
compass[6] = tile2;
compass[7] = tile3;

}//setBottomTiles

public void turnoff() {
this.color = BLANK;
repaint();
}



public void possible() {
this.color = POSSIBLE; 
repaint();
} 

public String toString() {
String value;
switch(color) {
case (10): value = "black"; break;
case (11): value = "white"; break;
case (12): value = "choice"; break;
default: value = "blank";   break;
}//switch

return "Pool " + indicator + " " + value;  
}

public void occupy(int color) {
this.color = color; 
repaint(); 
}//occupy

protected void paintComponent(Graphics g) {
super.paintComponent(g);
int width = getSize().width; 
int height = getSize().height; 
switch(color) {
case (10): {g.setColor(Color.black); g.fillOval((int)(0.1*width),(int)(0.1*height),
											   (int)(0.8*height),(int)(0.8*height));
												blacks++; break;}
case (11): {g.setColor(Color.white); g.fillOval((int)(0.1*width),(int)(0.1*height),(int)(0.8*height),(int)(0.8*height));
												whites++; break;}
case (12): {g.setColor(Color.yellow); g.fillOval((int)(0.2*width),(int)(0.2*height),(int)(0.4*height),(int)(0.4*height));break;}
default: super.paintComponent(g); break; 
} //switch 
}//paintComponent

}//OthelloTile

} //OthelloAgent

///////////////////////////////////////////////////////////////////


 