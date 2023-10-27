# How to learn this project
You can start exploring of this project from class TelegramFacade.class
Here app handles requests from users by defining is that callBackQuery or
message.
    Further it splits on two classes MessageHandler and CallBackQueryHandler.
Where you can gradually find out how app works with requests from users.

# Config
Class TelegramBotConfig.class is responsible for three main parameters:
webHookPath (how to create that check into file CONTRIBUTING.md)
userName (you can get it while creating your telegram bot through app Telegram)
botToken (you can get it while creating your telegram bot through app Telegram)

# Bot states
You can find BotStates.class, that accumulates all possible states of your bot.
Just replace default states by yours own and support it into handler classes.

# Services
There are two classes, AnswerService.class works with replies to users.
PingTask.class provide to you opportunity to keep your server alive even if you get
low cost tariff by sending request somewhere you want every twenty minutes.

# Templates
There are also two classes which implements drawing of keyboard.

# Main class
PerfectTemplateBotApplication.class is main execution class that you need to execute
to start your application.