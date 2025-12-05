# COMP2522 - Lab 09  
## Lucky Vault: Country Edition (Scanner · Random · NIO)

A simple command-line game that picks a random country from `data/countries.txt` and asks the player to guess it.  
After each attempt, the program provides feedback (length mismatch or number of correctly positioned letters).  
The game tracks the best score and logs every session automatically.

---

##  Features
- Load country names from UTF-8 text file  
- Random secret selection using `Random`  
- Input validation with `Scanner`  
- Detailed guess feedback  
- High-score persistence (`data/highscore.txt`)  
- Per-session log files (`data/logs/…`)  
- Uses NIO (`Path`, `Files`, buffered readers/writers) with try-with-resources  
- Clean OOP structure (WordList, HighScoreService, LoggerService, Game)

---

##  Assignment Specification  
See **Lucky_Vault_lab09.pdf** for full instructions.  


---

##  Authors
- **Author 1** - Abdullah Asmy  
- **Author 2** - Indy Grewel
