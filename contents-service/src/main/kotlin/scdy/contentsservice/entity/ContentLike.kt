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
        val id : Long? = null,

        @Column(nullable = false)
        var userId : Long,

        @JoinColumn(name = "content")
        @ManyToOne
        var content : Content
) {

}