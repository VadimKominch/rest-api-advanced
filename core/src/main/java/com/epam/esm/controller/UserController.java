package com.epam.esm.controller;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value="/users")
public class UserController {

    private UserDao userRepository;
    private GiftService certificateRepository;
    private TagService tagRepository;

    @Autowired
    public UserController(UserDao userRepository, GiftService certificateRepository, TagService tagRepository) {
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer pageSize,@RequestParam(required = true) Integer pageNumber) {
        List<User> users = userRepository.getAll();
        users.forEach(el->{
            el.add(linkTo(methodOn(UserController.class).getById(el.getId())).withSelfRel());
        });
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    /**
     *
     * Get entity by id
     * */
    @GetMapping(value="/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) {
        throw new EmptyResultDataAccessException(1);
    }

    @PostMapping(value="/add")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity<String>("OK",HttpStatus.CREATED);
    }

    @PostMapping(value="{id}/purchase")
    public ResponseEntity<?> processOrder(@RequestBody GiftCertificate giftCertificate, @PathVariable int id) {
        User user =userRepository.getById(id);
        if(user != null) {
            giftCertificate.setUser(user);
            giftCertificate.setCreationDate(new Date());
            giftCertificate.setCreationDate(new Date());
            if(giftCertificate.getTags()!=null) {
                giftCertificate.getTags().forEach(el->{
                    try{
                        Tag tag = tagRepository.getByName(el.getName());
                        el.setId(tag.getId());
                    } catch(EmptyResultDataAccessException e) {
                        tagRepository.save(el);
                    }
                    //save tag if its not present

                });
            }
            System.out.println(giftCertificate);
            certificateRepository.save(giftCertificate);
            return new ResponseEntity<>("OK",HttpStatus.ACCEPTED);
        } else {
            return  new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}/delete_order/{order_id}")
    public ResponseEntity<?> deleteOrder(@PathVariable int id, @PathVariable int order_id) {
        certificateRepository.delete(order_id);
        return new ResponseEntity<String>("Ok",HttpStatus.OK);
    }
}
