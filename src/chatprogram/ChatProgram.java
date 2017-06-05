package chatprogram;

import chatprogram.Client.LoginPage;
import java.time.LocalDateTime;

public class ChatProgram {

    private LoginPage loginPage;

    public static void main(String[] args) {
        ChatProgram chatProgram = new ChatProgram();
        chatProgram.startLoginPage();
    }

    private void startServer() {
        //Starta ny thread
    }

    private void startLoginPage() {
        loginPage = new LoginPage();
        loginPage.start();
    }

}
