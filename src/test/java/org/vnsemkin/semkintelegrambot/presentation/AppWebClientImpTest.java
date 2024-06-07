package org.vnsemkin.semkintelegrambot.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.vnsemkin.semkintelegrambot.SemkinTelegramBotApplicationTests;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.presentation.web_client.AppWebClientImp;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppWebClientImpTest extends SemkinTelegramBotApplicationTests {
    private final static String TEST = "Test";
    private final static String ERROR = "Error";
    private final static String REGISTRATION_ENDPOINT = "/registration";
    private final static String BASE_URL = "http://localhost:";

    @Autowired
    AppWebClientImp webClient;
    @Autowired
    ObjectMapper objectMapper;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        System.setProperty("middle-service.base-url",
            BASE_URL + wireMockServer.port());
    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void whenRegisterCustomer_Success() throws JsonProcessingException {
        // ARRANGE
        CustomerDto customerDto = new CustomerDto(TEST, TEST, TEST);
        String customerJson = objectMapper.writeValueAsString(customerDto);
        stubFor(post(urlEqualTo(REGISTRATION_ENDPOINT))
            .willReturn(aResponse()
                .withStatus(HttpStatus.CREATED.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(customerJson)));
        // ACT
        Result<CustomerDto, String> result = webClient.registerCustomer(customerDto);
        //ASSERT
        verify(postRequestedFor(urlEqualTo(REGISTRATION_ENDPOINT)));
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isPresent());
        assertEquals(result.getData().get().name(), customerDto.name());
        assertEquals(result.getData().get().email(), customerDto.email());
        assertEquals(result.getData().get().password(), customerDto.password());
    }

    @Test
    public void whenRegisterCustomer_Fail() throws JsonProcessingException {
        // ARRANGE
        CustomerDto customerDto = new CustomerDto(TEST, TEST, TEST);
        String customerJson = objectMapper.writeValueAsString(customerDto);
        stubFor(post(urlEqualTo(REGISTRATION_ENDPOINT))
            .willReturn(aResponse()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withBody(ERROR)));
        // ACT
        Result<CustomerDto, String> result = webClient.registerCustomer(customerDto);
        //ASSERT
        verify(postRequestedFor(urlEqualTo(REGISTRATION_ENDPOINT)));
        assertNotNull(result);
        assertTrue(result.isError());
        assertFalse(result.isSuccess());
        assertEquals(result.getError().get(), ERROR);
    }
}