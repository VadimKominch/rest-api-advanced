package com.epam.esm.hateoas;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinker extends AbstractLinker<User>{
    private TagLinker tagLinker;
    private CertificateLinker certificateLinker;

    @Autowired
    public UserLinker(TagLinker tagLinker, CertificateLinker certificateLinker) {
        this.tagLinker = tagLinker;
        this.certificateLinker = certificateLinker;
    }

    @Override
    public void setLinks(RepresentationModel<User> t) {
        User user = (User) t;
        t.add(linkTo(methodOn(UserController.class).getById(user.getId())).withSelfRel());
        t.add(linkTo(methodOn(UserController.class).getOrderInfo(user.getId(),null)).withRel("order_info"));
        user.getOrders().forEach(el->{
            el.getCertificate().forEach(el1->certificateLinker.setLinks(el1));
            el.add(linkTo(methodOn(UserController.class).getOrderInfo(user.getId(),el.getId())).withSelfRel());
        });
        t.add(linkTo(methodOn(UserController.class).processOrder(null,user.getId())).withRel("order"));
    }
}
