package co.tpg.catalog.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, co.tpg.catalog.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, co.tpg.catalog.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, co.tpg.catalog.domain.User.class.getName());
            createCache(cm, co.tpg.catalog.domain.Authority.class.getName());
            createCache(cm, co.tpg.catalog.domain.User.class.getName() + ".authorities");
            createCache(cm, co.tpg.catalog.domain.Role.class.getName());
            createCache(cm, co.tpg.catalog.domain.Role.class.getName() + ".userRoles");
            createCache(cm, co.tpg.catalog.domain.UserRole.class.getName());
            createCache(cm, co.tpg.catalog.domain.UserRole.class.getName() + ".roles");
            createCache(cm, co.tpg.catalog.domain.Subject.class.getName());
            createCache(cm, co.tpg.catalog.domain.Requirement.class.getName());
            createCache(cm, co.tpg.catalog.domain.Qualification.class.getName());
            createCache(cm, co.tpg.catalog.domain.TeachingStaff.class.getName());
            createCache(cm, co.tpg.catalog.domain.TeachingStaff.class.getName() + ".papers");
            createCache(cm, co.tpg.catalog.domain.TeachingClass.class.getName());
            createCache(cm, co.tpg.catalog.domain.Paper.class.getName());
            createCache(cm, co.tpg.catalog.domain.Paper.class.getName() + ".teachingStaffs");
            createCache(cm, co.tpg.catalog.domain.Contact.class.getName());
            createCache(cm, co.tpg.catalog.domain.Address.class.getName());
            createCache(cm, co.tpg.catalog.domain.EducationalInstituition.class.getName());
            createCache(cm, co.tpg.catalog.domain.Campus.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
