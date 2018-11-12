import java.io.File


fun main(args: Array<String>) {
            lexicalAnalysis("resources/testData/Programm_1.txt")
        }

        //Проверка поиска всех вещественных чисел в указанной строке
        fun floatMachineTest() {
            println(AutomatonXmlReader()
                    .readAutomaton("C:\\Users\\GizatullinDA\\Documents\\GitHub\\FormalLanguagesTasks\\resources\\FloatMachine.xml")
                    ?.stringSearching("qweqwe12.qweq+-123eeqwe4124adl435kasf+12.5.4e+43"))
        }

        fun lexicalAnalysis(address:String) {
            //Используемые автоматы
            val autos= listOf(
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
            val str=File(address).readText(Charsets.UTF_8)
            var i = 0

            //Проход по строке
            while (i < str.length) {
                var maxMatch= autos.map { automaton -> Pair(automaton?.maxString(str,i),automaton?.name)}
                        .filter { pair -> pair.first?.first ?:false  }
                        .map { pair->Pair(pair.second,pair.first?.second?:0) }
                        .maxBy { pair ->pair.second  }

                if (maxMatch != null) {
                    println("<"+maxMatch.first+"|"+str.slice(i until i+maxMatch.second)+">")
                    i+=maxMatch.second
                }
                else
                    i++

            }

        }

        //Проверка работы недетерминированного автомата
        fun undeterminitedTest() {
            val readerXML = AutomatonXmlReader()
            val autoResult = readerXML.readAutomaton("C:\\Users\\GizatullinDA\\Downloads\\FormalLanguagesTasks-master\\resources\\NonDeterminited.xml")
                    ?.transitionFunction("2",'a')
            println(autoResult)
        }

        fun keywordTest() {
            val readerXML = AutomatonXmlReader()
            var autoResult=readerXML
                    .readAutomaton("resources/KeyWordAutomaton.xml")
                    ?.stringSearching("var founded on program is boolean for string:= 0")
            println(autoResult)
        }

        fun eqTest() {
            val readerXML = AutomatonXmlReader()
            var autoResult=readerXML
                    .readAutomaton("resources/OperationEqAutomaton.xml")
                    ?.stringSearching("var founded on program is boolean for string:= 0")
            println(autoResult)
        }

