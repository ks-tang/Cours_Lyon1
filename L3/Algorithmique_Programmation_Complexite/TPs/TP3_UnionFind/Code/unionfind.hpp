#ifndef LIFAP6_UNIONFIND_HPP_
#define LIFAP6_UNIONFIND_HPP_

#include <vector>


class UnionFind {

    public:
        UnionFind(int n);
        void unionE(int a, int b);
        int recherche(int x);


    private:
        std::vector<int> parents;


};


#endif