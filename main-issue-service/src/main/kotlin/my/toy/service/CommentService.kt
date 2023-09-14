package my.toy.service

import my.toy.domain.Comment
import my.toy.domain.CommentRepository
import my.toy.domain.IssueRepository
import my.toy.exception.NotFoundException
import my.toy.model.CommentRequest
import my.toy.model.CommentResponse
import my.toy.model.toResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val issueRepository: IssueRepository,
) {

    @Transactional
    fun create(issueId: Long, userId: Long, username: String, request: CommentRequest) : CommentResponse {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("not found issue")

        val comment = Comment(
            issue = issue,
            userId = userId,
            username = username,
            body = request.body
        )

        issue.comments.add(comment)
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    fun edit(id: Long, userId: Long, request: CommentRequest) {
        commentRepository.findByIdAndUserId(id, userId)?.run{
            body = request.body
            commentRepository.save(this).toResponse()
        }

    }


    @Transactional
    fun delete(issueId: Long, id: Long, userId: Long) {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("not found issue")
        commentRepository.findByIdAndUserId(id, userId)?.let {
            comment -> issue.comments.remove(comment)
        }
    }

}