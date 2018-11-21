package com.company.appname.app.service;

import com.company.appname.app.dto.user.UserSimpleDTO;
import com.company.appname.app.persistence.model.User;
import com.company.appname.app.persistence.repository.UserRepo;
import com.company.appname.common.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class UserService extends BaseService<UserRepo, User> {

    public List<UserSimpleDTO> findAllUsers() {
        return super.findAll(UserSimpleDTO.class);
    }

}
