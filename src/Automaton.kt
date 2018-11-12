class AutomatonTransition(val currentState:String,val inputSymbol:String,val resultStates:List<String>)

//Автомат определяется тройкой: набор переходов, начальных и конечных состояний
class Automaton(val name:String,private val transitions: List<AutomatonTransition>, val startStates:Set<String>, val endStates:Set<String>) {

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
        if(digitKeywordFlag && CharRange('1','9').contains(symbol))
            resultStates.addAll(transitions.firstOrNull{it->it.currentState==state && it.inputSymbol=="#digitNotZero"}?.resultStates ?: listOf())
        //Обеспечивает использование к. слова #digit, обозначающего все цифры
        if(digitNotZeroKeywordFlag && symbol.isDigit())
            resultStates.addAll(transitions.firstOrNull{it->it.currentState==state && it.inputSymbol=="#digit"}?.resultStates ?: listOf())
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