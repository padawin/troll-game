SRC := $(shell find src -name *.java)

all:
	javac -Xlint:unchecked src/troll/Start.java -sourcepath src -d bin
#~	javac -Xlint:unchecked src/troll/Start.java $(SRC) -d bin

