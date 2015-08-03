// Hello.h

#ifndef HELLO_HELLO_H
#define HELLO_HELLO_H

class Hello
{
    private:
        const char * who;
    public:
        Hello(const char * who);
        void sayHello(unsigned n = 1);
};

#endif //HELLO_HELLO_H
