package com.ashikhman.x5;

import org.junit.jupiter.api.Test;

public class AgaTest {

    @Test
    void xxa() {
        var aga = new Child();

        System.out.println("XAXA: " + aga);
    }

    public static interface Parent {

    }

    public static class Child {
        @Override
        public String toString() {
            return "XAXAX";
        }
    }
}
