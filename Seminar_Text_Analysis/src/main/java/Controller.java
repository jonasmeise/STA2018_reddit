import java.io.IOException;
import java.util.ArrayList;

import type.Post;

public class Controller {
	
	public static void main(String[] args) throws IOException {
		String fileurl = "C:\\Users\\Jonas\\eclipse-workspace\\1\\output_askscience.txt";
		
		Dictionary myDic = new Dictionary();
		myDic.loadFromFile("C:\\Users\\Jonas\\Downloads\\STA\\basicenglish.txt");
		
		Reader myReader = new Reader();
		DifficultyEvaluator myEval = new DifficultyEvaluator();
		
		int count;
		double wordCount = 0;
		double diffWordCount = 0;
		double avgFlesch = 0;
		double avgSMOG = 0;
		double avgCLI = 0;
		int threadcount = 0;
		
		ArrayList<String> all = myReader.readFile(fileurl);
		ArrayList<String> tags = new ArrayList<String>();
		
		ArrayList<Post> parsed = myReader.parseJsonBody(all.get(0), false, null);	
		System.out.println(parsed.size());
		
		String currentTag = null;
		
		ArrayList<String> myArrayList = new ArrayList<String>();
		ArrayList<String> myTagList = new ArrayList<String>();
		for(Post p : parsed) {
			if(p.getTag()!="empty") {
				System.out.println(p.getTag());
				
				threadcount++;
				
				if(!tags.contains(p.getTag())) {
					tags.add(p.getTag());
					currentTag = p.getTag();
				}
			}
			else {
				myArrayList.add(p.getMessage());
				myTagList.add(currentTag.toString());
				
				wordCount += p.getMessage().split(" ").length;
			}
		}
		
		System.out.println(myArrayList.size() + " - " + threadcount);
		
		for(String myTag : tags) {
			wordCount = 0;
			
			ArrayList<String> subList = new ArrayList<String>();
			
			System.out.println("Tag: " + myTag);
			for(int i=0;i<myTagList.size();i++) {
				if(myTagList.get(i).compareTo(myTag)==0) {
					subList.add(myArrayList.get(i));
					wordCount += myArrayList.get(i).split(" ").length;
				}
			}
			
			/*avgFlesch = myEval.evalFleschGrade(subList);
			avgSMOG = myEval.evalSMOG(subList);
			avgCLI = myEval.evalCLI(subList);
			diffWordCount = myEval.findDifficultWords(subList,myDic).size();*/
			
			ArrayList<String> avgAbb = new ArrayList<String>();
			
			for(String str : subList) {
				avgAbb.addAll(myEval.findAbbrevations(str));
			}
			
			System.out.println("Flesch: " + avgFlesch);
			System.out.println("SMOG: " + avgSMOG);
			System.out.println("CLI: " + avgCLI);
			System.out.println("Abb: " + avgAbb);
			System.out.println("%difficult Words: " + diffWordCount/wordCount);
			
		}
	}
}
