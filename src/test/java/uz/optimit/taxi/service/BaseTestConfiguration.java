package uz.optimit.taxi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import uz.optimit.taxi.repository.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class BaseTestConfiguration {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected AnnouncementDriverRepository announcementDriverRepository;
    @Autowired
    protected AnnouncementPassengerRepository announcementPassengerRepository;
    @Autowired
    protected AttachmentRepository attachmentRepository;
    @Autowired
    protected AutoCategoryRepository autoCategoryRepository;
    @Autowired
    protected AutoModelRepository autoModelRepository;
    @Autowired
    protected CarRepository carRepository;
    @Autowired
    protected CityRepository cityRepository;
    @Autowired
    protected FamiliarRepository familiarRepository;
    @Autowired
    protected NotificationRepository notificationRepository;
    @Autowired
    protected RegionRepository regionRepository;
    @Autowired
    protected SeatRepository seatRepository;
    @Autowired
    protected StatusRepository statusRepository;
    @Autowired
    protected UserRepository userRepository;

    protected static final PostgreSQLContainer<?> postgres;
    private static final String IMAGE_NAME = "postgres:latest";


    static {
        postgres = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse(IMAGE_NAME))
                .withInitScript("sql/table-init.sql")
                .withExposedPorts(5432);
        postgres.withReuse(true);
        Startables.deepStart(postgres).join();
    }

    @DynamicPropertySource
    static void setUpPostgresConnectionProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.database", postgres::getDatabaseName);
    }
}
