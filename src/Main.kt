
fun main(args : Array<String>) {
    var readerXML=AutomatonXmlReader()
    println(readerXML.readAutomaton("C:\\Users\\dangiz\\Desktop\\New Text Document.xml").transitionFunction("3",'a'))
}