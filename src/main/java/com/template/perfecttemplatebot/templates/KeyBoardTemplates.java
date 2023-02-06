package com.template.perfecttemplatebot.templates;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.cash.Memory;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.enums.BotState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class KeyBoardTemplates {

    private UserDAO userDAO;

    @Value("${telegrambot.adminId}")
    private int admin_id;

    private final Memory memory;
    private final BotStateCash botStateCash;

    public KeyBoardTemplates(UserDAO userDAO, Memory memory, BotStateCash botStateCash) {
        this.userDAO = userDAO;
        this.memory = memory;
        this.botStateCash = botStateCash;
    }

    private SendMessage createMessageWithKeyboard(long chatId, String textMessage, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    public SendMessage getMainMenuMessage(final String textMessage, final long userId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(userId);
        return createMessageWithKeyboard(userId, textMessage, replyKeyboardMarkup);
    }

    //Main menu
    private ReplyKeyboardMarkup getMainMenuKeyboard(long userId) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton("Меню1"));
        row2.add(new KeyboardButton("Меню2"));
        row3.add((new KeyboardButton("Меню3")));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        if (userId == admin_id) {
            KeyboardRow row4 = new KeyboardRow();
            row4.add(new KeyboardButton("Админсоке меню 1"));
            row4.add(new KeyboardButton("Админсоке меню 2"));
            keyboard.add(row4);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public Keyboard getFirstKeyboard(boolean isEdit, long userId, BotState botState) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton firstBtn = new InlineKeyboardButton();
        firstBtn.setText("Первая кнопка");
        InlineKeyboardButton secondBtn = new InlineKeyboardButton();
        secondBtn.setText("Вторая кнопка");
        InlineKeyboardButton thirdBtn = new InlineKeyboardButton();
        thirdBtn.setText("Третья кнопка");
        InlineKeyboardButton fourthBtn = new InlineKeyboardButton();
        fourthBtn.setText("Четвертая кнопка");

        firstBtn.setCallbackData("first_button");
        secondBtn.setCallbackData("second_button");
        thirdBtn.setCallbackData("third_button");
        fourthBtn.setCallbackData("fourth_button");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(firstBtn);
        keyboardButtonsRow1.add(secondBtn);
        keyboardButtonsRow1.add(thirdBtn);
        keyboardButtonsRow1.add(fourthBtn);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return new Keyboard(inlineKeyboardMarkup, "Это первая клавиатура", memory, botState, botStateCash, userId);
    }

    //set calbackquery keyboard for push edit
    public Keyboard getSecondKeyBoard(boolean isEdit, Long userId, BotState botState) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton firstBtnSecondMenu = new InlineKeyboardButton();
        firstBtnSecondMenu.setText("1 кнопка второго меню");
        InlineKeyboardButton secondBtnSecondMenu = new InlineKeyboardButton();
        secondBtnSecondMenu.setText("2 кнопка второго меню");
        InlineKeyboardButton thirdBtnSecondMenu = new InlineKeyboardButton();
        thirdBtnSecondMenu.setText("3 кнопка второго меню");
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");

        firstBtnSecondMenu.setCallbackData("first_btn_second_menu");
        secondBtnSecondMenu.setCallbackData("second_btn_second_menu");
        thirdBtnSecondMenu.setCallbackData("third_btn_second_menu");
        back.setCallbackData("back");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();


        keyboardButtonsRow1.add(firstBtnSecondMenu);
        keyboardButtonsRow2.add(secondBtnSecondMenu);
        keyboardButtonsRow3.add(thirdBtnSecondMenu);
        keyboardButtonsRow4.add(back);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);

        inlineKeyboardMarkup.setKeyboard(rowList);
        if (isEdit) {
            return new Keyboard("Без сохранения", inlineKeyboardMarkup, "Это вторая клавиатура", memory, botState, botStateCash, userId);
        } else {
            return new Keyboard(inlineKeyboardMarkup, "Это вторая клавиатура", memory, botState, botStateCash, userId);
        }
    }

    public Keyboard getThirdKeyBoard(boolean isEdit, Long userId, BotState botState) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton firstBtnSecondMenu = new InlineKeyboardButton();
        firstBtnSecondMenu.setText("1 кнопка третьего меню");
        InlineKeyboardButton secondBtnSecondMenu = new InlineKeyboardButton();
        secondBtnSecondMenu.setText("2 кнопка третьего меню");
        InlineKeyboardButton thirdBtnSecondMenu = new InlineKeyboardButton();
        thirdBtnSecondMenu.setText("3 кнопка третьего меню");
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");

        firstBtnSecondMenu.setCallbackData("first_btn_third_menu");
        secondBtnSecondMenu.setCallbackData("second_btn_third_menu");
        thirdBtnSecondMenu.setCallbackData("third_btn_third_menu");
        back.setCallbackData("back");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();


        keyboardButtonsRow1.add(firstBtnSecondMenu);
        keyboardButtonsRow2.add(secondBtnSecondMenu);
        keyboardButtonsRow3.add(thirdBtnSecondMenu);
        keyboardButtonsRow4.add(back);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);

        inlineKeyboardMarkup.setKeyboard(rowList);
        if (isEdit) {
            return new Keyboard("Без сохранения", inlineKeyboardMarkup, "Это третья клавиатура", memory, botState, botStateCash, userId);
        } else {
            return new Keyboard(inlineKeyboardMarkup, "Это третья клавиатура", memory, botState, botStateCash, userId);
        }
    }
}