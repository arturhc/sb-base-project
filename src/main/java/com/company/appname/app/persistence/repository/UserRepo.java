package com.company.appname.app.persistence.repository;

import com.company.appname.app.persistence.model.User;
import com.company.appname.common.persistence.repository.SoftDeleteRepository;

import java.util.Optional;

public interface UserRepo extends SoftDeleteRepository<User, Long> {

    Optional<User> findByName(String description);

}
