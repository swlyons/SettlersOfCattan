/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.main.Catan;
import java.awt.EventQueue;

/**
 *
 * @author ddennis
 */
public class Client {

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Client client = new Client();
                new Catan();
            }
        });

    }
}
