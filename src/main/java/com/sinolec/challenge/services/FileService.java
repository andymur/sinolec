package com.sinolec.challenge.services;

import com.sinolec.challenge.exceptions.InternalException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;

@Service
public class FileService {

    public FileSystemResource getFileResource(String location) {
        File inputFile = new File(location);

        if (!inputFile.exists()) {
            throw new InternalException("File not generated yet");
        }

        return new FileSystemResource(inputFile);
    }

    public String storeResult(String taskId, URL url) throws IOException {
        File outputFile = File.createTempFile(taskId, ".zip");
        outputFile.deleteOnExit();
        String storageLocation = outputFile.getAbsolutePath();
        try (InputStream is = url.openStream();
             OutputStream os = new FileOutputStream(outputFile)) {
            IOUtils.copy(is, os);
        }
        return storageLocation;
    }
}
