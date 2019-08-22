package com.kycrm.syn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kycrm.member.service.trade.ITradeDTOService;

/** 
* @author wy
* @version 创建时间：2018年1月29日 下午3:06:08
*/
@Controller
public class Test {
    @Autowired
    private ITradeDTOService tradeDTOService;
    @RequestMapping("/test")
    public void test(){
        System.out.println(this.tradeDTOService.findTradeStatusByTid(5L, 1L));
    }
}
