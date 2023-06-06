package com.nekonex.services.department;

import com.nekonex.services.department.model.Department;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;

import static io.micronaut.http.HttpStatus.UNAUTHORIZED;
import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Property(name = "in-memory-store.enabled", value = "true")
@Requires(env = Environment.TEST)
public class DepartmentControllerTests {
    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void add() {

        Department department2 = Instancio.of(Department.class)
                .set(Select.field(Department::organizationId), 1L)
                .ignore(Select.field(Department::id))
                .create();
        //User didn t login
        Executable e = () -> client.toBlocking().exchange(HttpRequest.POST("/api/departments", department2).accept(TEXT_PLAIN));

        // then: 'returns unauthorized'
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(UNAUTHORIZED, thrown.getStatus());

        //User didn t login with correct credentials
        e = () -> client.toBlocking().exchange(HttpRequest.POST("/api/departments", department2)
                .basicAuth("john", "secret2").accept(TEXT_PLAIN));

        // then: 'returns unauthorized'
        thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(UNAUTHORIZED, thrown.getStatus());


        Department department = Instancio.of(Department.class)
                .set(Select.field(Department::organizationId), 1L)
                .ignore(Select.field(Department::id))
                .create();
        department = client.toBlocking()
                .retrieve(HttpRequest.POST("/api/departments", department)
                        .basicAuth("john", "secret"), Department.class);
        assertNotNull(department);
        assertNotNull(department.id());
    }

    @Test
    void findAll() {
        add();
        Department[] departments = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/departments")
                        .basicAuth("john", "secret"), Department[].class);
        assertTrue(departments.length == 2);
    }

    @Test
    void findById() {
        add();
        Department department = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/departments/10" )
                        .basicAuth("john", "secret"), Department.class);
        assertNotNull(department);
        assertNotNull(department.id());
    }

    @Test
    void findByOrganization() {
        add();
        Department[] departments = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/departments/organization/" + 1L)
                        .basicAuth("john", "secret"), Department[].class);
        assertTrue(departments.length > 0);
    }
}
