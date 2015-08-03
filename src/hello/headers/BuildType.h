// BuildType.h

#ifndef HELLO_BUILDTYPE_H
#define HELLO_BUILDTYPE_H

#include <string>

namespace BuildType {

    enum Flavor { COMMUNITY, ENTERPRISE };
    
    class FlavorMeta {
        public:
            FlavorMeta();
            FlavorMeta(Flavor);
            std::string getName();
        private:
            std::string name;
    };

    Flavor getFlavor();

}

#endif //HELLO_BUILDTYPE_H
