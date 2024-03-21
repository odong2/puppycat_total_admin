package com.architecture.admin.controllers.shopping.product;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.AllergyDto;
import com.architecture.admin.models.dto.pet.HealthDto;
import com.architecture.admin.models.dto.shopping.attributeSet.AttributeSetDto;
import com.architecture.admin.models.dto.shopping.brand.BrandDto;
import com.architecture.admin.models.dto.shopping.category.CategoryDto;
import com.architecture.admin.models.dto.shopping.certification.CertificationDto;
import com.architecture.admin.services.pet.PetAllergyService;
import com.architecture.admin.services.pet.PetHealthService;
import com.architecture.admin.services.shopping.attributeSet.AttributeSetService;
import com.architecture.admin.services.shopping.brand.BrandService;
import com.architecture.admin.services.shopping.category.CategoryService;
import com.architecture.admin.services.shopping.certification.CertificationService;
import com.architecture.admin.services.shopping.ingredient.IngredientService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/shop/product/mp/")
public class ProductMpController extends BaseController {

    private final CategoryService categoryService;
    private final BrandService brandService;
    private final CertificationService certificationService;
    private final AttributeSetService attributeSetService;
    private final IngredientService ingredientService;
    private final PetAllergyService petAllergyService;
    private final PetHealthService petHealthService;


    /**
     * MP 상품 목록
     * @param model
     * @param page
     * @param searchDto
     * @return
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(70);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "shopping/product/list.js.html");

        return "shopping/product/list";
    }


    /**
     * MP 상품 등록
     * @param model
     * @return
     */
    @GetMapping("/regist")
    public String regist(Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(70);

        Map<Integer, List<CategoryDto>> categoryList = categoryService.getCategoryList();
        Map<Integer, List<BrandDto>> brandList = brandService.getBrandList();
        List<BrandDto> brandGroupList = brandService.getBrandGroupList();
        List<CertificationDto> certificationList = certificationService.getCertificationList();
        Map<Integer, List<AttributeSetDto>> attributeSetList = attributeSetService.getAttributeSetList();
        List<AllergyDto> ingredientList = petAllergyService.getAllAllergyList();
        List<AllergyDto> unitList = ingredientService.getUnitList();
        List<HealthDto> healthList = petHealthService.getAllHealthList();


        JSONObject jsonData = new JSONObject();
        jsonData.put("brandGroupList", brandGroupList);
        jsonData.put("brandList", brandList);
        jsonData.put("categoryList", categoryList);
        jsonData.put("certificationList", certificationList);
        jsonData.put("attributeSetList", attributeSetList);
        jsonData.put("ingredientList", ingredientList);
        jsonData.put("unitList", unitList);
        jsonData.put("healthList", healthList);

        model.addAttribute("jsonData", jsonData);

        hmImportFile.put("importJs", "shopping/product/regist.js.html");

        return "shopping/product/regist";
    }

    /**
     * MP 상품 상세
     * @param idx
     * @param model
     * @return
     */
    @GetMapping(value = "/view/{idx}")
    public String view(@PathVariable(name = "idx", required = false) Integer idx, Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(70);

        Map<Integer, List<CategoryDto>> categoryList = categoryService.getCategoryList();
        Map<Integer, List<BrandDto>> brandList = brandService.getBrandList();
        List<BrandDto> brandGroupList = brandService.getBrandGroupList();
        List<CertificationDto> certificationList = certificationService.getCertificationList();
        Map<Integer, List<AttributeSetDto>> attributeSetList = attributeSetService.getAttributeSetList();
        List<AllergyDto> ingredientList = petAllergyService.getAllAllergyList();
        List<AllergyDto> unitList = ingredientService.getUnitList();
        List<HealthDto> healthList = petHealthService.getAllHealthList();

        JSONObject jsonData = new JSONObject();
        jsonData.put("brandGroupList", brandGroupList);
        jsonData.put("brandList", brandList);
        jsonData.put("categoryList", categoryList);
        jsonData.put("certificationList", certificationList);
        jsonData.put("attributeSetList", attributeSetList);
        jsonData.put("ingredientList", ingredientList);
        jsonData.put("unitList", unitList);
        jsonData.put("healthList", healthList);

        model.addAttribute("jsonData", jsonData);
        model.addAttribute("idx", idx);

        hmImportFile.put("importJs", "shopping/product/view.js.html");

        return "shopping/product/view";
    }

}
