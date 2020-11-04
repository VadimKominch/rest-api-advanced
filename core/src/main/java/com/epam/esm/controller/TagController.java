package com.epam.esm.controller;

import com.epam.esm.entity.ErrorResponse;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for tags. Uses /tag mapping for convenient usage.
 * Connects to database with tagDao.
 *
 * @see {TagDao}
 */
@RestController
@RequestMapping(value = "/tag")
public class TagController {

    private TagService tagService;
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Constructor for TagController.Autowire dao for connection with database.
     * */
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Get request for receiving one entity from database.
     *
     * @param id id of requested entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTagById(@PathVariable int id) {
        logger.debug(String.format("Got request to id: %d", id));
        Tag tag = tagService.getById(id);
        if(tag != null) {
            return new ResponseEntity<Tag>(tag, HttpStatus.OK);
        } else {
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

    @GetMapping(value = "/all")
    public List<Tag> getAllTags() {
        logger.debug("Got all tags request");
        return tagService.getAll();
    }


    /**
     * Delete method for tag.
     * @param id id of deleting tag.
     * */
    @DeleteMapping(value="delete/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable int id) {
        logger.debug("attempt to delete tag with id: " + id);
        boolean delete = tagService.delete(id);
        if(delete) {
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
