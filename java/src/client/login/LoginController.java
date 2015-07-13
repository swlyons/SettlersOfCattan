package client.login;

import client.base.*;
import client.managers.UserManager;
import client.misc.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

	private IMessageView messageView;
	private IAction loginAction;
	
	/**
	 * LoginController constructor
	 * 
	 * @param view Login view
	 * @param messageView Message view (used to display error messages that occur during the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {

		super(view);
		
		this.messageView = messageView;
	}
	
	public ILoginView getLoginView() {
		
		return (ILoginView)super.getView();
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	
	/**
	 * Sets the action to be executed when the user logs in
	 * 
	 * @param value The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		
		loginAction = value;
	}
	
	/**
	 * Returns the action to be executed when the user logs in
	 * 
	 * @return The action to be executed when the user logs in
	 */
	public IAction getLoginAction() {
		
		return loginAction;
	}

	@Override
	public void start() {
		
		getLoginView().showModal();
	}

	@Override
	public void signIn() {
		UserManager userManager = new UserManager();
		String username = getLoginView().getLoginUsername();
		String password = getLoginView().getLoginPassword();
		boolean sucessful = userManager.authenticateUser(username, password);

		// If log in succeeded
		if (sucessful) {
			getLoginView().closeModal();
			loginAction.execute();
		}
	}

	@Override
	public void register() {
		UserManager userManager = new UserManager();
		String username = getLoginView().getRegisterUsername();
		String password = getLoginView().getRegisterPassword();
		String passConf = getLoginView().getRegisterPasswordRepeat();
		boolean sucessful = userManager.createUser(username, password, passConf);
		
		// If register succeeded
		if (sucessful) {
			getLoginView().closeModal();
			loginAction.execute();
		}
	}

}

