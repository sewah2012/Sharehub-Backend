package com.luslusdawmpfe.PFEBackent.utils;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


@Slf4j
@Component
public class FileUploadHelpers {
    static final private String type = "service_account";
    static final private String storageBucket = "share-hub-4607b.appspot.com";

    static String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/"+storageBucket+"/o/%s?alt=media";


    static {
        InputStream serviceAccount = null;
        try {
            serviceAccount = new ClassPathResource(
                    "fbSecurityConfig.json").getInputStream();
        } catch (IOException e) {
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
    public static String uploadFile(File file, String fileName) throws IOException {

        log.info("Uploading file....");
        BlobId blobId = BlobId.of(storageBucket, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream resource = new ClassPathResource(
                "fbSecurityConfig.json").getInputStream();
//        InputStream targetStream = new ByteArrayInputStream(jsonString.getBytes());

        Credentials credentials = GoogleCredentials.fromStream(resource);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));


        log.info("File uploaded successfully: imageUrl: "+ DOWNLOAD_URL);
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    public static Boolean deleteFile(String fileName) throws IOException {
        log.info("Deleting file....");
        Bucket bucket = StorageClient.getInstance().bucket(storageBucket);
        log.info("File deleted successfully");
        return bucket.get(fileName).delete();



//        BlobId blobId = BlobId.of(storageBucket, fileName);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
//        InputStream resource = new ClassPathResource(
//                "fbSecurityConfig.json").getInputStream();
//        InputStream targetStream = new ByteArrayInputStream(jsonString.getBytes());

//        Credentials credentials = GoogleCredentials.fromStream(resource);
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//        storage.create(blobInfo, Files.readAllBytes(file.toPath()));



//        log.info("File uploaded successfully: imageUrl: "+ DOWNLOAD_URL);
//        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }


    public static File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
