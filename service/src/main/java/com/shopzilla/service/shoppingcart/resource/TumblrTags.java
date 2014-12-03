package com.shopzilla.service.shoppingcart.resource;


import java.net.*;
import java.io.*;
import java.util.Vector;
import org.json.*;
import org.apache.poi.*;


public class TumblrTags{ 
	//add fuzzy match resource from excel

	private FuzzyMatch fm = new FuzzyMatch();
	public Vector<String> tags = new Vector<String>();


	private Vector<String> tparser(String response, Vector<String> old_tags) throws Exception{
		JSONObject obj = new JSONObject(response);
		Vector<String> brandnames = fm.readingExcel("src/main/java/com/shopzilla/service/shoppingcart/resource/brandname.xlsx");
		JSONArray responses = obj.getJSONArray("response");
		for(int i=0;i< responses.length(); i++){
			JSONObject cur_response = responses.getJSONObject(i);
			JSONArray t_tags = cur_response.getJSONArray("tags");
			for(int j=0; j<t_tags.length(); j++){
				String tag = t_tags.get(j).toString();
				if(!old_tags.contains(tag)){
					//System.out.println(tag);
					//System.out.println("============");
					for(int k=0; k<brandnames.size();k++){
						//if score is over 0.85, than cosider this tag is a valid tag break
						double score = fm.LevenshteinDistance(tag, brandnames.get(k));
						if(score >= 0.85 && score <= 1.0){
							System.out.println(score);
							System.out.println(brandnames.get(k));
							System.out.println(tag+"<=========================================================");
							old_tags.add(tag);
							break;
						}
					}
				}
			}
		}
		return old_tags;
	}

	public Vector<String> tumblrcalls(String keywords, String api_key, Vector<String> old_tags, int count) throws Exception{
		String response = "";
		String query = keywords.replaceAll(" ", "");
		String m_api_key = api_key;
		String m_url = "http://api.tumblr.com/v2/tagged?tag="+query+"&api_key="+api_key+"&limit=" + count;
		URL apicall = new URL(m_url);
		URLConnection t_ac = apicall.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        t_ac.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response+=inputLine;
        in.close();
		tags = tparser(response, old_tags);	
		return tags;

	}
}