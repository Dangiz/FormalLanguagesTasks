import java.io.File


fun main(args: Array<String>) {
            //lexicalAnalysis("resources/testData/Programm_1.txt")
            regexGenerationTest()
    print("abb".slice(1 until 0))

        }

fun regexGenerationTest() {
    var result=GenerateAutoByRegex(":=a*b")
    print(result?.maxString(":=aaaaab",0))
}

fun concatenateTest() {
            val A=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/KeyWordAutomaton.xml")
            val B=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/OperationEqAutomaton.xml")
            val C=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/numbersAutomaton.xml")
            var result=Concatenate(A!!,B!!)
            result=Concatenate(result,C!!)
            print(result.maxString("var:=100",0))
}

fun iterationTest() {
    val A=AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/KeyWordAutomaton.xml")
    var result=Iteration(A!!)
    print(result.maxString("",0))
}

fun stringAutoTest() {
    val A=GenerateAutoByString("A")
    print(A?.maxString("Abbabc",0))
}

fun lexicalAnalysis(address:String) {
            //Используемые автоматы
            val autos = listOf(
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/CommentAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/KeyWordAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/idAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/leftBracketAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/numbersAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/OperationEqAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/OperationAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/rightBracketAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/semicolonAutomaton.xml"),
                    AutomatonXmlReader().readAutomaton("resources/lexicalAnalysis/stringAutomaton.xml")
            )

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

