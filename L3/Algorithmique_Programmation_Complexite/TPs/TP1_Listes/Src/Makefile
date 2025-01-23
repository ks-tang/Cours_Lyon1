CXX      = g++

.DEFAULT_GOAL := test_liste

SOURCES = 
SOURCES += cellule.cpp
SOURCES += liste.cpp

OBJECTS = $(SOURCES:.cpp=.o)

CXXFLAGS  += -g -Wall -std=c++11 -pedantic
LDFLAGS +=

$(OBJECTS) : %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

CLEAN_OBJECTS = $(OBJECTS)
TARGETS = 

########## test_liste ##########

LISTE_SOURCES = test_liste.cpp
LISTE_OBJECTS = $(LISTE_SOURCES:.cpp=.o)

test_liste : $(LISTE_OBJECTS) $(OBJECTS) $(HEADERS)
	$(CXX) $(LISTE_OBJECTS) $(OBJECTS) -o $@ $(LDFLAGS)
	
$(LISTE_OBJECTS): %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

all : test_liste
TARGETS += test_liste
CLEAN_OBJECTS += $(LISTE_OBJECTS)

########## serpent ##########

SERPENT_SOURCES = coordonnees.cpp
SERPENT_SOURCES += niveau.cpp
SERPENT_SOURCES += serpent.cpp
SERPENT_SOURCES += jeu_serpent.cpp

SERPENT_OBJECTS = $(SERPENT_SOURCES:.cpp=.o)

jeu_serpent : $(SERPENT_OBJECTS) $(OBJECTS) $(HEADERS)
	$(CXX) $(SERPENT_OBJECTS) $(OBJECTS) -o $@ $(LDFLAGS) -lncurses
	
$(SERPENT_OBJECTS): %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

# decommenter ci-dessous pour faire le serpent
# attention ncurses pas disponible sous windows
# all : jeu_serpent
TARGETS += jeu_serpent
CLEAN_OBJECTS += $(SERPENT_OBJECTS)

########## cleanup ##########
DEPS = $(CLEAN_OBJECTS:.o=.d)

clean:
	@rm -f $(DEPS) $(TARGETS) $(CLEAN_OBJECTS)

-include $(DEPS)
