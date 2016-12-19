/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callback_support;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Dylan
 */
public class ChatClientImpl extends UnicastRemoteObject implements ChatRoomClientInterface {
    
    public ChatClientImpl() throws RemoteException{
    }
    
    @Override
    public void newMessageNotification(String newMessage) throws RemoteException {
        System.out.println(newMessage);
    }
    
}