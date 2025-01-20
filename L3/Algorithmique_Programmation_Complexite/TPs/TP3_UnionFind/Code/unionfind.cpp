#include "unionfind.hpp"


UnionFind::UnionFind(int n)
{
    for(int i = 0; i<n; i++)
    {
        parents.push_back(i);
    }
}


void UnionFind::unionE(int A, int B)
{
    int parentA = recherche(A);
    int parentB = recherche(B);

    if (parentA != parentB)
    {
        parents[parentB] = parentA; //racine de A devient la racine de B
    }
}



int UnionFind::recherche(int x)
{
    if(x == parents[x])
    {
        return x;
    } else {
        int racine = recherche(parents[x]);     //stockage de la racine
        parents[x] = racine;                    // chaque x prend comme parent la racine
        return racine;                          //return la racine
    }

}

