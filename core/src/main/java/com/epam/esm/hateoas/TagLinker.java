package com.epam.esm.hateoas;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagLinker extends AbstractLinker<Tag>{

    @Override
    public void setLinks(RepresentationModel<Tag> t) {
        Tag tag = (Tag)t;
        t.add(linkTo(methodOn(TagController.class).getTagById(tag.getId())).withSelfRel());
        t.add(linkTo(methodOn(TagController.class).saveTag(null)).withRel("add"));
        tag.add(linkTo(methodOn(TagController.class).getAllTags(null,null)).withRel("tagPage"));
        tag.add(linkTo(methodOn(TagController.class).deleteTag(null)).withRel("delete"));
    }
}
