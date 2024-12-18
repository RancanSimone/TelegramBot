# Telegram Bot per la Ricerca di Videogiochi Craccati

## Introduzione
Questo documento descrive l’analisi e la progettazione di un bot Telegram sviluppato in Java, utilizzando la libreria TelegramBots. Il bot consente agli utenti di cercare il nome di un videogioco per verificare se è stato craccato, restituendo, in caso positivo, un link di download recuperato automaticamente tramite web scraping da siti come hisceacked e DODI repack.

### Descrizione dell'applicazione
L'applicazione è progettata per rendere rapida e semplice la ricerca di versioni craccate di videogiochi. Gli utenti interagiscono con il bot inviando comandi o nomi di giochi, e ricevono risposte basate sui dati raccolti da un web scraper e memorizzati in un database MySQL.

### Features principali
- Ricerca per nome: Gli utenti inviano il nome del gioco, e il bot verifica se è disponibile una versione craccata.
- Link di download: Per i giochi disponibili, il bot fornisce il link diretto.
- Suggerimenti: Se il gioco cercato non è disponibile, il bot suggerisce titoli simili.
- Aggiornamenti database: Un amministratore può aggiornare manualmente i dati per garantire accuratezza e completezza.
- Sistema interattivo: Interfaccia semplice e intuitiva per rendere l’utilizzo immediato.
- API: 

### Tecnologie utilizzate
- Linguaggio di programmazione: Java.
- Libreria TelegramBots: Gestione dell’interazione con l’API di Telegram (link GitHub).
- Libreria JSoup: Per il web scraping dei siti web (link JSoup).
- Database: MySQL, per memorizzare i dati sui videogiochi e i link.

### Progettazione del database
- Progettazione concettuale: Il database memorizza i dati essenziali relativi ai videogiochi e ai loro stati di craccatura.

### Entità principali:
- Videogioco: Contiene le informazioni sui giochi (nome, stato craccato, link di download).
- Utente: Facoltativa, per tracciare le richieste o interazioni.


### Gestione dei messaggi in ingresso e uscita
- Messaggi in ingresso: Gli utenti inviano comandi o nomi di videogiochi tramite Telegram.
- Elaborazione: Il bot utilizza la libreria TelegramBots per interpretare i comandi e verifica nel database l’esistenza del gioco. Se necessario, attiva il web scraper per recuperare nuovi dati.
- Messaggi in uscita: Il bot restituisce all’utente una risposta personalizzata, includendo informazioni come lo stato del gioco e il link di download (se disponibile).

### Comandi principali
- /start: Avvia la conversazione con il bot e presenta le funzionalità.
- /cerca [nome_gioco]: Cerca nel database il gioco specificato.
- /suggerisci: Suggerisce giochi correlati se il titolo cercato non è disponibile.
- /aggiorna: Comando riservato all’amministratore per aggiornare i dati.
- /aiuto: Mostra una lista dei comandi disponibili.

### Guida allo sviluppo
#### Preparazione dell'ambiente di lavoro:
- Configurare un progetto Java.
- Integrare la libreria TelegramBots e JSoup.
- Configurare MySQL come database e preparare le tabelle.
#### Sviluppo del bot:
- Implementare il metodo onUpdateReceived per gestire i messaggi.
- Creare funzioni per l’interazione con il database (es. ricerca giochi, aggiornamento dati).
- Scrivere il web scraper per raccogliere dati dai siti di riferimento.
#### Test e debug:
- Testare il bot con query di esempio.
- Verificare che il sistema risponda correttamente alle richieste e che il web scraper funzioni come previsto.

### Conclusioni
Il bot Telegram rappresenta uno strumento utile e innovativo per la ricerca di videogiochi craccati, semplificando l’accesso a link di download. La sua implementazione in Java, combinata con tecnologie moderne come TelegramBots e JSoup, garantisce un sistema scalabile, affidabile e facile da utilizzare.


