import bot.Bot;
import org.jibble.pircbot.IrcException;

import java.io.IOException;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        Bot bot = new Bot("adrian");

        bot.setVerbose(true);
        try {
            bot.connect("sashimiquality.com");
            bot.joinChannel("#test");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }

    }

}
