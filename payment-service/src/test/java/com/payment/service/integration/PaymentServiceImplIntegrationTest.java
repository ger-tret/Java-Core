package com.payment.service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.payment.service.model.Payment;
import com.payment.service.model.PaymentStatus;
import com.payment.service.model.dto.CreatePaymentDto;
import com.payment.service.model.dto.PaymentDto;
import com.payment.service.repository.PaymentRepository;
import com.payment.service.service.PaymentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class PaymentServiceImplIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017);

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
            .withExposedPorts(9093);

    private static WireMockServer wireMockServer;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long TEST_ORDER_ID = 666L;
    private static final Long TEST_USER_ID = 912L;
    private static final BigDecimal TEST_AMOUNT = BigDecimal.valueOf(920);

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        wireMockServer.resetAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // MongoDB config
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

        // Kafka config
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");

        // External API config
        registry.add("external.random-number-api.url", () -> "http://localhost:" + wireMockServer.port());
    }

    @Test
    @DisplayName("Should create payment with SUCCESS status when external API returns even number")
    void createPayment_ShouldReturnSuccess_WhenExternalApiReturnsEven() throws Exception {
        CreatePaymentDto createPaymentDto = new CreatePaymentDto();
        createPaymentDto.setOrderId(TEST_ORDER_ID);
        createPaymentDto.setUserId(TEST_USER_ID);
        createPaymentDto.setPaymentAmount(TEST_AMOUNT);

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1.0/random?min=100&max=1000&count=1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[200]")
                        .withStatus(200)));

        
        Payment result = paymentService.createPayment(createPaymentDto);

        
        assertThat(result).isNotNull();
        assertThat(result.getPaymentId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(result.getOrderId()).isEqualTo(TEST_ORDER_ID);
        assertThat(result.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(result.getPaymentAmount()).isEqualTo(TEST_AMOUNT);

        Optional<Payment> savedPayment = paymentRepository.findById(result.getPaymentId());
        assertThat(savedPayment).isPresent();
        assertThat(savedPayment.get().getStatus()).isEqualTo(PaymentStatus.SUCCESS);

        wireMockServer.verify(1,
                WireMock.getRequestedFor(WireMock.urlEqualTo("/api/v1.0/random?min=100&max=1000&count=1")));
    }

    @Test
    @DisplayName("Should create payment with FAILED status when external API returns odd number")
    void createPayment_ShouldReturnFailed_WhenExternalApiReturnsOdd() throws Exception {
        CreatePaymentDto createPaymentDto = new CreatePaymentDto();
        createPaymentDto.setOrderId(TEST_ORDER_ID);
        createPaymentDto.setUserId(TEST_USER_ID);
        createPaymentDto.setPaymentAmount(TEST_AMOUNT);

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1.0/random?min=100&max=1000&count=1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[201]")
                        .withStatus(200)));

        
        Payment result = paymentService.createPayment(createPaymentDto);

        
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.FAILED);

        Optional<Payment> savedPayment = paymentRepository.findById(result.getPaymentId());
        assertThat(savedPayment).isPresent();
        assertThat(savedPayment.get().getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    @DisplayName("Should handle external API failure")
    void createPayment_ShouldHandleExternalApiFailure() {
        CreatePaymentDto createPaymentDto = new CreatePaymentDto();
        createPaymentDto.setOrderId(TEST_ORDER_ID);
        createPaymentDto.setUserId(TEST_USER_ID);
        createPaymentDto.setPaymentAmount(TEST_AMOUNT);

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1.0/random?min=100&max=1000&count=1"))
                .willReturn(WireMock.aResponse()
                        .withStatus(503)
                        .withBody("Service unavailable")));

        assertThatThrownBy(() -> paymentService.createPayment(createPaymentDto))
                .isInstanceOf(RuntimeException.class);

        assertThat(paymentRepository.count()).isZero();
    }

}