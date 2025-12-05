package com.aarouf.telegrambot;

import io.github.natanimn.telebof.BotClient;
import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.CallbackHandler;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.enums.MessageType;
import io.github.natanimn.telebof.enums.ParseMode;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardButton;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardMarkup;
import io.github.natanimn.telebof.types.keyboard.KeyboardButton;
import io.github.natanimn.telebof.types.keyboard.ReplyKeyboardMarkup;
import io.github.natanimn.telebof.types.updates.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class TelegramBotApplication {

    public static String getBotToken() {
        return "1993250215:AAGMycwA0JRqXh5wp3zIeby8Qe3IJuhc-eY";
    }
    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
        final BotClient bot = new BotClient(getBotToken());
        bot.addHandler(new TelegramBotApplication());
        bot.startPolling(); // Start the bot
    }
    Map<Long, List<File>> userImages = new HashMap<>();
    Map<Long, Integer> userPage = new HashMap<>();



    @MessageHandler(commands = "start")
    void start(BotContext context, Message message){

        var keyboard = new ReplyKeyboardMarkup().resizeKeyboard(true);

        keyboard.add(
                "Lepka", "Dizayn"  // Display without slash
        );

        context.sendMessage(message.chat.id, "Welcome! Please choose an option:")
                .replyMarkup(keyboard)
                .exec();
    }

        @MessageHandler(texts = {"Lepka",})
    void lepka(BotContext context, Message message) {
            var keyboard = new ReplyKeyboardMarkup().resizeKeyboard(true);
            keyboard.add(
                    "Karniz", "Moldin", "Bort", "Dekor"
            );
            keyboard.add(
                    "orqaga"
            );
            // SEND THE MESSAGE WITH THE KEYBOARD
            context.sendMessage(message.chat.id, "Choose one:")
                    .replyMarkup(keyboard)
                    .exec();
    }

    // ======= KARNIZ 1st page =======
    @MessageHandler(texts = {"Karniz"})
    void karniz(BotContext context, Message message) {

        long chatId = message.chat.id;

        List<File> images = getKarnizImages();
        userImages.put(chatId, images);
        userPage.put(chatId, 0);

        sendPage(context, chatId);
    }

    @MessageHandler(texts = {"Moldin"})
    void moldin(BotContext context, Message message) {

        long chatId = message.chat.id;

        List<File> images = getMoldingImages();
        userImages.put(chatId, images);
        userPage.put(chatId, 0);

        sendPage(context, chatId);
    }

    // Send a page of images
    void sendPage(BotContext context, long chatId) {
        List<File> images = userImages.get(chatId);
        int page = userPage.get(chatId);

        int from = page * 5;
        int to = Math.min(from + 5, images.size());

        // Send 5 images
        for (int i = from; i < to; i++) {
            context.sendPhoto(chatId, images.get(i))
                    .caption(images.get(i).getName().replaceAll(".png",""))
                    .exec();
        }

        // Build inline button only if more images remain
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        if (to < images.size()) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton("Oldinga ➡", "next_karniz");
            markup.addKeyboard(nextButton);
            context.sendMessage(chatId, "Sahifa: " + (page + 1))
                    .replyMarkup(markup)
                    .exec();
        } else {
            // Last page — just send page info, no button
            context.sendMessage(chatId, "Sahifa: " + (page + 1) + " (Ohirgi sahifa)").exec();
        }
    }

    @MessageHandler(texts = {"Bort",})
    void bort(BotContext context, Message message) {
        String response;
        var keyboard = new ReplyKeyboardMarkup().resizeKeyboard(true);
        context.sendMessage(message.chat.id, "You said: " + message.text).exec();
    }

    @MessageHandler(texts = {"Dekor",})
    void dekor(BotContext context, Message message) {
        String response;
        var keyboard = new ReplyKeyboardMarkup().resizeKeyboard(true);
        context.sendMessage(message.chat.id, "You said: " + message.text).exec();
    }

    @MessageHandler(texts = {"orqaga",})
    void orqaga(BotContext context, Message message) {
        var keyboard = new ReplyKeyboardMarkup().resizeKeyboard(true);
        keyboard.add(
                "Lepka", "Dizayn"  // Display without slash
        );
        // SEND THE MESSAGE WITH THE KEYBOARD
        context.sendMessage(message.chat.id, "Choose one:")
                .replyMarkup(keyboard)
                .exec();
    }

    public List<File> getKarnizImages() {
        List<File> files = new ArrayList<>();

        try {
            var resolver = new PathMatchingResourcePatternResolver();
            var resources = resolver.getResources("classpath:/static/image/karniz/*");

            for (var r : resources) {
                files.add(r.getFile());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

    public List<File> getMoldingImages() {
        List<File> files = new ArrayList<>();

        try {
            var resolver = new PathMatchingResourcePatternResolver();
            var resources = resolver.getResources("classpath:/static/image/molding/*");

            for (var r : resources) {
                files.add(r.getFile());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

    @CallbackHandler
    void onCallback(BotContext context, io.github.natanimn.telebof.types.updates.CallbackQuery query) {

        long chatId = query.message.chat.id;
        String data = query.data; // contains "next_karniz" or "prev_karniz"

        if (data.equals("next_karniz")) {
            int page = userPage.get(chatId);
            userPage.put(chatId, page + 1);
            sendPage(context, chatId);
        }
    }


}
