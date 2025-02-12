/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.sgoeschl.freemarker.cli.model;

import com.github.sgoeschl.freemarker.cli.activation.StringDataSource;
import com.github.sgoeschl.freemarker.cli.impl.CloseableReaper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

import static java.nio.charset.Charset.forName;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.io.IOUtils.lineIterator;

/**
 * Source document which encapsulates a data source. When accessing
 * content it is loaded on demand on not kept in memory to allow
 * processing of large volumes of data.
 */
public class Document implements Closeable {

    private static final int UKNOWN_LENGTH = -1;

    /** Human-radable name of the document */
    private final String name;

    /** Charset for directly accessing text-based content */
    private final Charset charset;

    /** The data source */
    private final DataSource dataSource;

    /** The location of the content */
    private final String location;

    /** Collect all closables handed out to the caller to be closed when the document is closed itself */
    private final CloseableReaper closables;

    public Document(String name, DataSource dataSource, String location, Charset charset) {
        this.name = requireNonNull(name);
        this.dataSource = requireNonNull(dataSource);
        this.location = requireNonNull(location);
        this.charset = requireNonNull(charset);
        this.closables = new CloseableReaper();
    }

    public String getName() {
        return name;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Try to get the length lazily, efficient and without consuming the input stream.
     *
     * @return Length of document or "-1"
     */
    public long getLength() {
        if (dataSource instanceof FileDataSource) {
            return ((FileDataSource) dataSource).getFile().length();
        } else if (dataSource instanceof StringDataSource) {
            return ((StringDataSource) dataSource).getContent().length();
        } else {
            return UKNOWN_LENGTH;
        }
    }

    /**
     * Get an input stream which is closed together with this document.
     *
     * @return InputStream
     */
    public InputStream getInputStream() throws IOException {
        return closables.add(dataSource.getInputStream());
    }

    /**
     * Get an input stream which needs to be closed by the caller.
     *
     * @return InputStream
     */
    public InputStream getUnsafeInputStream() throws IOException {
        return dataSource.getInputStream();
    }

    public String getText() throws IOException {
        return getText(getCharset().name());
    }

    public String getText(String charsetName) throws IOException {
        final StringWriter writer = new StringWriter();
        try (InputStream is = dataSource.getInputStream()) {
            IOUtils.copy(is, writer, forName(charsetName));
            return writer.toString();
        }
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the specified character encoding.
     *
     * @return the list of Strings, never null
     * @throws IOException if an I/O error occurs
     */
    public List<String> getLines() throws IOException {
        return getLines(getCharset().name());
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the specified character encoding.
     *
     * @param charsetName The name of the requested charset
     * @return the list of Strings, never null
     * @throws IOException if an I/O error occurs
     */
    public List<String> getLines(String charsetName) throws IOException {
        try (InputStream inputStream = dataSource.getInputStream()) {
            return IOUtils.readLines(inputStream, charsetName);
        }
    }

    /**
     * Returns an Iterator for the lines in an <code>InputStream</code>, using
     * the default character encoding specified. The caller is responsible to close
     * the line iterator.
     *
     * @return line iterator
     * @throws IOException if an I/O error occurs
     */
    public LineIterator getLineIterator() throws IOException {
        return getLineIterator(getCharset().name());
    }

    /**
     * Returns an Iterator for the lines in an <code>InputStream</code>, using
     * the character encoding specified. The caller is responsible to close
     * the line iterator.
     *
     * @param charsetName The name of the requested charset
     * @return line iterator
     * @throws IOException if an I/O error occurs
     */
    public LineIterator getLineIterator(String charsetName) throws IOException {
        final LineIterator lineIterator = lineIterator(getUnsafeInputStream(), forName(charsetName));
        return closables.add(lineIterator);
    }

    public byte[] getBytes() throws IOException {
        try (InputStream inputStream = dataSource.getInputStream()) {
            return IOUtils.toByteArray(inputStream);
        }
    }

    /**
     * Some tools create a {@link java.io.Closeable} which can bound to the
     * lifecycle of the document. When the document is closed all the bound
     * {@link java.io.Closeable} are closed as well.
     *
     * @param closeable Closable
     * @param <T>       Type of closable
     * @return Closable
     */
    public <T extends Closeable> T addClosable(T closeable) {
        return closables.add(closeable);
    }

    @Override
    public void close() {
        closables.close();
    }

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", charset='" + charset + '\'' +
                '}';
    }
}
