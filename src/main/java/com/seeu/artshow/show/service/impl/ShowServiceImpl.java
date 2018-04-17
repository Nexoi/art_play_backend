package com.seeu.artshow.show.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.installation.service.ShowMapService;
import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.service.FolderService;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.repository.ShowRepository;
import com.seeu.artshow.show.service.BeaconService;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.artshow.showauth.service.ShowAuthService;
import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowServiceImpl implements ShowService {
    @Resource
    private ShowRepository repository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private ShowAuthService showAuthService;
    @Autowired
    private UserService userService;
    @Autowired
    private BeaconService beaconService;
    @Autowired
    private ShowMapService showMapService;
    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ResourceItemService resourceItemService;

    @Override
    public Show findOne(Long showId) throws ResourceNotFoundException {
        Show show = repository.findOne(showId);
        if (show == null) throw new ResourceNotFoundException("show", "id: " + showId);
        loadShowHallNames(show);
        return show;
    }

    @Override
    public void viewOnce(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
        repository.viewItOnce(show.getId());
    }

    @Override
    public void likeOnce(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
        repository.likeItOnce(show.getId());
    }

    @Override
    public void cancelLikeOnce(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
        repository.cancelLikeItOnce(show.getId());
    }

    @Override
    public void audioPlusOne(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
        repository.audioPlusOne(show.getId());
    }

    @Override
    public void audioMinusOne(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
        repository.audioMinusOne(show.getId());
    }

    @Override
    public Page<Show> findAll(Pageable pageable) {
        Page<Show> page = repository.findAllByOrderByEndTimeDesc(pageable);
        List<Show> showList = page.getContent();
        for (Show show : showList) {
            if (show == null) continue;
            loadShowHallNames(show);
        }
        return page;
    }

    @Override
    public Page<Show> searchAll(String title, Pageable pageable) {
        Page<Show> page = repository.findAllByTitleLikeOrderByEndTimeDesc("%" + title + "%", pageable);
        List<Show> showList = page.getContent();
        for (Show show : showList) {
            if (show == null) continue;
            loadShowHallNames(show);
        }
        return page;
    }

    // 筛选权限
    @Override
    public Page<Show> findAll(Long adminUid, Pageable pageable) throws NoSuchUserException {
        if (userService.isAdminX(adminUid)) {
            // 终极管理员，不用过滤
            Page<Show> page = repository.findAll(pageable);
            List<Show> showList = page.getContent();
            for (Show show : showList) {
                if (show == null) continue;
                loadShowHallNames(show);
            }
            return page;
        } else {
            List<Long> showIds = showAuthService.listAllShowIdForAdmin(adminUid);
            if (null == showIds || showIds.isEmpty()) return new PageImpl<Show>(new ArrayList<>(), pageable, 0);
            Page<Show> page = repository.findAllByIdIn(showIds, pageable);
            List<Show> showList = page.getContent();
            for (Show show : showList) {
                if (show == null) continue;
                loadShowHallNames(show);
            }
            return page;
        }
    }

    // 筛选权限
    @Override
    public Page<Show> searchAll(Long adminUid, String title, Pageable pageable) throws NoSuchUserException {
        if (userService.isAdminX(adminUid)) {
            // 终极管理员，不用过滤
            Page<Show> page = repository.findAllByTitleLike("%" + title + "%", pageable);
            List<Show> showList = page.getContent();
            for (Show show : showList) {
                if (show == null) continue;
                loadShowHallNames(show);
            }
            return page;
        } else {
            List<Long> showIds = showAuthService.listAllShowIdForAdmin(adminUid);
            if (null == showIds || showIds.isEmpty()) return new PageImpl<Show>(new ArrayList<>(), pageable, 0);
            Page<Show> page = repository.findAllByIdInAndTitleLike(showIds, "%" + title + "%", pageable);
            List<Show> showList = page.getContent();
            for (Show show : showList) {
                if (show == null) continue;
                loadShowHallNames(show);
            }
            return page;
        }
    }

    @Override
    public List<Show> findAll(Collection<Long> showIds) {
        if (showIds == null || showIds.isEmpty()) return new ArrayList<>();
        List<Show> showList = repository.findAllByIdIn(showIds);
        for (Show show : showList) {
            if (show == null) continue;
            loadShowHallNames(show);
        }
        return showList;
    }

    @Override
    public List<Show> findAll() {
        return repository.findAll();
    }

    @Override
    public Show add(Show show, Image image) throws ActionParameterException {
        if (show == null) throw new ActionParameterException("参数不能为空");
        Date now = new Date();
        show.setId(null);
        show.setLikeTimes(0L);
        show.setViewTimes(0L);
        show.setUpdateTime(now);
        // 初始化的时候不允许有值
        show.setShowHallNames(null);
        show.setMaps(null);
        if (null != image) {
            image.setCreateTime(now);
            image.setFolderId(-1L);
            image.setId(null);
            show.setPosterImage(imageService.save(image));
        }
        show = repository.save(show); // 持久化才会有 id
        // 自动添加素材文件夹
        String folderName = show.getTitle() == null ? "展览素材" : show.getTitle();
        Long showId = show.getId();
        Folder folder1 = new Folder();
        folder1.setShowId(showId);
        folder1.setCreateTime(now);
        folder1.setName(folderName);
        folder1.setType(Folder.TYPE.audio);
        Folder folder2 = new Folder();
        folder2.setShowId(showId);
        folder2.setCreateTime(now);
        folder2.setName(folderName);
        folder2.setType(Folder.TYPE.video);
        Folder folder3 = new Folder();
        folder3.setShowId(showId);
        folder3.setCreateTime(now);
        folder3.setName(folderName);
        folder3.setType(Folder.TYPE.picture);
        folderService.add(folder1);
        folderService.add(folder2);
        folderService.add(folder3);
        return show;
    }

    @Override
    public Show update(Show show, Image image) throws ActionParameterException, ResourceNotFoundException {
        if (show == null || show.getId() == null) throw new ActionParameterException("参数不能为空");
        Show savedShow = findOne(show.getId());
        if (show.getStartTime() != null) savedShow.setStartTime(show.getStartTime());
        if (show.getEndTime() != null) savedShow.setEndTime(show.getEndTime());
        if (show.getTitle() != null) savedShow.setTitle(show.getTitle());
        if (show.getIntroduceText() != null) savedShow.setIntroduceText(show.getIntroduceText());
//        if (show.getShowHallName() != null) savedShow.setShowHallName(show.getShowHallName());
//        if (show.getPosterImage() != null) savedShow.setPosterImage(show.getPosterImage());
        if (null != image) {
            image.setCreateTime(new Date());
            image.setFolderId(-1L);
            image.setId(null);
            savedShow.setPosterImage(imageService.save(image));
        }
        if (show.getTitle() != null) {
            if (!savedShow.getTitle().equals(show.getTitle())) {
                // 修改文件夹名称
                changeNameForFolder(show.getId(), show.getTitle());
            }
        }
        return repository.save(savedShow);
    }

    // 修改文件夹名称
    private List<Folder> changeNameForFolder(Long showId, String folderName) {
        List<Folder> folders = folderService.findAllByShowId(showId);
        if (null == folders || folders.isEmpty()) return new ArrayList<>();
        for (Folder folder : folders) {
            folder.setName(folderName);
        }
        return folderService.updateAll(folders);
    }

    @Override
    public void delete(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
//        // 先删除分配的 beacon
        beaconService.deleteAllByShowId(show.getId());
//        // 删除资源列表，关联了的，不必单独删除
//        resourceItemService.deleteAllByShowId(showId);
        // 删除资源组（会自动删除底下的资源列表）
        List<ResourceGroup> groups = resourceGroupService.findAll(showId);
        List<Long> groupIds = groups.parallelStream().map(ResourceGroup::getId).collect(Collectors.toList());
        resourceGroupService.delete(groupIds);
//      // 删除地图，关联了的，不必单独删除
//        showMapService.deleteAllByShowId(showId);
        // 删除对应的素材文件夹即可，内容就算了。。。
        folderService.deleteByShowId(showId);
        // 删除自己
        repository.delete(show.getId());
    }

    /**
     * lack validated
     */
    private void loadShowHallNames(Show show) {
        List<ShowMap> maps = show.getMaps();
        if (!maps.isEmpty()) {
            List<String> hallNames = new ArrayList<>();
            for (ShowMap map : maps) {
                if (map == null) continue;
                hallNames.add(map.getShowHallName());
            }
            show.setShowHallNames(hallNames);
        }
    }
}
