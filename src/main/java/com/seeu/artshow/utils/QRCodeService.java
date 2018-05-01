package com.seeu.artshow.utils;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public interface QRCodeService {

    BufferedImage genCodeImage(String title, String content) throws IOException;

    BufferedImage getResourceGroup(Long groupId) throws ResourceNotFoundException, IOException;

    BufferedImage getWebItem(Long itemId) throws ResourceNotFoundException, IOException;

    InputStream genZipFile4ResGroups(Collection<Long> groupIds) throws IOException, ActionParameterException;

    InputStream genZipFile4WebItems(Collection<Long> itemIds) throws ActionParameterException, IOException;
}
