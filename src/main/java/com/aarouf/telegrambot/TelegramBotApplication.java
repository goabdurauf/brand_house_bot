package com.aarouf.telegrambot;

import io.github.natanimn.telebof.BotClient;
import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.CallbackHandler;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.enums.MessageType;
import io.github.natanimn.telebof.enums.ParseMode;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardButton;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardMarkup;
import io.github.natanimn.telebof.types.keyboard.ReplyKeyboardMarkup;
import io.github.natanimn.telebof.types.updates.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegramBotApplication {
    public static String getBotToken() {
        return "8460314845:AAH0mojwMvyKOCMnnPef4sE_HfH-4vaqWRY";
    }
    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
        final BotClient bot = new BotClient(getBotToken());
        bot.addHandler(new TelegramBotApplication());
        bot.startPolling(); // Start the bot
    }

    @MessageHandler(commands = "start")
    private void start(BotContext ctx, Message message){
        // Ask for the user's name
        ctx.sendMessage(message.from.id, "Hey! What is your name?").exec();

        // Set the user's state to "name" to indicate we're expecting their name next
        ctx.setState(message.from.id, "name");
    }

    @MessageHandler(texts = {"ID", "Username", "Language"})
    void text(BotContext context, Message message){
        var user = message.from;
        String response;

        switch (message.text) {
            case "ID":
                response = String.format("<b>Your ID is:</b> <code>%d</code>", user.id);
                break;

            case "Username":
                if (user.username == null) {
                    response = "<i>You don't have a username set in your Telegram profile.</i>";
                } else {
                    response = String.format("<b>Your username is:</b> @%s", user.username);
                }
                break;

            case "Language":
                String language = (user.language_code != null) ? user.language_code : "not specified";
                response = String.format("<b>Your language code is:</b> %s", language);
                break;

            default:
                response = "Unknown option selected.";
        }

        context.sendMessage(message.chat.id, response)
                .parseMode(ParseMode.HTML)
                .exec();
    }


    @MessageHandler(type = MessageType.TEXT, priority = 1)
    void echo(BotContext context, Message message){
        context.sendMessage(message.chat.id, "You said: " + message.text).exec();
    }

}
