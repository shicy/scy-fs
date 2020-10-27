package org.scy.fs.controller;

import org.scy.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件处理
 * Created by shicy on 2020/10/27
 */
@Controller
public class FileController extends BaseController {

    @RequestMapping(value = "/file/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "/file/info/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public Object info(HttpServletRequest request, @PathVariable("uuid") String uuid) {
        return null;
    }

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(HttpServletRequest request, MultipartFile file) {
        return null;
    }

    @RequestMapping(value = "/file/download", method = RequestMethod.GET)
    public Object download(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @RequestMapping(value = "/file/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "/file/move", method = RequestMethod.POST)
    @ResponseBody
    public Object move(HttpServletRequest request) {
        return null;
    }

}
