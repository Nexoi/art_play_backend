package com.seeu.artshow.ar.service.impl;

import com.seeu.artshow.ar.service.ArService;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.file.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ArServiceImpl implements ArService {
    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ShowService showService;
    @Autowired
    private StorageService storageService;


    @Override
    public void asyncZipFile(Long showId) throws IOException {
        List<ResourceGroup> groups = resourceGroupService.findAll(showId);
//        List<ResourceGroup> groupList = groups.parallelStream().filter(it -> null != it.getAr()).collect(Collectors.toList());

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(storageService.getPath("/" + showId + ".zip")));
        ZipOutputStream zos = new ZipOutputStream(bos);
        for (ResourceGroup group : groups) {
            Image image = group.getAr();
            if (null == image || null == image.getUrl()) continue;
            String filePath = image.getUrl().replace("/", "_").substring(8);
            Resource material = storageService.loadAsResource(filePath);
            File file = material.getFile();

            // 创建Zip条目
            ZipEntry entry = new ZipEntry(file.getName());
            zos.putNextEntry(entry);

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            byte[] b = new byte[1024];

            while (bis.read(b, 0, 1024) != -1) {
                zos.write(b, 0, 1024);
            }
            bis.close();
            zos.closeEntry();
        }
        zos.flush();
        zos.close();
    }

    @Override
    public Resource loadFile(Long showId) {
        Resource material = storageService.loadAsResource(storageService.getPath("/" + showId + ".zip"));
        return material;
    }

    @Override
    public List<ArConfig> loadConfig(Long showId) throws ResourceNotFoundException {
        showService.findOne(showId);
        List<ResourceGroup> groups = resourceGroupService.findAll(showId);
        List<ArConfig> arConfigs = new ArrayList<>();
        for (ResourceGroup group : groups) {
            Image image = group.getAr();
            if (null == image || null == image.getUrl()) continue;
            String filePath = image.getUrl().replace("/", "_").substring(8);
            ArConfig config = new ArConfig();
            config.setImage(filePath);
            config.setName("" + group.getId());
            arConfigs.add(config);
        }
        return arConfigs;
    }
}
