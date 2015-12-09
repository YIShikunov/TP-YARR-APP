package archon.tp_yarr_app.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GetHTTP {

    public static String streamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    public static JSONObject getResponseByUrl(String url) {
        JSONObject json = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            json = new JSONObject(streamToString(in));
            in.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    public void callWebService(String q){
    }
}
