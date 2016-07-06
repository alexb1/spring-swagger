package com.abel.swagger;

import com.abel.swagger.model.User;
import com.abel.swagger.repository.UserRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * User resource Rest controller.
 *
 * @author Alex Belikov
 */
@RestController
@RequestMapping("/user")
@Api(value = "users")
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource msgSource;

    @ApiOperation(value = "getUsers", notes = "Returns all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class, responseContainer = "List")}
    )
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private List<User> users() {
        return userRepository.findAll();
    }

    @ApiOperation(value = "getUser", notes = "Return user with specified id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "User's id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class)})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<User> user(@PathVariable Long id) {
        validateUser(id);
        return ResponseEntity.ok(userRepository.findOne(id));
    }

    @ApiOperation(value = "createUser", notes = "Create user")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created")})
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "updateUser", notes = "Update user")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Accepted")})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        validateUser(id);
        user.setId(id);
        userRepository.save(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "deleteUser", notes = "Delete user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "User's id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 202, message = "Accepted")})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        validateUser(id);
        userRepository.delete(id);
        return ResponseEntity.accepted().build();
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class UserNotFound extends RuntimeException {

        private static final String ERROR_MSG = "User with id %1$d was not found";

        public UserNotFound(Long id) {
            super(String.format(ERROR_MSG, id));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String processValidationError(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();

        if (error != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            return msgSource.getMessage(error.getDefaultMessage(), null, currentLocale);
        }

        return e.getLocalizedMessage();
    }

}
