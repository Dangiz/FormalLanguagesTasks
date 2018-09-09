class AutomatonTransition(val currentState:String,val inputSymbol:Char,val resultStates:List<String>);

class Automaton(private val transitions: List<AutomatonTransition>)
{
    fun transitionFunction(state:String,symbol:Char):List<String>
    {
        return transitions.first {it->it.currentState==state && it.inputSymbol==symbol }.resultStates
    }
}