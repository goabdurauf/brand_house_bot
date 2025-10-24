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
    void start(BotContext context, Message message){

        // Create a reply keyboard with resize option for better mobile appearance
        var keyboard = new ReplyKeyboardMarkup().resizeKeyboard(true);

        // Add buttons to the keyboard (in a single row by default)
        keyboard.add("UZB", "RUS");

        // Send welcome message with the keyboard attached
        context.sendMessage(message.chat.id, "Welcome! Please choose an option:")
                .replyMarkup(keyboard) // Attach the keyboard to the message
                .exec();
    }

    @MessageHandler(texts = {"UZB", "RUS"})
    void text(BotContext context, Message message){
        var user = message.from;
        String response;
        var keyboard = new ReplyKeyboardMarkup().resizeKeyboard(true);

        switch (message.text) {
            case "UZB":
                keyboard.add("LEPKA", "DIZAY_1");
                break;

            case "RUS":
                keyboard.add("LEPKA_RU", "DIZAY_RU");
                break;
            default:
                response = "Unknown option selected.";
        }

        context.sendMessage(message.chat.id, "OPTIONLAR")
                .replyMarkup(keyboard) // Attach the keyboard to the message
                .exec();
    }


//    @MessageHandler(type = MessageType.TEXT, priority = 1)
//    void echo(BotContext context, Message message){
//        context.sendMessage(message.chat.id, "You said: " + message.text).exec();
//    }

}
