package com.epam.esm.controller;

import com.epam.esm.entity.ErrorResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.hateoas.CertificateLinker;
import com.epam.esm.hateoas.TagLinker;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Rest Controller for basic crud operations and operations with list of data.
 * Use service as way to connect with database
 *
 * @author Vadim Kominch
 */
@RestController
public class GiftController {


    private TagService tagService;
    private GiftService giftService;

    private CertificateLinker certificateLinker;
    private TagLinker tagLinker;

    protected static final Logger logger = LogManager.getLogger();

    @Autowired
    public GiftController(GiftService giftService, TagService tagService, CertificateLinker certificateLinker, TagLinker tagLinker) {
        this.tagService = tagService;
        this.giftService = giftService;
        this.certificateLinker = certificateLinker;
        this.tagLinker = tagLinker;
    }

    /**
     * Get method for reading one entity if its present. Receive id as integer in url path
     * and find entity in database.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<GiftCertificate> getGiftById(@PathVariable int id) {
        GiftCertificate giftCertificateById = giftService.getById(id);
        certificateLinker.setLinks(giftCertificateById);
        return new ResponseEntity<GiftCertificate>(giftCertificateById, HttpStatus.OK);
    }

    /**
     * Get request for finding certificates with a list of tags.
     */
    @GetMapping(value = "/find")
    public ResponseEntity<?> getGiftsByTags(@RequestBody List<String> tagNames, @RequestParam(required = false, name = "page") Integer page, @RequestParam(required = false, name = "page_size") Integer pageSize) {
        List<Tag> tags = tagNames.stream().map(el -> new Tag(el, 0)).collect(Collectors.toList());
        List<GiftCertificate> certificatesByTagNames = giftService.getCertificatesByTagNames(tags);
        if (certificatesByTagNames.isEmpty()) {
            return new ResponseEntity<ErrorResponse>(new ErrorResponse("Certificate not found", "404"), HttpStatus.NOT_FOUND);
        } else {
            certificatesByTagNames.forEach(el -> certificateLinker.setLinks(el));
            return new ResponseEntity<List<GiftCertificate>>(certificatesByTagNames, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/most")
    public ResponseEntity<?> getTheMostUsedTagInTheMostExpensiveOrderByUser() {
        Tag mostUsableTag = giftService.getMostUsableTagInMostExpensiveCostOFOrdersByOneUser();
        if (mostUsableTag != null) {
            tagLinker.setLinks(mostUsableTag);
            return new ResponseEntity<Tag>(mostUsableTag, HttpStatus.OK);
        } else
            return new ResponseEntity<String>("Tag not found", HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/get_page")
    public ResponseEntity<List<GiftCertificate>> getPage(@RequestParam(required = false, name = "page") Integer page,
                                                         @RequestParam(required = false, name = "page_size") Integer pageSize,
                                                         @RequestParam(required = false, name = "sort") String sort) {
        List<GiftCertificate> pageOfCertificates = giftService.getPageOfCertificates(page, pageSize, Optional.of(sort));
        pageOfCertificates.forEach(el -> certificateLinker.setLinks(el));
        return new ResponseEntity<List<GiftCertificate>>(pageOfCertificates, HttpStatus.OK);
    }

    /**
     * Rest endpoint for getting user from request body. Post method is used.
     * Entity is saved in database.
     */
    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveEntity(@RequestBody GiftCertificate giftCertificate) {
        giftCertificate.getTags().forEach(el -> {
            Tag tag = tagService.getByName(el.getName());
            if (tag == null)
                tagService.save(el);
            else
                el.setId(tag.getId());
        });
        giftService.save(giftCertificate);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    /**
     * Rest endpoint for modifying certificate. Receive json body of new entity object.
     *
     * @param id id of changing entity
     */
    @PostMapping(value = "/modify/{id}")
    public ResponseEntity<String> modifyCertificate(@RequestBody GiftCertificate giftCertificate, @PathVariable int id) {
        GiftCertificate certificate = giftService.getById(id);
        if (certificate == null) {
            giftService.save(giftCertificate); // Status Created
        } else {
            giftService.update(id, giftCertificate); //Status OK
        }
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }


    /**
     * Post method for storing list of data from json format. Entity saved in database. Receive
     * list in JSON.
     * Return 201 status code if request is successful.
     */
    @PostMapping(value = "/add_all")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addAll(@RequestBody List<GiftCertificate> certificates) {
        certificates.forEach(el -> {
            el.setLastUpdateDate(new Date());
            el.setCreationDate(new Date());
            giftService.save(el);
        });
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    /**
     * Delete method for deleting entity by id from database. Receive id as integer
     * and find entity with id in database.
     *
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteOne(@PathVariable Integer id) {
        giftService.delete(id);
        return new ResponseEntity<String>("OK", HttpStatus.NO_CONTENT);
    }
}
