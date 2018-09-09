class AutomatonTransition(val currentState:String,val inputSymbol:Char,val resultStates:List<String>);

class Automaton(private val transitions: List<AutomatonTransition>,private val startState:String) {
    fun transitionFunction(state:String,symbol:Char):List<String> {
        return transitions.first {it->it.currentState==state && it.inputSymbol==symbol }.resultStates
    }
    fun maxString(string: String,k:Int,endStates:List<String>):Pair<Boolean,Int> {
        var result=Pair(false,0)
        var currentStates= listOf(startState)
            for (i in k until string.length) {
                val newStates=mutableListOf<String>()
                for(results in currentStates.map { it->transitionFunction(it,string[i]) })
                   newStates.addAll(results)
                if(newStates.any { it->endStates.contains(it) })
                    result=Pair(true,i-k+1)
                currentStates=newStates.filter { it->!endStates.contains(it) }
            }
        return result
    }
}