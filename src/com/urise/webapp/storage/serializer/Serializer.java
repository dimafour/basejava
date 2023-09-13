package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.Resume;

import java.io.*;

public interface Serializer {
    void doWrite(Resume r, OutputStream os) throws IOException;
    Resume doRead(InputStream is) throws IOException;
}
