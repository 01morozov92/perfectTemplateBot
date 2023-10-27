# Getting Started
для создания базы можно использовать команду в docker:
docker run -d --network="bridge" --name bot-postgres -e POSTGRES_PASSWORD=qwer1234 -p 5432:5432 postgres
команды для назначения вебхука: 
https://api.telegram.org/bot<token>/setWebhook?url=
https://api.telegram.org/bot5859581807:AAGkAh0kjeuG6eeTHiDasktmz4RAHhE6_18/setWebhook?url=https://9431-178-90-194-84.eu.ngrok.io
чтобы создать открытый адрес для тестирования бота можно использовать ngrock:
к ярлыку нужно дописать C:\Users\YourUser\Desktop\ngrok.exe http 5000