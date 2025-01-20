#include "niveau.hpp"
#include "serpent.hpp"

#include <iostream>
#include <ncurses.h>
#include <unistd.h>
#include <stdexcept>

int main() {
  /* vitesse */
  float delay = 0.05 ;

  /* elements de jeu */
  Niveau niveau(40, 40) ;
  Serpent serpent(20, 20) ;

  /* debut de ncurses */
  initscr() ;
  curs_set(0) ;
  noecho() ;
  keypad(stdscr, TRUE) ;
  start_color() ;

  /* dessin initial */
  niveau.dessiner() ;
  serpent.dessiner() ;
  refresh() ;

  /* attente d'une premiere touche */
  std::cin.peek() ;

  /* plus d'attente sur les carracteres */
  nodelay(stdscr, TRUE) ;

  /* boucle d'evenements */
  try {
    while(1) {
      /* handle typed keys */
      usleep(delay * 1E6) ;
      int c = getch() ;
      if(c != ERR) {
        /* arrow keys */
        if(c == KEY_RIGHT) {
          serpent.direction = Serpent::DROITE ;
        } else if (c == KEY_UP) {
          serpent.direction = Serpent::HAUT ;
        } else if (c == KEY_LEFT) {
          serpent.direction = Serpent::GAUCHE ;
        } else if (c == KEY_DOWN) {
          serpent.direction = Serpent::BAS ;
        } else {
          /* quit if any other key is typed */
          break ;
        }
      }
      serpent.avancer(niveau) ;
      serpent.rafraichir() ;
      refresh() ;
    }

    /* fin de ncurses */
    endwin() ;
  } catch (std::runtime_error &e) {
    endwin() ;
    std::cout << "perdu !" << std::endl ;
  }

  return 0 ;
}
