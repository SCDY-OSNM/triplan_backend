package scdy.contentsservice.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Getter
@NoArgsConstructor
@Table(name = "contentLike")
class ContentLike(
        @Id
        @Column(name = "contentLikeId")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val contentLikeId : Long? = null,

        @Column(nullable = false)
        var userId : Long,

        @JoinColumn(name = "content_id")
        @ManyToOne(fetch = FetchType.LAZY)
        var content : Content
) {

}