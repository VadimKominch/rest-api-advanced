package com.epam.esm.controller;

import com.epam.esm.entity.ErrorResponse;
import com.epam.esm.entity.Tag;
import com.epam.esm.hateoas.AbstractLinker;
import com.epam.esm.hateoas.TagLinker;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Rest controller for tags. Uses /tag mapping for convenient usage.
 * Connects to database with tagService.
 *
 * @see {TagService}
 */
@RestController
@RequestMapping(value = "/tag")
public class TagController {

    private TagService tagService;

    private TagLinker tagLinker;
    private static final Logger logger = LogManager.getLogger();

    /**
     * Constructor for TagController.Autowire service for work with database.
     * @param tagService used service layer
     * @param tagLinker used to generate links for json hateoas
     * */
    @Autowired
    public TagController(TagService tagService, TagLinker tagLinker) {
        this.tagService = tagService;
        this.tagLinker = tagLinker;
    }

    /**
     * Get request for receiving one entity from database. Generate links for rest requests using hateoas
     *
     * @param id id of requested entity
     * @see {Hateoas}
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTagById(@PathVariable int id) {
        logger.debug(String.format("Got request to id: %d", id));
        try {
        Tag tag = tagService.getById(id);
        tagLinker.setLinks(tag);
            return new ResponseEntity<Tag>(tag, HttpStatus.OK);
        } catch(EmptyResultDataAccessException | NullPointerException e) {
            logger.info("No entity with id: "+id);
            return new ResponseEntity<ErrorResponse>(new ErrorResponse("Can't access tag with id: " + id,"40401"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Post request. which receive name of tag for saving in database.
     *
     * @param name name of tag to be stored in database
     */
    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveTag(@RequestBody String name) {
        logger.debug("attempt to save tag ");
        Tag tag = new Tag(name,0);
        tagService.save(tag);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    /**
     * Get request for getting all available tags.
     * */
    @GetMapping(value = "/all")
    public ResponseEntity<List<Tag>> getAllTags(@RequestParam(required = false,name = "page") Integer page,@RequestParam(required = false,name = "page_size") Integer pageSize) {
        logger.debug("Got all tags request");
        List<Tag> tags =  tagService.getPageOfList(page,pageSize);
        tags.forEach(el->el.add(linkTo(methodOn(TagController.class).getTagById(el.getId())).withSelfRel()));

        return new ResponseEntity<List<Tag>>(tags,HttpStatus.OK);
    }

    /**
     * Get tag count for pagination purpose.
     * */
    @GetMapping(value = "all/count")
    public ResponseEntity<?> getAllTagCount() {
        return new ResponseEntity<Long>(tagService.getTagCount(),HttpStatus.OK);
    }

    /**
     * Delete method for tag.
     * @param id id of deleting tag.
     * */
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Integer id) {
        logger.debug("attempt to delete tag with id: " + id);
        boolean delete = tagService.delete(id);
        if(delete) {
            return new ResponseEntity<String>("deleted", HttpStatus.OK);
        } else {
            logger.info("No entity with id: "+id);
            return new ResponseEntity<ErrorResponse>(new ErrorResponse(" tag not found, id: " + id,"40402"), HttpStatus.NOT_FOUND);
        }
    }
}
