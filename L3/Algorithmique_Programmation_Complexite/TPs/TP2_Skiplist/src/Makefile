CXX      = g++

.DEFAULT_GOAL := test_skipliste

SOURCES = 
SOURCES += skip_cellule.cpp
SOURCES += skip_liste.cpp

OBJECTS = $(SOURCES:.cpp=.o)

CXXFLAGS  += -g -Wall -std=c++11 -pedantic
LDFLAGS +=

$(OBJECTS) : %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

CLEAN_OBJECTS = $(OBJECTS)
TARGETS = 

########## test_skipliste ##########

TEST_SOURCES = test_skipliste.cpp
TEST_OBJECTS = $(TEST_SOURCES:.cpp=.o)

test_skipliste : $(TEST_OBJECTS) $(OBJECTS) $(HEADERS)
	$(CXX) $(TEST_OBJECTS) $(OBJECTS) -o $@ $(LDFLAGS)
	
$(TEST_OBJECTS): %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

all : test_skipliste
TARGETS += test_skipliste
CLEAN_OBJECTS += $(TEST_OBJECTS)

########## nettoyage ##########

DEPS = $(CLEAN_OBJECTS:.o=.d)

clean:
	@rm -f $(DEPS) $(TARGETS) $(CLEAN_OBJECTS)

-include $(DEPS)
