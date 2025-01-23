

#include <stdlib.h>
#include <iostream>
#include <cstring>
using namespace std;




int main(int argc, char** argv)
{
    int i;
    cout<<"nb arg="<<argc<<endl;

    for(i=0;i<argc;i++)
    {
        cout<<i<<" "<<argv[i]<<endl;
        if (strcmp(argv[i],"bonjour")==0)
            cout<<"BONJOUR"<<endl;
        if (strcmp(argv[i],"--test")==0)
            cout<<"App en mode TEST"<<endl;
    }
    
    return 0;
}
