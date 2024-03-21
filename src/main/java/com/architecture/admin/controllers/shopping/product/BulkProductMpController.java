package com.architecture.admin.controllers.shopping.product;

import com.architecture.admin.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/shop/product/mp/bulk")
public class BulkProductMpController extends BaseController {


    /**
     * MP 상품 대량 등록
     * @return
     */
    @GetMapping()
    public String bulk() {

        hmImportFile.put("importJs", "shopping/productMpBulk/view.js.html");

        return "shopping/productMpBulk/view";
    }

}
