package scdy.planservice.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry){

        stompEndpointRegistry.addEndpoint("/chat")
                // 실사용에 cors 도메인으로 바꿔주기
                .setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry){
        // 토픽 구독 요청 엔드포인트 (송신)
        messageBrokerRegistry.enableSimpleBroker("/sub");

        // 메세지 발행 요청 엔드포인트 (수신)
        messageBrokerRegistry.setApplicationDestinationPrefixes("/pub");
    }


}
