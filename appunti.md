# Appunti

## BitBoard

l'idea è quella di rappresentare la scacchiera come un insieme di bit, alcune considerazioni:

* posso salvare diverse BitBoard, ad esempio, una BitBoard per le pedine bianche, una per le pedine nere ed una per il re, in questo modo se voglio sapere se il mio re è al centro mi basta confrontare in AND la sua bitboard con una bitboard in cui il re è al centro
