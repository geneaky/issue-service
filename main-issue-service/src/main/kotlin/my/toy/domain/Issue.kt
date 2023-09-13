package my.toy.domain

import my.toy.domain.enums.IssuePriority
import my.toy.domain.enums.IssueStatus
import my.toy.domain.enums.IssueType
import javax.persistence.*

@Entity
class Issue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var userId: Long,
    var description: String,
    var summary: String,
    @Enumerated(EnumType.STRING)
    var type: IssueType,
    @Enumerated(EnumType.STRING)
    var priority: IssuePriority,
    @Enumerated(EnumType.STRING)
    var status: IssueStatus,
) : BaseEntity() {

}
