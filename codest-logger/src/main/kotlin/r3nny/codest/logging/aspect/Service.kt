package r3nny.codest.logging.aspect

import r3nny.codest.shared.annotation.LogMethod

class Service {

    @LogMethod
    fun someWork(){
        print("someWork")
    }
}