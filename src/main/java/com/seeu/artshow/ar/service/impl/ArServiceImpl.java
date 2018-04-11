package com.seeu.artshow.ar.service.impl;

import com.seeu.artshow.ar.service.ArService;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.file.storage.StorageFileNotFoundException;
import com.seeu.file.storage.StorageService;
import me.chanjar.weixin.common.util.fs.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            String filePath = image.getUrl().replace("/", "_").replace(".", "-").substring(8);
            Resource material = null;
            try {
                material = storageService.loadAsResource(filePath);
            } catch (StorageFileNotFoundException e) {
                // download and save, reload
                try {
                    UrlResource urlResource = new UrlResource(image.getUrl());
                    InputStream inputStream = urlResource.getInputStream();
                    org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream,new File(storageService.getPath("/" + filePath)));

                    // 读取会残缺乱码文件
//                    OutputStream bos2 = new FileOutputStream(storageService.getPath("/" + filePath));
//                    byte[] b = new byte[10240];
//                    while (inputStream.read(b) != -1) {
//                        bos2.write(b);
//                    }
//                    bos2.close();
                    // reload
                    material = storageService.loadAsResource(filePath);
                } catch (Exception exc) {
                    // 所有的异常都被捕获，避免迭代文件终止
                }
            }
            if (null == material) continue;
            File file = material.getFile();

            // 创建Zip条目
            ZipEntry entry = new ZipEntry(file.getName());
            zos.putNextEntry(entry);

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            byte[] b = new byte[10240];

            while (bis.read(b, 0, 10240) != -1) {
                zos.write(b, 0, 10240);
            }
            bis.close();
            zos.closeEntry();
        }
        zos.flush();
        zos.close();
    }

    @Override
    public Resource loadFile(Long showId) {
//        Resource material = storageService.loadAsResource("/" + showId + ".zip");
//        return material;
        try {
            Path file = Paths.get(storageService.getPath("/" + showId + ".zip"));
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: showId:" + showId);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: showId: " + showId, e);
        }
    }

    @Override
    public List<ArConfig> loadConfig(Long showId) throws ResourceNotFoundException {
        showService.findOne(showId);
        List<ResourceGroup> groups = resourceGroupService.findAll(showId);
        List<ArConfig> arConfigs = new ArrayList<>();
        for (ResourceGroup group : groups) {
            Image image = group.getAr();
            if (null == image || null == image.getUrl()) continue;
            String filePath = image.getUrl().replace("/", "_").replace(".", "-").substring(8);
            ArConfig config = new ArConfig();
            config.setImage(filePath);
            config.setName("" + group.getId());
            arConfigs.add(config);
        }
        return arConfigs;
    }
}
