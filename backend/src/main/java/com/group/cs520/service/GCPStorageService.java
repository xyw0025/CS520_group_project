package com.group.cs520.service;

import org.springframework.stereotype.Service;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.OutputStream;
import java.io.IOException;

@Service
public class GCPStorageService {
    @Value("${spring.cloud.gcp.storage.bucket.name}")
    private String bucketName;

    @Value("${gcp.url}")
    private String GCP_URL;

    @Autowired
    private ResourceLoader resourceLoader;

    public String uploadImage(MultipartFile file) throws IOException {
        String filename = "images/" + file.getOriginalFilename(); // or generate a new filename
        String gcsLocation = "gs://" + bucketName + "/" + filename;

        WritableResource resource = (WritableResource) resourceLoader.getResource(gcsLocation);

        try (OutputStream os = resource.getOutputStream()) {
            os.write(file.getBytes());
        }

        String photoURL = GCP_URL + bucketName + "/" + filename;

        return photoURL;
    }
}
