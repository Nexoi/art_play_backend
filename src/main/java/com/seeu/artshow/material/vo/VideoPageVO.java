package com.seeu.artshow.material.vo;

import com.seeu.artshow.material.model.Video;
import com.seeu.artshow.material.model.Folder;
import org.springframework.data.domain.Page;

public class VideoPageVO {
    private Folder currentFolder;
    private Page<Video> page;

    public VideoPageVO(Folder currentFolder, Page<Video> page) {
        this.currentFolder = currentFolder;
        this.page = page;
    }

    public Folder getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(Folder currentFolder) {
        this.currentFolder = currentFolder;
    }

    public Page<Video> getPage() {
        return page;
    }

    public void setPage(Page<Video> page) {
        this.page = page;
    }
}
