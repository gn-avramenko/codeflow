/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/


package com.gridnine.codeflow.validation;


import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.utils.XSUtil;

import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ValidationBuilder {


    private NodeValidation currentNode;

    private final NodeValidation rootNode;

    private final String routeId;

    private boolean hasError;

    private Set<ContextKey<?>> keys = new HashSet<>();

    public ValidationBuilder(final String route) {
        currentNode = new NodeValidation(route, null, "route", null);
        rootNode = currentNode;
        routeId = route;
    }

    public NodeValidation getRootNode() {
        return rootNode;
    }

    public void startElement(final String elementId, final String type, String validation) {
        currentNode = new NodeValidation(elementId, currentNode, type, validation);
        if(validation != null){
            hasError = true;
        }
    }


    public void endBlock() {
        if (currentNode == null) {
            return;
        }
        currentNode = currentNode.parentNode;

    }

    private void addElement(final XMLStreamWriter writer, final NodeValidation data,
                            final boolean skipRoot) throws Exception {
        if (data == null) {
            return;
        }
        if (skipRoot && (data.type.equals("route"))) {
            NodeValidation firstElm = data.children.peek();
            if (firstElm != null) {
                addElement(writer, firstElm, false);
                return;
            }
        }
        String elementName = data.type;
        writer.writeStartElement(elementName);
        try {
            writer.writeAttribute("id", data.nodeId);
            if(data.validation != null){
                writer.writeStartElement("validation");
                writer.writeCData(data.validation);
                writer.writeEndElement();
            }
            for (Iterator<NodeValidation> it = data.children.iterator(); it
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

    public String buildValidation() {
        try {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                final XMLStreamWriter writer =
                        XSUtil.wrapForPrettyPrinting(XSUtil
                                .getXMLOutputFactory().createXMLStreamWriter(out));
                try {
                    NodeValidation rn = rootNode;
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

    static class NodeValidation {
        String nodeId;

        final ConcurrentLinkedQueue<NodeValidation> children =
                new ConcurrentLinkedQueue<>();

        NodeValidation parentNode;

        String type;

        String validation;

        public NodeValidation(final String id, final NodeValidation parent,
                              final String type, final String validation) {
            super();
            nodeId = id;
            parentNode = parent;
            this.type = type;
            this.validation = validation;
            if (parentNode != null) {
                parentNode.children.add(this);
            }
        }

        @Override
        public String toString() {
            return String.format("%s:%s", type, nodeId); //$NON-NLS-1$
        }
    }

    public boolean isHasError() {
        return hasError;
    }

    public Set<ContextKey<?>> getKeys() {
        return keys;
    }
}
