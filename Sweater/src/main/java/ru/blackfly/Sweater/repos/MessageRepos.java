package ru.blackfly.Sweater.repos;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.blackfly.Sweater.models.Message;

import java.util.List;

@Repository
public interface MessageRepos extends CrudRepository<Message, Long> {

    List<Message> findByTag(String tag);
}
