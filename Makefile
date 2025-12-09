# --- Configuration ---
# Nom du fichier JAR final
JAR_NAME = TP2.jar

# Dossier où placer les fichiers compilés
BIN_DIR = out/production/TP2

# Commandes
JAVAC = javac
JAVA = java
JAR = jar

# Options de compilation
JFLAGS = -g -d $(BIN_DIR) -sourcepath src

# Trouve automatiquement tous les fichiers .java dans src
SOURCES = $(shell find src -name "*.java")

# --- Cibles ---

# Par défaut, on compile et on exécute
default: exec

# 1. Compilation
compile:
	@echo "=== Compilation du projet TP2 ==="
	mkdir -p $(BIN_DIR)
	$(JAVAC) $(JFLAGS) $(SOURCES)
	@echo "Compilation terminée."

# 2. Création du JAR (TP2.jar)
jar: compile
	@echo "=== Création de $(JAR_NAME) ==="
	echo "Main-Class: Main" > manifest.txt
	$(JAR) cvfm $(JAR_NAME) manifest.txt -C $(BIN_DIR) .
	rm manifest.txt
	@echo "Fichier $(JAR_NAME) généré."

# 3. Exécution
exec: jar
	@echo "=== Exécution de TP2 ==="
	$(JAVA) -jar $(JAR_NAME)

# 4. Nettoyage
clean:
	@echo "=== Nettoyage des fichiers générés ==="
	rm -rf out
	rm -f $(JAR_NAME) manifest.txt
	rm -f TP2_Rendu.zip

# 5. Création du ZIP pour le rendu
zip: clean
	@echo "=== Création de l'archive TP2_Rendu.zip ==="
	zip -r TP2_Rendu.zip src Makefile README.md resources