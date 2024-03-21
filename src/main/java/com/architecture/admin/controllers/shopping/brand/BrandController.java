package com.architecture.admin.controllers.shopping.brand;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/shop/brand")
public class BrandController extends BaseController {

    @GetMapping("/list")
    public String lists(Model model
            , @RequestParam(value = "page", defaultValue = "1") int page
            , @ModelAttribute SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(65);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "shopping/brand/list.js.html");

        return "shopping/brand/list";
    }

}
