package pl.dawid.HsNewsfeed;





import pl.dawid.HsNewsfeed.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dawid on 03/06/16.
 */
public class GCMSender {


      public static final String API_KEY = "API TOKEN";
    public static void sendPush(String key, String message){

   try{
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            jData.put("message", message);
            jData.put("body", message);
            // Where to send GCM message.
            jGcmData.put("to", "/topics/"+key);
            // What to send in GCM message.
            jGcmData.put("data", jData);

            // Create connection to send GCM Message request.
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.


       InputStream inputStream = conn.getInputStream();
       String resp = inputStream.toString();
       System.out.println(resp);


   } catch (IOException e) {
            System.out.println("Unable to send GCM message.");
            e.printStackTrace();
        }
    }

}
