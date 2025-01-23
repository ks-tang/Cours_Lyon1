#include "labyrinthe.hpp"

#include <algorithm>
#include <cassert>

/************************************ Murs ************************************/

static const Tuile MUR_E = 1 ;
static const Tuile MUR_N = 2 ;
static const Tuile MUR_O = 4 ;
static const Tuile MUR_S = 8 ;

static const Tuile MUR_TOUS = 15 ;
static const Tuile MUR_AUCUN = 0 ;

/******************************** Construction ********************************/

Labyrinthe::Labyrinthe(int largeur, int hauteur)
 : m_largeur(largeur), m_hauteur(hauteur), m_tuiles(largeur * hauteur, MUR_TOUS), uf(largeur * hauteur)
{
  /** à compléter **/

  for(int l = 0; l < m_hauteur; l++)
  {
    for(int c = 0; c < m_largeur; c++)
    {

      if ( l != m_hauteur-1)
      {
        Mur murBas;
        murBas.l1 = l;
        murBas.c1 = c;
        murBas.l2 = l+1;
        murBas.c2 = c;
        tab_murs.inserer(murBas);

      }

      if ( c != m_largeur-1)
      {
        Mur murDroite;
        murDroite.l1 = l;
        murDroite.c1 = c;
        murDroite.l2 = l;
        murDroite.c2 = c+1;
        tab_murs.inserer(murDroite);
      }
    }
  }



  while(tab_murs.taille() != 0)
  {
    Mur m = tab_murs.retirer();
    int x1 = (uf.recherche(m.l1*largeur + m.c1));
    int x2 = (uf.recherche(m.l2*largeur + m.c2));

    if(x1 != x2)
    {
      uf.unionE(x1, x2);
      abattre_mur(m.l1, m.c1, m.l2, m.c2);
    }

  }

}

/********************************* Utilitaire *********************************/

Tuile Labyrinthe::tuile(
    int l, 
    int c
    ) const
{
  //gestion des tuiles extérieures au plateau
  if(l < 0) {
    if( l == -1 && c >= 0 && c < m_largeur ) return MUR_S ;
    return MUR_AUCUN ;
  }
  if(l >= m_hauteur) {
    if( l == m_hauteur && c >= 0 && c < m_largeur ) return MUR_N ;
    return MUR_AUCUN ;
  }
  if(c < 0) {
    if( c == -1 && l >= 0 && l < m_hauteur ) return MUR_E ;
    return MUR_AUCUN ;
  }
  if(c >= m_largeur) {
    if( c == m_largeur && l >= 0 && l < m_hauteur ) return MUR_O ;
    return MUR_AUCUN ;
  }

  //les tuiles du plateau sont stockées
  return m_tuiles[l*m_largeur + c] ;
}

/********************************** Affichage *********************************/

std::ostream& operator<<(
    std::ostream& out,
    const Labyrinthe& lab
    )
{
  /* caracteres pour l'affichage des murs */
#ifdef LABYRINTHE_AFFICHAGE_SIMPLE
  static const char* symboles_murs[18] = {
    " ", "-", " ", "+", "-", "-", "+", "+", " ",
    "+", "|", "+", "+", "+", "+", "+", "-" ,"|"
  } ;
#else
  static const char* symboles_murs[18] = {
    " ", 
    "\xe2\x95\xb6", "\xe2\x95\xb7", "\xe2\x95\xad", 
    "\xe2\x95\xb4", "\xe2\x94\x80", "\xe2\x95\xae", 
    "\xe2\x94\xac", "\xe2\x95\xb5", "\xe2\x95\xb0", 
    "\xe2\x94\x82", "\xe2\x94\x9c", "\xe2\x95\xaf", 
    "\xe2\x94\xb4", "\xe2\x94\xa4", "\xe2\x94\xbc", 
    "\xe2\x94\x80" ,"\xe2\x94\x82"
  } ;
#endif

  /*iteration sur les lignes*/
  for(int l = 0; l <= lab.m_hauteur; ++l) 
  {
    /* affichage des murs du haut */
    for(int c = 0; c <= lab.m_largeur; ++c) {
      char tl = lab.tuile(l-1,c-1) ;
      char br = lab.tuile(l,c) ;

      /* types des murs autour du coin, encode sur quatre bits */
      int type_coin = 0 ;
      if(tl & MUR_E) ++type_coin ;
      type_coin *= 2 ;
      if(tl & MUR_S) ++type_coin ;
      type_coin *= 2 ;
      if(br & MUR_O) ++type_coin ;
      type_coin *= 2 ;
      if(br & MUR_N) ++type_coin ;

      /* affichage du coin */
      out << symboles_murs[type_coin] ;

      /* mur du haut */
      if(c < lab.m_largeur) {
        if(br & MUR_N) {
          out << symboles_murs[16] << symboles_murs[16] ;
        } else {
          out <<  "  " ;
        }
      }
    }
    out <<  std::endl ;

    /* affichage des murs droite gauche */
    if(l < lab.m_hauteur) {
      for(int c = 0; c <= lab.m_largeur; ++c) {
        char br = lab.tuile(l,c) ;
        if(br & MUR_O) {
          out << symboles_murs[17] ;
        } else {
          out <<  " " ;
        }
        if(c < lab.m_largeur) {
          out << "  " ;
        }
      }
      out << std::endl ;
    }
  }

  return out ;
}

/*************************** Gestion des murs ********************************/

void Labyrinthe::abattre_tous_les_murs()
{
  //tous les murs sont abattus
  m_tuiles.assign(m_tuiles.size(),MUR_AUCUN) ;

  //murs au bord
  for(int t= 0; t < m_hauteur; ++t) {
    m_tuiles[t*m_largeur] |= MUR_O ;
    m_tuiles[(t+1)*m_largeur - 1] |= MUR_E ;
  }
  for(int t= 0; t < m_largeur; ++t) {
    m_tuiles[t] |= MUR_N ;
    m_tuiles[m_hauteur*m_largeur - t - 1] |= MUR_S ;
  }
}

void Labyrinthe::abattre_mur(int l1, int c1, int l2, int c2)
{
  //verification de la validite du mur
  assert(l1 >= 0 && l1 < m_hauteur) ;
  assert(l2 >= 0 && l2 < m_hauteur) ;
  assert(c1 >= 0 && c1 < m_largeur) ;
  assert(c2 >= 0 && c2 < m_largeur) ;
  assert(l1 == l2 || c1 == c2) ;

  //mur vertical
  if(l1 == l2) {
    int c = c1 < c2 ? c1 : c2 ;
    if(c1+c2-2*c == 1 && c >= 0 && c < m_largeur - 1) {
      m_tuiles[l1*m_largeur + c]   &= MUR_TOUS ^ MUR_E ;
      m_tuiles[l1*m_largeur + c+1] &= MUR_TOUS ^ MUR_O ;
    }
  }

  //mur horizontal
  if(c1 == c2) {
    int l = l1 < l2 ? l1 : l2 ;
    if(l1+l2-2*l == 1 && l >= 0 && l < m_hauteur - 1) {
      m_tuiles[l*m_largeur + c1]     &= MUR_TOUS ^ MUR_S ;
      m_tuiles[(l+1)*m_largeur + c1] &= MUR_TOUS ^ MUR_N ;
    }
  }
}

void Labyrinthe::monter_tous_les_murs()
{
  //tous les murs de toutes les tuiles sont debout
  m_tuiles.assign(m_tuiles.size(), MUR_TOUS) ;
}

void Labyrinthe::monter_mur(int l1, int c1, int l2, int c2)
{
  //verification de la validite du mur
  assert(l1 >= 0 && l1 < m_hauteur) ;
  assert(l2 >= 0 && l2 < m_hauteur) ;
  assert(c1 >= 0 && c1 < m_largeur) ;
  assert(c2 >= 0 && c2 < m_largeur) ;
  assert(l1 == l2 || c1 == c2) ;

  //mur vertical
  if(l1 == l2) {
    int c = c1 < c2 ? c1 : c2 ;
    if(c1+c2-2*c == 1 && c >= 0 && c < m_largeur - 1) {
      m_tuiles[l1*m_largeur + c]   |= MUR_E ;
      m_tuiles[l1*m_largeur + c+1] |= MUR_O ;
    }
  }

  //mur horizontal
  if(c1 == c2) {
    int l = l1 < l2 ? l1 : l2 ;
    if(l1+l2-2*l == 1 && l >= 0 && l < m_hauteur - 1) {
      m_tuiles[l*m_largeur + c1]     |= MUR_S ;
      m_tuiles[(l+1)*m_largeur + c1] |= MUR_N ;
    }
  }
}

bool Labyrinthe::mur(int l1, int c1, int l2, int c2) const
{
  //verification de la validite du mur
  assert(l1 >= 0 && l1 < m_hauteur) ;
  assert(l2 >= 0 && l2 < m_hauteur) ;
  assert(c1 >= 0 && c1 < m_largeur) ;
  assert(c2 >= 0 && c2 < m_largeur) ;
  assert(l1 == l2 || c1 == c2) ;

  //mur vertical
  if(l1 == l2) {
    int c = c1 < c2 ? c1 : c2 ;
    return tuile(l1,c)  & MUR_E ;
  }

  //mur horizontal
  if(c1 == c2) {
    int l = l1 < l2 ? l1 : l2 ;
    return tuile(l,c1) & MUR_S ;
  }

  //on ne doit jamais passer ici
  return false ;
}

