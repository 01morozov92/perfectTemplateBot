package com.template.perfecttemplatebot.templates;

import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
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

    public KeyBoardTemplates(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private SendMessage createMessageWithKeyboard(final long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage, final long userId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(userId);

        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
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
        row1.add(new KeyboardButton("Осталось тренировок"));
        row2.add((new KeyboardButton("Меню3")));
        keyboard.add(row1);
        keyboard.add(row2);
        if (userId == admin_id) {
            KeyboardRow row3 = new KeyboardRow();
            KeyboardRow row4 = new KeyboardRow();
            KeyboardRow row5 = new KeyboardRow();
            KeyboardRow row6 = new KeyboardRow();
            KeyboardRow row7 = new KeyboardRow();
            row3.add(new KeyboardButton("Списать тренировку"));
            row4.add(new KeyboardButton("Список всех подписок"));
            row4.add(new KeyboardButton("Список действующих подписок"));
            row5.add(new KeyboardButton("Список истекающих подписок"));
            row6.add(new KeyboardButton("Список просроченных подписок"));
            row7.add(new KeyboardButton("Добавить/Продлить подписок"));
            keyboard.add(row3);
            keyboard.add(row4);
            keyboard.add(row5);
            keyboard.add(row6);
            keyboard.add(row7);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public KeyBoard getFirstKeyBoard() {
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

        return new KeyBoard(inlineKeyboardMarkup, "Это первая клавиатура");
    }

    //set calbackquery keyboard for push edit
    public KeyBoard getSecondKeyBoard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton firstBtnSecondMenu = new InlineKeyboardButton();
        firstBtnSecondMenu.setText("1 кнопка второго меню");
        InlineKeyboardButton secondBtnSecondMenu = new InlineKeyboardButton();
        secondBtnSecondMenu.setText("2 кнопка второго меню");
        InlineKeyboardButton thirdBtnSecondMenu = new InlineKeyboardButton();
        thirdBtnSecondMenu.setText("3 кнопка второго меню");

        firstBtnSecondMenu.setCallbackData("first_btn_second_menu");
        secondBtnSecondMenu.setCallbackData("second_btn_second_menu");
        thirdBtnSecondMenu.setCallbackData("third_btn_second_menu");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();


        keyboardButtonsRow1.add(firstBtnSecondMenu);
        keyboardButtonsRow2.add(secondBtnSecondMenu);
        keyboardButtonsRow3.add(thirdBtnSecondMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return new KeyBoard(inlineKeyboardMarkup, "Это вторая клавиатура");
    }

    public KeyBoard getThirdKeyBoard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton firstBtnSecondMenu = new InlineKeyboardButton();
        firstBtnSecondMenu.setText("1 кнопка третьего меню");
        InlineKeyboardButton secondBtnSecondMenu = new InlineKeyboardButton();
        secondBtnSecondMenu.setText("2 кнопка третьего меню");
        InlineKeyboardButton thirdBtnSecondMenu = new InlineKeyboardButton();
        thirdBtnSecondMenu.setText("3 кнопка третьего меню");

        firstBtnSecondMenu.setCallbackData("first_btn_third_menu");
        secondBtnSecondMenu.setCallbackData("second_btn_third_menu");
        thirdBtnSecondMenu.setCallbackData("third_btn_third_menu");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();


        keyboardButtonsRow1.add(firstBtnSecondMenu);
        keyboardButtonsRow2.add(secondBtnSecondMenu);
        keyboardButtonsRow3.add(thirdBtnSecondMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return new KeyBoard(inlineKeyboardMarkup, "Это третья клавиатура");
    }

    @Getter
    @Setter
    @Builder
    static public class KeyBoard {

        private final ReplyKeyboard replyKeyboard;
        private final String keyBoardDescription;

        public KeyBoard(ReplyKeyboard replyKeyboard, String keyBoardDescription) {
            this.replyKeyboard = replyKeyboard;
            this.keyBoardDescription = keyBoardDescription;
        }
    }
}