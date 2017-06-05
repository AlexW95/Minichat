package chatprogram.Client;

import chatprogram.DatabaseCall;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatPage extends javax.swing.JFrame {

    int userId, lastReadMessageID, conversationId;
    DatabaseCall databaseCall;
    BufferedReader reader;
    ArrayList<String> messagesList;
    String lastMessageSent, currentMessage;
//    int lastMessageSize = 0; //TODO ändra det här namnet.

    public ChatPage(int userId, int conversationId) {
        initComponents();
        this.userId = userId;
        this.conversationId = conversationId;
        databaseCall = new DatabaseCall();
        messagesList = databaseCall.getMessages(conversationId, false);
        addExistingMessages();
        lastMessageSent = getLastMessageSent();
        ListenThread();
    }

    private void addExistingMessages() {
        for (int i = 1; i < messagesList.size(); i++) {
            chatArea.append(messagesList.get(i) + " ");
            if (i % 3 == 0) {
                chatArea.append("\n");
            }
        }
    }

    private String getLastMessageSent() {
        String lastMessage = "";
        messagesList.clear();
        messagesList = databaseCall.getMessages(conversationId, true);
        for (int i = 1; i < messagesList.size(); i++) {
            lastMessage += messagesList.get(i) + " ";
        }
        return lastMessage;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        messageArea = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        exitChatButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        exitChatButton.setText("Exit Chatroom");
        exitChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitChatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageArea, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(exitChatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messageArea, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(exitChatButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        //Här ska jag skicka mitt meddelande till databasen samt köra metoden för att uppdatera textarean.
        lastMessageSent = databaseCall.addMessage(messageArea.getText(), userId, (LocalTime.now().toString()), conversationId);
        messageArea.setText("");
    }//GEN-LAST:event_sendButtonActionPerformed

    private void exitChatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitChatButtonActionPerformed
        ChatRoomListPage chatRoomPage = new ChatRoomListPage(userId);
        chatRoomPage.start(userId);
        this.setVisible(false);
    }//GEN-LAST:event_exitChatButtonActionPerformed

    public void start(int userId, int conversationId) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatPage(userId, conversationId).setVisible(true);
            }
        });
    }

    public void ListenThread() {
        Timer timer = new Timer();
        timer.schedule(new IncomingReader(), 0, 1000);
    }

    public class IncomingReader extends TimerTask implements Runnable {

        String currentMessage = "";

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
                messagesList.clear();
                messagesList = databaseCall.getMessages(conversationId, true);
                currentMessage = "";
                for (int i = 1; i < messagesList.size(); i++) {
                    currentMessage += messagesList.get(i) + " ";
                }
                if (!currentMessage.equals(lastMessageSent)) {
                    chatArea.append(currentMessage);
                    chatArea.append("\n");
                    currentMessage = "";
                    lastMessageSent = getLastMessageSent();
                } else {
                    chatArea.append("");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatArea;
    private javax.swing.JButton exitChatButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField messageArea;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
