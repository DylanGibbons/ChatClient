/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callback_support;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Dylan
 */
//client
public class ChatClientImpl extends UnicastRemoteObject implements ChatRoomClientInterface {
    
    private final JTextArea messageBoard;
    private final DefaultListModel loggedUsersMod;
    private final JTextArea privateBoard;
    
    public ChatClientImpl() throws RemoteException{
        messageBoard = new JTextArea();
        loggedUsersMod = new DefaultListModel();
        privateBoard = new JTextArea();
    }
    
    public ChatClientImpl(JTextArea messages, DefaultListModel userList, JTextArea privateMsg) throws RemoteException{
        messageBoard = messages;
        loggedUsersMod = userList;
        privateBoard = privateMsg;
    }
    
    @Override
    public void newMessageNotification(String newMessage) throws RemoteException {
        messageBoard.append(newMessage + "\n");
    }
    
    @Override
    public void newPrivateMessageNotification(String newMessage) throws RemoteException {
        privateBoard.append(newMessage + "\n");
    }
    
    @Override
    public void newLoginNotification(String newMessage) throws RemoteException {
        messageBoard.append(newMessage + " has logged in!\n");
        loggedUsersMod.addElement(newMessage);
    }
    
    @Override
    public void newLogoutNotification(String newMessage) throws RemoteException {
        messageBoard.append(newMessage + " has logged out!\n");
        loggedUsersMod.removeElement(newMessage);
    }
    
}