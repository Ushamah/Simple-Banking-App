package com.ushwamala.simplebankingapp.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ApplicationsApiControllerTest {
    @Test
    public void testConstructor() {
        assertFalse((new ApplicationsApiController(null)).getDelegate().getRequest().isPresent());
        assertNull(null);
    }
}

