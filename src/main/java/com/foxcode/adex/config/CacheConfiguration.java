package com.foxcode.adex.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.foxcode.adex.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.foxcode.adex.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Driver.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Driver.class.getName() + ".transports", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Driver.class.getName() + ".fuels", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Driver.class.getName() + ".distances", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Truck.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Truck.class.getName() + ".transports", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Truck.class.getName() + ".fuels", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Truck.class.getName() + ".distances", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Trailer.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Trailer.class.getName() + ".transports", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Transport.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.CompanyMain.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.CompanyMain.class.getName() + ".transports", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.CompanyMain.class.getName() + ".factories", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.CompanyFactory.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.CompanyFactory.class.getName() + ".transports", jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Distance.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxcode.adex.domain.Fuel.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
