package ru.puchinets.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.puchinets.model.dto.request.UserRequest;
import ru.puchinets.model.dto.response.Response;

import java.util.Optional;

public interface UserService {

   Optional<Response> getById(Long id);
   Page<Response> getAllUsers(Pageable pageable);
   Response createUser(UserRequest request);
   Optional<Response> updateUser(Long userId, UserRequest request);
   boolean deleteUser(Long userId);

}
