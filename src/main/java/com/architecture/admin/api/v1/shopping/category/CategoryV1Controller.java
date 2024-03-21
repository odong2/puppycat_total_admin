package com.architecture.admin.api.v1.shopping.category;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.shopping.category.CategoryDto;
import com.architecture.admin.services.shopping.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/category")
public class CategoryV1Controller extends BaseController {

    private final CategoryService categoryService;


    /**
     * 상품 카테고리 목록
     * @return
     */
    @GetMapping()
    public ResponseEntity lists() {
        // 관리자 접근 권한
        super.adminAccessFail(61);
        // list
        Map<Integer, List<CategoryDto>> categoryList = categoryService.getCategoryList();

        JSONObject joData = new JSONObject();
        joData.put("list", categoryList);

        String sMessage = "";

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 상품 카테고리 하위 마지막 노드
     * @param categoryIdx
     * @return
     */
    @GetMapping("/lastNode/{categoryIdx}")
    public ResponseEntity lastNode(@PathVariable(name = "categoryIdx") Integer categoryIdx) {
        // 관리자 접근 권한
        super.adminAccessFail(61);
        CategoryDto lastNode = categoryService.getCategoryLastNode(categoryIdx);

        Map<String, Object> map = new HashMap<>();
        map.put("lastNode", lastNode);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (lastNode == null) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.category.exception.searchFail");

        }
        return displayJson(true, "1000", sMessage, data);
    }

}
