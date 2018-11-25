import java.io.File


fun main(args: Array<String>) {
    lexicalAnalysis("resources/testData/Programm_1.txt")
    //regexGenerationTest()
}

//(+|-)(1|2|3|4|5|6|7|8|9)(.(0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)*)*

fun regexGenerationTest() {
    var result=GenerateAutoByRegex("(/a)((/a)|(/d))*","reg")
    print(result?.maxString("124",0))
}

fun UnionTest(){
    val A=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/KeyWordAutomaton.xml")
    val B=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/OperationEqAutomaton.xml")
    var result=Union(A!!,B!!)
    print(result?.maxString(":=",0))
}

fun concatenateTest() {
            val A=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/KeyWordAutomaton.xml")
            val B=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/OperationEqAutomaton.xml")
            val C=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/numbersAutomaton.xml")
            var result=Concatenate(A!!,B!!)
            result=Concatenate(result,C!!)
            print(result?.maxString("var:=100",0))
}

fun iterationTest() {
    val A=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/KeyWordAutomaton.xml")
    var result=Iteration(A!!)
    print(result.maxString("",0))
}

fun stringAutoTest() {
    val A=GenerateAutoByString("A","A")
    print(A?.maxString("Abbabc",0))
}

fun lexicalAnalysis(address:String) {
    var kw=GenerateAutoByRegex("program|var|real|string|boolean|if|then|else|for|to|do|begin|end","kw")
    kw?.name="kw"
    var id=GenerateAutoByRegex("(/a)((/a)|(/d))*","id")
    id?.name="id"
    var lb=GenerateAutoByString("(","lb")
    lb?.name="lb"
    //Используемые автоматы
    val autos = listOf(
            AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/CommentAutomaton.xml"),
            kw,
            id,
            lb,
            AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/numbersAutomaton.xml"),
            AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/OperationEqAutomaton.xml"),
            AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/OperationAutomaton.xml"),
            AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/rightBracketAutomaton.xml"),
            AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/semicolonAutomaton.xml"),
            AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/stringAutomaton.xml"))

    //Тестовая строка
    val str = File(address).readText(Charsets.UTF_8)
    var i = 0

    //Проход по строке
    while (i < str.length) {
        var maxMatch = autos.map { automaton -> Pair(automaton?.maxString(str, i), automaton?.name) }
                .filter { pair -> pair.first?.first ?: false }
                .map { pair -> Pair(pair.second, pair.first?.second ?: 0) }
                .maxBy { pair -> pair.second }

        if (maxMatch != null) {
            println("<" + maxMatch.first + "|" + str.slice(i until i + maxMatch.second) + ">")
            i += maxMatch.second
        } else
            i++

    }
}

