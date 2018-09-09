
fun main(args : Array<String>) {
    var readerXML=AutomatonXmlReader()
    val auto=readerXML.readAutomaton("C:\\Users\\dangiz\\Documents\\GitHub\\FormalLanguagesTasks\\resources\\IntMachine.xml").maxString("+21111",0, listOf("digitFounded"))
    println(auto)
}