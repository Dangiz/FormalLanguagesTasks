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
}