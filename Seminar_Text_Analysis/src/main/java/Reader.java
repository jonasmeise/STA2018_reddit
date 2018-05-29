import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import type.Post;

public class Reader {
	
	public ArrayList<Post> allPosts = new ArrayList<Post>();
	
	public static void main(String[] args) {
		//RUN CONTROLLER.JAVA AS THE MAIN PROGRAM

		String fileurl = ".\\data_eli5.txt";
		
		Reader myReader = new Reader();
		try {
			ArrayList<Post> myReturn = myReader.getStructure(myReader.readFile(fileurl).get(0),false,null,0);
			
			for(Post p : myReturn) {
				System.out.println(p.getLevel() + "- " + p.getMessage());
			}

			//myReader.parseJsonBody(myReader.readFile(fileurl), 1, "\"body\"", "\",\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> readFile(String fileurl) throws IOException {
		ArrayList<String> returnList = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(fileurl));		
	    String data = reader.readLine();
	    while(data != null){
	    	
	    	returnList.add(data.toString());
	        data = reader.readLine();
	    }
	    
		return returnList;
	}
	
	public int count(String raw, char c) {
		int returnValue=0;
		
		for(char c2 : raw.toCharArray()) {
			if(c2==c) {
				returnValue++;
			}
		}
		
		return returnValue;
	}
	
	public ArrayList<Post> getStructure(String raw, boolean recursive, Post head, int currentReaderPosition) {
		ArrayList<Post> returnList = new ArrayList<Post>();
		int startIndex = 0;
		int titleIndex, endIndex, currentLevel, nextLevel=0;
		int countOpen, countClosed;
		boolean finished=false;
		
		if(recursive==false) {
			titleIndex = raw.indexOf("\"title\":\"", currentReaderPosition);
			
			if(titleIndex > 0) { //treffer
				startIndex = raw.indexOf("\"kind\":\"t3\"", currentReaderPosition);
				endIndex = raw.indexOf("\"kind\":\"t3\"", startIndex+1);
				//startIndex = raw.indexOf(",\"body\":\"", startIndex);
				
				Post newHead = new Post();
				newHead.setLevel(-1);
				newHead.setMessage(getJsonTag(raw, "\"title\":\"", "\",\"", titleIndex,0));
				newHead.setParent(newHead);
				newHead.setTag(getJsonTag(raw, "\"link_flair_text\":\"","\",\"",titleIndex,0));
				
				returnList.add(newHead);
				if(endIndex > 0) {
					returnList.addAll(getStructure(raw.substring(startIndex+1, endIndex), true, newHead, titleIndex+1));
				}
				else
				{
					returnList.addAll(getStructure(raw.substring(startIndex+1), true, newHead, titleIndex+1));
				}
				
				return returnList;
			}
		}
		else {
			while(!finished) {
				startIndex = raw.indexOf(",\"body\":\"", startIndex+1);
				endIndex = raw.indexOf(",\"body\":\"", startIndex+1);
				
				if(endIndex > startIndex) { 
					currentLevel = nextLevel;
					String sub = raw.substring(startIndex, endIndex);
					
					countOpen = count(sub,'{');
					countClosed = count(sub,'}');
					
					if(countOpen==4 && countClosed==0) {
						nextLevel = currentLevel+1;
					}
					else {
						if(countOpen==2) {
							nextLevel = currentLevel - ((countClosed-2) / 4);
						}
						else {
							nextLevel = currentLevel - ((countClosed-2) / 4);
							//System.out.println("WEIRD DATA INPUT");
						}
					}
					
					if(currentLevel<0) { currentLevel = 0; }
					
					Post myPost = new Post();
					myPost.setLevel(currentLevel);
					myPost.setMessage(getJsonTag(sub, ",\"body\":\"", "\",\"", 0, 0));
					myPost.setTag("empty");
					//System.out.println(currentLevel + "-" + countOpen + "/" + countClosed + ":  " + getJsonTag(sub, ",\"body\":\"", "\",\"", 0, 0));
					returnList.add(myPost);
				}
				else
				{
					finished=true;
				}
			}
			
		}
		
		return returnList;
	}
	
	public ArrayList<Post> parseJsonBody(String raw, boolean recursive, Post head) {
		ArrayList<Post> returnList = new ArrayList<Post>();
		int currentPosition = 0;

		while(currentPosition >= 0) {
			returnList.addAll(getStructure(raw, false, null, currentPosition));
			currentPosition = raw.indexOf("\"title\":\"", currentPosition+1);
		}
	
		return returnList;
	}
	
	public String getJsonTag(String raw, String tagStart, String tagEnd, int pos, int offset) {
		int fromStart = raw.indexOf(tagStart, pos)+tagStart.length()+offset;
		int toEnd = raw.indexOf(tagEnd, fromStart);
		
		if(fromStart > 0) {
			return raw.substring(fromStart, toEnd );
		} else {
			return null;
		}
	}
	
	public void setAllPosts(ArrayList<Post> allPosts) {
		this.allPosts = allPosts;
	}
	
	public ArrayList<Post> getAllPosts() {
		return this.allPosts;
	}
}
