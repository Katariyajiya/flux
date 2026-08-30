package com.flux.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    public FirebaseConfig() throws IOException{
        FileInputStream serviceAccount = new FileInputStream(
                "/Users/jiyakataria/Downloads/flux-39061-firebase-adminsdk-fbsvc-59cdb82f3d.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(
                        GoogleCredentials.fromStream(serviceAccount)
                ).build();
        FirebaseApp.initializeApp(options);
    }
}
