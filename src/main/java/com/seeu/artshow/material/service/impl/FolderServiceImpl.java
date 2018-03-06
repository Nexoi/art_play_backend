package com.seeu.artshow.material.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.repository.FolderRepository;
import com.seeu.artshow.material.service.FolderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    @Resource
    private FolderRepository repository;

    @Override
    public Folder findOne(Folder.TYPE type) {
        return repository.findFirstByType(type);
    }

    @Override
    public Folder findOne(Long id) throws ResourceNotFoundException {
        Folder folder = repository.findOne(id);
        if (folder == null)
            throw new ResourceNotFoundException("folder", "id" + id);
        return folder;
    }

    @Override
    public Folder findByName(String name) throws ResourceNotFoundException {
        Folder folder = repository.findByName(name);
        if (folder == null)
            throw new ResourceNotFoundException("folder", name);
        return folder;
    }

    @Override
    public List<Folder> findAllByType(Folder.TYPE type) {
        return repository.findAllByType(type);
    }

    @Override
    public Folder add(Folder folder) throws ActionParameterException {
        if (folder == null || folder.getName() == null) return null;
        if (folder.getName().contains(" ")) throw new ActionParameterException("参数：name 不能包含空格");
        if (!isValidFileName(folder.getName())) throw new ActionParameterException("参数：name 格式错误");
        folder.setId(null);
        folder.setCreateTime(new Date());
        return repository.save(folder);
    }

    @Override
    public Folder changeName(Long id, String name) throws ResourceNotFoundException {
        Folder folder = findOne(id);
        if (name == null) return folder;
        folder.setName(name);
        return repository.save(folder);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }

    private boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255) return false;
        else
            return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }

}
