/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import client.ClientException;
import client.data.Game;
import client.data.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
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
 * @author De-Wayne Dennis (added ability to pass parameters with a get request IN THE URL!!!)
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
	 * @param location
	 *            the location of the file to download.
	 * @return the response generated during an http communication. The response
	 *         object is user defined and not part the java api.
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
			System.out.println("CC: " + url.toString() + " Response: " + connection.getResponseCode());
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
	 * @param commandName
	 *            the name mapped to the corresponding handler in the server on
	 *            the other side.
	 * @param params
	 *            the parameters pass for the specified web call
	 * @return the response generated during an http communication. The response
	 *         object is user defined and not part the java api.
         * @throws client.ClientException
	 */
	public HttpURLResponse doGet(String commandName, String params)
			throws ClientException {

		assert commandName != null && commandName.length() > 0;

		HttpURLResponse result = new HttpURLResponse();

		try {
			URL url = new URL(URL_PREFIX + "/" + commandName + "/" + params);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HTTP_GET);
			connection.connect();

			result.setResponseCode(connection.getResponseCode());
			result.setResponseLength(connection.getContentLength());

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				if (connection.getContentLength() == -1) {
                                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                    String content = br.readLine();
                                    //only games/list and game/create have a different model
                                    if(commandName.equals("game/list")){
                                        result.setResponseBody(model.fromJson(content, ListCreate.class));
                                    }
                                    else{
                                        result.setResponseBody(model.fromJson(content, Game.class));
                                    }
                                }
			} else {
				throw new ClientException(String.format(
						"doGet failed: %s (http code %d)", commandName,
						connection.getResponseCode()));
			}
		} catch (IOException e) {
			throw new ClientException(String.format("doGet failed: %s",
					e.getMessage()), e);
		}
		return result;
	}

	public HttpURLResponse doPost(String commandName, Object postData)
			throws ClientException {
		assert commandName != null;
		assert postData != null;
                
		HttpURLResponse result = new HttpURLResponse();

		try {
			URL url = new URL(URL_PREFIX + "/" + commandName);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.connect();
			String data = model.toJson(postData);
                        
                        connection.getOutputStream().write(data.getBytes());
                        
			connection.getOutputStream().close();
                         
			result.setResponseCode(connection.getResponseCode());
			result.setResponseLength(connection.getContentLength());
                        
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				if (connection.getContentLength() != -1) {
                                   
                                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                    String content = br.readLine();
                                    if(commandName.equals("game/create")){
                                        result.setResponseBody(model.fromJson(content, ListCreate.class));
                                    }
                                    else if(commandName.contains("user")){
                                        result.setResponseBody(content);
                                        if(commandName.equals("user/login") && content.equals("Success"))
                                        {
                                            String cookie = connection.getHeaderField("Set-Cookie").split(";")[0].split("=")[1];
                                            String decodedCookie = URLDecoder.decode(cookie, "UTF-8");
                                            int id = model.fromJson(decodedCookie, Cookie.class).playerID;
                                            cookies.put(id, cookie);
                                        }  
                                    }
                                    else{
                                        result.setResponseBody(model.fromJson(content, Game.class));
                                    }
				}
			} else {
                                if(commandName.contains("user")){
                                        result.setResponseBody("Fail");
                                }
                                else{
                                    throw new ClientException(String.format(
						"doPost failed: %s (http code %d)", commandName,
						connection.getResponseCode()));
                                }
			}
		} catch (IOException e) {
			throw new ClientException(String.format("doPost failed: %s",
					e.getMessage()), e);
		}
		return result;
	}

	// Auxiliary Constants, Attributes, and Methods
	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 8081;
	private static String URL_PREFIX = "http://" + SERVER_HOST + ":"
			+ SERVER_PORT;
	private static final String HTTP_GET = "GET";
	private static final String HTTP_POST = "POST";

	private Gson model;
        private Map<Integer,String> cookies;
        
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
        
        //response models outside of Game Model
        // games/list or games/create
        public class ListCreate{
            private Game game;
            private Player player;
        }
        
        //cookie model
        public class Cookie{
           private String name;
           private String password;
           private int playerID;
        }
            
        
}
