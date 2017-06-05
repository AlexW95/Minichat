package chatprogram.Client;

import chatprogram.DatabaseCall;
import java.awt.Label;
import java.io.BufferedReader;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;

public class ChatRoomListPage extends javax.swing.JFrame {

    int userId, lastReadMessageID, conversationID;
    DatabaseCall databaseCall;
    BufferedReader reader;
    ArrayList<String> messagesList;
    String lastMessageSent, currentMessage;

    public ChatRoomListPage(int userId) {
        initComponents();
        this.userId = userId;
        databaseCall = new DatabaseCall();
        setChatroomList();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        chatRoomList = new javax.swing.JList<>();
        joinRoomButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(743, 642));

        chatRoomList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(chatRoomList);

        joinRoomButton.setText("Join room");
        joinRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinRoomButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(350, Short.MAX_VALUE)
                .addComponent(joinRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(348, 348, 348))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(joinRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void joinRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinRoomButtonActionPerformed
        //Hämta valda rummets id och gå med skicka med det till chattsidan
        conversationID = chatRoomList.getSelectedIndex() + 1;
        ChatPage chatClient = new ChatPage(userId, conversationID);
        chatClient.start(userId, conversationID);
        this.setVisible(false);
    }//GEN-LAST:event_joinRoomButtonActionPerformed

    public void setChatroomList() {
        ArrayList<Integer> chatRoomsList;
        chatRoomsList = databaseCall.getChatRooms();
        ArrayList<String> strings = new ArrayList<>();
        for (int cId = 0; cId < chatRoomsList.size(); cId++) {
            strings.add("" + chatRoomsList.get(cId));
        }
        chatRoomList.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                return strings.size();
            }

            public String getElementAt(int i) {
                return "Chatroom: " + strings.get(i);
            }
        });
    }

    public void start(int userId) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatRoomListPage(userId).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> chatRoomList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton joinRoomButton;
    // End of variables declaration//GEN-END:variables
}
