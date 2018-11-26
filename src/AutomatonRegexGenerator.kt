import java.io.File

fun CharIsOperator(char:Char):Boolean {
    return char=='|' || char=='(' || char==')' || char=='*'
}

fun FindRightBracket(string: String,leftBracketPoisition :Int):Int{
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

fun FindNextUnionOp(regex: String):Int{
    var i=0
    while(i<regex.length){
        if(regex[i]=='|')
            return i
        if(regex[i]=='(')
            i=FindRightBracket(regex,i)+1
        else i++
    }
    return -1
}

fun GenerateAutoByRegex(regex:String,autoName:String):Automaton? {
    if(regex.isEmpty())
        return null
    var resultAuto:Automaton?=null

    //Если строка не содержит операторов
    if(!regex.any{c ->CharIsOperator(c)}){
        return GenerateAutoByString(regex,"$autoName string $regex")
    }

    //Если строка содержит оператор объединения на текущем уровне
    var unionId=FindNextUnionOp(regex)
    if(unionId!=-1) {
        return Union(GenerateAutoByRegex(regex.slice(0 until unionId),"$autoName | left")!!,
                GenerateAutoByRegex(regex.slice(unionId+1 until regex.length),"$autoName | right")!!)
    }

    var firstOpId=regex.indexOf(regex.find { c->CharIsOperator(c) }!!)

    if(regex[firstOpId]=='*') {
        when(firstOpId) {
            1 ->resultAuto=Iteration(GenerateAutoByString(regex[firstOpId - 1].toString(),"$autoName iterableSymbol"))
            else->resultAuto = Concatenate(
                    GenerateAutoByString(regex.slice(0 until firstOpId - 1),"$autoName preIteration string"),
                    Iteration(GenerateAutoByString(regex[firstOpId - 1].toString(),"$autoName iterableSymbol")))
        }

    }

    else if(regex[firstOpId]=='(') {
        var bracketId=FindRightBracket(regex,firstOpId)

        if(bracketId==-1)
            return null
        resultAuto=GenerateAutoByRegex(regex.slice(firstOpId+1 until bracketId),"$autoName brackets inside")

        if(bracketId<regex.length-1 && regex[bracketId+1]=='*') {
            bracketId++
            resultAuto = Iteration(resultAuto!!)
        }

        if(firstOpId>0)
            resultAuto=Concatenate(GenerateAutoByString(regex.slice(0 until firstOpId),"$autoName pre brackets"),resultAuto)
        firstOpId=bracketId
    }

    if(firstOpId!=regex.length-1)
        return Concatenate(resultAuto,GenerateAutoByRegex(regex.slice(firstOpId+1 until regex.length),"$autoName % "))
    else
        return resultAuto
}


fun ReadRegexes(fileAddress:String):List<Automaton> {
    var list= mutableListOf<Automaton>()
    val lines=File(fileAddress).readLines()
    for(str in lines)
    {
        val split=str.split(" ")
        val  auto:Automaton
        if(split[1].length==1)
          auto=GenerateAutoByString(split[1],split[0])
           else
            auto=GenerateAutoByRegex(split[1],split[0])!!
        auto?.name=split[0]
        list.add(auto!!)
    }
    return list
}