class AutomatonTransition(val currentState:String,val inputSymbol:Char,val resultStates:List<String>)

class Automaton(private val transitions: List<AutomatonTransition>,val startStates:Set<String>,val endStates:Set<String>) {

    fun transitionFunction(state:String,symbol:Char):List<String> {
        return transitions.firstOrNull {it->it.currentState==state && it.inputSymbol==symbol }?.resultStates ?: listOf()
    }

    fun maxString(string: String,k:Int):Pair<Boolean,Int> {
        var result=Pair(startStates.any{it->endStates.contains(it)},0)
        var currentStates= startStates
            for (i in k until string.length) {
                val newStates= mutableSetOf<String>()
                for(results in currentStates.map { it->transitionFunction(it,string[i]) })
                   newStates.addAll(results)
                currentStates=newStates
                if(currentStates.any { it->endStates.contains(it) })
                    result=Pair(true,i-k+1)
            }
        return result
    }

    fun stringSearching(str:String):List<String> {
        val result = mutableListOf<String>()
        var i = 0
        while (i < str.length) {
            val check = maxString(str, i)
            if (check?.first == true) {
                result.add(str.substring(i..i + check.second - 1))
                i += check.second
            } else
                i++

        }
        return  result
    }
}