CXX      = g++

CXXFLAGS  += -g -Wall -std=c++11 -pedantic
LDFLAGS +=

.DEFAULT_GOAL := all

SOURCES = 
SOURCES += labyrinthe.cpp

OBJECTS = $(SOURCES:.cpp=.o)

%.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

CLEAN_OBJECTS = $(OBJECTS)
TARGETS = 

########## test ##########

TEST_SOURCES = test_labyrinthe.cpp

TEST_OBJECTS = $(TEST_SOURCES:.cpp=.o)

test_labyrinthe : $(TEST_OBJECTS) $(OBJECTS)
	$(CXX) $(TEST_OBJECTS) $(OBJECTS) -o $@ $(LDFLAGS)

all : test_labyrinthe
TARGETS += test_labyrinthe
CLEAN_OBJECTS += $(TEST_OBJECTS)

########## cleanup ##########
DEPS = $(CLEAN_OBJECTS:.o=.d)

clean:
	@rm -f $(DEPS) $(TARGETS) $(CLEAN_OBJECTS)

-include $(DEPS)
