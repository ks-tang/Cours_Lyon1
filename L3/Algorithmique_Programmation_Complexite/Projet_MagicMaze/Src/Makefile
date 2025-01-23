CXX = g++
CXXFLAGS += -g -Wall -std=c++11 -pedantic

CAIRO_FLAGS = $(shell pkg-config --cflags librsvg-2.0)
CAIRO_CXXFLAGS = $(if $(CAIRO_FLAGS),$(CAIRO_FLAGS),-DNO_CAIRO)

IMAGICK_FLAGS = $(shell PKG_CONFIG_PATH=/usr/lib/imagemagick6/pkgconfig pkg-config --cflags ImageMagick++)
IMAGICK_CXXFLAGS = $(if $(IMAGICK_FLAGS),$(IMAGICK_FLAGS),-DNO_IMAGICK)

CXXFLAGS += $(CAIRO_CXXFLAGS) $(IMAGICK_CXXFLAGS)

DRAW_LDFLAGS = $(shell pkg-config --libs librsvg-2.0)
DRAW_LDFLAGS += $(shell PKG_CONFIG_PATH=/usr/lib/imagemagick6/pkgconfig pkg-config --libs ImageMagick++)

LDFLAGS += $(DRAW_LDFLAGS)

.DEFAULT_GOAL := all

SOURCES = 
SOURCES += melangeur.cpp
SOURCES += couleurs.cpp
SOURCES += case.cpp
SOURCES += mur.cpp
SOURCES += site.cpp
SOURCES += direction.cpp
SOURCES += console_pad.cpp
SOURCES += draw.cpp
SOURCES += tuiles.cpp
SOURCES += unionfind.cpp
SOURCES += graphe.cpp
SOURCES += noeud.cpp
SOURCES += arete.cpp

OBJECTS = $(SOURCES:.cpp=.o)

$(OBJECTS) : %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

CLEAN_OBJECTS = $(OBJECTS)

TARGETS = 

########## test_melangeur ##########

TEST_MELANGEUR_SOURCES = test_melangeur.cpp
TEST_MELANGEUR_OBJECTS = $(TEST_MELANGEUR_SOURCES:.cpp=.o)

test_melangeur : $(TEST_MELANGEUR_OBJECTS) $(OBJECTS)
	$(CXX) $(TEST_MELANGEUR_OBJECTS) $(OBJECTS) -o $@ $(LDFLAGS)
	
$(TEST_MELANGEUR_OBJECTS): %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

all : test_melangeur
TARGETS += test_melangeur
CLEAN_OBJECTS += $(TEST_MELANGEUR_OBJECTS)

########## test_draw ##########

TEST_DRAW_SOURCES = test_draw.cpp
TEST_DRAW_OBJECTS = $(TEST_DRAW_SOURCES:.cpp=.o)

test_draw : $(TEST_DRAW_OBJECTS) $(OBJECTS)
	$(CXX) $(TEST_DRAW_OBJECTS) $(OBJECTS) -o $@ $(LDFLAGS)
	
$(TEST_DRAW_OBJECTS): %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

all : test_draw
TARGETS += test_draw
CLEAN_OBJECTS += $(TEST_DRAW_OBJECTS)

########## test_tuiles ##########

TEST_TUILES_SOURCES = test_tuiles.cpp
TEST_TUILES_OBJECTS = $(TEST_TUILES_SOURCES:.cpp=.o)

test_tuiles : $(TEST_TUILES_OBJECTS) $(OBJECTS)
	$(CXX) $(TEST_TUILES_OBJECTS) $(OBJECTS) -o $@ $(LDFLAGS)
	
$(TEST_TUILES_OBJECTS): %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

all : test_tuiles
TARGETS += test_tuiles
CLEAN_OBJECTS += $(TEST_TUILES_OBJECTS)

########## test_graphe ##########

TEST_GRAPHE_SOURCES = test_graphe.cpp
TEST_GRAPHE_OBJECTS = $(TEST_GRAPHE_SOURCES:.cpp=.o)

test_graphe : $(TEST_GRAPHE_OBJECTS) $(OBJECTS)
	$(CXX) $(TEST_GRAPHE_OBJECTS) $(OBJECTS) -o $@ $(LDFLAGS)
	
$(TEST_GRAPHE_OBJECTS): %.o : %.cpp
	$(CXX) -MMD $(CXXFLAGS) -c $< -o $@

all : test_graphe
TARGETS += test_graphe
CLEAN_OBJECTS += $(TEST_GRAPHE_OBJECTS)

########## cleanup ##########
DEPS = $(CLEAN_OBJECTS:.o=.d)

clean:
	@rm -f $(DEPS) $(TARGETS) $(CLEAN_OBJECTS)

-include $(DEPS)
