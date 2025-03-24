package scdy.notificationservice.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;


@Slf4j
@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseMessaging firebaseMessaging(){
        try{
            FileInputStream serviceAccount = new FileInputStream("C:/Users/Yearm404/Desktop/project/Triplan/triplan_backend/serviceAccountKey.json");
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
        }catch (Exception e){
            //e.printStackTrace();
            log.error("error", e);
        }

        return FirebaseMessaging.getInstance();
    }

}

