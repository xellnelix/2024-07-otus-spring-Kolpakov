package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {
    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.book.id = :bookId", Comment.class
        );
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }


    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            return insert(comment);
        }
        return update(comment);
    }

    @Override
    public void deleteById(long id) {
        Comment foundComment = entityManager.find(Comment.class, id);
        if (foundComment == null) {
            throw new EntityNotFoundException("Comment with id = " + id + " has not found");
        }
        entityManager.remove(foundComment);
    }

    private Comment insert(Comment comment) {
        entityManager.persist(comment);
        return comment;
    }

    private Comment update(Comment comment) {
        entityManager.merge(comment);
        return comment;
    }
}
