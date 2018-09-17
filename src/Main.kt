        fun main(args: Array<String>) {
            floatMachineUnitTest()
            //undeterminitedUnitTest()
        }

        fun floatMachineUnitTest() {
            val readerXML = AutomatonXmlReader()
            val autoResult = readerXML.readAutomaton("C:\\Users\\GizatullinDA\\Downloads\\FormalLanguagesTasks-master\\resources\\FloatMachine.xml")
                    ?.maxString("-192.168e314", 0)
            println(autoResult)
        }

        fun undeterminitedUnitTest() {
            val readerXML = AutomatonXmlReader()
            val autoResult = readerXML.readAutomaton("C:\\Users\\GizatullinDA\\Downloads\\FormalLanguagesTasks-master\\resources\\NonDeterminited.xml")
                    ?.transitionFunction("2",'a')
            println(autoResult)
        }