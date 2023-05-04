package com.github.vlubchen.gradproject.web.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.vlubchen.gradproject.model.User;
import com.github.vlubchen.gradproject.repository.UserRepository;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public User get(int id) {
        log.info("get user with id {}", id);
        return repository.getExisted(id);
    }

    public void delete(int id) {
        log.info("delete user with id {}", id);
        repository.deleteExisted(id);
    }
}