package com.epam.esm.hateoas;

import org.springframework.hateoas.RepresentationModel;

public abstract class AbstractLinker<T extends RepresentationModel<? extends T>> {
    abstract void setLinks(RepresentationModel<T> t);
}
