// Hello.cpp

#include "Hello.h"
#include "BuildType.h"
#include <iostream>

using namespace std;

Hello::Hello(const char * who)
{
	this->who = who;
}

void Hello::sayHello( unsigned n )
{
        BuildType::FlavorMeta meta;
	for (unsigned i=0; i<n; i++)
	{
		cout << (i+1) << ".\tHello Mr. " << who << "\t(" << meta.getName() << ")" << endl;
	}
}
