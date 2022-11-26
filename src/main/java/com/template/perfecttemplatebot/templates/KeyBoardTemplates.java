package com.template.perfecttemplatebot.templates;

import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.data_base.entity.User;
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
            row4.add(new KeyboardButton("Все подписки"));
            row4.add(new KeyboardButton("Действующие подписки"));
            row5.add(new KeyboardButton("Истекающие подписки"));
            row5.add(new KeyboardButton("Просроченные подписки"));
            row6.add(new KeyboardButton("Добавить подписку"));
            row6.add(new KeyboardButton("Продлить подписку"));
            row7.add(new KeyboardButton("Ожидающие подтверждения"));
            keyboard.add(row3);
            keyboard.add(row4);
            keyboard.add(row5);
            keyboard.add(row6);
            keyboard.add(row7);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public KeyBoard getAmountOfDaysKeyBoard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton firstBtn = new InlineKeyboardButton();
        firstBtn.setText("8");
        InlineKeyboardButton secondBtn = new InlineKeyboardButton();
        secondBtn.setText("16");
        InlineKeyboardButton thirdBtn = new InlineKeyboardButton();
        thirdBtn.setText("24");
        InlineKeyboardButton fourthBtn = new InlineKeyboardButton();
        fourthBtn.setText("32");

        firstBtn.setCallbackData("8");
        secondBtn.setCallbackData("16");
        thirdBtn.setCallbackData("24");
        fourthBtn.setCallbackData("32");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(firstBtn);
        keyboardButtonsRow1.add(secondBtn);
        keyboardButtonsRow1.add(thirdBtn);
        keyboardButtonsRow1.add(fourthBtn);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return new KeyBoard(inlineKeyboardMarkup, "Выберите количество оплаченных тренировок");
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

    public KeyBoard createWaitingKeyboard() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<User> users = userDAO.findAllBySubscriber(false);
        for (User user : users) {
            rowList.add(getButton(
                    String.format("%s %s", user.getFirstName(), user.getLastName()),
                    user.getTelegramTag()
            ));
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        if (rowList.isEmpty()) {
            rowList.add(getButton(
                    "Назад",
                    "back_from_waiting_list"
            ));
            inlineKeyboardMarkup.setKeyboard(rowList);
            return new KeyBoard(inlineKeyboardMarkup, "Нет ожидающих подтверждения");
        }
        rowList.add(getButton(
                "Назад",
                "back_from_waiting_list"
        ));
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new KeyBoard(inlineKeyboardMarkup, "Ожидающие подтверждения");
    }

    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }
}