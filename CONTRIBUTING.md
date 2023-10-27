# Beginning
# How to set up your environment and run tests
To create a database you can use the command in docker:
docker run -d --network="bridge" --name bot-postgres -e POSTGRES_PASSWORD=qwer1234 -p 5432:5432 postgres
command to use webhook:
https://api.telegram.org/bot<token>/setWebhook?url=
https://api.telegram.org/bot5859581807:AAGkAh0kjeuG6eeTHiDasktmz4RAHhE6_18/setWebhook?url=https://9431-178-90-194-84.eu.ngrok.io
to create a public address for testing the bot, you can use ngrock:
you need to add C:\Users\YourUser\Desktop\ngrok.exe http 5000 to the shortcut
To check spring context run PerfectTemplateBotApplicationTests.class

# How to suggest a new feature
Create pull request

# How to file a bug report
