
fun main(args : Array<String>) {
    var readerXML=AutomatonXmlReader()
    println(readerXML.readAutomaton("C:\\Users\\dangiz\\Desktop\\New Text Document.xml").maxString("aaa",0, listOf("3")))
}