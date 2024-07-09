package com.example.onefit.service;

import com.example.onefit.config.BotConfig;
import com.example.onefit.entity.SportTypeEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final SportTypeService sportTypeService;


    public TelegramBot(BotConfig botConfig, SportTypeService sportTypeService) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.sportTypeService = sportTypeService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start" -> startCommand(chatId, update.getMessage().getChat().getFirstName());
                case "Subscription" -> showSportTypes(chatId);
                case "☎️ Contact" -> checkingIfExist(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            showSportTypeDetails(chatId, callbackData);
        }
    }

    private void checkingIfExist(long chatId) {
    }

    private void startCommand(long chatId, String name) {
        String answer = "Hello " + name + "!";
        sendMessageWithKeyboard(chatId, answer);
    }

    private void sendMessageWithKeyboard(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(getMainKeyboard());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void showSportTypes(long chatId) {
        List<SportTypeEntity> sportTypes = sportTypeService.getAllSportTypes();
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Available Sport Types:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (SportTypeEntity sportType : sportTypes) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(sportType.getName() + " - $" + sportType.getPrice());
            button.setCallbackData(String.valueOf(sportType.getId()));
            row.add(button);
            rows.add(row);
        }

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void showSportTypeDetails(long chatId, String sportTypeId) {
        SportTypeEntity sportType = sportTypeService.getSportTypeById(Long.parseLong(sportTypeId));
        if (sportType != null) {
            StringBuilder details = new StringBuilder();
            details.append("Name: ").append(sportType.getName()).append("\n");
            details.append("Price: $").append(sportType.getPrice()).append("\n");
            details.append("Limit: ").append(sportType.getLimitation()).append(" times per month\n");
            details.append("Description: ").append(sportType.getDescription()).append("\n");
            details.append("Location: ").append(sportType.getLocation()).append("\n");

            sendMessageWithKeyboard(chatId, details.toString());
        } else {
            sendMessageWithKeyboard(chatId, "Sport type not found!");
        }
    }

    private ReplyKeyboardMarkup getMainKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton contactButton = new KeyboardButton("☎️ Contact");
        contactButton.setRequestContact(true);
        row1.add(contactButton);

        KeyboardButton locationButton = new KeyboardButton("📍 Location");
        locationButton.setRequestLocation(true);
        row1.add(locationButton);

        KeyboardButton sportsButton = new KeyboardButton("\uD83C\uDFC3\u200D♂\uFE0F Sports");
        row1.add(sportsButton);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Subscription"));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

}

/*
package com.example.onefit.service;

import com.example.onefit.config.BotConfig;
import com.example.onefit.entity.SportTypeEntity;
import com.example.onefit.service.imp.SportTypeServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private SportTypeServiceImpl sportTypeServiceImpl;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start" -> startCommand(chatId, update.getMessage().getChat().getFirstName());
                case "Subscription" -> showSportTypes(chatId);
            }
        }
    }

    private void startCommand(long chatId, String name) {
        String answer = "Hello " + name + "!";
        sendMessageWithKeyboard(chatId, answer);
    }

    private void sendMessageWithKeyboard(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(getMainKeyboard());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void showSportTypes(long chatId) {
        List<SportTypeEntity> sportTypes = sportTypeServiceImpl.getAllSportTypes();
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Available Sport Types:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (SportTypeEntity sportType : sportTypes) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(sportType.getName() + " - $" + sportType.getPrice());
            button.setCallbackData(String.valueOf(sportType.getId()));
            row.add(button);
            rows.add(row);
        }

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void showSportTypeDetails(long chatId, String sportTypeId) {
        SportTypeEntity sportType = sportTypeServiceImpl.getSportTypeById(Long.parseLong(sportTypeId));
        if (sportType != null) {
            StringBuilder details = new StringBuilder();
            details.append("Name: ").append(sportType.getName()).append("\n");
            details.append("Price: $").append(sportType.getPrice()).append("\n");
            details.append("Limit: ").append(sportType.getLimit()).append(" times per month\n");
            details.append("Description: ").append(sportType.getDescription()).append("\n");
            details.append("Location: ").append(sportType.getLocation()).append("\n");

            sendMessageWithKeyboard(chatId, details.toString());
        } else {
            sendMessageWithKeyboard(chatId, "Sport type not found!");
        }
    }

    private ReplyKeyboardMarkup getMainKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton contactButton = new KeyboardButton("☎️ Contact");
        contactButton.setRequestContact(true);
        row1.add(contactButton);

        KeyboardButton locationButton = new KeyboardButton("📍 Location");
        locationButton.setRequestLocation(true);
        row1.add(locationButton);

        KeyboardButton sportsButton = new KeyboardButton("\uD83C\uDFC3\u200D♂\uFE0F Sports");
        locationButton.setRequestLocation(true);
        row1.add(sportsButton);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Subscription"));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}*/
