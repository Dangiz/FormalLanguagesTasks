
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


class AutomatonXmlReader {

    private fun getTransitionsByStateNode(node: Node):List<AutomatonTransition> {
        val stateName=node.attributes.getNamedItem("name").nodeValue
        val transitions= mutableListOf<AutomatonTransition>()
        for(i in 1 until node.childNodes.length step 2)
            transitions.add(AutomatonTransition(
                    stateName,
                    node.childNodes.item(i).attributes.getNamedItem("symbol").nodeValue,
                    getTransitionsListByNode(node.childNodes.item(i))))
        return transitions
    }

    private fun getTransitionsListByNode(node:Node):List<String>{
        val states= mutableListOf<String>()
        for(i in 1 until node.childNodes.length step 2)
            states.add(node.childNodes.item(i).attributes.getNamedItem("name").nodeValue)
        return states
    }

    private fun getStartEndStatesByNode(node:Node):Pair<List<String>,List<String>>{
        val result=Pair(mutableListOf<String>(), mutableListOf<String>())
        for(i in 1 until node.childNodes.length step 2) {
            val attr=node.childNodes.item(i).attributes
            val name=attr.getNamedItem("name").nodeValue
            val startFlag= attr.getNamedItem("startState")?.nodeValue?.toBoolean()
            val endFlag= attr.getNamedItem("endState")?.nodeValue?.toBoolean()
            if(startFlag==true)
                result.first.add(name)
            if(endFlag==true)
                result.second.add(name)
        }
        return result
    }

    fun readAutomaton(filePath:String):Automaton? {
        if(File(filePath).exists()) {
            val doc = getXmlDocument(filePath)
                val automatonNode = doc.getElementsByTagName("automaton").item(0)

                val transitions = mutableListOf<AutomatonTransition>()
                for (i in 1 until automatonNode.childNodes.length step 2)
                    transitions.addAll(getTransitionsByStateNode(automatonNode.childNodes.item(i)))
                val criticalStates=getStartEndStatesByNode(automatonNode)
                return Automaton(transitions,
                        criticalStates.first.toSet(),
                        criticalStates.second.toSet())
        }
        else return null
    }

    private fun getXmlDocument(filePath: String): Document {
        return  DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(InputSource(StringReader(File(filePath).readText())))
    }
}