package SpamBot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import javax.security.auth.login.LoginException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import static bots.JDAAddon.CJDABuilder.*;

public class Main {

	private JDA Bot;
	private Scanner input = new Scanner(System.in);

	Main(){

		String token = getToken();


		if (Bot == null){
			try {
				Bot = new JDABuilder(AccountType.CLIENT).setToken(token).buildBlocking();
				Bot.setAutoReconnect(true);

			}catch(LoginException | InterruptedException | RateLimitedException e){
				e.printStackTrace();
			}
		}

		TextChannel spam = selector(selector(Bot.getGuilds()).getTextChannels());

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

		while(true){

			spam.sendMessage(msg.toString()).queue();
			try {
				Thread.sleep(200);
			} catch (InterruptedException ignored) {}
		}

	}

	private <T> T selector(List<T> list){
		while(true) {
			T selected;

			for (int i = 0; i < list.size(); i++) {
				System.out.println(i + ": " + list.get(i));
			}

			try{
				selected = list.get(input.nextInt());

			}catch (InputMismatchException e){
				System.err.println("please put in Integer");
				continue;

			}catch (IndexOutOfBoundsException e){
				System.err.println("please put in valid index");
				continue;
			}

			return selected;
		}
	}

	public static void main(String args[]){
		new Main();
	}

}
