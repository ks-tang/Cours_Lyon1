#include "unionfind.hpp"

UnionFind::UnionFind(int n){
    for(int i=0; i<n; i++){
        parents.push_back(i);
    }
}

void UnionFind::unionE(int A, int B){
    int parentA = recherche(A);
    int parentB = recherche(B);

    if(parentA != parentB){
        parents[parentB] = parentA;
    }
}

int UnionFind::recherche(int x){
    if(x == parents[x]){
        return x;
    }
    else {
        int racine = recherche(parents[x]);
        parents[x] = racine;
        return racine;
    }
}

int UnionFind::getParents(int i){
    return parents[i];
}

void UnionFind::setParents(int i){
    parents.push_back(i);
}