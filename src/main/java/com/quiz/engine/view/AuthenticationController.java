package com.quiz.engine.view;

import com.quiz.engine.dto.UserDTO;
import com.quiz.engine.models.User;
import com.quiz.engine.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Validated
public class AuthenticationController {

    private final UserService userService;
    private final ModelMapper mapper;

    public AuthenticationController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = userService.findByEmail(userDTO.getEmail());
        if(optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            User user = mapper.map(userDTO, User.class);
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
