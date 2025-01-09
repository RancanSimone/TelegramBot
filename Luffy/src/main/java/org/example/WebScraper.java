package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebScraper {
    public static void main(String[] args) {
        // URL della pagina da cui fare scraping
        String url = "https://dodi-repacks.site/god-of-war-ragnarok/";

        try {
            // Crea un oggetto URL
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

            // Imposta il metodo di richiesta
            connection.setRequestMethod("GET");

            // Aggiungi header personalizzati per simulare un vero browser
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            connection.setRequestProperty("Accept-Language", "en-US;q=0.7,en;q=0.3");
            connection.setRequestProperty("Referer", "https://dodi-repacks.site/?__cf_chl_tk=M9AR3lp3mqHt_f0MC_j45mBZoOPg42gT1QO4CAn77JE-1735738964-1.0.1.1-7mHhZq8d.k2s7fS1ily5JhYG_SjoSZZ7Fobm7V6Guqo");
            //cookie da aggiornare giornalmente
            connection.setRequestProperty("Cookie", "cf_clearance=U.b8V6FHLGt3oZPpNQmg1pxslEN4uLLRLiDt2VkmW8g-1736320960-1.2.1.1-5C.6OqLMYvLPpnVIxyeiZNo2iigAfqxXd4vZ8czQ5Uhmrz.57y6Aw1rw_BL7MI2nubGrHP30rGVgCI1JLB_AY5f251._tufCH1eyyDMlnAFGgtw4P7oWKPjhB4jKemyEQvP1te3Y2Lm6ADv_jfy9A7spuxP1WIV4pVTjX_nnW6Jy0tEWLdf1NgwKY1Hi27Y3wXdsV2m.NA0996VPovIslHS_hZuYtZEXRyYbuH3v3X4wdDiyNBhutiJgQHa9DIqIB.vERjEhtj.Qws8rjUoaUteu1aiaXsReoGb_ih8pFBmaiO0pGBAD7vSayAkEjyAOsCjXN25qij1OqG32VTq2BHx1wVBZ6V3L9PE91.Wt9XXf1xtuDKCbUhe9TA9h.Lh52vw_.xtrGwA8xeq75SsINwA2Y1URCc4Loy5W4FLzxEwrDNk654IMqa35g2cNtUcO");
            connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
            connection.setRequestProperty("sec-ch-ua-arch", "\"x86\"");
            connection.setRequestProperty("sec-ch-ua-bitness", "\"64\"");
            connection.setRequestProperty("sec-ch-ua-full-version", "\"131.0.6778.205\"");
            connection.setRequestProperty("sec-fetch-dest", "document");
            connection.setRequestProperty("sec-fetch-mode", "navigate");
            connection.setRequestProperty("sec-fetch-site", "same-origin");
            connection.setRequestProperty("sec-fetch-user", "?1");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);


            // Timeout per connessione e lettura
            connection.setConnectTimeout(50000);  // 5 secondi di timeout per la connessione
            connection.setReadTimeout(50000);     // 5 secondi di timeout per la lettura della risposta

            // Ottieni la risposta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Leggi il contenuto della risposta
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // Usa jsoup per analizzare il contenuto HTML
                Document doc = Jsoup.parse(content.toString());

                // Estrai il titolo della pagina
                String title = doc.title();
                System.out.println("Titolo della pagina: " + title);

                // Estrai tutti i link
                doc.select("a").forEach(element -> {
                    System.out.println("Link trovato: " + element.attr("href"));
                });
            } else {
                System.out.println("Errore nella richiesta, codice di risposta: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
