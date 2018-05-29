import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;

public class DifficultyEvaluator {

	public ArrayList<String> splitList;
	
	public String[] noise = {"(",")",".",",","(","!","?",";",":", "\n", "\r"};
	
	public static void main(String[] args) {
		DifficultyEvaluator df = new DifficultyEvaluator();

		System.out.println(df.startsWithCaps("Test"));
		System.out.println(df.startsWithCaps("test"));
		System.out.println(df.startsWithCaps(" aest"));
		System.out.println(df.startsWithCaps(" Test"));
		
		ArrayList<String> testList = new ArrayList<String>();
		testList.add("Philosophy is the study of humans and the world by thinking and asking questions. It is a science and an art. Philosophy tries to answer important questions by coming up with answers about real things and asking \"why?\"\r\n" + 
				"\r\n" + 
				"Sometimes, philosophy tries to answer the same questions as religion and science. Philosophers do not all give the same answers to questions. Some people think there are no right answers in philosophy, only better answers and worse answers. Many types of philosophy criticize or even attack the beliefs of science and religion.\r\n" + 
				"\r\n" + 
				"In his work Critique of Pure Reason, Immanuel Kant asked the following questions:[2]\r\n" + 
				"\r\n" + 
				"What can I say?\r\n" + 
				"What shall I do?\r\n" + 
				"What dare I hope?\r\n" + 
				"What is man?\r\n" + 
				"The answers to these questions gives the different domains or categories of philosophy.\r\n" + 
				"\r\n" + 
				"Categories in philosophy\r\n" + 
				"Philosophy can be divided into different groups, based on the types of questions that it asks. Below is a list of questions split into groups. One possible list of answers to these questions can be called a 'philosophy'. There are many different 'philosophies', because all of these questions have many different answers according to different people. Not all philosophies ask the same questions. These are the questions that are usually asked by philosophers from the Western world:\r\n" + 
				"\r\n" + 
				"Metaphysics:\r\n" + 
				"\r\n" + 
				"Metaphysics is sometimes split up into ontology (the philosophy of real life and living things), the philosophy of mind and the philosophy of religion; but these sub-branches are very close together.\r\n" + 
				"\r\n" + 
				"Ontology:\r\n" + 
				"\r\n" + 
				"What is the world that we see around us? (What is reality?)\r\n" + 
				"Is there more to the world than just what we see or hear?\r\n" + 
				"If nobody sees something happening, does that mean that it did not happen?\r\n" + 
				"What does it mean to say that something is possible? Do other worlds exist?\r\n" + 
				"Is there anything very special about being a human being or being alive at all?\r\n" + 
				"If not, why do some people think that there is?\r\n" + 
				"What is space? What is time?\r\n" + 
				"The philosophy of mind:\r\n" + 
				"\r\n" + 
				"What is a mind?\r\n" + 
				"What is a body?\r\n" + 
				"What is consciousness?\r\n" + 
				"Do people make choices, or can they only choose to do one thing? (Do people have free will?)\r\n" + 
				"What makes words or ideas meaningful? (What is the relation between meaningful words or ideas and the things that they mean?)\r\n" + 
				"The philosophy of religion:\r\n" + 
				"\r\n" + 
				"Do people have souls?\r\n" + 
				"Is there a God who created the Universe?\r\n" + 
				"In epistemology:\r\n" + 
				"\r\n" + 
				"What is knowledge?\r\n" + 
				"How can we know anything?\r\n" + 
				"What is science?\r\n" + 
				"What is truth?\r\n" + 
				"In ethics:\r\n" + 
				"\r\n" + 
				"What are right and wrong, good and bad?\r\n" + 
				"Should people do some things and not others?\r\n" + 
				"What is justice?\r\n" + 
				"In aesthetics:\r\n" + 
				"\r\n" + 
				"What is beauty? What if one person thinks a painting is beautiful, but another person thinks the painting is ugly? Can the painting be beautiful and ugly at the same time?\r\n" + 
				"Are true things beautiful?\r\n" + 
				"Are good things beautiful?\r\n" + 
				"What is art? We commonly think that a sculpture in a museum is art. If a sculptor sculpts a sculpture of a rock from clay, and puts it in a museum, many would call it art. But what if a person picks up a rock from the ground - is the rock a piece of art?\r\n" + 
				"In logic:\r\n" + 
				"\r\n" + 
				"What do the words we use mean?\r\n" + 
				"How can we say things (especially ideas) in a way that only has one meaning?\r\n" + 
				"Can all ideas be expressed using language?\r\n" + 
				"How does the truth of an argument's premise affect the truth of its conclusion?\r\n" + 
				"How can we reason correctly?\r\n" + 
				"In axiology:\r\n" + 
				"\r\n" + 
				"What has value?\r\n" + 
				"Is time really money? or have we made it so?\r\n" + 
				"Does love, beauty, or justice hold any value?\r\n" + 
				"Other divisions include eschatology, teleology and theology. In past centuries natural science was included in philosophy, and called \"natural philosophy\".");
		
		
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
			myDic.loadFromFile("C:\\Users\\Jonas\\Downloads\\STA\\basicenglish.txt");
			
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
