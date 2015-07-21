/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.login.LoginController;

/**
 *
 * @author ddennis
 */
public class LoginState extends State {

    @Override
    public void doAction(Controller controller) {
        LoginController loginController = (LoginController) controller;
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "LoginState{" + '}';
    }
}
