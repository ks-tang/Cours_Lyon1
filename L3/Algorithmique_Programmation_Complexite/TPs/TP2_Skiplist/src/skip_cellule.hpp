#ifndef LIFAP6_SKIP_CELLULE_HPP
#define LIFAP6_SKIP_CELLULE_HPP

#include <vector>

class SkipCellule {
  public:
    /* construction, destruction */
    SkipCellule(int v) ;
    ~SkipCellule() ;

    /* valeur, comme dans une liste normale */
    int valeur ;

    /* dans une skip liste, on a une suivante par niveau */
    std::vector<SkipCellule*> suivante ;
} ;

#endif
