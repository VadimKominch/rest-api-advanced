package com.epam.esm.controller;

import com.epam.esm.entity.ErrorResponse;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
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
    private static final Logger logger = LogManager.getLogger();

    /**
     * Constructor for TagController.Autowire service for work with database.
     * @param tagService used service layer
     * */
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
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
        tag.add(linkTo(methodOn(TagController.class).getAllTags()).withRel("all"));
        tag.add(linkTo(methodOn(TagController.class).getTagById(id)).withSelfRel());
        tag.add(linkTo(methodOn(TagController.class).deleteTag(id)).withSelfRel());
            return new ResponseEntity<Tag>(tag, HttpStatus.OK);
        } catch(EmptyResultDataAccessException e) {
            logger.info("No entity with id: "+id);
            return new ResponseEntity<ErrorResponse>(new ErrorResponse("Can't access tag with id: " + id,"40401"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Post request. which receive name of tag for saving in database.
     *
     * @param name name of tag to be stored in database
     */
    @PostMapping(value = "add")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveTag(@RequestBody String name) {
        logger.debug("attempt to save tag ");
        Tag tag = new Tag(name,0);
        tagService.save(tag);
        return "OK";
    }

    /**
     * Get request for getting all available tags.
     * */
//TODO init module3_base for creating repositories
    //TODO update repositories for pagination ans sorting
    @GetMapping(value = "/all")
    public List<Tag> getAllTags() {
        logger.debug("Got all tags request");
        List<Tag> tags =  tagService.getAll();
        tags.forEach(el->{
            el.add(linkTo(methodOn(TagController.class).getTagById(el.getId())).withSelfRel());
            el.add(linkTo(methodOn(TagController.class).deleteTag(el.getId())).withSelfRel());
        });

        return tags;
    }


    /**
     * Delete method for tag.
     * @param id id of deleting tag.
     * */
    @DeleteMapping(value="{id}/delete")
    public ResponseEntity<?> deleteTag(@PathVariable int id) {
        logger.debug("attempt to delete tag with id: " + id);
        tagService.delete(id);
        if(tagService.getById(id) == null) {
            return new ResponseEntity<String>("deleted", HttpStatus.OK);
        } else {
            logger.info("No entity with id: "+id);
            return new ResponseEntity<ErrorResponse>(new ErrorResponse(" tag not found, id: " + id,"40402"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/unknown")
    @ResponseBody
    public ResponseEntity<?> getUnknown(@RequestParam(required = false) Integer id) {
        if(id == null) {
            return new ResponseEntity<ErrorResponse>(new ErrorResponse("Bad request","401"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(tagService.getAll(), HttpStatus.ACCEPTED);
        }
    }
}
