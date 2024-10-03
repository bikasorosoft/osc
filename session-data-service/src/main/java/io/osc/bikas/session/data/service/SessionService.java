package io.osc.bikas.session.data.service;

import com.osc.bikas.avro.SessionTopicKey;
import io.osc.bikas.session.data.kafka.config.KafkaConstants;
import io.osc.bikas.session.data.kafka.producer.KafkaSessionPublisher;
import io.osc.bikas.session.data.model.Session;
import io.osc.bikas.session.data.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final KafkaSessionPublisher kafkaSessionProducer;
    private final KafkaStreamsInteractiveQueryService interactiveQueryService;

    public Session getSessionById(String sessionId) {
        return sessionRepository.findById(sessionId).get();
    }

    public boolean sessionExists(String userId, String device) {

        ReadOnlyKeyValueStore<SessionTopicKey, CharSequence> sessionStore = interactiveQueryService.retrieveQueryableStore(KafkaConstants.SESSION_STORE, QueryableStoreTypes.keyValueStore());

        SessionTopicKey key = SessionTopicKey.newBuilder().setUserId(userId).setDevice(device).build();
        CharSequence s = sessionStore.get(key);

        return s != null && !s.isEmpty();
    }

    public void createSession(String sessionId, String userId, String deviceType) {

        Session session = Session.builder()
                .sessionId(sessionId)
                .userId(userId)
                .deviceType(deviceType)
                .loginTIme(LocalDateTime.now())
                .build();
        sessionRepository.save(session);
        kafkaSessionProducer.publish(userId, deviceType, sessionId);
    }

    public void logout(String sessionId, String userId) {
        Session session = sessionRepository.findById(sessionId).get();
        String deviceType = session.getDeviceType();
        session.setLogoutTime(LocalDateTime.now());
        sessionRepository.save(session);
        kafkaSessionProducer.publish(userId, deviceType, null);
    }
}
