package com.architecture.admin.api.v1.menu;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dto.menu.MenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*****************************************************
 * 메뉴 API
 ****************************************************/
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/menu")
public class MenuV1ApiController extends BaseController {

    /**
     * 카테고리 추가
     *
     * @param menuDto (name/level/state)
     * @param result
     * @return
     */
    @PostMapping("/category")
    public Object registCate(@Valid MenuDto menuDto,
                             BindingResult result){
        // 관리자 접근 권한
        super.adminAccessFail(2);

        if (result.hasErrors()) {
            return displayError(result, 400);
        }
        // 등록처리
        menuService.cateRegist(menuDto);

        String message = super.langMessage("lang.menu.regist");
        return displayJson(true, "1000", message);
    }

    /**
     * 하위 메뉴 추가
     *
     * @param menuDto (name/link/level/state)
     * @param result
     * @return
     */
    @PostMapping("")
    public Object regist(@Valid MenuDto menuDto,
                         BindingResult result){
        // 관리자 접근 권한
        super.adminAccessFail(2);
        if (result.hasErrors()) {
            return displayError(result, 400);
        }
        // 등록처리
        menuService.regist(menuDto);

        String message = super.langMessage("lang.menu.regist");
        return displayJson(true, "1000", message);
    }

    /**
     * 카테고리 수정
     *
     * @param menuDto (name,level,state)
     * @param result
     * @return
     */
    @PutMapping("")
    public ResponseEntity modify(@Valid MenuDto menuDto, BindingResult result){
        // 관리자 접근 권한
        super.adminAccessFail(2);
        if (result.hasErrors()) {
            return displayError(result, 400);
        }
        // 수정 처리
        menuService.cateModify(menuDto);

        String message = super.langMessage("lang.menu.modify");
        return displayJson(true, "1000", message);
    }

    /**
     * 카테고리 정렬 수정
     *
     * @param idx  ( menu.idx)
     * @param sort (정렬값)
     * @return
     */
    @GetMapping("/swap/{idx}/{sort}")
    public ResponseEntity swap(@PathVariable("idx") Integer idx,
                               @PathVariable("sort") String sort){
        // 관리자 접근 권한
        super.adminAccessFail(2);
        // 메뉴 정보 가져오기
        MenuDto oCateInfo = menuService.getCateInfo(idx);

        Integer iSwapIdx = null;
        if (Objects.equals(sort, "next")) {
            iSwapIdx = menuService.getNextSort(oCateInfo.getParent(), oCateInfo.getSort());
        } else if (Objects.equals(sort, "prev")) {
            iSwapIdx = menuService.getPrevSort(oCateInfo.getParent(), oCateInfo.getSort());
        } else {
            throw new CustomException(CustomError.MENU_SWAP_FAIL);
        }
        // 스왑활 idx 체크
        if (iSwapIdx == null) {
            throw new CustomException(CustomError.MENU_SWAP_FAIL);
        }

        // 스왑 메뉴 정보 가져오기
        MenuDto oSwapInfo = menuService.getCateInfo(iSwapIdx);

        Integer iOriSort = oCateInfo.getSort();     // 현 메뉴의 정렬순서
        Integer iSwapSort = oSwapInfo.getSort();    // 스왑할 메뉴의 정렬순서

        // 순서값들의 차이가 2 이상인 경우 1로 줄여주기
        // 오름차순 정렬이므로, 작은 값을 기준으로 큰값을 줄여줌
        if (iOriSort < iSwapSort && (iSwapSort - iOriSort) > 1) {
            iSwapSort = iOriSort + 1;
        } else {
            if (iOriSort > iSwapSort && (iOriSort - iSwapSort) > 1) {
                iOriSort = iSwapSort + 1;
            }
        }
        // 정렬 수정
        menuService.updateSort(idx, iSwapSort);
        menuService.updateSort(iSwapIdx, iOriSort);

        String message = super.langMessage("lang.menu.modify");
        return displayJson(true, "1000", message);
    }


    /**
     * 카테고리 정렬 수정
     *
     * @param myParams (idx[] menuidx값)
     * @return
     */
    @PatchMapping("/sort")
    public ResponseEntity sort(@RequestParam(value = "idx[]") String[] myParams){
        // 관리자 접근 권한
        super.adminAccessFail(2);
        List<String> list = new ArrayList<>(Arrays.asList(myParams));

        for (int i = 0; i < list.size(); i++) {
            menuService.updateSort(Integer.valueOf(list.get(i)), i + 1);
        }

        String message = super.langMessage("lang.menu.modify");
        return displayJson(true, "1000", message);
    }
}