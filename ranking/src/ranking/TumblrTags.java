package ranking; /**
 * Created by mac on 14-11-23.
 */

import java.net.*;
import java.io.*;
import java.util.Vector;
import org.json.*;

public class TumblrTags{
    public Vector<String> tags = new Vector<String>();
    private Vector<String> tparser(String response, Vector<String> old_tags) throws Exception{
        JSONObject obj = new JSONObject(response);
        JSONArray responses = obj.getJSONArray("response");
        for(int i=0;i< responses.length(); i++){
            JSONObject cur_response = responses.getJSONObject(i);
            JSONArray t_tags = cur_response.getJSONArray("tags");
            for(int j=0; j<t_tags.length(); j++){
                String tag = t_tags.get(j).toString();
                if(!old_tags.contains(tag)){
                    old_tags.add(tag);
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
