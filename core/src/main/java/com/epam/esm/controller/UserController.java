package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value="/users")
public class UserController {

    private UserRepository userRepository;
    private CertificateRepository certificateRepository;
    private TagRepository tagRepository;

    @Autowired
    public UserController(UserRepository userRepository, CertificateRepository certificateRepository, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer pageSize,@RequestParam(required = true) Integer pageNumber) {
        List<User> users = userRepository.findAll(PageRequest.of(pageNumber,pageSize)).getContent();
        users.forEach(el->{
            el.add(linkTo(methodOn(UserController.class).getById(el.getId())).withSelfRel());
        });
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

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
        Optional<User> user =userRepository.findById(id);
        if(user.isPresent()) {
            giftCertificate.setUser(user.get());
            giftCertificate.setCreationDate(new Date());
            giftCertificate.setCreationDate(new Date());
            if(giftCertificate.getTags()!=null) {
                giftCertificate.getTags().forEach(el->{
                    Optional<Tag> tag = tagRepository.findByName(el.getName());
                    tag.ifPresentOrElse(value -> el.setId(value.getId()),()->tagRepository.save(el));
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
        certificateRepository.deleteById(order_id);
        return new ResponseEntity<String>("Ok",HttpStatus.OK);
    }
}