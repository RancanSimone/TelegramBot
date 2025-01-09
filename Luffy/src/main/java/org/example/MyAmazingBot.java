package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {

    private TelegramClient telegramClient = new OkHttpTelegramClient("8192069321:AAGHldWhP_U94sGX7Pr_8VD48-sCVVQ2m3o");

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            String chatId = String.valueOf(update.getMessage().getChatId());

            System.out.println(userMessage);

            // Comando /start con tema piratesco
            if (userMessage.equals("/start")) {
                String startMessage = "🦜 Ahoy, giovane pirata! Benvenuto a bordo della nave del Re dei Pirati! 🏴‍☠️\n\n" +
                        "Sono il temuto e potente capitano del mare e posso aiutarti a trovare i link dei tesori nascosti (aka giochi)! ⚓️\n\n" +
                        "Per cominciare, digita il comando /Torrent seguito dal nome del gioco per ottenere il link del tesoro.\n\n" +
                        "Es: /Torrent The Witcher 3\n\n" +
                        "Pronto a solcare i mari per trovare il tuo bottino? ⚓️💰";
                sendMessage(chatId, startMessage);
            }

            // Comando /help con lista dei comandi pirateschi
            if (userMessage.equals("/help")) {
                String helpMessage = "🏴‍☠️ I comandi disponibili per un pirata audace come te sono:\n\n" +
                        "/start - Benvenuto a bordo, pirata! Scopri il mio mondo di avventure.\n" +
                        "/help - Lista dei comandi per solcare i mari.\n" +
                        "/Torrent [nome gioco] - Cerca il link del tesoro (torrent) per il gioco che desideri!\n" +
                        "Fai attenzione, il mare può essere insidioso... e anche il mio codice! ⚔️";
                sendMessage(chatId, helpMessage);
            }

            // Comando /Torrent per cercare il link del gioco
            if (userMessage.startsWith("/Torrent")) {
                String gameName = userMessage.replace("/Torrent", "").trim();

                if (!gameName.isEmpty()) {
                    String torrentLink = WebScraperFunction.scrapeGamePage(gameName);
                    String responseMessage = (torrentLink != null)
                            ? "🏴‍☠️ Ahoy! Ecco il link del tesoro per *" + gameName + "*:\n" + torrentLink
                            : "☠️ Non riuscivo a trovare il link del tesoro per *" + gameName + "*... la marea è contraria!";

                    sendMessage(chatId, responseMessage);
                } else {
                    sendMessage(chatId, "⚔️ Per favore, fornisci il nome del gioco dopo il comando /Torrent, giovane pirata!");
                }
            }
        }
    }

    private void sendMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        try {
            telegramClient.execute(sendMessage); // Esegue l'invio del messaggio
        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendMessage(chatId, "⚠️ Oops! Qualcosa è andato storto, prova di nuovo, pirata!");
        }
    }
}
