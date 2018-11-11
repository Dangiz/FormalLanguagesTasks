        fun main(args: Array<String>) {
            lexicalAnalysis()
        }

        //Проверка поиска всех вещественных чисел в указанной строке
        fun floatMachineTest() {
            println(AutomatonXmlReader()
                    .readAutomaton("C:\\Users\\GizatullinDA\\Documents\\GitHub\\FormalLanguagesTasks\\resources\\FloatMachine.xml")
                    ?.stringSearching("qweqwe12.qweq+-123eeqwe4124adl435kasf+12.5.4e+43"))
        }

        fun lexicalAnalysis() {
            //Используемые автоматы
            val autos= listOf(
                    AutomatonXmlReader().readAutomaton("resources/KeyWordMachine.xml"),
                    AutomatonXmlReader().readAutomaton("resources/IdMachine.xml"),
                    AutomatonXmlReader().readAutomaton("resources/OperationEqMachine.xml"),
                    AutomatonXmlReader().readAutomaton("resources/FloatMachine.xml")
            )

            //Тестовая строка
            val str="var founded on program is boolean for string:= 10"
            var i = 0

            //Проход по строке
            while (i < str.length) {
                var maxMatch= autos.map { automaton -> Pair(automaton?.maxString(str,i),automaton?.name)}
                        .filter { pair -> pair.first?.first ?:false  }
                        .map { pair->Pair(pair.second,pair.first?.second?:0) }
                        .maxBy { pair ->pair.second  }

                if (maxMatch != null) {
                    println("<"+maxMatch.first+":"+str.slice(i until i+maxMatch.second)+">")
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
                    .readAutomaton("resources/KeyWordMachine.xml")
                    ?.stringSearching("var founded on program is boolean for string:= 0")
            println(autoResult)
        }

        fun eqTest() {
            val readerXML = AutomatonXmlReader()
            var autoResult=readerXML
                    .readAutomaton("resources/OperationEqMachine.xml")
                    ?.stringSearching("var founded on program is boolean for string:= 0")
            println(autoResult)
        }

