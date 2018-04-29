package com.seeu.artshow.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface QRCodeService {

    BufferedImage genCodeImage(String title, String content) throws IOException;

}
