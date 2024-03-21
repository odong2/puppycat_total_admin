package com.architecture.admin.controllers.menu;

import com.architecture.admin.config.AdminLevelConfig;
import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.menu.MenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

/*****************************************************
 * SNS 회원 컨트롤러
 ****************************************************/
@RequiredArgsConstructor
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    /**
     * 메뉴 리스트
     *
     * @param params MenuDto
     * @return 메뉴 리스트
     */
    @GetMapping("/list")
    public String list(@ModelAttribute("param") final MenuDto params) {
        // 관리자 접근 권한
        super.adminAccessFail(2);
        // get menu
        List<MenuDto> list = menuService.getList(params);
        // set view data
        hmDataSet.put("list", list);
        // view pages
        hmImportFile.put("importJs", "menu/menu.js.html");
        return "menu/list";
    }

    /**
     * 카테고리 추가
     *
     * @return 카테고리 추가
     */
    @GetMapping("/category/regist")
    public String cateRegist() {
        // 관리자 접근 권한
        super.adminAccessFail(2);
        // 관리자 레벨 가져오기
        HashMap<Integer, String> hmAdminLevel = (HashMap<Integer, String>) AdminLevelConfig.getAdminLevel();
        // set view data
        hmDataSet.put("adminLevel", hmAdminLevel);
        // view pages
        hmImportFile.put("importJs", "menu/menu.js.html");
        return "menu/category/regist";
    }

    /**
     * 하위 메뉴 추가
     *
     * @param idx parent idx
     * @return 하위메뉴 추가 페이지
     */
    @GetMapping("/regist/{idx}")
    public String regist(@PathVariable("idx") Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(2);

        // 관리자 레벨 가져오기
        HashMap<Integer, String> hmAdminLevel = (HashMap<Integer, String>) AdminLevelConfig.getAdminLevel();
        // set view data
        hmDataSet.put("adminLevel", hmAdminLevel);
        hmDataSet.put("idx", idx);
        // view pages
        hmImportFile.put("importJs", "menu/menu.js.html");
        return "menu/regist";
    }

    /**
     * 카테고리 수정
     *
     * @param idx (sns_menu.idx)
     * @return
     */
    @GetMapping("/category/modify/{idx}")
    public String cateModify(@PathVariable("idx") Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(2);
        // get list
        MenuDto oCate = menuService.getCateInfo(idx);
        // 관리자 레벨 가져오기
        HashMap<Integer, String> hmAdminLevel = (HashMap<Integer, String>) AdminLevelConfig.getAdminLevel();
        // set view data
        hmDataSet.put("adminLevel", hmAdminLevel);
        hmDataSet.put("data", oCate);
        // view pages
        hmImportFile.put("importJs", "menu/menu.js.html");
        return "menu/category/modify";
    }

    /**
     * 카테고리 하위 메뉴 수정
     *
     * @param idx (menu.inx)
     * @return
     */
    @GetMapping("/modify/{idx}")
    public String modify(@PathVariable("idx") Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(2);
        // get list
        MenuDto oCate = menuService.getCateInfo(idx);
        // 관리자 레벨 가져오기
        HashMap<Integer, String> hmAdminLevel = (HashMap<Integer, String>) AdminLevelConfig.getAdminLevel();
        // set view data
        hmDataSet.put("adminLevel", hmAdminLevel);
        hmDataSet.put("idx", idx);
        hmDataSet.put("data", oCate);
        // view pages
        hmImportFile.put("importJs", "menu/menu.js.html");
        return "menu/modify";
    }
}