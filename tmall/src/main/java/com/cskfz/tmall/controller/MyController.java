package com.cskfz.tmall.controller;

import com.cskfz.tmall.dao.TProductHome;
import com.cskfz.tmall.entity.TProduct;
import com.cskfz.tmall.pojo.ActionResult;
import com.cskfz.tmall.pojo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * @Author: yangzc
 * @Description:
 * @Date: Created on 16:04 2019/11/16
 * @Modified By:
 */
@Controller
@RequestMapping("/")
public class MyController {

    @Autowired
    private TProductHome productDao;


    @GetMapping("/index")
    public String homePage() {
        return "index";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    /**
     * 获取商品列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/portal/list",method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getProducts(@RequestBody Map<String,String> map) {
        //int rows = Integer.parseInt(req.getParameter("rows"));
        int rows = Integer.parseInt(map.get("rows"));
        //
        //int page = Integer.parseInt(req.getParameter("page"));
        int page = Integer.parseInt(map.get("page"));
        int begin = (page-1)*rows;
        int total = productDao.getTotal();
        List<TProduct> list = productDao.findByExample(begin, rows);
        List<ProductVO> list2 = new ArrayList<ProductVO>();
        Map<String,Object> data = new HashMap<String,Object>();
        for(TProduct p:list) {
            ProductVO pvo = new ProductVO();
            pvo.setId(p.getId());
            pvo.setName(p.getName());
            pvo.setImageUrl(p.getImageUrl());
            pvo.setPrice(p.getPrice());
            pvo.setStock(p.getStock());
            pvo.setQuantity(1);
            list2.add(pvo);
        }
        data.put("total", total);
        data.put("rows", list2);
        return ActionResult.ok(data);
    }

}
