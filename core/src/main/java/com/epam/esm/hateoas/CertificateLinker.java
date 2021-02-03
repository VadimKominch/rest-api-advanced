package com.epam.esm.hateoas;

import com.epam.esm.controller.GiftController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateLinker extends AbstractLinker<GiftCertificate> {

    private TagLinker tagLinker;

    @Autowired
    public CertificateLinker(TagLinker tagLinker) {
        this.tagLinker = tagLinker;
    }

    @Override
    public void setLinks(RepresentationModel<GiftCertificate> t) {
        GiftCertificate certificate = (GiftCertificate)t;
        t.add(linkTo(methodOn(GiftController.class).getGiftById(certificate.getId())).withSelfRel());
        certificate.add(linkTo(methodOn(UserController.class).processOrder(null,null)).withRel("purchase"));
        certificate.add(linkTo(methodOn(GiftController.class).getPage(null,null,null)).withRel("page"));
        certificate.add(linkTo(methodOn(GiftController.class).deleteOne(null)).withRel("delete"));
        certificate.getTags().forEach(el->tagLinker.setLinks(el));
    }
}
