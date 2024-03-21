package com.architecture.admin.controllers;

import com.architecture.admin.config.SessionConfig;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;

/*****************************************************
 * 메인페이지
 ****************************************************/
@Controller
public class MainController extends BaseController {
    @RequestMapping("/")
    public String main(@RequestParam(value="pid", defaultValue = "") String sPid) {

        return "main";
    }
}
