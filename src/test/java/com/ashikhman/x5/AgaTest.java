package com.ashikhman.x5;

import org.junit.jupiter.api.Test;

public class AgaTest {

    @Test
    void xxa() {
        var envs = System.getenv();

        System.out.println("XAXAXA: " + envs.get("rs.endpoint"));
    }
}
