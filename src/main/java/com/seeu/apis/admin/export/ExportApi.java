package com.seeu.apis.admin.export;

import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Api(tags = "Export")
@Controller
@RequestMapping("/export")
public class ExportApi {

    @Autowired
    private UserService userService;

    @RequestMapping("/users.txt")
    public void exportUsers(HttpServletResponse response) throws IOException {
        List<User> users = userService.findAll(User.TYPE.USER);
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (null == user) continue;
            if (null == user.getPhone()) continue;
            bf.append(user.getPhone()).append("\n");
        }
//        Resource resource = new ByteArrayResource(bf.toString().getBytes());
//        response.getWriter().write(bf.toString());
//        response.setStatus(200);
        InputStream is = new ByteArrayInputStream(bf.toString().getBytes());
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/text;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=export_users.txt");
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        return;
    }
}
