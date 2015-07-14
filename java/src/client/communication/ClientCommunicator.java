/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import client.ClientException;
import client.data.Game;
import client.data.GameInfo;
import client.managers.GameManager;
import client.proxy.BuyDevCard;
import client.proxy.Command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * An example of a client communicator we might use for CS 340. It is not the
 * most general of ClientCommunicators. For instance, it does allow for the
 * passing of parameters in the URL of a GET method. It expects the content
 * length of a response to be 0 or -1.
 *
 *
 * @author De-Wayne Dennis (added ability to pass parameters with a get request
 * IN THE URL!!!)
 */
public class ClientCommunicator {

    // Class Methods
    /**
     * Used to implement the getSingleton method of the singleton pattern.
     *
     * @return an ClientCommunicator
     */
    public static ClientCommunicator getSingleton() {
        if (singleton == null) {
            singleton = new ClientCommunicator();
        }
        return singleton;
    }
    
    private GameManager gm;
    
    public GameManager getGameManager() {
        if(gm == null){
            gm = new GameManager();
        }
    	return gm;
    }

    // Constructors
    /**
     * Default Constructor. Used only to create the singleton.
     */
    private ClientCommunicator() {
        model = new GsonBuilder().create();
        cookies = new HashMap<>();
    }

    // Queries
    /**
     * The Http Get Method. It sends a request to a server and gets a response.
     *
     * @param location the location of the file to download.
     * @return the response generated during an http communication. The response
     * object is user defined and not part the java api.
     * @throws client.ClientException
     */
    public HttpURLResponse download(String location) throws ClientException {
        assert location != null && location.length() > 0;

        HttpURLResponse result = new HttpURLResponse();
        try {
            URL url = new URL(URL_PREFIX + "/" + location);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod(HTTP_GET);
            connection.connect();
            result.setResponseCode(connection.getResponseCode());
            result.setResponseLength(connection.getContentLength());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result.setResponseBody(IOUtils.toByteArray(connection.getInputStream()));
            } else {
                throw new ClientException(String.format(
                        "doGet failed: %s (http code %d)", location,
                        connection.getResponseCode()));
            }
        } catch (IOException e) {
            throw new ClientException(String.format("doGet failed: %s",
                    e.getMessage()), e);
        }
        return result;
    }

    /**
     * The Http Get Method. It sends a request to a server WITH PARAMETERS and
     * gets a response.
     *
     * @param commandName the name mapped to the corresponding handler in the
     * server on the other side.
     * @param params the parameters pass for the specified web call
     * @param playerID
     * @return the response generated during an http communication. The response
     * object is user defined and not part the java api.
     * @throws client.ClientException
     */
    public HttpURLResponse doGet(String commandName, String params, int playerID)
            throws ClientException {

        assert commandName != null && commandName.length() > 0;

        HttpURLResponse result = new HttpURLResponse();

        try {
            URL url = new URL(URL_PREFIX + "/" + commandName + params);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod(HTTP_GET);
            //set cookie
            if (playerID > -1) {
                if(playerIdThisOne!=-1){
                    playerID = playerIdThisOne;
                }                
                connection.setRequestProperty("Cookie", cookies.get(playerID));
            }
            connection.connect();

            result.setResponseCode(connection.getResponseCode());
            result.setResponseLength(connection.getContentLength());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (connection.getContentLength() != -1) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String content = br.readLine();
                    //only games/list
                    if (commandName.equals("games/list")) {
//                    if (commandName.equals("games/list") || commandName.equals("game/commands")) {
                        Type listType = new TypeToken<ArrayList<GameInfo>>() {
                        }.getType();
                        result.setResponseBody(model.fromJson(content, listType));
                    } else if (commandName.equals("game/commands")){
                        Type listType = new TypeToken<ArrayList<Game>>() {
                        }.getType();
                        result.setResponseBody(model.fromJson(content, listType));
                    } else if (commandName.equals("game/listAI")) {
                        Type listType = new TypeToken<ArrayList<String>>() {
                        }.getType();
                        result.setResponseBody(model.fromJson(content, listType));
                    }else {
                        if (content.equals("\"true\"")) {
                            result.setResponseBody(new Game(content));
                        } else {
                            result.setResponseBody(model.fromJson(content, GameInfo.class));
                        }
                    }
                }
            } else {
                 String content = "Failed"; //br.readLine();
                    result.setResponseBody(content);
            }
        } catch (IOException e) {
            throw new ClientException(String.format("doGet failed: %s",
                    e.getMessage()), e);
        }
        return result;
    }

    public HttpURLResponse doPost(String commandName, Object postData, int playerID)
            throws ClientException {
        assert commandName != null;
        assert postData != null;

        HttpURLResponse result = new HttpURLResponse();

        try {
            URL url = new URL(URL_PREFIX + "/" + commandName);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod(HTTP_POST);

            //set cookie by player id
            if (playerID > -1) {
                if(playerIdThisOne!=-1){
                    playerID = playerIdThisOne;
                }
                connection.setRequestProperty("Cookie", cookies.get(playerID));
            }
            connection.setDoOutput(true);
            connection.connect();

            String data = model.toJson(postData);
            
            connection.getOutputStream().write(data.getBytes());

            connection.getOutputStream().close();

            result.setResponseCode(connection.getResponseCode());
            result.setResponseLength(connection.getContentLength());
            //if(commandName.equals("moves/Monopoly"))
                //System.out.println(data);
            //System.out.println(cookies.get(playerID))
            //get the content
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                if (connection.getContentLength() != -1) {

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String content = br.readLine();
                    if (commandName.contains("user")) {
                        result.setResponseBody(content);
                        if (commandName.equals("user/login") && content.equals("Success")) {

                            String userCookie = connection.getHeaderField("Set-Cookie").split(";")[0];
                            String decodedCookie = URLDecoder.decode(userCookie.split("=")[1], "UTF-8");
                            playerIdThisOne = model.fromJson(decodedCookie, CookieModel.class).getPlayerID();
                            name = model.fromJson(decodedCookie, CookieModel.class).getName();
                            cookies.put(playerIdThisOne, userCookie + ((cookies.get(playerIdThisOne) != null) ? "; " + cookies.get(playerIdThisOne) : ""));
                        }
                    } else if (commandName.equals("games/join")) {
                        result.setResponseBody(content);
                        if (content.equals("Success")) {
                            String gameCookie = connection.getHeaderField("Set-Cookie").split(";")[0];
                            //cookies.put(playerID, "");
                            cookies.put(playerID, ((cookies.get(playerID) != null) ? cookies.get(playerID) + "; " : "") + gameCookie);
                        }
                    } else if(commandName.equals("games/create")){
                        result.setResponseBody(model.fromJson(content, GameInfo.class));
                    } else if(commandName.equals("game/addAI")){
                        result.setResponseBody(content);
                    } else {
                        if (commandName.equals("games/save") || commandName.equals("games/load")) {
                            result.setResponseBody(content);
                        }
                        result.setResponseBody(model.fromJson(content, GameInfo.class));
                    }
                }
            } else {
                String content = "Failed"; //br.readLine();
                result.setResponseBody(content);
                /*if (commandName.contains("user")) {
                    
                 } else {
                 throw new ClientException(String.format(
                 "doPost failed: %s (http code %d)", commandName,
                 connection.getResponseCode()));
                 }*/
            }
        } catch (IOException e) {
            throw new ClientException(String.format("doPost failed: %s",
                    e.getMessage()), e);
        }
        return result;
    }
    
     public Map<Integer, String> getCookies() {
        return cookies;
    }
     
    // Auxiliary Constants, Attributes, and Methods
    private static String SERVER_HOST = "localhost";
    private static int SERVER_PORT = 8081;
    private static String URL_PREFIX = "http://" + SERVER_HOST + ":"
            + SERVER_PORT;
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";

    private Gson model;
    private Map<Integer, String> cookies;

    private Integer playerIdThisOne = -1;
    private String name = "";
    
    public Integer getPlayerId(){
        return playerIdThisOne;
    }
    
    public String getName(){
        return name;
    }

   

    // Singleton Instance
    private static ClientCommunicator singleton = null;

    /* Methods for setting and getting the server HOST and PORT */
    public String getSERVER_HOST() {
        return SERVER_HOST;
    }

    public void setSERVER_HOST(String sERVER_HOST) {
        SERVER_HOST = sERVER_HOST;
        URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
    }

    public int getSERVER_PORT() {
        return SERVER_PORT;
    }

    public void setSERVER_PORT(int sERVER_PORT) {
        SERVER_PORT = sERVER_PORT;
        URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
    }
}
