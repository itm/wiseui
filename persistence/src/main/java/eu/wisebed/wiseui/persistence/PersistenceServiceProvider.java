package eu.wisebed.wiseui.persistence;

import eu.wisebed.wiseui.api.PersistenceService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Provides an configured instance of the {@link eu.wisebed.wiseui.persistence.service.PersistenceServiceImpl}.
 *
 * @author Soenke Nommensen
 */
public class PersistenceServiceProvider {

    /**
     * Provides an configured instance of the {@link eu.wisebed.wiseui.persistence.service.PersistenceServiceImpl}.
     * The instance will be a Singleton as defined in the Spring configuration (this the default for Beans).
     * @return Configured singleton instance of the {@link eu.wisebed.wiseui.persistence.service.PersistenceServiceImpl}
     */
    public static PersistenceService newPersistenceService() {
        final String springConfig = "persistence-spring-config.xml";
        final ApplicationContext springContext = new ClassPathXmlApplicationContext(springConfig);
        return springContext.getBean(PersistenceService.class);
    }
}