import java.io.File

private fun charIsOperator(char:Char):Boolean {
    return char=='|' || char=='(' || char==')' || char=='*'
}

private fun findRightBracket(string: String, leftBracketPoisition :Int):Int{
    var bracketCount=1
    var bracketId=-1
    for(i in leftBracketPoisition+1 until string.length) {
        if(string[i]==')') {
            bracketCount--
        }
        else if(string[i]=='(')
            bracketCount++
        if(bracketCount==0) {
            bracketId=i
            break
        }
    }
    return bracketId
}

fun findNextUnionOp(regex: String):Int{
    var i=0
    while(i<regex.length){
        if(regex[i]=='|')
            return i
        if(regex[i]=='(')
            i=findRightBracket(regex,i)+1
        else i++
    }
    return -1
}

fun generateAutoByRegex(regex:String, autoName:String):Automaton? {
    if(regex.isEmpty())
        return null
    var resultAuto:Automaton?=null

    //Если строка не содержит операторов
    if(regex.length==1 || !regex.any{c ->charIsOperator(c)}){
        return GenerateAutoByString(regex,"$autoName string $regex")
    }

    //Если строка содержит оператор объединения на текущем уровне
    var unionId=findNextUnionOp(regex)
    if(unionId!=-1) {
        return Union(generateAutoByRegex(regex.slice(0 until unionId),"$autoName | left")!!,
                generateAutoByRegex(regex.slice(unionId+1 until regex.length),"$autoName | right")!!)
    }

    var firstOpId=regex.indexOf(regex.find { c->charIsOperator(c) }!!)

    if(regex[firstOpId]=='*') {
        when(firstOpId) {
            1 ->resultAuto=Iteration(GenerateAutoByString(regex[firstOpId - 1].toString(),"$autoName iterableSymbol"))
            else->resultAuto = Concatenate(
                    GenerateAutoByString(regex.slice(0 until firstOpId - 1),"$autoName preIteration string"),
                    Iteration(GenerateAutoByString(regex[firstOpId - 1].toString(),"$autoName iterableSymbol")))
        }

    }

    else if(regex[firstOpId]=='(') {
        var bracketId=findRightBracket(regex,firstOpId)

        if(bracketId==-1)
            return null
        resultAuto=generateAutoByRegex(regex.slice(firstOpId+1 until bracketId),"$autoName brackets inside")

        if(bracketId<regex.length-1 && regex[bracketId+1]=='*') {
            bracketId++
            resultAuto = Iteration(resultAuto!!)
        }

        if(firstOpId>0)
            resultAuto=Concatenate(GenerateAutoByString(regex.slice(0 until firstOpId),"$autoName pre brackets"),resultAuto)
        firstOpId=bracketId
    }

    if(firstOpId!=regex.length-1)
        return Concatenate(resultAuto,generateAutoByRegex(regex.slice(firstOpId+1 until regex.length),"$autoName % "))
    else
        return resultAuto
}

fun readRegexes(fileAddress:String):List<Automaton> {
    var list= mutableListOf<Pair<Int,Automaton>>()
    val lines=File(fileAddress).readLines()
    for(str in lines)
    {
        val split=str.split(" ")
        val  auto:Automaton
        auto=generateAutoByRegex(split[2],split[1])!!
        auto.name=split[1]
        list.add(Pair(split[0].toInt(),auto))
    }
    list.sortBy { pair->pair.first }
    return list.map { pair->pair.second }
}