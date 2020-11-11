package com.epam.esm.generator;

import com.epam.esm.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TagGenerator {
    public static List<Tag> getTags(int count) {
        return IntStream.range(0,count).mapToObj(el->new Tag("Tag №"+el,0)).collect(Collectors.toList());
    }
}
