package com.seeu.artshow.material.vo;

import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.model.Folder;
import org.springframework.data.domain.Page;

public class ImagePageVO {
    private Folder currentFolder;
    private Page<Image> page;

    public ImagePageVO(Folder currentFolder, Page<Image> page) {
        this.currentFolder = currentFolder;
        this.page = page;
    }

    public Folder getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(Folder currentFolder) {
        this.currentFolder = currentFolder;
    }

    public Page<Image> getPage() {
        return page;
    }

    public void setPage(Page<Image> page) {
        this.page = page;
    }
}
