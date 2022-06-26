package com.luslusdawmpfe.PFEBackent.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.luslusdawmpfe.PFEBackent.configs.FirebaseCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


@Slf4j
@Service
public class FileUploadHelpers {
    @Autowired
    private FbConfigurations fbConfigurations;

    private String storageBucket;

    @PostConstruct
     private void initializeFbApp(){
        storageBucket = fbConfigurations.getBucketName();
        InputStream serviceAccount;
        try {
            log.info("Value from config prop: {}",fbConfigurations.getPrivateKey());

            serviceAccount = createFirebaseCredential();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FirebaseApp.initializeApp(options);
    }
    public String uploadFile(File file, String fileName) throws Exception {
        log.info("Uploading file....");

        BlobId blobId = BlobId.of(storageBucket, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream targetStream = createFirebaseCredential();

        Credentials credentials = GoogleCredentials.fromStream(targetStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/"+storageBucket+"/o/%s?alt=media";

        log.info("File uploaded successfully: imageUrl: "+ DOWNLOAD_URL);
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    public Boolean deleteFile(String fileName) throws IOException {
//        initializeFbApp();
        log.info("Deleting file....");
        Bucket bucket = StorageClient.getInstance().bucket(storageBucket);
        log.info("File deleted successfully");
        return bucket.get(fileName).delete();
    }


    public File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

//    @PostConstruct
    private InputStream createFirebaseCredential() throws Exception {
        FirebaseCredentials firebaseCredential = new FirebaseCredentials();
        //private key
        String privateKey = fbConfigurations.getPrivateKey().replace("\\n", "\n");
//        log.info("private key : {}",privateKey);

        firebaseCredential.setProject_id(fbConfigurations.getProjectId());
        firebaseCredential.setPrivate_key_id(fbConfigurations.getPrivateKeyId());
        firebaseCredential.setPrivate_key(privateKey);
        firebaseCredential.setClient_email(fbConfigurations.getClientEmail());
        firebaseCredential.setClient_id(fbConfigurations.getClientId());
        firebaseCredential.setAuth_uri(fbConfigurations.getAuthUri());
        firebaseCredential.setToken_uri(fbConfigurations.getTokenUri());
        firebaseCredential.setAuth_provider_x509_cert_url(fbConfigurations.getAuthProvider_x509_cert_url());
        firebaseCredential.setClient_x509_cert_url(fbConfigurations.getClient_x509_cert_url());
        firebaseCredential.setType(fbConfigurations.getType());

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(firebaseCredential);
       return new ByteArrayInputStream(jsonString.getBytes());
    }

}
