// support.cpp

#include "support.h"
#include "Hello.h"

extern "C" {
    void hello_sayHello(const char * who, unsigned n) {
        Hello hello (who);
        hello.sayHello(n);
    }
}

