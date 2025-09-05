package scdy.configservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class DeletedImageLog {

    @Id
    private Long id;

    @Column(nullable = false)
    private String s3Key;

    @Column(nullable = false)
    private LocalDateTime deletionRequestedAt;

    private int retryCount = 0;


    public DeletedImageLog(String s3Key) {
        this.s3Key = s3Key;
        this.deletionRequestedAt = LocalDateTime.now();
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

}
