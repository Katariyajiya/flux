package com.flux.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    public FirebaseConfig() throws IOException {

        String firebaseBase64 =
                System.getenv("FIREBASE_SERVICE_ACCOUNT_BASE64");

        if (firebaseBase64 == null || firebaseBase64.isBlank()) {
            throw new IllegalStateException(
                    "FIREBASE_SERVICE_ACCOUNT_BASE64 environment variable is missing"
            );
        }

        byte[] decoded = Base64.getDecoder().decode(firebaseBase64);

        GoogleCredentials credentials =
                GoogleCredentials.fromStream(
                        new ByteArrayInputStream(decoded)
                );

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}