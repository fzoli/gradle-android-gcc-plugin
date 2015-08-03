// BuildType.cpp

#include "BuildType.h"

namespace BuildType {

    Flavor getFlavor() {
        #ifdef FLAVOR_ENTERPRISE
            return ENTERPRISE;
        #else
            return COMMUNITY;
        #endif
    }

    FlavorMeta::FlavorMeta() : FlavorMeta(getFlavor()) {
    }

    FlavorMeta::FlavorMeta(Flavor flavor) {
        switch (flavor) {
            case ENTERPRISE:
                name = "Enterprise";
                break;
            default:
                name = "Community";
        }
    }

    std::string FlavorMeta::getName() {
        return name;
    }

}
