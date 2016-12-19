/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import business.Message;
import business.User;
import callback_support.ChatClientImpl;
import callback_support.ChatRoomClientInterface;
import chat_functionality.ChatRoomInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dylan
 */
public class ChatClient {
    public static void main (String [] args) {
        try {
            int portNum = 55555;

            String registryPath = "rmi://localhost:" + portNum;
            String objectLabel = "/chatroomService";

            ChatRoomInterface chatRoomService = (ChatRoomInterface) Naming.lookup(registryPath + objectLabel);

            ChatRoomClientInterface thisClient = new ChatClientImpl();
            chatRoomService.registerForCallback(thisClient);

            Message m = new Message("I'll be back", "Ahnawld");
            boolean added = chatRoomService.addMessage(m);

            System.out.println("Message added? " + added);

            Message newMessage = chatRoomService.getMessage();
            System.out.println(newMessage);

            User u = new User("Craig David", "password");

            System.out.println("Registered: " + chatRoomService.register(u));   
        } catch (NotBoundException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
