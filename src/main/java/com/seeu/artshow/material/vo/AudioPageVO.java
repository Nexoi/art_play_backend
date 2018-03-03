package com.seeu.artshow.material.vo;

import com.seeu.artshow.material.model.Audio;
import com.seeu.artshow.material.model.Folder;
import org.springframework.data.domain.Page;

public class AudioPageVO {
    private Folder currentFolder;
    private Page<Audio> page;

    public AudioPageVO(Folder currentFolder, Page<Audio> page) {
        this.currentFolder = currentFolder;
        this.page = page;
    }

    public Folder getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(Folder currentFolder) {
        this.currentFolder = currentFolder;
    }

    public Page<Audio> getPage() {
        return page;
    }

    public void setPage(Page<Audio> page) {
        this.page = page;
    }
}
