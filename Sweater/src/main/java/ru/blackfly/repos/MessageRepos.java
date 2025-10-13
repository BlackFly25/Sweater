package ru.blackfly.repos;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.blackfly.models.Message;
import ru.blackfly.models.User;

import java.util.List;

@Repository
public interface MessageRepos extends CrudRepository<Message, Long> {

    List<Message> findByTag(String tag);
    List<Message> findByAuthor(User author);
}
