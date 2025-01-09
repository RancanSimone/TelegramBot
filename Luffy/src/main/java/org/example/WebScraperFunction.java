package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class WebScraperFunction {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/loonepiece";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static String scrapeGamePage(String gameName) {
        String formattedGameName = gameName.toLowerCase()
                .replace(" ", "-")
                .replace("'", "");

        // Controllo se il gioco esiste già nel database
        DatabaseResult dbResult = checkGameInDatabase(formattedGameName);
        if (dbResult != null) {
            // Controlla la data salvata rispetto alla data sul sito
            try {
                String url = "https://dodi-repacks.site/" + formattedGameName + "/";
                URL urlObj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

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

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder content = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    Document doc = Jsoup.parse(content.toString());

                    // Estrarre la data pubblicata dal sito
                    Element timeElement = doc.selectFirst("time.entry-date.published");
                    java.sql.Date publicationDate = null;
                    if (timeElement != null) {
                        String dateTime = timeElement.attr("datetime");
                        publicationDate = java.sql.Date.valueOf(dateTime.substring(0, 10));
                    }

                    // Confronto della data
                    if (publicationDate != null && publicationDate.equals(dbResult.date)) {
                        System.out.println("Gioco e data trovati nel database. Link: " + dbResult.link);
                        return dbResult.link;
                    } else {
                        // Aggiorna il link e la data nel database
                        for (Element link : doc.select("a[href]")) {
                            String href = link.attr("href");
                            if (href.contains("dayuploads")) {
                                String downloadLink = scrapeDownloadLink(href);
                                if (downloadLink != null) {
                                    updateGameInDatabase(formattedGameName, downloadLink, publicationDate);
                                    return downloadLink;
                                }
                            }
                        }
                    }
                } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    System.out.println("Errore 403: Utilizzo dati dal database. Link: " + dbResult.link);
                    return dbResult.link;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Il gioco non è nel database, tenta di trovarlo online
            try {
                String url = "https://dodi-repacks.site/" + formattedGameName + "/";
                URL urlObj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

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

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder content = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    Document doc = Jsoup.parse(content.toString());

                    // Estrai il link del download
                    for (Element link : doc.select("a[href]")) {
                        String href = link.attr("href");
                        if (href.contains("dayuploads")) {
                            String downloadLink = scrapeDownloadLink(href);
                            if (downloadLink != null) {
                                // Inserisci il nuovo gioco nel database
                                insertGameInDatabase(formattedGameName, downloadLink);
                                return downloadLink;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Gioco non trovato. Operazione fallita.");
        return null;
    }

    private static String scrapeDownloadLink(String pageUrl) {
        try {
            URL urlObj = new URL(pageUrl);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                Document doc = Jsoup.parse(content.toString());
                Element downloadButton = doc.selectFirst(".download-button.hidden");
                if (downloadButton != null) {
                    String onclickValue = downloadButton.attr("onclick");
                    if (onclickValue.contains("http")) {
                        return onclickValue.substring(onclickValue.indexOf("http"), onclickValue.lastIndexOf("'"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DatabaseResult checkGameInDatabase(String gameName) {
        String query = "SELECT Link, Data FROM giochi WHERE Nome = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, gameName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new DatabaseResult(rs.getString("Link"), rs.getDate("Data"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void updateGameInDatabase(String gameName, String link, java.sql.Date date) {
        String query = "UPDATE giochi SET Link = ?, Data = ? WHERE Nome = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, link);
            stmt.setDate(2, date);
            stmt.setString(3, gameName);
            stmt.executeUpdate();
            System.out.println("Gioco aggiornato nel database: " + gameName + ", Data: " + date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertGameInDatabase(String gameName, String link) {
        String query = "INSERT INTO giochi (Nome, Link) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, gameName);
            stmt.setString(2, link);
            stmt.executeUpdate();
            System.out.println("Gioco inserito nel database: " + gameName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class DatabaseResult {
        String link;
        java.sql.Date date;

        DatabaseResult(String link, java.sql.Date date) {
            this.link = link;
            this.date = date;
        }
    }
}
