
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.coroutines.experimental.buildSequence


class AutomatonXmlReader() {

    private fun getTransitionsByStateNode(node: Node):List<AutomatonTransition> {
        val stateName=node.attributes.getNamedItem("name").nodeValue
        val transitions= mutableListOf<AutomatonTransition>()
        for(i in 1 until node.childNodes.length step 2)
            transitions.add(AutomatonTransition(
                    stateName,
                    node.childNodes.item(i).attributes.getNamedItem("symbol").nodeValue[0],
                    getTransitionsListByNode(node.childNodes.item(i))))
        return transitions
    }

    private fun getTransitionsListByNode(node:Node):List<String>{
        val states= mutableListOf<String>()
        for(i in 1 until node.childNodes.length step 2)
            states.add(node.childNodes.item(i).attributes.getNamedItem("name").nodeValue)
        return states
    }

    fun readAutomaton(filePath:String):Automaton {
        val doc = getXmlDocument(filePath)
        val automatonNode=doc.getElementsByTagName("automaton").item(0)

        val transitions= mutableListOf<AutomatonTransition>()
        for(i in 1 until automatonNode.childNodes.length step 2)
            transitions.addAll(getTransitionsByStateNode(automatonNode.childNodes.item(i)))
        return Automaton(transitions)
    }

    private fun getXmlDocument(filePath: String): Document {
        return DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(InputSource(StringReader(File(filePath).readText())))
    }
}