class AutomatonTransition(val currentState:String,val inputSymbol:String,var resultStates:List<String>)

//Автомат определяется тройкой: набор переходов, начальных и конечных состояний
class Automaton(var name:String,val transitions: List<AutomatonTransition>, val startStates:Set<String>, val endStates:Set<String>) {

    //Флаги для оптимизации использования ключевых слов в xml
    private var digitKeywordFlag:Boolean
    private var digitNotZeroKeywordFlag:Boolean
    private var alphaKeywordFlag:Boolean
    private var anyKeywordFlag:Boolean

    init {
        digitKeywordFlag=transitions.any{it->it.inputSymbol=="#digit"}
        digitNotZeroKeywordFlag=transitions.any{it->it.inputSymbol=="#digitNotZero"}
        alphaKeywordFlag=transitions.any{it->it.inputSymbol=="#alpha"}
        anyKeywordFlag=transitions.any{it->it.inputSymbol=="#notBrace"}

    }



    fun transitionFunction(state:String,symbol:Char):List<String> {
        val resultStates= mutableListOf<String>()
        //Далее проверки для использования ключевых слов, обозначающих множество входных символов для переходов автомата
        //Обеспечивает использование к. слова #digitNoneZero, обозначающего все цифры от 1 до 9
        if(digitKeywordFlag && symbol.isDigit() )
            resultStates.addAll(transitions.firstOrNull{it->it.currentState==state && it.inputSymbol=="#digit"}?.resultStates ?: listOf())
        //Обеспечивает использование к. слова #digit, обозначающего все цифры
        if(digitNotZeroKeywordFlag && CharRange('1','9').contains(symbol))
            resultStates.addAll(transitions.firstOrNull{it->it.currentState==state && it.inputSymbol=="#digitNotZero"}?.resultStates ?: listOf())
        //Обеспечивает использование к. слова #alpha, обозначающего все буквы
        if(alphaKeywordFlag && symbol.isLetter())
            resultStates.addAll(transitions.firstOrNull{it->it.currentState==state && it.inputSymbol=="#alpha"}?.resultStates ?: listOf())
        //Конец проверок. Добавление всех остальных переходов по введенному символу
        if(anyKeywordFlag && symbol!='}')
            resultStates.addAll(transitions.firstOrNull{it->it.currentState==state && it.inputSymbol=="#notBrace"}?.resultStates ?: listOf())
        resultStates.addAll(transitions.firstOrNull {it->it.currentState==state && it.inputSymbol==symbol.toString() }?.resultStates ?: listOf())
        return resultStates
    }

    //Вывод самого длинного соотвтетствия автомату в строке+флаг
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


    //Вывод всех подстрок строки, удовлетворяющих поведению атвомата
    fun stringSearching(str:String):List<String> {
        val result = mutableListOf<String>()
        var i = 0
        while (i < str.length) {
            val check = maxString(str, i)
            if (check?.first) {
                result.add(str.substring( i.until(i + check.second) ))
                i += check.second
            } else
                i++

        }
        return  result
    }
}

fun Concatenate(A:Automaton?,B:Automaton?):Automaton? {
    if(A==null && B==null)
        return null
    if(A==null)
        return B
    if(B==null)
        return A

    var newTransitions= mutableListOf<AutomatonTransition>()
    newTransitions.addAll(B.transitions)
    newTransitions.addAll(A.transitions.map{
        automatonTransition -> if(automatonTransition.resultStates.any{s ->A.endStates.contains(s)  }) {
            val transitions= automatonTransition.resultStates.toMutableList()
            transitions.addAll(B.startStates)
            AutomatonTransition(automatonTransition.currentState,automatonTransition.inputSymbol,transitions)
        }
        else automatonTransition
    })

    return Automaton("",newTransitions,A.startStates,B.endStates)
}

fun Iteration(A:Automaton):Automaton {
    var endStates= mutableSetOf<String>()
    endStates.addAll(A.endStates)
    endStates.addAll(A.startStates)

    var newTransitions= mutableListOf<AutomatonTransition>()
    newTransitions.addAll(A.transitions.map{
        automatonTransition -> if(automatonTransition.resultStates.any{s ->A.endStates.contains(s)  }) {
        val transitions= automatonTransition.resultStates.toMutableList()
        transitions.addAll(A.startStates)
        AutomatonTransition(automatonTransition.currentState,automatonTransition.inputSymbol,transitions)
    }
    else automatonTransition
    })

    return Automaton(A.name,newTransitions,A.startStates,endStates)
}

fun Union(A:Automaton?,B: Automaton?):Automaton? {
    if(A==null && B==null)
        return null
    if(A==null)
        return B
    if(B==null)
        return A

    var newTransitions= mutableListOf<AutomatonTransition>()


    newTransitions.addAll(A.transitions)
    for(transition in B.transitions) {
        var eqTran=newTransitions.firstOrNull{newTransition->newTransition.currentState==transition.currentState && newTransition.inputSymbol==transition.inputSymbol }
        if(eqTran!=null)
        {
            val temp=eqTran.resultStates.toMutableList()
            temp.addAll(transition.resultStates)
            eqTran.resultStates=temp
        }
        else
        {
            newTransitions.add(transition)
        }
    }
    val newStartStates= mutableSetOf<String>()
    newStartStates.addAll(A.startStates)
    newStartStates.addAll(B.startStates)
    val newEndStates= mutableSetOf<String>()
    newEndStates.addAll(A.endStates)
    newEndStates.addAll(B.endStates)
    return Automaton("${A.name} ${B.name} auto",newTransitions,newStartStates,newEndStates)
}

fun GenerateAutoByString(string:String,autoName:String):Automaton{
    var startStates= setOf("[$autoName] $string start")
    var endStates= setOf("[$autoName] $string end")
    var currentState=startStates.first()
    var transitions= mutableListOf<AutomatonTransition>()
    var i=0
    while(i <string.length-1)
    {
        var symbol=string[i].toString()
        if(symbol=="/") {
            if(string[i+1].toString()=="d")
                symbol="#digit"
            if(string[i+1].toString()=="a")
                symbol="#alpha"
            i++

            if(i==string.length-1) {
                transitions.add(AutomatonTransition(currentState, symbol, endStates.toList()))
                return Automaton(autoName, transitions, startStates, endStates)
            }
        }
        val resultState = "[$autoName] $currentState : $symbol founded in word $string"
        transitions.add(AutomatonTransition(currentState, symbol, listOf(resultState)))
        currentState = resultState
        i++
    }

    val symbol=string.last().toString()
    transitions.add(AutomatonTransition(currentState,symbol, endStates.toList()))
    return Automaton(autoName,transitions,startStates,endStates)
}




