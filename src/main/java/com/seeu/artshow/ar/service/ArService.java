package com.seeu.artshow.ar.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.List;

public interface ArService {

    @Async
    void asyncZipFile(Long showId) throws IOException;

    Resource loadFile(Long showId);

    List<ArConfig> loadConfig(Long showId) throws ResourceNotFoundException;

    class ArConfig {
        private Long uid;
        private String name;
        private String image;

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
