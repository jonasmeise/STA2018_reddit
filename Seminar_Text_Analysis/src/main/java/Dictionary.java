import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dictionary {
	//RUN CONTROLLER.JAVA AS THE MAIN PROGRAM
	
	ArrayList<String> wordList;

	public Dictionary() {
		wordList = new ArrayList<String>();
	}
	
	public ArrayList<String> getDictionary() {
		return wordList;
	}
	
	public void enterData(String word) {
		if(word.length()>0) {
			wordList.add(word.trim());
		}
	}
	
	public String calculateClosestWord(String matchWord) {
		int minimumDistance=100,currentDistance=0;
		String minimumMatch = null;
		
		for(String dicWord : getDictionary()) {
			currentDistance=computeLevenshteinDistance(matchWord.toLowerCase(), dicWord);
			
			if(currentDistance<minimumDistance) {
				minimumDistance=currentDistance;
				minimumMatch = dicWord;
			}
		}
		
		return minimumMatch;
	}
	
	public int calculateMinimumDistance(String matchWord) {
		int minimumDistance=100,currentDistance=0;
		
		for(String dicWord : getDictionary()) {
			currentDistance=computeLevenshteinDistance(matchWord.toLowerCase(), dicWord);
			
			if(currentDistance==0) {
				minimumDistance=0;
				break;
			}
			else if(currentDistance<minimumDistance) {
				minimumDistance=currentDistance;
			}
		}
		
		return minimumDistance;
	}
	
	 private int minimum(int a, int b, int c) {   //credit to https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java                         
	        return Math.min(Math.min(a, b), c);                                      
	    }                                                                            
	                                                                                 
    public int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) { //credit to https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
        int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];        
                                                                                 
        for (int i = 0; i <= lhs.length(); i++)                                 
            distance[i][0] = i;                                                  
        for (int j = 1; j <= rhs.length(); j++)                                 
            distance[0][j] = j;                                                  
                                                                                 
        for (int i = 1; i <= lhs.length(); i++)                                 
            for (int j = 1; j <= rhs.length(); j++)                             
                distance[i][j] = minimum(                                        
                        distance[i - 1][j] + 1,                                  
                        distance[i][j - 1] + 1,                                  
                        distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));
                                                                                 
        return distance[lhs.length()][rhs.length()];                           
    }                                
	
	public ArrayList<String> loadFromFile(String filepath) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
		
		try {
		    String line = bufferedReader.readLine();

		    while (line != null) {
		    	String[] mySplit = line.split("( |\\n)");
		    	
		    	for(String word : mySplit) {
		    		if(word.length()>0) {
		    			enterData(word);
		    		//System.out.println(word);
		    		}
		    	}
		    	
		        line = bufferedReader.readLine();
		    }
		} finally {
		    bufferedReader.close();
		}
		
		return getDictionary();
	}
	
}
