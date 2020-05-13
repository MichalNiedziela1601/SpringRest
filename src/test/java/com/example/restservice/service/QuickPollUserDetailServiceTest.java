package com.example.restservice.service;

import com.example.restservice.RestServiceApplication;
import com.example.restservice.domain.User;
import com.example.restservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class QuickPollUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    QuickPollUserDetailService service;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLoadUserByUsernameGetUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setLastName("kowalski");
        user.setFirstName("Tomasz");
        user.setAdmin(false);
        user.setPassword("password");

        when(userRepository.findByUsername(any(String.class))).thenReturn(user);

        UserDetails userDetails = service.loadUserByUsername("user");

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void testLoadUserByUsernameGetAdmin() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setLastName("kowalski");
        user.setFirstName("Tomasz");
        user.setAdmin(true);
        user.setPassword("password");

        when(userRepository.findByUsername(any(String.class))).thenReturn(user);

        UserDetails userDetails = service.loadUserByUsername("admin");

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertFalse(userDetails.getAuthorities().isEmpty());
        Object[] objects = userDetails.getAuthorities().toArray();
        assertEquals("ROLE_ADMIN", objects[0].toString());
    }

    @Test
    void testLoadUserByUsernameWhenEmptyThenThrowUserNotFoundException() {
        try {
            service.loadUserByUsername("mickey");
        } catch (UsernameNotFoundException e) {
            assertEquals("User with the username mickey doesn't exists", e.getMessage());
        }
    }
}