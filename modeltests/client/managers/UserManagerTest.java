package client.managers;

import static org.junit.Assert.*;
import client.managers.UserManager;

import org.junit.Test;

public class UserManagerTest {

	@Test
	public void testCanLogin() {
		String goodUsername = "Jamie";
		String badUsername = "Jammiie";
		String goodPassword = "password";
		String badPassword = "";
		
		UserManager target = new UserManager(); //Set up manager with testing proxy
		assertTrue(target.authenticateUser(goodUsername, goodPassword));
		assertFalse(target.authenticateUser(badUsername, goodPassword));
		assertFalse(target.authenticateUser(goodUsername, badPassword));
		assertFalse(target.authenticateUser(badUsername, badPassword));
	}

	@Test
	public void testCanRegister() {
		String newUsername = "Danny";
		String usedUsername = "Jamie";
		String newPassword = "helloWarld_-";
		String badPassword = "";
		String goodConfirmationPassword = "helloWarld_-";
		String badConfirmationPassword = "";
		
		UserManager target = new UserManager(); //Set up manager with testing proxy
		assertTrue(target.createUser(newUsername, newPassword, goodConfirmationPassword));
		assertFalse(target.createUser(newUsername, newPassword, badConfirmationPassword));
		assertFalse(target.createUser(usedUsername, newPassword, goodConfirmationPassword));
		assertFalse(target.createUser(newUsername, badPassword, badConfirmationPassword));
	}
}
