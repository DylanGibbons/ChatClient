/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import business.Message;
import business.PrivateMessage;
import business.User;
import callback_support.ChatClientImpl;
import callback_support.ChatRoomClientInterface;
import chatroom_functionality.ChatRoomInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Dylan
 */

/**
 * 
 * A Client GUI to interact with the server
 */

public class ChatClient extends javax.swing.JFrame {
    private static ChatRoomClientInterface thisClient = null;
    private static LogInDialog logInDialog;
    private static RegisterDialog registerDialog;
    private static ChatRoomInterface chatRoomService = null;
    private static User loggedInUser = null;
    private static String privateRecipient = null;
    private static DefaultListModel activeUsersMod;
    private static boolean archiveHintClicked;
    private static boolean liveHintClicked;

    /**
    * 
    * Constructor for overall chat client gui
    * chatRoomService is initialized, granting the ability to call methods from the server in the client
    * JDialog is created for log in, must be completed before the actual JFrame can be accessed
    * 
    */
    public ChatClient() {
        this.setVisible(false);
        this.getContentPane().setBackground(Color.black);
                
        initComponents();
        activeUsersMod = new DefaultListModel();
        
        archiveHintClicked = false;        
        liveHintClicked = false;
        /**
        * 
        * Tries to establish a connection to the server, on failure a message is displayed through the log in JDialog
        * JDialog is created to make the user log in
        *
        */
        try {
            int portNum = 7777;

            String registryPath = "rmi://localhost:" + portNum;
            String objectLabel = "/chatroomService";

            chatRoomService = (ChatRoomInterface) Naming.lookup(registryPath + objectLabel);

            logInDialog = new LogInDialog(this, true);
            logInDialog.setVisible(true);

            thisClient = new ChatClientImpl(messagesTxtArea, activeUsersMod, privateTxtArea);
            chatRoomService.registerForCallback(loggedInUser.getUsername(), thisClient);
            ArrayList<String> loggedUserList = chatRoomService.getLoggedUsers(loggedInUser.getUsername());
            loggedUserList.stream().forEach(u -> activeUsersMod.addElement(u));
        
        } catch (NotBoundException ex) {
            Logger.getLogger(chatclient.ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(chatclient.ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(chatclient.ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        setResizable(false);
        activeUsersList.setModel(activeUsersMod);
        messagesTxtArea.setEditable(false);
        privateTxtArea.setEditable(false);
        privateTxtArchive.setEditable(false);
        
        /**
        *
        * Listener to handle the event of a window closing
        * Reverts all relevant variables back to null and disconnects from the server
        */
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dispose();
                try {
                    chatRoomService.unregisterForCallback(loggedInUser.getUsername(), thisClient);
                    chatRoomService.logout(loggedInUser);
                    

                } catch (RemoteException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //added Listener so that when a user hits enter the message is sent
        privateTxtBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
              if (e.getKeyCode()==KeyEvent.VK_ENTER){
                 privateChatBtn.doClick();
              }
            }
        });
        //added Listener so that when a user hits enter the message is sent
        messageTxtBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
              if (e.getKeyCode()==KeyEvent.VK_ENTER){
                 sendButton.doClick();
              }
            }
        });
        
        archivePrivateTxtBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
              if (e.getKeyCode()==KeyEvent.VK_ENTER){
                 if(archivePrivateTxtBox.getText() != null && !archivePrivateTxtBox.getText().isEmpty()) {
                     if(archivePrivateTxtBox.getText().equals(loggedInUser.getUsername())) {
                         privateTxtArchive.setText("Please select a user\nother than yourself!");
                         return;
                     }
                    try {
                        if(chatRoomService.isValidUser(archivePrivateTxtBox.getText())) {
                            // fill text area with their messages
                            ArrayList<PrivateMessage> pm = chatRoomService
                                .getPrivateMessageHistory(loggedInUser.getUsername(), archivePrivateTxtBox.getText().trim());

                            if(pm != null && pm.size() > 0) {
                                Collections.sort(pm);
                                StringBuilder sb = new StringBuilder();
                                pm.stream().forEach(msg -> sb.append(PrivateMessage.messageFormat(msg)));
                                privateTxtArchive.setText(sb.toString());
                            } else if(pm == null) {
                                privateTxtArchive.setText("No messages found!\nwhy not send " 
                                        + archivePrivateTxtBox.getText().trim() + "\na new message today?");
                            }
                        } else {
                            showMessageDialog(null, "Username not recognised, please check spelling!");
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
              }
            }
        });
        
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        messagesTxtArea = new javax.swing.JTextArea();
        messageTxtBox = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        usernameLabel = new javax.swing.JLabel();
        usernameTxt = new javax.swing.JLabel();
        logOutButton = new javax.swing.JButton();
        statusLbl = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        activeUsersList = new javax.swing.JList();
        privateChatBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        privateTxtArea = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        privateTxtArchive = new javax.swing.JTextArea();
        privateTxtBox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        livePrivateTxtBox = new javax.swing.JTextField();
        archivePrivateTxtBox = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        messagesTxtArea.setColumns(20);
        messagesTxtArea.setRows(5);
        jScrollPane1.setViewportView(messagesTxtArea);

        sendButton.setText("Send Public");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        usernameLabel.setForeground(new java.awt.Color(204, 204, 0));
        usernameLabel.setText("Logged In As:");

        usernameTxt.setForeground(new java.awt.Color(204, 204, 0));
        usernameTxt.setText("username");

        logOutButton.setText("Log Out");
        logOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutButtonActionPerformed(evt);
            }
        });

        statusLbl.setForeground(new java.awt.Color(255, 0, 0));

        activeUsersList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                activeUsersListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(activeUsersList);

        privateChatBtn.setText("Send Private");
        privateChatBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                privateChatBtnActionPerformed(evt);
            }
        });

        privateTxtArea.setColumns(20);
        privateTxtArea.setRows(5);
        jScrollPane2.setViewportView(privateTxtArea);

        privateTxtArchive.setColumns(20);
        privateTxtArchive.setRows(5);
        jScrollPane4.setViewportView(privateTxtArchive);

        jLabel1.setForeground(new java.awt.Color(204, 204, 0));
        jLabel1.setText("Public Chat:");

        jLabel2.setForeground(new java.awt.Color(204, 204, 0));
        jLabel2.setText("Who's online:");

        jLabel3.setForeground(new java.awt.Color(204, 204, 0));
        jLabel3.setText("Live Private Chat: [Click to view hint]");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(204, 204, 0));
        jLabel4.setText("Private Chat History: [Click to view hint]");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("sansserif", 0, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 102, 0));
        jLabel5.setText("Phil and Dyls Chat-O-Rama");

        livePrivateTxtBox.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(statusLbl)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(usernameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(235, 235, 235))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(messageTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(256, 256, 256)
                                .addComponent(logOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(privateChatBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(privateTxtBox))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3)
                                            .addComponent(livePrivateTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addComponent(archivePrivateTxtBox))))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(usernameLabel)
                        .addComponent(usernameTxt))
                    .addComponent(logOutButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel1))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(archivePrivateTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(livePrivateTxtBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(privateTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendButton)
                            .addComponent(messageTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(privateChatBtn))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLbl)
                .addGap(34, 34, 34))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    *
    * Button to send a message to the general message area for every active user to see 
    * Gets the text from the message text box and checks if it's null
    * If so then an error message is displayed to the user
    * If not then the message is sent to the server to be added to the general messages text area
    *
    */
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        String message = messageTxtBox.getText();
        if (message != null) {
            Message newMessage = new Message(message, loggedInUser.getUsername());
            try {
                chatRoomService.addMessage(newMessage);
                messageTxtBox.setText("");
            } catch (RemoteException ex) {
                Logger.getLogger(chatclient.ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            statusLbl.setText("You must enter a message to send!");
        }
    }//GEN-LAST:event_sendButtonActionPerformed

    /**
    *
    * Button to log out of the gui
    * Disposes of the interface
    * Disconnects from the server using appropriate methods
    * Reverts all relevant variables back to null
    *
    */
    private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutButtonActionPerformed
        dispose();
        try {
            chatRoomService.unregisterForCallback(loggedInUser.getUsername(), thisClient);
            chatRoomService.logout(loggedInUser);
            
            
        } catch (RemoteException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        loggedInUser = null;
        chatRoomService = null;
        thisClient = null;
        new ChatClient();
    }//GEN-LAST:event_logOutButtonActionPerformed

    /**
    *
    * Button to send a private message to a specific user
    * Checks that text from the private text box contains a message
    * If not then an error message is shown to the user
    * If so then checks that the recipient text box is not empty, will use recipient text box over the list of users
    * If nothing is entered into the recipient text box then a message can be sent to the currently selected user
    * A private message is created using the user logged into the client, the message entered into the private text box and the recipient
    * The private message is added to the private message text area
    *
    */
    private void privateChatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_privateChatBtnActionPerformed
        if (!livePrivateTxtBox.getText().isEmpty() && livePrivateTxtBox.getText() != null) {
            privateRecipient = livePrivateTxtBox.getText().trim();
        }
        
        if(privateRecipient != null && !privateRecipient.isEmpty()) {
            if(privateRecipient.equals(loggedInUser.getUsername())) {
                showMessageDialog(null, "Stop trying to talk to yourself!");
                privateTxtBox.setText("");
                return;
            }
            
            try {
                if(chatRoomService.isValidUser(privateRecipient)) {
                    PrivateMessage pm = new PrivateMessage(loggedInUser.getUsername(), privateTxtBox.getText(), privateRecipient);
                    chatRoomService.addPrivateMessage(pm);
                } else {
                    showMessageDialog(null, "Username not recognised, please check spelling!");
                }
                privateTxtBox.setText("");
            } catch (RemoteException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_privateChatBtnActionPerformed

    private void activeUsersListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_activeUsersListValueChanged
        // Get selected value
        // set recipient to selected value name
        if(activeUsersList.getSelectedValue() != null) {
            privateRecipient = activeUsersList.getSelectedValue().toString();
            livePrivateTxtBox.setText(privateRecipient);
            archivePrivateTxtBox.setText(privateRecipient);
        }
        if(privateRecipient != null && !privateRecipient.isEmpty()) {
            try {
                // fill text area with their messages
                ArrayList<PrivateMessage> pm = chatRoomService
                        .getPrivateMessageHistory(loggedInUser.getUsername(), privateRecipient);
                
                if(pm != null) {
                    Collections.sort(pm);
                    StringBuilder sb = new StringBuilder();
                    pm.stream().forEach(msg -> sb.append(PrivateMessage.messageFormat(msg)));
                    privateTxtArchive.setText(sb.toString());
                }
                
                privateTxtBox.requestFocusInWindow();
            } catch (RemoteException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_activeUsersListValueChanged

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        archiveHintClicked ^= true;
        if(archiveHintClicked) {
            jLabel4.setText("Enter username, hit 'Enter' to see history");
        } else {
            jLabel4.setText("Private Chat History: [Click to view hint]");
        }
        
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        liveHintClicked ^= true;
        if(liveHintClicked) {
            jLabel3.setText("Enter username to receive message");
        } else {
            jLabel3.setText("Live Private Chat: [Click to view hint]");
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatClient().setVisible(true);
            }
        });
    }
   
    /**
    *
    * Log in Dialog to allow the user to log into the chat service
    * If log in is successful the JFrame is made visible and the users details are loaded in
    *
    */
    class LogInDialog extends JDialog {
        private final JLabel usernameLbl = new JLabel("Username");
        private final JLabel passwordLbl = new JLabel("Password");

        private final JTextField usernameTxtBox = new JTextField(15);
        private final JPasswordField passwordTxtBox = new JPasswordField();

        private final JButton LogInButton = new JButton("Login");
        private final JButton registerButton = new JButton("Register");

        private final JLabel statusLbl = new JLabel(" ");

        public LogInDialog() {
            this(null, true);
        }

        public LogInDialog(final JFrame parent, boolean modal) {
            super(parent, modal);
            
            this.setTitle("Phil and Dyls Chat-o-Rama");
            
            setResizable(false);

            JPanel p3 = new JPanel(new GridLayout(2, 1));
            p3.add(usernameLbl);
            p3.add(passwordLbl);

            JPanel p4 = new JPanel(new GridLayout(2, 1));
            p4.add(usernameTxtBox);
            p4.add(passwordTxtBox);

            JPanel p1 = new JPanel();
            p1.add(p3);
            p1.add(p4);

            JPanel p2 = new JPanel();
            p2.add(LogInButton);
            p2.add(registerButton);

            JPanel p5 = new JPanel(new BorderLayout());
            p5.add(p2, BorderLayout.CENTER);
            p5.add(statusLbl, BorderLayout.NORTH);
            statusLbl.setForeground(Color.RED);
            statusLbl.setHorizontalAlignment(SwingConstants.CENTER);

            setLayout(new BorderLayout());
            add(p1, BorderLayout.CENTER);
            add(p5, BorderLayout.SOUTH);
            pack();
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            addWindowListener(new WindowAdapter() {  
                @Override
                public void windowClosing(WindowEvent e) {  
                    System.exit(0);  
                }  
            });

            /**
            *
            * Action listener fo the log in button
            * If chatRoomService has not successfully been initialized, an error message is displayed to the user
            * A user object is created out of the users input to the gui and compared to the database
            * If that user is stored on the database, it is logged into the service and the log in dialog is disposed
            * If not, an error message is displayed to the user
            *
            */
            LogInButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chatRoomService != null) {
                        try {
                            String username = usernameTxtBox.getText();
                            String password = String.valueOf(passwordTxtBox.getPassword());
                            User u = new User(username, password);

                            if (chatRoomService.login(u)) {
                                loggedInUser = u;
                                usernameTxt.setText(loggedInUser.getUsername());
                                messagesTxtArea.append("You are logged in!\n");
                                parent.setVisible(true);
                                dispose();
                            } else {
                                statusLbl.setText("Invalid username or password");
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(chatclient.ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to establish a connection to the server");
                    }
                }
            });
            
            /**
            *
            * Button listener that creates a registration JDialog if the button clicked
            *
            */
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    registerDialog = new RegisterDialog(parent, true);
                    registerDialog.setVisible(true);
                }
            });
        }
    }
    
    /**
    *
    * Register dialog to allow users to register to the chat service
    * Upon successful registration the JFrame is made visible and the users details are loaded in
    *
    */
    class RegisterDialog extends JDialog {
        private final JLabel usernameLbl = new JLabel("Username");
        private final JLabel passwordLbl = new JLabel("Password");
        private final JLabel confirmPasswordLbl = new JLabel("Confirm\nPassword");
        
        private final JTextField usernameTxtBox = new JTextField(15);
        private final JPasswordField passwordTxtBox = new JPasswordField();
        private final JPasswordField confirmPasswordTxtBox = new JPasswordField();
        
        private final JButton backButton = new JButton("Back");
        private final JButton confirmButton = new JButton("Confirm");
        
        private final JLabel statusLbl = new JLabel(" ");
        
        private String username;
        private String password;
        private String confirmPassword;
        
        public RegisterDialog() {
            this(null, true);
        }
        
        public RegisterDialog(JFrame parent, boolean modal) {
            super(parent, modal);
            
            this.setTitle("Phil and Dyls Chat-o-Rama");
            
            setResizable(false);
            
            JPanel p3 = new JPanel(new GridLayout(3, 1));
            p3.add(usernameLbl);
            p3.add(passwordLbl);
            p3.add(confirmPasswordLbl);
            
            JPanel p4 = new JPanel(new GridLayout(3, 1));
            p4.add(usernameTxtBox);
            p4.add(passwordTxtBox);
            p4.add(confirmPasswordTxtBox);
            
            JPanel p1 = new JPanel();
            p1.add(p3);
            p1.add(p4);
            
            JPanel p2 = new JPanel();
            p2.add(backButton);
            p2.add(confirmButton);
            
            JPanel p5 = new JPanel(new BorderLayout());
            p5.add(p2, BorderLayout.CENTER);
            p5.add(statusLbl, BorderLayout.NORTH);
            statusLbl.setForeground(Color.RED);
            statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
            
            setLayout(new BorderLayout());
            add(p1, BorderLayout.CENTER);
            add(p5, BorderLayout.SOUTH);
            pack();
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            addWindowListener(new WindowAdapter() {  
                @Override
                public void windowClosing(WindowEvent e) {  
                    System.exit(0);  
                }  
            });
            
            /**
            *
            * Action listener for the back button, when pressed disposes the register dialog and makes log in dialog visible again
            *
            */
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registerDialog.dispose();
                    logInDialog.setVisible(true);
                }
            });
            
            /**
            *
            * Action listener for the confirm button
            * If chatRoomService has not successfully been initialized, an error message is displayed to the user
            * If the entry details meet the criteria of CheckEntryDetails then a new user object is created
            * If the user is successfully added to the database then they are logged into the service
            *
            */
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chatRoomService != null) {
                        username = usernameTxtBox.getText();
                        password = String.valueOf(passwordTxtBox.getPassword());
                        confirmPassword = String.valueOf(confirmPasswordTxtBox.getPassword());
                        if (CheckEntryDetails(username, password, confirmPassword)) {
                            User newUser = new User(username, password);
                            try {
                                if (chatRoomService.register(newUser)) {
                                    loggedInUser = newUser;
                                    usernameTxt.setText(loggedInUser.getUsername());
                                    parent.setVisible(true);
                                    dispose();
                                }
                            } catch (RemoteException ex) {
                                Logger.getLogger(chatclient.ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to establish a connection to the server");
                    }
                }
            });
        }
        
        /**
        *
        * Method that checks if the entry details in the register form conform to the standard required for registration
        * Returns a boolean declaring if so or if not
        *
        */
        public boolean CheckEntryDetails(String username, String password, String confirmPassword) {
            if (username != null || username.length() > 0) {
                if (password.equals(confirmPassword)) {
                    if (password.length() >= 6) {
                        return true;
                    } else {
                        statusLbl.setText("Password must be more than 6 characters long");
                        return false;
                    }
                } else {
                    statusLbl.setText("Passwords do not match");
                }
                return false;
            } else {
                statusLbl.setText("Username cannot be blank");
                return false;
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList activeUsersList;
    private javax.swing.JTextField archivePrivateTxtBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField livePrivateTxtBox;
    private javax.swing.JButton logOutButton;
    private javax.swing.JTextField messageTxtBox;
    private javax.swing.JTextArea messagesTxtArea;
    private javax.swing.JButton privateChatBtn;
    private javax.swing.JTextArea privateTxtArchive;
    private javax.swing.JTextArea privateTxtArea;
    private javax.swing.JTextField privateTxtBox;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel usernameTxt;
    // End of variables declaration//GEN-END:variables
}