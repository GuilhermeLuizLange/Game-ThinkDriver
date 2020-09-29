package br.com.thinkdriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameConf {
	boolean isNewQuestion, isFinished, contScore, contBarrier, isHit;
	int frame_x, 
		frame_y, 
		nOpponent,
		qtdOpponent,
		score, 
		highScore, 
		sceneController,
		phase,
		aswerWay,
		threadSleep,
		threadCont,
		difficultIndex,
		difficultPosx,
		difficultPosy,
		lastOponentIndex;
	long initial_time, time_response, isHitInitialTime, isHitRealTime, hitController, last_OpponentCar;
	List<Integer> lastOponentsId = new ArrayList<Integer>();
	List<Integer> ways;
	List<String> difficult = Arrays.asList("FÁCIL", "MÉDIO", "DIFÍCIL");
	List<Integer> difficultLengthX = Arrays.asList(120,152, 160);
}
