/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.utils;


import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

@SuppressWarnings("nls")
public class XSUtil {

    private static final ThreadLocal<XMLInputFactory> INPUT_FACTORIES =
        new ThreadLocal<XMLInputFactory>() {
            @Override
            protected XMLInputFactory initialValue() {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread()
                        .setContextClassLoader(getClass().getClassLoader());
                    return XMLInputFactory.newInstance();
                } catch (Exception e) {
                    throw new Error("failed creating XML builder", e);
                } finally {
                    Thread.currentThread().setContextClassLoader(cl);
                }
            }
        };

    public static XMLInputFactory getXMLInputFactory() {
        return INPUT_FACTORIES.get();
    }

    private static final ThreadLocal<XMLOutputFactory> OUTPUT_FACTORIES =
        new ThreadLocal<XMLOutputFactory>() {
            @Override
            protected XMLOutputFactory initialValue() {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread()
                        .setContextClassLoader(getClass().getClassLoader());
                    return XMLOutputFactory.newInstance();
                } catch (Exception e) {
                    throw new Error("failed creating XML builder", e);
                } finally {
                    Thread.currentThread().setContextClassLoader(cl);
                }
            }
        };

    public static XMLOutputFactory getXMLOutputFactory() {
        return OUTPUT_FACTORIES.get();
    }


    public static XMLStreamWriter wrapForPrettyPrinting(
            final XMLStreamWriter writer) {
        return new XMLStreamWriter() {
            private int level = 0;

            private boolean lastWasStart = false;

            private void indent() throws XMLStreamException {
                for (int i = 0; i < level; i++) {
                    writer.writeCharacters("    ");
                }
            }

            @Override
            public void writeStartElement(final String localName)
                    throws XMLStreamException {
                if (level > 0) {
                    writer.writeCharacters("\n");
                }
                lastWasStart = true;
                indent();
                level++;
                writer.writeStartElement(localName);
            }

            @Override
            public void writeStartElement(final String namespaceURI,
                    final String localName) throws XMLStreamException {
                writer.writeCharacters("\n");
                lastWasStart = true;
                indent();
                level++;
                writer.writeStartElement(namespaceURI, localName);
            }

            @Override
            public void writeStartElement(final String prefix,
                    final String localName, final String namespaceURI)
                    throws XMLStreamException {
                writer.writeCharacters("\n");
                lastWasStart = true;
                level++;
                writer.writeStartElement(prefix, localName, namespaceURI);
            }

            @Override
            public void writeEmptyElement(final String namespaceURI,
                    final String localName) throws XMLStreamException {
                writer.writeCharacters("\n");
                lastWasStart = false;
                indent();
                writer.writeEmptyElement(namespaceURI, localName);
            }

            @Override
            public void writeEmptyElement(final String prefix,
                    final String localName, final String namespaceURI)
                    throws XMLStreamException {
                writer.writeCharacters("\n");
                lastWasStart = false;
                indent();
                writer.writeEmptyElement(prefix, localName, namespaceURI);
            }

            @Override
            public void writeEmptyElement(final String localName)
                    throws XMLStreamException {
                writer.writeCharacters("\n");
                lastWasStart = false;
                indent();
                writer.writeEmptyElement(localName);
            }

            @Override
            public void writeEndElement() throws XMLStreamException {
                level--;
                if (!lastWasStart) {
                    writer.writeCharacters("\n");
                    indent();
                } else {
                    lastWasStart = false;
                }
                writer.writeEndElement();
            }

            @Override
            public void writeEndDocument() throws XMLStreamException {
                if (!lastWasStart) {
                    writer.writeCharacters("\n");
                    indent();
                } else {
                    lastWasStart = false;
                }
                writer.writeEndDocument();
            }

            @Override
            public void close() throws XMLStreamException {
                level = 0;
                lastWasStart = false;
                writer.close();
            }

            @Override
            public void flush() throws XMLStreamException {
                writer.flush();
            }

            @Override
            public void writeAttribute(final String localName,
                    final String value) throws XMLStreamException {
                writer.writeAttribute(localName, value);
            }

            @Override
            public void writeAttribute(final String prefix,
                    final String namespaceURI, final String localName,
                    final String value) throws XMLStreamException {
                writer.writeAttribute(prefix, namespaceURI, localName, value);
            }

            @Override
            public void writeAttribute(final String namespaceURI,
                    final String localName, final String value)
                    throws XMLStreamException {
                writer.writeAttribute(namespaceURI, localName, value);
            }

            @Override
            public void writeNamespace(final String prefix,
                    final String namespaceURI) throws XMLStreamException {
                writer.writeNamespace(prefix, namespaceURI);
            }

            @Override
            public void writeDefaultNamespace(final String namespaceURI)
                    throws XMLStreamException {
                writer.writeDefaultNamespace(namespaceURI);
            }

            @Override
            public void writeComment(final String data)
                    throws XMLStreamException {
                writer.writeCharacters("\n");
                lastWasStart = false;
                indent();
                writer.writeComment(data);
            }

            @Override
            public void writeProcessingInstruction(final String target)
                    throws XMLStreamException {
                writer.writeProcessingInstruction(target);
            }

            @Override
            public void writeProcessingInstruction(final String target,
                    final String data) throws XMLStreamException {
                writer.writeProcessingInstruction(target, data);
            }

            @Override
            public void writeCData(final String data)
                    throws XMLStreamException {
                if (data == null) {
                    writer.writeCData(data);
                } else {
                    String[] parts = data.split("]]>");
                    if (parts.length == 1) {
                        writer.writeCData(data);
                    } else {
                        for (int i = 0; i < parts.length; i++) {
                            StringBuilder sb = new StringBuilder();
                            if (i > 0) {
                                sb.append(">");
                            }
                            sb.append(parts[i]);
                            if ((i + 1) < parts.length) {
                                sb.append("]]");
                            }
                            writer.writeCData(sb.toString());
                        }
                    }
                }
            }

            @Override
            public void writeDTD(final String dtd) throws XMLStreamException {
                writer.writeDTD(dtd);
            }

            @Override
            public void writeEntityRef(final String name)
                    throws XMLStreamException {
                writer.writeEntityRef(name);
            }

            @Override
            public void writeStartDocument() throws XMLStreamException {
                writer.writeStartDocument();
                writer.writeCharacters("\n");
            }

            @Override
            public void writeStartDocument(final String version)
                    throws XMLStreamException {
                writer.writeStartDocument(version);
                writer.writeCharacters("\n");
            }

            @Override
            public void writeStartDocument(final String encoding,
                    final String version) throws XMLStreamException {
                writer.writeStartDocument(encoding, version);
                writer.writeCharacters("\n");
            }

            @Override
            public void writeCharacters(final String text)
                    throws XMLStreamException {
                writer.writeCharacters(text);
            }

            @Override
            public void writeCharacters(final char[] text, final int start,
                    final int len) throws XMLStreamException {
                writer.writeCharacters(text, start, len);
            }

            @Override
            public String getPrefix(final String uri)
                    throws XMLStreamException {
                return writer.getPrefix(uri);
            }

            @Override
            public void setPrefix(final String prefix, final String uri)
                    throws XMLStreamException {
                writer.setPrefix(prefix, uri);
            }

            @Override
            public void setDefaultNamespace(final String uri)
                    throws XMLStreamException {
                writer.setDefaultNamespace(uri);
            }

            @Override
            public void setNamespaceContext(final NamespaceContext context)
                    throws XMLStreamException {
                writer.setNamespaceContext(context);
            }

            @Override
            public NamespaceContext getNamespaceContext() {
                return writer.getNamespaceContext();
            }

            @Override
            public Object getProperty(final String name)
                    throws IllegalArgumentException {
                return writer.getProperty(name);
            }
        };
    }

}
