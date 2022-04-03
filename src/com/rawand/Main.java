package com.rawand;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UTFDataFormatException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class Main {

    private static HttpURLConnection connection;

    // going to be calling https://jsonplaceholder.typicode.com/
    public static void main(String[] args) {
/*        //method 1 - java.net.httpurlconnection pre java 11
        BufferedReader bufferedReader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/albums");
            connection = (HttpURLConnection)url.openConnection();

            //request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            System.out.println(status);

            if(status > 299){
                bufferedReader = new BufferedReader((new InputStreamReader(connection.getErrorStream())));
                while((line = bufferedReader.readLine()) != null){
                    responseContent.append(line);
                }
                bufferedReader.close();
            } else{
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = bufferedReader.readLine()) !=null){
                    responseContent.append(line);
                }
            bufferedReader.close();
            }
            System.out.println(responseContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }*/

        //method 2 - java 11 - java.net.http.HTTPClient api itself handles asyncros operation

        //set up client and request

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://jsonplaceholder.typicode.com/albums")).build();

        //send async request, get response, then apply getbody method to HTTP response, the use that body to print out
        client.sendAsync(request,HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(Main::parse).join();

        //happens in background doesn't block main thread
    }
    public static void parse(String responseBody){
        JSONArray albums = new JSONArray(responseBody);
        for (int i = 0; i < albums.length(); i++) {
            System.out.println(albums.getJSONObject(i).get("title"));
        }
    }
}
