import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Crawler2 
{
	public String headerName = "test-script/0.1 by luxu5";
	public String basicAuthUser = "6NqNBqpFrSf2zQ";
	public String basicAuthPw = "gIyBp-ecZR0QYTkgW3lDGOb2ReM";
	
	public String filename = "output.txt";
	
	public int cntRequests = 0; 
	public int maxRequests = 50;
	public long createdAt;
	
	
	public static void main(String[] Args) {
		Crawler2 mycrawler = new Crawler2();

		mycrawler.crawl();
	}
	
	public void crawl() {
		createdAt = System.currentTimeMillis();
		
		//Hauptwebsite auslesen
		try {
			JsonNode returnFull = getJsonFromURL("http://www.reddit.com/r/explainlikeimfive/top/.json?sort=top&t=day/");
			ArrayList<String> linksToCrawl = getMetaDataFromJson(returnFull, "url");
			
			System.out.println(linksToCrawl);
			
			while(!linksToCrawl.isEmpty()) {
				System.out.println("Waiting....");
				while(getAgeInSeconds() < 3) {
					//alle 2 Sekunden eine weitere Request rausballern
				}
				System.out.println("... finished, " + linksToCrawl.size() + " links left!");
				
				if(cntRequests <= maxRequests) {
					JsonNode subThread = getJsonFromURL(linksToCrawl.remove(0) + ".json");
					cntRequests++;
					
					System.out.println("request #" + cntRequests);
					writeToEOF(this.filename, subThread.toString());
				}
				else {
					linksToCrawl = new ArrayList<String>(); //pseudo-remove
				}
				
				createdAt = System.currentTimeMillis();
			}
			
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	/*public void login() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse = Unirest.post("https://www.reddit.com/api/v1/access_token")
				  .header("User-Agent", "test-script/0.1 by luxu5")
				  .field("grant_type", "client_credentials" )
				  .basicAuth("6NqNBqpFrSf2zQ", "gIyBp-ecZR0QYTkgW3lDGOb2ReM")
				  .asJson();
		
		System.out.println(jsonResponse.getBody() + " " + jsonResponse.getStatusText() + " " + jsonResponse.getStatus() + " " + jsonResponse.getRawBody().toString() );
	}*/
	
	
	public JsonNode getJsonFromURL(String url) throws UnirestException {
		try {
		HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
				  .header("User-Agent", headerName)
				  /*.field("grant_type", "client_credentials" )
				  .basicAuth("6NqNBqpFrSf2zQ", "gIyBp-ecZR0QYTkgW3lDGOb2ReM")*/
				  .asJson();
		
		//"url": ... " 
		
		return jsonResponse.getBody();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> getMetaDataFromJson(JsonNode json, String tag) {
		return getMetaDataFromJson(json.toString(), tag);
	}
	
	public ArrayList<String> getMetaDataFromJson(String json, String tag) {
		String jsonString = json.toString();
		ArrayList<String> returnList = new ArrayList<String>();
		int pos_end;
		int charoffset = 2;
		
		
		tag = "\"" + tag + "\"";
		int pos = jsonString.indexOf(tag);
		
		while(pos > -1) {
			 pos_end = jsonString.indexOf(",\"", pos+1);
			 
			 if (pos_end > -1) {
				returnList.add(jsonString.substring(pos + tag.length() + charoffset, pos_end - 1));
				pos = jsonString.indexOf(tag, pos+1);
			}
		}
		
		return returnList;
	}
	
    public int getAgeInSeconds() {
        long nowMillis = System.currentTimeMillis();
        return (int)((nowMillis - this.createdAt) / 1000);
    }
	
	public void writeToEOF(String filename, String text) {
		File file = new File(filename);
		PrintWriter out = null;
		
		try {
			if(!file.exists()) {
					file.createNewFile();
	
			}
			
			out = new PrintWriter(new FileWriter(file, true));
			out.append(text);
			System.out.println("Writing " + text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
