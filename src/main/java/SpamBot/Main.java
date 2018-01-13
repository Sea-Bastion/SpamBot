package SpamBot;

import bots.JDAAddon.CJDA;
import bots.JDAAddon.CJDABuilder;
import bots.JDAAddon.Input;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static bots.JDAAddon.CJDABuilder.getToken;

public class Main extends Application implements Runnable {

	private CJDA Bot;
	private Input BotIn;
	@FXML
	TextArea input;
	@FXML
	TextArea view;

	public Main(){
		String token = getToken();


		while (Bot == null) {
			try {
				Bot = new CJDABuilder(AccountType.CLIENT).addMessageHandlers(this::out).setToken(token).buildBlocking();
				Bot.setAutoReconnect(true);
				Bot.addEventListener();

			} catch (LoginException | InterruptedException | RateLimitedException e) {
				e.printStackTrace();
			}
		}

		BotIn = Bot.getInput();
	}

	public void start(Stage stage){

		try {
			FXMLLoader fxml = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
			fxml.setController(this);
			stage.setScene(fxml.load());
			stage.show();
			stage.setOnCloseRequest(e -> System.exit(0));
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();

		}

		new Thread(this).start();

	}

	private void out(String msg){
		view.appendText(msg + System.lineSeparator());
		view.setScrollTop(Double.MAX_VALUE);
	}

	private String in(){
		synchronized (input){
			try {
				input.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String msg = input.getText();
		while (msg.endsWith("\n"))
			msg = msg.substring(0, msg.length()-1);
		input.clear();
		return msg;
	}

	public void Key(KeyEvent event){
		if(event.getCode().equals(KeyCode.ENTER) && !input.getText().isEmpty()){
			synchronized (input){
				input.notifyAll();
			}
		}
	}

	public static void main(String args[]){
		launch(args);
	}

	@Override
	public void run() {
		BotIn.send("/channels");
		BotIn.send(in());
		BotIn.send(in());

		try {
			Thread.sleep(100);
		} catch (InterruptedException ignored) {
		}

		MessageChannel spam = BotIn.getSelectedChannel();

		view.appendText("message");
		view.setScrollTop(Double.MAX_VALUE);
		String inputStr;
		StringBuilder msg = new StringBuilder();


		while (true) {
			inputStr = in();
			if (!inputStr.isEmpty()) break;
		}

		while ((msg.length() + inputStr.length() + 1) <= 2000) {
			msg.append(inputStr).append("\n");
		}

		//noinspection InfiniteLoopStatement
		while (true) {

			spam.sendMessage(msg.toString()).queue();
			try {
				Thread.sleep(200);
			} catch (InterruptedException ignored) {
			}
		}

	}
}
