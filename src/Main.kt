        fun main(args: Array<String>) {

            println(AutomatonXmlReader()
                    .readAutomaton("C:\\Users\\GizatullinDA\\Downloads\\FormalLanguagesTasks-master\\resources\\FloatMachine.xml")
                    ?.stringSearching("qweqwe12.qweq+-123eeqwe4124adl435kasf+12.5.4e+43"))
            //undeterminitedTest()
        }

        fun floatMachineTest() {
            val readerXML = AutomatonXmlReader()
            val autoResult = readerXML.readAutomaton("C:\\Users\\GizatullinDA\\Downloads\\FormalLanguagesTasks-master\\resources\\FloatMachine.xml")
                    ?.maxString("-192.168e314", 0)
            println(autoResult)
        }

        fun undeterminitedTest() {
            val readerXML = AutomatonXmlReader()
            val autoResult = readerXML.readAutomaton("C:\\Users\\GizatullinDA\\Downloads\\FormalLanguagesTasks-master\\resources\\NonDeterminited.xml")
                    ?.transitionFunction("2",'a')
            println(autoResult)
        }