/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callback_support;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Dylan
 */
public class ChatClientImpl extends UnicastRemoteObject implements ChatRoomClientInterface {
    
    private final JTextArea messageBoard;
    private final JTextArea loggedUsers;

    
    public ChatClientImpl() throws RemoteException{
        messageBoard = new JTextArea();
        loggedUsers = new JTextArea();
    }
    
    public ChatClientImpl(JTextArea messages, JTextArea userList) throws RemoteException{
        messageBoard = messages;
        loggedUsers = userList;
    }
    
    @Override
    public void newMessageNotification(String newMessage) throws RemoteException {
        messageBoard.append(newMessage + "\n");
    }
    
    @Override
    public void newLoginNotification(String newMessage) throws RemoteException {
        loggedUsers.append(newMessage);
    }
    
}