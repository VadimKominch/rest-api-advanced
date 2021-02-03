package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.hateoas.UserLinker;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userRepository;
    private GiftService giftService;
    private OrderService orderService;
    private UserLinker userLinker;

    @Autowired
    public UserController(UserService userRepository, GiftService certificateRepository, OrderService orderService, UserLinker userLinker) {
        this.userRepository = userRepository;
        this.giftService = certificateRepository;
        this.orderService = orderService;
        this.userLinker = userLinker;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer pageSize, @RequestParam(required = true) Integer pageNumber) {
        List<User> users = userRepository.getAll(pageNumber,pageSize);
        users.forEach(el -> {
            userLinker.setLinks(el);
        });
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    /**
     * Get entity by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) {
        User user = userRepository.getById(id);
        userLinker.setLinks(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity<String>("OK", HttpStatus.CREATED);
    }

    @PostMapping(value = "{id}/purchase")
    public ResponseEntity<?> processOrder(@RequestBody List<Integer> giftCertificates, @PathVariable Integer id) {
        User user = userRepository.getById(id);
        if (user != null) {
            List<GiftCertificate> certificates = giftCertificates.stream().map(el -> giftService.getById(el)).collect(Collectors.toList());
            Order order = new Order();
            order.setCertificate(certificates);
            order.setBuyDate(new Date());
            orderService.save(order);
            return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "{id}/get_orders")
    public ResponseEntity<?> getOrders(@PathVariable int id,@RequestParam(required = false) Integer pageSize, @RequestParam(required = true) Integer pageNumber) {
        List<Order> orderList = orderService.getOrderByUserId(id,pageNumber,pageSize);
        orderList.forEach(el -> el.add(linkTo(methodOn(UserController.class).getOrderInfo(id,el.getId())).withSelfRel()));
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }

    @GetMapping(value = "{id}/info/{order_id}")
    public ResponseEntity<?> getOrderInfo(@PathVariable Integer id, @PathVariable Integer order_id) {
        Order order = orderService.getById(id);
        order.add(linkTo(methodOn(UserController.class).getOrderInfo(id,order_id)).withSelfRel());
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @GetMapping(value="all/count")
    public ResponseEntity<?> getAllUsersCount() {
        return new ResponseEntity<Long>(userRepository.getUserCount(),HttpStatus.OK);
    }

    @GetMapping(value="{id}/orders/count")
    public ResponseEntity<?> getAllUsersCount(@PathVariable Integer id) {
        return new ResponseEntity<Long>(orderService.getUserOrdersCount(id),HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}/delete_order/{order_id}")
    public ResponseEntity<?> deleteOrder(@PathVariable int id, @PathVariable int order_id) {
        orderService.delete(order_id);
        return new ResponseEntity<String>("Ok", HttpStatus.OK);
    }
}
