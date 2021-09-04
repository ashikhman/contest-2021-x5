package com.ashikhman.x5;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgaTest {

    @Test
    void xxa() {
        var aga = new HashMap<Integer, List<String>>();
        aga.computeIfAbsent(1, k -> new ArrayList<>()).add("k1");
        aga.computeIfAbsent(1, k -> new ArrayList<>()).add("k2");

        System.out.println(aga);
    }
}
