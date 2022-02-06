package ru.uzaretskaya.todo.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uzaretskaya.todo.business.repository.entity.Priority;

import java.util.List;

public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findByUserEmailOrderByIdAsc(String email);

    @Query("SELECT c from Priority c where " +
            "(:title is null or :title='' " +
            "or lower(c.title) like lower(concat('%', :title, '%') ) ) " +
            "and c.user.email=:email " +
            "order by c.title asc")
    List<Priority> find(@Param("title") String title, @Param("email") String email);
}
