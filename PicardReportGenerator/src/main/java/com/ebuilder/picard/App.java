package com.ebuilder.picard;

//import org.json.JSONArray;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class App {

    public static void main(String[] args) {

        int numberOfSources = args.length;
        JSONArray source_list = new JSONArray();

        for (int i = 0; i < numberOfSources; i++) {
            System.out.println(args[i]);
            JsonConverter jc = new JsonConverter();
            JSONArray myArr = jc.converter(args[i]);   //+
            source_list.put(myArr);
        }

        System.out.println(source_list.length());
        HtmlConverter hc = new HtmlConverter();
        hc.converter(source_list);
    }
}
