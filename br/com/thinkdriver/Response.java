package br.com.thinkdriver;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Response {
	
	static Random random = new Random();
	boolean isNewQuestion;
	int correctAnswer, correctIndex, input1, input2, difficult;
	int[] options = new int[3];
	
	
	public Response() {
		isNewQuestion = true;
	}
	
	int[] getOptions() {
		input1 = random.ints(1, 1+(5 * difficult)).findFirst().getAsInt();
		input2 = random.ints(1, 1+(5 * difficult)).findFirst().getAsInt();
		correctAnswer =  input1 * input2;
		Set<Integer> SetOptions = new HashSet<>();
		
		while(SetOptions.size() < 3) {
			randomExecute();
			for (int i = 0; i < options.length; i++) {
				SetOptions.add(options[i]);
			}
		}; 
		
		return options;
	}

	private void randomExecute() {
		random.ints(0,3).distinct().limit(3).forEach(optionIndex -> {
	        if (random.ints(0,2).findFirst().getAsInt() % 2 == 0) {
	        	options[optionIndex] = Math.abs(correctAnswer - random.ints(1, 5).findFirst().getAsInt()); 
	        } else {
	        	options[optionIndex] = Math.abs(correctAnswer + random.ints(1, 5).findFirst().getAsInt());
	        }
		});	
		
		correctIndex = random.ints(0,3).findFirst().getAsInt();
		options[correctIndex] = correctAnswer;
	};
	
	
}
