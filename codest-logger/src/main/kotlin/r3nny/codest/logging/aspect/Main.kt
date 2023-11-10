package r3nny.codest.logging.aspect

class Main {

    @LogMethod
    fun someWork(work: String) : String{
        return "Hello $work!"
    }


}

fun main(args: Array<String>) {
       Main().someWork("work")
    }