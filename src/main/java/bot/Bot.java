package bot;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.jibble.pircbot.PircBot;
import org.json.JSONObject;
import sun.misc.Regexp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class Bot extends PircBot {
    public Bot(String name) {
        setName(name);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostName, String message) {
        super.onMessage(channel, sender, login, hostName, message);
        if(!message.substring(0, 3).equals("ud ")){
            return;
        }
        message = message.substring(3, message.length());
        System.out.println(message);
        HttpClient client = new DefaultHttpClient();
        try {
            final HttpGet request = new HttpGet("http://api.urbandictionary.com/v0/define?term=" + URLEncoder.encode(message, "UTF-8"));
            HttpResponse response = client.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            String completeLine = "";
            while((line = reader.readLine()) != null){
                completeLine += line;
            }
            JSONObject json = new JSONObject(completeLine);
            ArrayList<String> definitions = new ArrayList<String>();
            if(json.getString("result_type").equals("no_results")){
                sendMessage(channel, String.format("No definition for the word %s found.", message));
                return;
            }

            sendMessage(channel, "Definitions:");
            for(String definition :json.getJSONArray("list").getJSONObject(0).getString("definition").split("\n")){
              sendMessage(channel, "  " + definition);
            }
            sendMessage(channel, "Examples:");
            for(String example: json.getJSONArray("list").getJSONObject(0).getString("example").split("\n")){
                sendMessage(channel, "  " + example);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
