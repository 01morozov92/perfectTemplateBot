//package com.template.perfecttemplatebot.service;
//
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class InlineKeyboardMaker {
//
//    public InlineKeyboardMarkup getInlineMessageButtonsWithTemplate(String prefix, boolean isUserDictionaryNeed) {
//        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineMessageButtons(prefix, isUserDictionaryNeed);
//        inlineKeyboardMarkup.getKeyboard().add(getButton(
//                "Шаблон",
//                "callback"
//        ));
//        return inlineKeyboardMarkup;
//    }
//
//    public InlineKeyboardMarkup getInlineMessageButtons(String prefix) {
//        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//
//        for (DictionaryResourcePathEnum dictionary : DictionaryResourcePathEnum.values()) {
//            rowList.add(getButton(
//                    "",
//                    "callback"
//            ));
//        }
//
//        if (!rowList.isEmpty()) {
//            rowList.add(getButton(
//                    "Все классы",
//                    prefix + CallbackDataPartsEnum.ALL_GRADES.name()
//            ));
//        }
//
//        if (isUserDictionaryNeed) {
//            rowList.add(getButton(
//                    "Ваш словарь",
//                    prefix + CallbackDataPartsEnum.USER_DICTIONARY.name()
//            ));
//        }
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        inlineKeyboardMarkup.setKeyboard(rowList);
//        return inlineKeyboardMarkup;
//    }
//
//    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
//        InlineKeyboardButton button = new InlineKeyboardButton();
//        button.setText(buttonName);
//        button.setCallbackData(buttonCallBackData);
//
//        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
//        keyboardButtonsRow.add(button);
//        return keyboardButtonsRow;
//    }
//}
