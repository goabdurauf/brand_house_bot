package com.aarouf.telegrambot;

import io.github.natanimn.telebof.BotClient;
import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.CallbackHandler;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardButton;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardMarkup;
import io.github.natanimn.telebof.types.keyboard.ReplyKeyboardMarkup;
import io.github.natanimn.telebof.types.updates.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@SpringBootApplication
public class TelegramBotApplication {

    public static String getBotToken() {
        return "";
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

        context.sendMessage(
                message.chat.id, "Assalomu Allaykum BREND HOUSE rasmiy Telegram botiga xush kelibsiz! Quydagi tugmalardan birini bosing")
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
            context.sendMessage(message.chat.id, "Birini tanlang:")
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

    @MessageHandler(texts = {"Bort"})
    void bort(BotContext context, Message message) {

        long chatId = message.chat.id;

        List<File> images = getBortImages();
        userImages.put(chatId, images);
        userPage.put(chatId, 0);

        sendPage(context, chatId);
    }

    @MessageHandler(texts = {"Dekor"})
    void dekor(BotContext context, Message message) {

        long chatId = message.chat.id;

        List<File> images = getDekorImages();
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

    @MessageHandler(texts = {"orqaga",})
    void orqaga(BotContext context, Message message) {
        var keyboard = new ReplyKeyboardMarkup().resizeKeyboard(true);
        keyboard.add(
                "Lepka", "Dizayn"  // Display without slash
        );
        // SEND THE MESSAGE WITH THE KEYBOARD
        context.sendMessage(message.chat.id, "Birini tanlang:")
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
            sortByCreationDate(files);
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

            sortByCreationDate(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

    public List<File> getBortImages() {
        List<File> files = new ArrayList<>();

        try {
            var resolver = new PathMatchingResourcePatternResolver();
            var resources = resolver.getResources("classpath:/static/image/bort/*");

            for (var r : resources) {
                files.add(r.getFile());
            }
            sortByCreationDate(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }


    public List<File> getDekorImages() {
        List<File> files = new ArrayList<>();

        try {
            var resolver = new PathMatchingResourcePatternResolver();
            var resources = resolver.getResources("classpath:/static/image/dekor/*");

            for (var r : resources) {
                files.add(r.getFile());
            }
            sortByCreationDate(files);
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

    public static void sortByCreationDate(List<File> files) {
        files.sort(new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                try {
                    BasicFileAttributes attr1 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class);
                    BasicFileAttributes attr2 = Files.readAttributes(f2.toPath(), BasicFileAttributes.class);

                    // Newest first (DESCENDING)
                    return attr2.creationTime().compareTo(attr1.creationTime());

                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }


}
