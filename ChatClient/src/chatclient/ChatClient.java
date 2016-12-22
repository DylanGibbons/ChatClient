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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Dylan
 */
public class ChatClient extends javax.swing.JFrame {
    private static ChatRoomClientInterface thisClient = null;
    private static LogInDialog logInDialog;
    private static RegisterDialog registerDialog;
    private static ChatRoomInterface chatRoomService = null;
    private static User loggedInUser = null;
    private static String privateRecipient = null;
    private static DefaultListModel activeUsersMod;
    private static Message chatMessage;
    private static JTextArea messageBoard;
    
    /**
     * Creates new form ChatClient
     */
    public ChatClient() {
        this.setVisible(false);
        this.getContentPane().setBackground(Color.black);
        initComponents();
        activeUsersMod = new DefaultListModel();
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
        activeUsersList.setModel(activeUsersMod);
        
        setResizable(false);
        messagesTxtArea.setEditable(false);
        privateTxtArea.setEditable(false);
        privateTxtArchive.setEditable(false);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    chatRoomService.logout(loggedInUser);
                    chatRoomService.unregisterForCallback(thisClient);
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                loggedInUser = null;
                chatRoomService = null;
                thisClient = null;
                System.exit(0);
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
        privateRecipientTxtBox = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        messagesTxtArea.setColumns(20);
        messagesTxtArea.setRows(5);
        jScrollPane1.setViewportView(messagesTxtArea);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        usernameLabel.setForeground(new java.awt.Color(204, 204, 0));
        usernameLabel.setText("Logged In As:");

        usernameTxt.setForeground(new java.awt.Color(204, 204, 0));

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

        privateChatBtn.setText("Private Send");
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
        jLabel3.setText("Live Private Chat:");

        jLabel4.setForeground(new java.awt.Color(204, 204, 0));
        jLabel4.setText("Private Chat History:");

        jLabel5.setFont(new java.awt.Font("sansserif", 0, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 102, 0));
        jLabel5.setText("Phil and Dyls Chat-o-Ramma");

        jLabel6.setForeground(new java.awt.Color(204, 204, 0));
        jLabel6.setText("Enter offline recipient:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(privateChatBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(privateTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(usernameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usernameTxt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addGap(85, 85, 85)
                                .addComponent(logOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(66, 66, 66)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(privateRecipientTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addComponent(statusLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameLabel)
                            .addComponent(usernameTxt)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(logOutButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(privateRecipientTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(privateChatBtn, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(messageTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sendButton)
                        .addComponent(privateTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutButtonActionPerformed
        dispose();
        try {
            chatRoomService.logout(loggedInUser);
            chatRoomService.unregisterForCallback(thisClient);
            
        } catch (RemoteException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        loggedInUser = null;
        chatRoomService = null;
        thisClient = null;
        new ChatClient();
    }//GEN-LAST:event_logOutButtonActionPerformed

    private void privateChatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_privateChatBtnActionPerformed
        if (!privateTxtBox.getText().equals("")) {
        
            if (!privateRecipientTxtBox.getText().equals("") || privateRecipientTxtBox != null) {
                privateRecipient = activeUsersList.getSelectedValue().toString();
            } else {
                privateRecipient = privateRecipientTxtBox.getText();
            }

            PrivateMessage pm = new PrivateMessage(loggedInUser.getUsername(), privateTxtBox.getText(), privateRecipient);
            try {
                chatRoomService.addPrivateMessage(pm);
            } catch (RemoteException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            statusLbl.setText("You must enter a private message to send!");
        }
    }//GEN-LAST:event_privateChatBtnActionPerformed

    private void activeUsersListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_activeUsersListValueChanged

    }//GEN-LAST:event_activeUsersListValueChanged

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
   
    /* Log in interface to allow user to log in and assign value to loggedInUser variable from database */
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

            LogInButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chatRoomService != null) {
                        try {
                            System.out.println("1");
                            String username = usernameTxtBox.getText();
                            String password = String.valueOf(passwordTxtBox.getPassword());
                            User u = new User(username, password);
                            System.out.println(u.toString());
                            System.out.println("2");
                            if (chatRoomService.login(u)) {
                                System.out.println("chatroomservice login u");
                                loggedInUser = u;
                                usernameTxt.setText(loggedInUser.getUsername());
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
    
    class RegisterDialog extends JDialog {
        private final JLabel usernameLbl = new JLabel("Username");
        private final JLabel passwordLbl = new JLabel("Password");
        private final JLabel confirmPasswordLbl = new JLabel("Confirm Password");
        
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
            
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registerDialog.dispose();
                    logInDialog.setVisible(true);
                }
            });
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton logOutButton;
    private javax.swing.JTextField messageTxtBox;
    private javax.swing.JTextArea messagesTxtArea;
    private javax.swing.JButton privateChatBtn;
    private javax.swing.JTextField privateRecipientTxtBox;
    private javax.swing.JTextArea privateTxtArchive;
    private javax.swing.JTextArea privateTxtArea;
    private javax.swing.JTextField privateTxtBox;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel usernameTxt;
    // End of variables declaration//GEN-END:variables
}