package com.kycrm.member.dao.syntrade.tradeSetup;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;

public interface TradeSetupDaosyn {
    long countByExample(TradeSetupExample example);

    int deleteByExample(TradeSetupExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TradeSetup record);

    int insertSelective(TradeSetup record);

    List<TradeSetup> selectByExample(TradeSetupExample example);

    TradeSetup selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TradeSetup record, @Param("example") TradeSetupExample example);

    int updateByExample(@Param("record") TradeSetup record, @Param("example") TradeSetupExample example);

    int updateByPrimaryKeySelective(TradeSetup record);

    int updateByPrimaryKey(TradeSetup record);
}