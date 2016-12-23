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


public class ChatClientImpl extends UnicastRemoteObject implements ChatRoomClientInterface {
    
    private final JTextArea messageBoard;
    private final DefaultListModel loggedUsersMod;
    private final JTextArea privateBoard;
    
    /**
     *
     * @throws RemoteException
     */
    public ChatClientImpl() throws RemoteException{
        messageBoard = new JTextArea();
        loggedUsersMod = new DefaultListModel();
        privateBoard = new JTextArea();
    }
    
    /**
     *
     * @param messages the text area where the general chat is displayed
     * @param userList a list model to display active users
     * @param privateMsg the text area where private messages are displayed
     * @throws RemoteException
     */
    public ChatClientImpl(JTextArea messages, DefaultListModel userList, JTextArea privateMsg) throws RemoteException{
        messageBoard = messages;
        loggedUsersMod = userList;
        privateBoard = privateMsg;
    }
    
    /**
     *
     * @param newMessage the new message to be added to the general chat text area
     * @throws RemoteException
     */
    @Override
    public void newMessageNotification(String newMessage) throws RemoteException {
        messageBoard.append(newMessage + "\n");
    }
    
    /**
     *
     * @param newMessage the new message to be added to the private message text area
     * @throws RemoteException
     */
    @Override
    public void newPrivateMessageNotification(String newMessage) throws RemoteException {
        privateBoard.append(newMessage + "\n");
    }
    
    /**
     *
     * @param newMessage the latest user to log in, a notification informing other users is added to the general chat text area
     * The user is then added to the active users list
     * @throws RemoteException
     */
    @Override
    public void newLoginNotification(String newMessage) throws RemoteException {
        messageBoard.append(newMessage + " has logged in!\n");
        loggedUsersMod.addElement(newMessage);
    }
    
    /**
     *
     * @param newMessage the latest user to log out, a notification informing other users is added to the general chat text area
     * The user is then removed from the active users list
     * @throws RemoteException
     */
    @Override
    public void newLogoutNotification(String newMessage) throws RemoteException {
        messageBoard.append(newMessage + " has logged out!\n");
        loggedUsersMod.removeElement(newMessage);
    }
    
}