SRC_DIR := src
BIN_DIR := bin
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
MAIN := rushhours.Main

all: $(BIN_DIR)
	rm -rf $(BIN_DIR)
	javac -d $(BIN_DIR) $(SOURCES)

$(BIN_DIR):
	mkdir -p $(BIN_DIR)

run: all
	java -cp $(BIN_DIR) $(MAIN)

.PHONY: all clean run