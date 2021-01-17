package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Rest Controller for basic crud operations and operations with list of data.
 * Use service as way to connect with database
 * @author Vadim Kominch
 *
 * */
@RestController
public class GiftController {


    private TagService tagService;
    private GiftService giftService;
    protected static final Logger logger = LogManager.getLogger();

    @Autowired
    public GiftController(GiftService giftService, TagService tagService) {
        this.tagService = tagService;
        this.giftService = giftService;
    }

    @GetMapping(value = "/")
    public String getString() {
        return "Hello,world";
    }
/**
 * Get method for reading one entity if its present. Receive id as integer in url path
 * and find entity in database.
 * */
    @GetMapping(value = "/{id}")
    public GiftCertificate getGiftById(@PathVariable int id) {
        return giftService.getById(id);
    }
/**
 * Get method for requesting all existing certificates. Use gift service.
 * Recieve three parameters:
 *  - tagName finding certificates with
 * for receiving info from database.
 * @see GiftService
 * */
    @GetMapping(value = "/all")
    public List<GiftCertificate> getAll(@RequestParam(required = false,name = "tag") String tagName, @RequestParam(required = false,name="text") String text, @RequestParam(required = false,name = "sort") String sort) {
        System.out.println(sort);
        List<GiftCertificate> certificates = giftService.getAll();
        if (tagName != null) {
            Tag tag = new Tag(); tag.setName(tagName);
            certificates = certificates.stream().filter(el -> el.getTags().contains(tag)).collect(Collectors.toList());
        }
        if(text != null) {
            certificates = certificates.stream().filter(el->el.getName().contains(text) || el.getDescription().contains(text)).collect(Collectors.toList());
        }
        if(sort != null) {
            if(sort.equalsIgnoreCase("ASC")) {
                certificates.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            }
            if(sort.equalsIgnoreCase("DESC")) {
                certificates.sort((o1, o2) -> o2.getName().compareToIgnoreCase(o1.getName()));
            }
        }
        return certificates;
    }

    /**
     * Rest endpoint for getting user from request body. Post method is used.
     * Entity is saved in database.
     * */
    @PostMapping(value="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveEntity(@RequestBody GiftCertificate giftCertificate) {
        giftService.save(giftCertificate);
        return "OK";
    }
/**
 * Rest endpoint for modifying certificate. Receive json body of new entity object.
 * @param id id of changing entity
 * */
    @PostMapping(value = "/modify/{id}")
    public String modifyCertificate(@RequestBody GiftCertificate giftCertificate, @PathVariable int id) {
        GiftCertificate certificate = giftService.getById(id);
        System.out.println(giftCertificate);
        if(certificate == null) {
            giftService.save(giftCertificate); // Status Created
        } else {
            giftService.update(id, giftCertificate); //Status OK
        }
        return "OK";
    }


    @PostMapping(value = "/modify/{id}/part")
    public String modifyCertificateByPart(@RequestBody GiftCertificate giftCertificate, @PathVariable int id, @RequestParam(required = false) Double price, @RequestParam(required = false) Short duration) {
        GiftCertificate certificate = giftService.getById(id);
        System.out.println(giftCertificate);
        System.out.println(duration);
        System.out.println(price);
        if(certificate == null) {
            giftService.save(giftCertificate); // Status Created
        } else {

            giftService.update(id, giftCertificate); //Status OK
        }
        return "OK";
    }

    /**
     * Post method for storing list of data from json format. Entity saved in database. Receive
     * list in JSON.
     * Return 201 status code if request is successful.
     * */
    @PostMapping(value = "/add_all")
    @ResponseStatus(HttpStatus.CREATED)
    public String addAll(@RequestBody List<GiftCertificate> certificates) {
        certificates.forEach(el->{
            el.setCreationDate(new Date());
            giftService.save(el);
        });
        return "OK";
    }
    /**
     * Delete method for deleting entity by id from database. Receive id as integer
     * and find entity with id in database.
     * */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteOne(@PathVariable int id) {
        giftService.delete(id);
        return "OK";
    }
}
