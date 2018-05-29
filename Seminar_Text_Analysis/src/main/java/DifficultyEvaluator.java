import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;

public class DifficultyEvaluator {

	public ArrayList<String> splitList;
	
	public String[] noise = {"(",")",".",",","(","!","?",";",":", "\n", "\r"};
	
	public static void main(String[] args) {

		//RUN CONTROLLER.JAVA AS THE MAIN PROGRAM
		DifficultyEvaluator df = new DifficultyEvaluator();

		System.out.println(df.startsWithCaps("Test"));
		System.out.println(df.startsWithCaps("test"));
		System.out.println(df.startsWithCaps(" aest"));
		System.out.println(df.startsWithCaps(" Test"));
		
		ArrayList<String> testList = new ArrayList<String>();
		
		ArrayList<String> sentences = df.sentenceSplitter(testList);
		for(String sentence : sentences) {
			for(String word : df.wordTokenSplitter(sentence)) {
				System.out.print(df.syllableCount(word) + "/" + word + " ");
			}
			System.out.print("\n");
		}
		
		System.out.println(df.evalFleschGrade(testList));
		System.out.println(df.findAbbrevations(testList.get(0)));
		System.out.println(df.evalSMOG(testList));
		System.out.println(df.evalCLI(testList));
		
		Dictionary myDic = new Dictionary();
		try {
			myDic.loadFromFile(".\\basicenglish.txt");
			
			System.out.print(df.findDifficultWords(testList, myDic).toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double evalFleschGrade(ArrayList<String> text) {
		ArrayList<String> sentences = sentenceSplitter(text);
		double sentenceCount = 0;
		double wordCount = 0;
		double syllableCount = 0;
		
		
		for(String sentence : sentences) {
			sentenceCount++;
			ArrayList<String> words = wordTokenSplitter(sentence);
			
			wordCount = wordCount + words.size();
			
			for(String word : words) {
				syllableCount = syllableCount + this.syllableCount(word);
			}
		}
		
		double wordSentence = wordCount/sentenceCount;
		double syllableWord = syllableCount/wordCount;

		return (0.39 * (wordCount/sentenceCount) + 11.8 * (syllableCount / wordCount) - 15.59);
	}
	
	public double evalSMOG(ArrayList<String> text) {
		ArrayList<String> sentences = sentenceSplitter(text);
		double sentenceCount = 0;
		double polySyllable = 0;
		
		for(String sentence : sentences) {
			sentenceCount++;
			
			ArrayList<String> words = wordTokenSplitter(sentence);
			
			for(String word : words) {
				if(this.syllableCount(word)>2) {
					polySyllable++;
				}
			}
		}
		
		return 1.0430 * (Math.sqrt(polySyllable * (30 / sentenceCount) + 3.1291));
	}

	public double evalCLI(ArrayList<String> text) {
		ArrayList<String> sentences = sentenceSplitter(text);
		double letterCount = 0;
		double wordCount = 0;
		double sentenceCount = 0;
		
		for(String sentence : sentences) {
			sentenceCount++;
			
			ArrayList<String> words = wordTokenSplitter(sentence);
			
			for(String word : words) {
				wordCount++;
				
				letterCount += word.length();
			}
		}
		
		return (0.0588 * (letterCount / wordCount * 100) - (0.296 * (sentenceCount / wordCount * 100)) - 15.8);
	}
	
	public boolean isNumeric(String word) {
		Pattern pattern = Pattern.compile("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
		Matcher matcher = pattern.matcher(word);
		
		return matcher.matches();
	}
	
	public boolean isString(String word) {
		Pattern pattern = Pattern.compile("[a-z]+");
		Matcher matcher = pattern.matcher(word);
		
		return matcher.matches();
	}
	
	public ArrayList<String> findAbbrevations(String text) {
		ArrayList<String> returnList = new ArrayList<String>();
		Pattern pattern = Pattern.compile("([A-Z][A-Z]+)");
		Matcher matcher = pattern.matcher(text);
		
		while(matcher.find()) {
			returnList.add(matcher.group().toString());
		}
		return returnList;
	}
	
	public int syllableCount(String word) {
		if(isNumeric(word)) { return word.length(); }
		
		Pattern pattern = Pattern.compile("[aeiouy]+?\\w*?[^e]");
		Matcher matcher = pattern.matcher(word);
		
		int calculate = 0;
		while(matcher.find()) {
			calculate++;
		}
		
		if(calculate==0) {calculate++;}	
		return calculate;
	}
	
	public String removeNoiseFromString(String sentence) {
		String returnString = sentence.toString();
		
		for(String parameter : noise) {
			returnString = returnString.replace(parameter, " ");
		}
		
		return returnString;
	}
	
	public ArrayList<String> wordTokenSplitter(String sentence) {
		ArrayList<String> returnList = new ArrayList<String>();
		String[] splits = removeNoiseFromString(sentence).split(" ");
		
		for(String word : splits) {
			if(word.length()>0) {
				returnList.add(word.toString());
			}
		}
		
		return returnList;
	}
	
	public ArrayList<String> sentenceSplitter(ArrayList<String> completeList) {
		ArrayList<String> returnList = new ArrayList<String>();
		String overhead = "";
		
		for(String completeText : completeList) {
			String[] splits = completeText.split("([.!?])(?![0-9])");
			
			for(int i=0;i<splits.length;i++) {
				if(splits[i].length() > 2) { //mindestlänge von Satz: 3 Zeichen
					returnList.add(overhead + splits[i].toString());
					overhead="";
				}
				else {
					overhead += splits[i] + " ";
				}
			}
		}
		
		return returnList;
	}
	
	public ArrayList<String> findDifficultWords(String sentence, Dictionary dic) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(sentence);
		
		return findDifficultWords(list, dic);
	}
	
	public boolean startsWithCaps(String word) {
		return (!word.startsWith(word.toLowerCase().substring(0, 1)));
	}
	
	public ArrayList<String> findDifficultWords(ArrayList<String> sentences, Dictionary dic) {
		ArrayList<String> returnList = new ArrayList<String>();
		ArrayList<String> alreadyChecked = new ArrayList<String>();
		
		for(String sentence : sentences) {
			ArrayList<String> words = wordTokenSplitter(sentence);
			
			for(String word : words) {
				if(isString(word.toLowerCase())) {
					if(!alreadyChecked.contains(word.toLowerCase())) {
						if(isDifficultWord(word, dic)) {
							returnList.add(word);
							alreadyChecked.add(word.toLowerCase());
						}
					}
					else {
						returnList.add(word);
					}
				}
			}
		}
		
		return returnList;
	}
	
	public boolean isDifficultWord(String word, Dictionary dic) {
		return ((double)dic.calculateMinimumDistance(word)>0.8*(double)Math.sqrt(word.length()) && !startsWithCaps(word));
	}
}
