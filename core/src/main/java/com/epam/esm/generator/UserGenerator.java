package com.epam.esm.generator;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserGenerator {
    public static List<User> getTags(int count) {
        return IntStream.range(0,count).mapToObj(el->new User(0,"UserName №"+el,"User surname №"+el)).collect(Collectors.toList());
    }
}
