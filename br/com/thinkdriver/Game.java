package br.com.thinkdriver;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel{
	public static Random random = new Random();
	public static Car car = new Car();
	public static Car[] opponents = new Car[3];
	static Response response = new Response();
	static Barrier barrier = new Barrier();
	int[] options = new int[3];
	static GameConf gameConf = new GameConf();
	public String title;
	public int width, heigth;
	int lastDiff = 9999, cont_hit = 0;
	static int lastId;
    boolean isRigth, isLeft, isDown, isUp;  
    
    public Game(String title, int width, int heigth){
    	this.title  = title;
    	this.width  = width;
    	this.heigth = heigth;
    	gameConf.frame_x = gameConf.frame_y = -1200;   
        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) { 
                stopCar(e); 
            }
            public void keyPressed(KeyEvent e) { 
                moveCar(e); 
            }
        });
        addMouseListener(new MouseAdapter() {
    	    public void mouseClicked(MouseEvent e) {
    	        setDifficult(e);
    	    }
    	    
        });
        setFocusable(true); 
        
        car.imagemLoc = "imagens/car_self_2.png";
        car.imagemLocOpaco = "imagens/car_self_opaco.png";
        car.pos_x = 405;
        car.pos_y = 405;
        car.speed_x = 0;
        car.speed_y = 0;
        isRigth = isLeft = isUp = isDown = false;   
        gameConf.nOpponent = 0; 
        gameConf.isFinished = false; 
        gameConf.score = gameConf.highScore = 0;  
    }
    
    public void setDifficult(MouseEvent e) {
    	if(gameConf.phase == 0) {
    		if(e.getY() <= gameConf.difficultPosy && e.getY() >= gameConf.difficultPosy-50) { //Verify y position
    			if(e.getX() >= gameConf.difficultPosx && e.getX() <= gameConf.difficultPosx + gameConf.difficultLengthX.get(gameConf.difficultIndex)) {
    				gameConf.difficultIndex = ((gameConf.difficultIndex == 2) ? 0 : ++gameConf.difficultIndex);
    				gameConf.qtdOpponent = 1 + gameConf.difficultIndex;
    			}
    		} 
    	}
    }

	public void paint(Graphics g){
        super.paint(g);
        Graphics2D obj = (Graphics2D) g;
        obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try{
        	printGame(obj); 	           
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }

	private void printGame(Graphics2D obj) {
		obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/base_road_nova.png")),gameConf.frame_x, 0,this);
		obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/base_road_nova.png")),gameConf.frame_x + 1200, 0,this);
		
		if(gameConf.isHit) {
			if(gameConf.hitController%2 == 0) {
				obj.drawImage(getToolkit().getImage(getClass().getResource(car.imagemLoc)),car.pos_x,car.pos_y,this);
			} else {
				obj.drawImage(getToolkit().getImage(getClass().getResource(car.imagemLocOpaco)),car.pos_x,car.pos_y,this);
			}
		}else {
			obj.drawImage(getToolkit().getImage(getClass().getResource(car.imagemLoc)),car.pos_x,car.pos_y,this);
		}
		
		if (gameConf.sceneController%2 == 0) {
			obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/river_4.png")),gameConf.frame_x, 0,this);
			obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/river_2.png")),(gameConf.frame_x + 1200), 0,this);
		} else {
			obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/river_2.png")),gameConf.frame_x, 0,this);
			obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/river_4.png")),(gameConf.frame_x + 1200), 0,this);
		}

		if (gameConf.phase != 0) {
			obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/placa.png")),430,40,this);
			obj.setFont(new Font("Impact", Font.PLAIN, 36));
			obj.setColor(Color.BLACK);
			obj.drawString("TEMPO: " + (gameConf.time_response - ((System.currentTimeMillis() - gameConf.initial_time)/1000)), 10, 265);
			
			obj.setFont(new Font("Impact", Font.PLAIN, 56));
			obj.drawString(response.input1 + " x " + response.input2 + " = ?", 505, 130);
			
			obj.setFont(new Font("Impact", Font.PLAIN, 32));
			obj.drawString("Pontos: " + gameConf.score, 510, 170);
			
			obj.setFont(new Font("Impact", Font.PLAIN, 74)); 
			
			if (response.isNewQuestion == true) {
				response.difficult = gameConf.difficultIndex+1;
				options = response.getOptions();
			    response.isNewQuestion = false;
			}
			
			for (int i = 0; i < options.length; i++) {
				obj.setColor((i == response.correctIndex && gameConf.phase == 3) ? Color.GREEN : Color.RED);
				obj.drawString(Integer.toString(options[i]), 980, 365 + (120 * i));
			}
		}
		
		if (gameConf.phase == 0) {
			obj.setFont(new Font("Impact", Font.PLAIN, 72));
		    obj.setColor(Color.BLACK);
			obj.drawString("THINK, DRIVER!", 370, 155);
			
			obj.setFont(new Font("Impact", Font.PLAIN, 62));
			obj.setColor(Color.RED);
			obj.drawString(gameConf.difficult.get(gameConf.difficultIndex), gameConf.difficultPosx, gameConf.difficultPosy);
			
			obj.setColor(Color.BLACK);
			obj.setFont(new Font("Impact", Font.PLAIN, 20));
			obj.drawString("Pressione espaço para começar!", 415, 225);
			obj.setFont(new Font("Impact", Font.PLAIN, 24));
			obj.drawString("CONTROLES:", 15, 120);
			//  230,245,600
			obj.setFont(new Font("Impact", Font.PLAIN, 18));
			obj.drawString("Movimentação", 120, 190);
			obj.drawString("Escolhe Opção", 180, 235);
			obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/logo_2.png")),740,10,this);
			obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/setas.png")),10,135,this);
			obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/space.png")),15,215,this);
		}
         
		if(gameConf.isFinished){ 
		    obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/boom.png")),car.pos_x-30,car.pos_y-30,this); 
		}
		
		if(gameConf.nOpponent > 0){ 
		    for(int i=0;i<gameConf.nOpponent;i++){ 
		    	obj.drawImage(getToolkit().getImage(getClass().getResource(opponents[i].imagemLoc)),opponents[i].pos_x,opponents[i].pos_y,this);	
		    }
		}
		
		if(gameConf.phase == 3) {
			for (int i = 0; i < options.length; i++) {
				if(i != response.correctIndex) {
					barrier.pos_y[i] = gameConf.ways.get(i);
					obj.drawImage(getToolkit().getImage(getClass().getResource("imagens/barreira.png")), barrier.pos_x[i],barrier.pos_y[i],this);
				}	
			}
		}
	}
    
    void moveRoad(){
    	if(gameConf.frame_x <= -1200) {
    		gameConf.frame_x = 0;
    		gameConf.sceneController++;
    	} else {
    		gameConf.frame_x--;
    	}
    	
        car.pos_x += car.speed_x; 
        car.pos_y += car.speed_y; 
        
        if(gameConf.phase == 2) {
        	for (Integer way : gameConf.ways) {
        		int diff = Math.abs(way - car.pos_y);
        		if(diff < lastDiff) {
        			switch(way) {
        			case 295: gameConf.aswerWay = 0; 
        				break;
        			case 415: gameConf.aswerWay = 1; 
        				break;
        			case 535: gameConf.aswerWay = 2; 
        				break;
        			}
        		}
        		lastDiff = diff;
			}
        	
        	if(car.pos_y > gameConf.ways.get(gameConf.aswerWay)) {
        		car.pos_y -= 1;
        	} else if(car.pos_y < gameConf.ways.get(gameConf.aswerWay)) {
        		car.pos_y += 1;
        	} else {
        		gameConf.phase = 3;
        		gameConf.contBarrier = true;
        		gameConf.threadCont = 0;
        	}
        }
        
        if(car.pos_x < 0)   
            car.pos_x = 0;  
        
        if(car.pos_x+93 >= 1200) 
            car.pos_x = 1200-93; 
        
        if(car.pos_y < 280)    
            car.pos_y = 280;   
        
        if(car.pos_y >= 640-50) 
            car.pos_y = 640-50; 
        
        
        for(int i=0;i<gameConf.nOpponent;i++){ 
        	opponents[i].pos_x -= opponents[i].speed_x; 
        }
        
        
        int index[] = new int[gameConf.nOpponent];
        for(int i=0;i<gameConf.nOpponent;i++){
            if(opponents[i].pos_x >= -127){
                index[i] = 1;
            }
        }
        int c = 0;
        for(int i=0;i<gameConf.nOpponent;i++){
            if(index[i] == 1){
            	opponents[c].imagemLoc = opponents[i].imagemLoc;
            	opponents[c].pos_x = opponents[i].pos_x;
            	opponents[c].pos_y = opponents[i].pos_y;
            	opponents[c].speed_x = opponents[i].speed_x;
            	opponents[c].car_id  = opponents[i].car_id;
                c++;
            }
        }
        
        if(gameConf.score > gameConf.highScore)   
            gameConf.highScore = gameConf.score;  
        
        gameConf.nOpponent = c;
        
        int diff = 0; 
        if (gameConf.phase == 1 || gameConf.phase == 0) {
        	for(int i=0;i<gameConf.nOpponent;i++){ 
                diff = car.pos_y - opponents[i].pos_y; 
                if((opponents[i].pos_y >= car.pos_y && opponents[i].pos_y <= car.pos_y+75) 
                	|| (opponents[i].pos_y+75 >= car.pos_y && opponents[i].pos_y+75 <= car.pos_y+75)){   
                    if(car.pos_x+140 >= opponents[i].pos_x && !(car.pos_x >= opponents[i].pos_x+140)){  
                    	long id_ = opponents[i].car_id;
                    	if(gameConf.lastOponentsId.stream().filter(id -> id == id_).count() == 0) {
                    		if(gameConf.phase == 1) {
	                			gameConf.score--;
	                		} 
                    		gameConf.lastOponentsId.add(opponents[i].car_id);
	            			gameConf.isHit = true;
	                    	gameConf.isHitInitialTime = System.currentTimeMillis();                	
	                    }
                    	
                    }
                }
            }
        }
        
        gameConf.isHitRealTime = ((System.currentTimeMillis() - gameConf.isHitInitialTime)/1000);
        if (gameConf.isHitRealTime >= 1) {
			gameConf.isHit = false;
		}
        
        cont_hit++;
        if (cont_hit >= 30) {
        	cont_hit = 0;
        	gameConf.hitController++;
        }
        
        if ((gameConf.time_response - ((System.currentTimeMillis() - gameConf.initial_time)/1000)) <= 0 && gameConf.phase == 1) {
        	this.finish();
        }
        
        barrier.pos_x[0]--;
        barrier.pos_x[1]--;
        barrier.pos_x[2]--;
        
        if (gameConf.phase == 3) {
        	if(gameConf.contBarrier) {
				barrier.resetBarrier();
				gameConf.contBarrier = false;
			}
        	
        	for (int i = 0; i < options.length; i++) {
    			if(i != response.correctIndex) {
    				diff = car.pos_y - barrier.pos_y[i];
    				if((barrier.pos_y[i] >= car.pos_y && barrier.pos_y[i] <= car.pos_y+75) 
    	            	|| (barrier.pos_y[i]+75 >= car.pos_y && barrier.pos_y[i]+75 <= car.pos_y+75)){   
    	                if(car.pos_x+140 >= barrier.pos_x[i] && !(car.pos_x >= barrier.pos_x[i]+140)){  
    	                    this.finish(); 
    	                    gameConf.qtdOpponent = 1+gameConf.difficultIndex;//opponents.length;
    	                }
    	            }
    				if(barrier.pos_x[i] < car.pos_x && gameConf.contScore == false) {
    					gameConf.score++;
    					gameConf.contScore = true;
    				}
    				if (barrier.pos_x[i] < -100) {
    					gameConf.phase = 1;
    					gameConf.contScore = false;
    					response.isNewQuestion = true;
    					gameConf.threadCont = 0;
    					gameConf.qtdOpponent = 1+gameConf.difficultIndex;//opponents.length;
    					gameConf.time_response += 5;
    					break;
    				}
    			}
    		}
        }
    }
    
    void finish(){
        String str = "";    
        gameConf.isFinished = true;  
        this.repaint();     
        if(gameConf.score == gameConf.highScore && gameConf.score != 0) 
            str = "\nParabéns! Você bateu seu record";  
        JOptionPane.showMessageDialog(this,"Fim de Jogo!!!\nSua pontuação: "+gameConf.score+"\nMelhor Pontuação: "+gameConf.highScore+str, "Fim de Jogo", JOptionPane.YES_NO_OPTION);    //displays the congratulations message and a message saying game over and the users gameConf.score and the high gameConf.score
        gameConf.isFinished = false;
        gameConf.phase = 0;
        gameConf.score = 0;
        gameConf.time_response = 40;
        gameConf.lastOponentsId.clear();
    }
    
    
    public void moveCar(KeyEvent e){
    	if (gameConf.phase == 1 || gameConf.phase == 0) {
    		if(e.getKeyCode() == KeyEvent.VK_UP){   
            	isUp = true;
                car.speed_y = -1;
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN){ 
            	isDown = true;
            	car.speed_y = 1; 
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT){ 
            	isRigth = true;
            	car.speed_x = 2;
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT){
                isLeft = true;
                car.speed_x = -2;
            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            	if (gameConf.phase == 0) {
            		response.isNewQuestion = true;
            		gameConf.phase = 1;
            		gameConf.initial_time = System.currentTimeMillis();
            	} else {
            		gameConf.phase = 2;
                	gameConf.qtdOpponent = 0;
                	car.speed_x = 0;
                	car.speed_y = 0;
                	gameConf.threadCont = 0;	
            	}
            	gameConf.lastOponentsId.clear();
            }
    	}
        
        
    }
    
    public void stopCar(KeyEvent e){
    	if (gameConf.phase == 1 || gameConf.phase == 0) {
	        if(e.getKeyCode() == KeyEvent.VK_UP){   
	        	isUp = false;
	        	car.speed_y = 0; 
	        }
	        if(e.getKeyCode() == KeyEvent.VK_DOWN){   
	        	isDown = false;
	        	car.speed_y = 0; 
	        }
	        if(e.getKeyCode() == KeyEvent.VK_RIGHT){  
	        	isRigth = false;
	        	car.speed_x = 0; 
	        }
	        if(e.getKeyCode() == KeyEvent.VK_LEFT){   
	        	isLeft = false;
	        	car.speed_x = 0; 
	        }
    	}
    }

	public void Player(Game game) throws InterruptedException {
		gameConf.initial_time = System.currentTimeMillis();
		gameConf.sceneController = 0;
		gameConf.nOpponent = 0;
		gameConf.score = 0;      
	    gameConf.highScore = 0;  
	    gameConf.isFinished = false;
	    gameConf.phase = 0; 
	    gameConf.difficultIndex = 0;
	    gameConf.difficultPosx  = 20;
	    gameConf.difficultPosy  = 600;
	    gameConf.qtdOpponent = 1+gameConf.difficultIndex;//opponents.length;
	    gameConf.ways = Arrays.asList(295,415,535);
	    gameConf.threadSleep = 5;
	    gameConf.time_response = 40;
	    lastId = 0;
	    
		int count = 1;
		while(true){
        	boolean iteratorController = true;
            game.moveRoad();   
            while (iteratorController) {
            	game.repaint();
            	if (gameConf.phase == 3) {
            		gameConf.threadCont++; 
            		Thread.sleep(gameConf.threadSleep);
            		if(gameConf.threadSleep > 1 && gameConf.threadCont >= 30) {
            			gameConf.threadSleep--;
            			gameConf.threadCont = 0;
            		}
            	} else {
            		gameConf.threadCont++;
            		Thread.sleep(gameConf.threadSleep);
            		if(gameConf.threadSleep <= 4 && gameConf.threadCont >= 30) {
            			gameConf.threadSleep++;
            			gameConf.threadCont = 0;
            		}
            	}                
                iteratorController = false;
            }
            count++;
            setEnemyCarsSettings(game, count);
	    }
	}

	private static void setEnemyCarsSettings(Game game, int count) {
		if(gameConf.nOpponent < gameConf.qtdOpponent && count % 200 == 0){ 
		    Car enemyCar = new Car();
		    enemyCar.imagemLoc = "imagens/car_left_"+((int)((Math.random()*100)%3)+1)+".png";
		    enemyCar.pos_x = 1199;
		    enemyCar.speed_x = (int)(Math.random()*100)%2 + 2;
		    
		    int enemyWay = 295 + (120  * random.ints(0, 3).findFirst().getAsInt());
		    
		    for (int i = 0; i < opponents.length; i++) {
		    	try {
		    		if(enemyWay == opponents[i].pos_y && (enemyCar.pos_x / enemyCar.speed_x) + 50 > (opponents[i].pos_x / opponents[i].speed_x))
		    			enemyCar.speed_x = opponents[i].speed_x;
		    	} catch(Exception ex) {	
		    	}
			}
		    
		    enemyCar.pos_y = enemyWay;
		    enemyCar.car_id = ++lastId;
		    opponents[gameConf.nOpponent] = enemyCar;
		    gameConf.nOpponent++;
		}
	}
}
