package com.kycrm.syn.dao.item;

import com.kycrm.member.domain.entity.item.Item;

public class ItemWithBLOBs extends Item {
    private String descModules;

    private String props;

    private String propsName;

    private String wirelessDesc;

    public String getDescModules() {
        return descModules;
    }

    public void setDescModules(String descModules) {
        this.descModules = descModules == null ? null : descModules.trim();
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props == null ? null : props.trim();
    }

    public String getPropsName() {
        return propsName;
    }

    public void setPropsName(String propsName) {
        this.propsName = propsName == null ? null : propsName.trim();
    }

    public String getWirelessDesc() {
        return wirelessDesc;
    }

    public void setWirelessDesc(String wirelessDesc) {
        this.wirelessDesc = wirelessDesc == null ? null : wirelessDesc.trim();
    }
}