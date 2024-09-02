package com.example.jasper_springboot.Controller;

import com.example.jasper_springboot.ExceptionCustom.AppException;
import com.example.jasper_springboot.Model.User;
import com.example.jasper_springboot.Request.UserRequest;
import com.example.jasper_springboot.Response.Message;
import com.example.jasper_springboot.Service.UserService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class userController {
    @Autowired
    private UserService aus;
    @PostMapping("/add-user")
    public ResponseEntity<?> userAddController(@RequestBody @Valid UserRequest ur, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream().map(error -> {
                var defaultMessage = error.getDefaultMessage();
                if (error instanceof FieldError) {
                    var fieldError = (FieldError) error;
                    return String.format("%s: %s", fieldError.getField(), defaultMessage);
                } else {
                    return defaultMessage;
                }
            }).collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errorMessages);
        }else{
            try{
               User uadd = aus.addUser(ur);
                return ResponseEntity.ok(uadd);
            }
            catch (AppException e)
            {
                return ResponseEntity.status(e.getCode()).body(e.getMessage());
            }
        }

    }
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> userRemoveController(@PathVariable int id){
        try{
            Message result = aus.removeUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        catch (AppException e){
            return ResponseEntity.status(e.getCode()).body(new Message(e.getMessage()));
        }
    }
    @PatchMapping("/update-user/{id}")
    public ResponseEntity<?> updateUserController(@PathVariable int id, @RequestBody Map<Object,Object> field){
        System.out.println(field);
        try{
            User resultUpdate = aus.updateUser(id,field);
            return ResponseEntity.ok(resultUpdate);
        }catch (AppException ex)
        {
            return ResponseEntity.status(ex.getCode()).body(new Message(ex.getMessage()));
        }
    }
    @GetMapping("/get-user")
    public ResponseEntity<?> getAllUserController(@RequestParam(defaultValue = "0") Integer pageNumber ,@RequestParam(defaultValue = "5") Integer pageSize,@RequestParam(defaultValue = "id") String sortBy)
    {
        try{
            List<User> userList = aus.getAllUser(pageNumber, pageSize, sortBy);
           return ResponseEntity.ok(userList);
        }catch(AppException e)
        {
           return ResponseEntity.status(e.getCode()).body(new Message(e.getMessage()));
        }

    }
    @GetMapping("/getfile")
    public ResponseEntity<?> getFile(@RequestBody Map<String,String> path) throws JRException, FileNotFoundException {
        try{
            System.out.println(path);
           String result = aus.exportReport(path.get("path"));
           return ResponseEntity.status(200).body(new Message(result));
        }catch(AppException e)
        {
            return ResponseEntity.status(e.getCode()).body(new Message(e.getMessage()));
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?> findUserByTextSearch(@RequestParam String textSearch,@RequestParam(defaultValue = "0") Integer pageNumber ,@RequestParam(defaultValue = "3") Integer pageSize,@RequestParam(defaultValue = "id") String sortBy)  {
        try{

            List<User> result = aus.fullTextSearch(textSearch,pageNumber,pageSize,sortBy);
            return ResponseEntity.status(200).body(result);
        }catch(AppException e)
        {
            return ResponseEntity.status(e.getCode()).body(new Message(e.getMessage()));
        }
    }

}
