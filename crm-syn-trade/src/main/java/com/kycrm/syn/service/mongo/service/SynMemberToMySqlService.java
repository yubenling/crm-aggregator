package com.kycrm.syn.service.mongo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kycrm.syn.service.mongo.dao.MemberRepository;
import com.kycrm.syn.service.mongo.entity.OldMemberDTO;

/** 
* @author wy
* @version 创建时间：2018年2月5日 下午3:38:47
*/
@Service
public class SynMemberToMySqlService {
    
    @Autowired
    private MemberRepository  memberRepository;
    
    public void doHandle(){
        int sratNum = 0;
        int pageSize = 1;
        while(true){
            Query query  = new Query();
            query.skip(sratNum).limit(pageSize);
            query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "_id")));
            List<OldMemberDTO> OldMemberDTOList = memberRepository.find(query ,"表名");
            
        }
        
        
    }
    
}
