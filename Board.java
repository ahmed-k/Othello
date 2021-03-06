import javax.swing.*; 
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class Board extends JFrame{

private OthelloBoard logical; 
private OthelloBoard.OthelloTile[] game;
private GraphicTile[] tiles;
private JLabel blackScore; 
private JLabel whiteScore;

public Board(OthelloBoard logicalBoard) {
this.game = logicalBoard.getPosition();
this.logical = logicalBoard; 
JPanel outer = new JPanel();
JPanel sidebar = new JPanel();
sidebar.setPreferredSize(new Dimension(80,500));  
blackScore = new JLabel("Black: 2");
whiteScore = new JLabel("White: 2"); 
sidebar.add(blackScore);
sidebar.add(whiteScore);
outer.setLayout(new BorderLayout(1,1)); 
JPanel board = new JPanel();
board.setBackground(Color.black);
board.setBorder(new LineBorder(Color.black, 1));
board.setLayout(new GridLayout(8,8,1,1)); 
board.setPreferredSize(new Dimension(600,600));
tiles = new GraphicTile[game.length]; 
for (int i=0; i<tiles.length;i++) {
tiles[i] = new GraphicTile(game[i]); 
board.add(tiles[i]);
}//for

outer.add(board, BorderLayout.WEST);
outer.add(sidebar, BorderLayout.EAST);



setTitle("Othello");
this.setSize(600,600);
setResizable(false);
add(outer); 
pack();
setLocationRelativeTo(null);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setVisible(true);




}//constructor

public void updateScore(String blacktext, String whitetext) {
blackScore.setText(blacktext);
whiteScore.setText(whitetext);
}

public void update(int i) {
		tiles[i].repaint(); 
					}//update()
		
		
}//Board

class GraphicTile extends JPanel {
OthelloBoard.OthelloTile logicalTile;
public static final int BLACK = 10;
public static final int WHITE = 11;
public static final int BLANK = 99;
public static final int POSSIBLE = 12; 

public GraphicTile(OthelloBoard.OthelloTile tile) {
logicalTile = tile;
JLabel label = new JLabel("" + logicalTile.getIndicator());
add(label);
this.setBackground(Color.green);
this.setBorder(new LineBorder(Color.black)); 
this.addMouseListener(logicalTile); 
}//constructor

protected void paintComponent(Graphics g) {
super.paintComponent(g);
int width = getSize().width; 
int height = getSize().height; 
switch(logicalTile.getColor()) {
case (BLACK): {g.setColor(Color.black); g.fillOval((int)(0.1*width),(int)(0.1*height),
											   (int)(0.8*height),(int)(0.8*height));
											 break;}
case (WHITE): {g.setColor(Color.white); g.fillOval((int)(0.1*width),(int)(0.1*height),(int)(0.8*height),(int)(0.8*height));
												break;}
case (POSSIBLE): {g.setColor(Color.yellow); g.fillOval((int)(0.2*width),(int)(0.2*height),(int)(0.4*height),(int)(0.4*height));break;}
default: super.paintComponent(g); break; 
} //switch 
}//paintComponent


}//GraphicTile
