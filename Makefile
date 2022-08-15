JAVAC=javac
sources = $(wildcard */*/*/*/*.java */*/*/*/*/*.java */*/*/*/*/*/*.java */*/*/*/*/*/*/*.java)
classes = $(sources:.java=.class)

classpathify = $(subst $(eval) ,:,$(wildcard $1))
classpath = external_libs/* src/
all: $(classes)

clean:
	 rm $$(find . -name '*.class')

%.class: %.java
	$(JAVAC) $< --class-path $(call classpathify,$(classpath))

jar: $(classes)
	jar cvf manhunt.jar $(subst src/,-C src ,$(classes)) plugin.yml

.PHONY: all clean jar
