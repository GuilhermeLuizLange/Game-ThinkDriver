package br.com.thinkdriver;

public class Barrier {
	public int[] pos_x = new int[3];
	public int[] pos_y = new int[3];
	String imageLoc = "imagens/barreira.png"; 
	
	public Barrier() {
		pos_x[0] = 1200;
		pos_x[1] = 1200;
		pos_x[2] = 1200;
	}
	
	
	public void resetBarrier() {
		pos_x[0] = 1200;
		pos_x[1] = 1200;
		pos_x[2] = 1200;
	}
}
