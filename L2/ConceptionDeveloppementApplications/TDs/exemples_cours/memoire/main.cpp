#include <stdlib.h>
#include <iostream>
using namespace std;


void f1(void)
{
    int* x = new int[10];
    int* y = new int[10];

    x[10] = 614;

    cout<<x[10]<<endl;
}


void f2()
{
    int* p = new int[10];
    for(int i=0;i<10;++i)
        p[i] = i;
//    delete[] p;


    for(int i=0;i<10;++i) 
        cout<<"p["<<i<<"]="<<p[i]<<endl;
}


int main(void)
{
    //f1();
    f2();
    cout<<"fin"<<endl;
    return 0;
}
