package SpamBot;

import bots.JDAAddon.CJDA;
import bots.JDAAddon.CJDABuilder;
import bots.JDAAddon.Input;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static bots.JDAAddon.CJDABuilder.getToken;

public class Main {

	private CJDA Bot;
	private Scanner input = new Scanner(System.in);

	private Main(){

		String token = getToken();


		while (Bot == null){
			try {
				Bot = new CJDABuilder(AccountType.CLIENT).addMessageHandlers(System.out::println).setToken(token).buildBlocking();
				Bot.setAutoReconnect(true);

			}catch(LoginException | InterruptedException | RateLimitedException e){
				e.printStackTrace();
			}
		}


		Input BotIn = Bot.getInput();
		BotIn.send("/channels");
		BotIn.send(input.nextLine());
		BotIn.send(input.nextLine());

		try {
			Thread.sleep(100);
		} catch (InterruptedException ignored) {}

		MessageChannel spam = BotIn.getSelectedChannel();

		System.out.println("\n\ninput message");
		String inputStr;
		StringBuilder msg = new StringBuilder();



		while(true) {
			inputStr = input.nextLine();
			if (!inputStr.isEmpty()) break;
		}

		while((msg.length() + inputStr.length() + 1)<= 2000){
			msg.append(inputStr + "\n");
		}

		//noinspection InfiniteLoopStatement
		while(true){

			spam.sendMessage().queue();
			try {
				Thread.sleep(200);
			} catch (InterruptedException ignored) {}
		}

	}

	public static void main(String args[]){
		new Main();
	}

}
