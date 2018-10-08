        fun main(args: Array<String>) {
            keywordTest()
            eqTest()
            floatMachineTest()
        }

        //Проверка поиска всех вещественных чисел в указанной строке
        fun floatMachineTest() {
            println(AutomatonXmlReader()
                    .readAutomaton("C:\\Users\\GizatullinDA\\Documents\\GitHub\\FormalLanguagesTasks\\resources\\FloatMachine.xml")
                    ?.stringSearching("qweqwe12.qweq+-123eeqwe4124adl435kasf+12.5.4e+43"))
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

