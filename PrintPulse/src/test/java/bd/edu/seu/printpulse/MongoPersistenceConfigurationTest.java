package bd.edu.seu.printpulse;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MongoPersistenceConfigurationTest {

    @Test
    void printRequestIsMappedToPrintMongoCollection() throws Exception {
        Annotation document = Arrays.stream(PrintRequest.class.getAnnotations())
                .filter(annotation -> annotation.annotationType().getName()
                        .equals("org.springframework.data.mongodb.core.mapping.Document"))
                .findFirst()
                .orElse(null);

        assertNotNull(document, "PrintRequest must use MongoDB @Document");
        String collection = (String) document.annotationType()
                .getMethod("collection")
                .invoke(document);
        assertEquals("print", collection);
    }

    @Test
    void repositoryUsesMongoRepositoryWithIntegerId() {
        Type mongoRepositoryType = Arrays.stream(PrintRequestInterface.class.getGenericInterfaces())
                .filter(type -> type.getTypeName().startsWith("org.springframework.data.mongodb.repository.MongoRepository"))
                .findFirst()
                .orElse(null);

        assertNotNull(mongoRepositoryType, "PrintRequestInterface must extend MongoRepository");
        assertInstanceOf(ParameterizedType.class, mongoRepositoryType);

        ParameterizedType parameterizedType = (ParameterizedType) mongoRepositoryType;
        assertEquals(PrintRequest.class, parameterizedType.getActualTypeArguments()[0]);
        assertEquals(Integer.class, parameterizedType.getActualTypeArguments()[1]);
    }

    @Test
    void applicationPropertiesUseMongoUriInsteadOfMysqlDatasource() throws Exception {
        String properties = Files.readString(Path.of("src/main/resources/application.properties"));

        assertTrue(properties.contains("spring.mongodb.uri=${MONGODB_URI}"));
        assertFalse(properties.contains("spring.datasource."));
        assertFalse(properties.contains("spring.jpa."));
    }

    @Test
    void pomUsesMongoStarterInsteadOfJpaAndMysql() throws Exception {
        String pom = Files.readString(Path.of("pom.xml"));

        assertTrue(pom.contains("spring-boot-starter-data-mongodb"));
        assertFalse(pom.contains("spring-boot-starter-data-jpa"));
        assertFalse(pom.contains("mysql-connector-j"));
    }
}
