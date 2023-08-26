/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/


package com.gridnine.codeflow.info;


import com.gridnine.codeflow.utils.XSUtil;

import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InfoBuilder {


    private NodeInfo currentNode;

    private final NodeInfo rootNode;

    public InfoBuilder(final String routeId) {
        currentNode = new NodeInfo(routeId, null, "route", null);
        rootNode = currentNode;
    }

    public NodeInfo getRootNode() {
        return rootNode;
    }

    public void startElement(final String elementId, final String type, String information) {
        currentNode = new NodeInfo(elementId, currentNode, type, information);
    }


    public void endBlock() {
        if (currentNode == null) {
            return;
        }
        currentNode = currentNode.parentNode;

    }

    private void addElement(final XMLStreamWriter writer, final NodeInfo data,
                            final boolean skipRoot) throws Exception {
        if (data == null) {
            return;
        }
        if (skipRoot && (data.type.equals("route"))) {
            NodeInfo firstElm = data.children.peek();
            if (firstElm != null) {
                addElement(writer, firstElm, false);
                return;
            }
        }
        String elementName = data.type;
        writer.writeStartElement(elementName);
        try {
            writer.writeAttribute("id", data.nodeId);
            if(data.information != null){
                writer.writeStartElement("information");
                writer.writeCData(data.information);
                writer.writeEndElement();
            }
            for (Iterator<NodeInfo> it = data.children.iterator(); it
                    .hasNext(); ) {
                try {
                    if (it.hasNext()) {//possible concurrent issue
                        addElement(writer, it.next(), true);
                    }
                } catch (Exception e) {
                    //noops
                }
            }
        } finally {
            writer.writeEndElement();
        }
    }

    public String buildInformation() {
        try {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                final XMLStreamWriter writer =
                        XSUtil.wrapForPrettyPrinting(XSUtil
                                .getXMLOutputFactory().createXMLStreamWriter(out));
                try {
                    NodeInfo rn = rootNode;
                    while (rn.parentNode != null) {
                        rn = rn.parentNode;
                    }
                    addElement(writer, rn, false);
                    writer.writeEndDocument();
                } finally {
                    writer.close();
                }
                return out.toString();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    static class NodeInfo {
        String nodeId;

        final ConcurrentLinkedQueue<NodeInfo> children =
                new ConcurrentLinkedQueue<>();

        NodeInfo parentNode;

        String type;

        String information;

        public NodeInfo(final String id, final NodeInfo parent,
                        final String type, final String information) {
            super();
            nodeId = id;
            parentNode = parent;
            this.type = type;
            this.information = information;
            if (parentNode != null) {
                parentNode.children.add(this);
            }
        }

        @Override
        public String toString() {
            return String.format("%s:%s", type, nodeId); //$NON-NLS-1$
        }
    }

}
