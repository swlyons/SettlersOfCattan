/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.communication.ClientCommunicator;
import java.awt.EventQueue;

/**
 *
 * @author ddennis
 */
public class Client {

    public static void main(String[] args) {

        // set the host and port for the client to run on
        ClientCommunicator.getSingleton().setSERVER_HOST(args[0]);
        ClientCommunicator.getSingleton().setSERVER_PORT(
                Integer.parseInt(args[1]));

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Client client = new Client();
            }
        });

    }
}
