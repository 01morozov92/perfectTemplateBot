package com.template.perfecttemplatebot.templates;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.cash.Memory;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.data_base.entity.User;
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
        if (userId == admin_id) {
            KeyboardRow row3 = new KeyboardRow();
            KeyboardRow row4 = new KeyboardRow();
            KeyboardRow row5 = new KeyboardRow();
            KeyboardRow row6 = new KeyboardRow();
            row3.add(new KeyboardButton("Списать тренировку"));
            row3.add(new KeyboardButton("Добавить тренировку"));
            row4.add(new KeyboardButton("Все подписки"));
            row4.add(new KeyboardButton("Действующие подписки"));
            row5.add(new KeyboardButton("Истекающие подписки"));
            row5.add(new KeyboardButton("Просроченные подписки"));
            row6.add(new KeyboardButton("Продлить подписку"));
            row6.add(new KeyboardButton("Ожидающие подтверждения"));
            keyboard.add(row3);
            keyboard.add(row4);
            keyboard.add(row5);
            keyboard.add(row6);
        } else {
            KeyboardRow row1 = new KeyboardRow();
            KeyboardRow row2 = new KeyboardRow();
            row1.add(new KeyboardButton("Осталось тренировок"));
            row2.add((new KeyboardButton("Расписание тренировок")));
            keyboard.add(row1);
            keyboard.add(row2);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public Keyboard getAmountOfDaysKeyboard(boolean isEdit, long userId, BotState botState) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton firstBtn = new InlineKeyboardButton();
        firstBtn.setText("8");
        InlineKeyboardButton secondBtn = new InlineKeyboardButton();
        secondBtn.setText("16");
        InlineKeyboardButton thirdBtn = new InlineKeyboardButton();
        thirdBtn.setText("24");
        InlineKeyboardButton fourthBtn = new InlineKeyboardButton();
        fourthBtn.setText("32");
        InlineKeyboardButton backToMainMenu = new InlineKeyboardButton();
        backToMainMenu.setText("Вернуться в меню");

        firstBtn.setCallbackData("8");
        secondBtn.setCallbackData("16");
        thirdBtn.setCallbackData("24");
        fourthBtn.setCallbackData("32");
        backToMainMenu.setCallbackData("back_to_main_menu");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(firstBtn);
        keyboardButtonsRow1.add(secondBtn);
        keyboardButtonsRow1.add(thirdBtn);
        keyboardButtonsRow1.add(fourthBtn);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(backToMainMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        if (isEdit) {
            return new Keyboard("Без сохранения", inlineKeyboardMarkup, "Выберите количество оплаченных тренировок", memory, botState, botStateCash, userId);
        } else {
            return new Keyboard(inlineKeyboardMarkup, "Выберите количество оплаченных тренировок", memory, botState, botStateCash, userId);
        }
    }

    public Keyboard getGroupsKeyboard(boolean isEdit, Long userId, BotState botState) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton firstBtn = new InlineKeyboardButton();
        firstBtn.setText("Младшая гр.");
        InlineKeyboardButton secondBtn = new InlineKeyboardButton();
        secondBtn.setText("Средняя гр.");
        InlineKeyboardButton thirdBtn = new InlineKeyboardButton();
        thirdBtn.setText("Женская индивидуальная гр.");
        InlineKeyboardButton fourthBtn = new InlineKeyboardButton();
        fourthBtn.setText("Взрослая группа");
        InlineKeyboardButton backToMainMenu = new InlineKeyboardButton();
        backToMainMenu.setText("Вернуться в меню");

        firstBtn.setCallbackData("CHILDREN");
        secondBtn.setCallbackData("TEENAGER");
        thirdBtn.setCallbackData("FEMALE_INDIVIDUAL");
        fourthBtn.setCallbackData("ADULT");
        backToMainMenu.setCallbackData("back_to_main_menu");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(firstBtn);
        keyboardButtonsRow1.add(secondBtn);
        keyboardButtonsRow1.add(thirdBtn);
        keyboardButtonsRow1.add(fourthBtn);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(backToMainMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        if (isEdit) {
            return new Keyboard("Без сохранения", inlineKeyboardMarkup, "Выберите группу для участника.", memory, botState, botStateCash, userId);
        } else {
            return new Keyboard(inlineKeyboardMarkup, "Выберите группу для участника.", memory, botState, botStateCash, userId);
        }
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
        if (isEdit) {
            return new Keyboard("Без сохранения", inlineKeyboardMarkup, "Это третья клавиатура", memory, botState, botStateCash, userId);
        } else {
            return new Keyboard(inlineKeyboardMarkup, "Это третья клавиатура", memory, botState, botStateCash, userId);
        }
    }

    public Keyboard getSubscriptionKeyboard(boolean isEdit, Long userId, BotState botState, List<User> users) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (User user : users) {
            rowList.add(getButton(
                    "%s %s %s".formatted(userDAO.findByTelegramTag(user.getTelegramTag()).getAmountOfDays(), user.getFirstName(), user.getLastName()),
                    user.getTelegramTag()
            ));
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        if (rowList.isEmpty()) {
            rowList.add(getButton(
                    "Вернуться в меню",
                    "back_to_main_menu"
            ));
            inlineKeyboardMarkup.setKeyboard(rowList);
            return new Keyboard(inlineKeyboardMarkup, "Нет пользователей", memory, botState, botStateCash, userId);
        } else {
            rowList.add(getButton(
                    "Назад",
                    "back"
            ));
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        if (isEdit) {
            return new Keyboard("Без сохранения", inlineKeyboardMarkup, "Список пользователей", memory, botState, botStateCash, userId);
        } else {
            return new Keyboard(inlineKeyboardMarkup, "Список пользователей", memory, botState, botStateCash, userId);
        }
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