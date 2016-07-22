package com.sap.bulletinboard.ads.model;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import com.sap.bulletinboard.ads.config.EmbeddedDatabaseConfig;
import com.sap.bulletinboard.ads.models.Advertisement;
import com.sap.bulletinboard.ads.models.AdvertisementRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmbeddedDatabaseConfig.class)
public class AdvertisementRepositoryTest {
    
    @Inject
    private AdvertisementRepository repo;

    @Test
    public void shouldSetIdOnFirstSave() {
        Advertisement entity = new Advertisement();
        entity.setTitle("title");
        entity = repo.save(entity);
        assertThat(entity.getId(), is(notNullValue()));
    }
    
    @Test
    public void shouldSetCreatedTimestampOnFirstSaveOnly() throws InterruptedException{
        Advertisement entity = new Advertisement();
        entity.setTitle("title");

        entity = repo.save(entity);
        Timestamp timestampAfterCreation = entity.getCreatedAt();
        assertThat(timestampAfterCreation, is(notNullValue()));

        entity.setTitle("Updated Title");
        Thread.sleep(5); //Better: mock time!

        entity = repo.save(entity);
        Timestamp timestampAfterUpdate = entity.getCreatedAt();
        assertThat(timestampAfterUpdate, is(timestampAfterCreation));
    }

}
