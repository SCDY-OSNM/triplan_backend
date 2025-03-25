package scdy.notificationservice.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
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
            String FcmServiceAccount = System.getenv("FCM_SECRET_KEY");
            FileInputStream serviceAccount = new FileInputStream(FcmServiceAccount);

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

