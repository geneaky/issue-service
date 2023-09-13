package my.toy.domain.enums

enum class IssueType {

    BUG, TASK;

    //operator invoke
    companion object {
        operator fun invoke(type: String) = valueOf(type.uppercase())
    }
}
