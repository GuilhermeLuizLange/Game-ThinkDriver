package br.com.thinkdriver;

import javax.swing.JFrame;

public class GameMain {
	public static void main(String[] args) throws InterruptedException {
		Game game = new Game("Think, Driver!", 1200, 680);
    	JFrame frame = new JFrame(game.title);   
        frame.add(game);		
        frame.setSize(game.width,game.heigth); 
        frame.setVisible(true); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.createBufferStrategy(4);
        frame.setLocationRelativeTo(null);       
        
        game.Player(game);
	}
}
