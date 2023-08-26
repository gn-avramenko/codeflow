/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/


package com.gridnine.codeflow.tracer;


import com.gridnine.codeflow.utils.XSUtil;

import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Tracer {


    private NodeData currentNode;

    private final NodeData rootNode;

    private final String routeId;


    private static final DateTimeFormatter DTF_JODA_DATETIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault()); //$NON-NLS-1$

    public Tracer(final String route) {
        currentNode = new NodeData(route, null, "route");
        rootNode = currentNode;
        routeId = route;
    }

    public void startElement(final String elementId, final String type) {
        currentNode = new NodeData(elementId, currentNode, type);
    }

    public void message(final String msg) {
        new NodeData("message", currentNode, "message").message = //$NON-NLS-1$
                msg;
    }

    public void message(final String msg, final Throwable e) {
        new NodeData("message", currentNode, "message").message = //$NON-NLS-1$
                String.format("%s, details:\r\n%s", msg, //$NON-NLS-1$
                        e.getMessage());
    }

    public void endBlock() {
        if (currentNode == null) {
            return;
        }
        currentNode.endTime = System.currentTimeMillis();
        currentNode = currentNode.parentNode;

    }

    public void endRoute() {
        rootNode.endTime = System.currentTimeMillis();
    }

    private void addElement(final XMLStreamWriter writer, final NodeData data) throws Exception {
        if (data == null) {
            return;
        }
        String elementName = data.nodeType;
        writer.writeStartElement(elementName);
        try {
            writer.writeAttribute("id", data.nodeId);
            writer.writeAttribute("start", //$NON-NLS-1$
                    DTF_JODA_DATETIME.format(Instant.ofEpochMilli(data.startTime)));
            if (data.endTime > 0) {
                writer.writeAttribute("duration", //$NON-NLS-1$
                        String.format("%s ms", //$NON-NLS-1$
                                data.endTime - data.startTime));
            }
            for (Iterator<NodeData> it = data.children.iterator(); it
                    .hasNext(); ) {
                try {
                    if (it.hasNext()) {//possible concurrent issue
                        addElement(writer, it.next());
                    }
                } catch (Exception e) {
                    //noops
                }
            }
        } finally {
            writer.writeEndElement();
        }
    }

    public String buildDebugInfo() {
        try {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                final XMLStreamWriter writer =
                        XSUtil.wrapForPrettyPrinting(XSUtil
                                .getXMLOutputFactory().createXMLStreamWriter(out));
                try {
                    writer.writeStartDocument();
                    writer.writeStartElement("FlowTrace");
                    writer.writeAttribute("prepared",
                            DTF_JODA_DATETIME.format(Instant.ofEpochMilli(System.currentTimeMillis())));
                    NodeData rn = rootNode;
                    while (rn.parentNode != null) {
                        rn = rn.parentNode;
                    }
                    addElement(writer, rn);
                    writer.writeEndElement();
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

    static class NodeData {
        String nodeId;

        long startTime;

        long endTime;

        final ConcurrentLinkedQueue<NodeData> children =
                new ConcurrentLinkedQueue<>();

        NodeData parentNode;

        String message;

        String nodeType;

        public NodeData(final String id, final NodeData parent,
                        final String type) {
            super();
            nodeId = id;
            parentNode = parent;
            startTime = System.currentTimeMillis();
            nodeType = type;
            if (parentNode != null) {
                parentNode.children.add(this);
            }
        }

        @Override
        public String toString() {
            return String.format("%s:%s", nodeType, nodeId); //$NON-NLS-1$
        }
    }

}
