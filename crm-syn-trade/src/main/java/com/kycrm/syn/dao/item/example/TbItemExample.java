package com.kycrm.syn.dao.item.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbItemExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbItemExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andNumIidIsNull() {
            addCriterion("num_iid is null");
            return (Criteria) this;
        }

        public Criteria andNumIidIsNotNull() {
            addCriterion("num_iid is not null");
            return (Criteria) this;
        }

        public Criteria andNumIidEqualTo(Long value) {
            addCriterion("num_iid =", value, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidNotEqualTo(Long value) {
            addCriterion("num_iid <>", value, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidGreaterThan(Long value) {
            addCriterion("num_iid >", value, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidGreaterThanOrEqualTo(Long value) {
            addCriterion("num_iid >=", value, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidLessThan(Long value) {
            addCriterion("num_iid <", value, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidLessThanOrEqualTo(Long value) {
            addCriterion("num_iid <=", value, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidIn(List<Long> values) {
            addCriterion("num_iid in", values, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidNotIn(List<Long> values) {
            addCriterion("num_iid not in", values, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidBetween(Long value1, Long value2) {
            addCriterion("num_iid between", value1, value2, "numIid");
            return (Criteria) this;
        }

        public Criteria andNumIidNotBetween(Long value1, Long value2) {
            addCriterion("num_iid not between", value1, value2, "numIid");
            return (Criteria) this;
        }

        public Criteria andNickIsNull() {
            addCriterion("nick is null");
            return (Criteria) this;
        }

        public Criteria andNickIsNotNull() {
            addCriterion("nick is not null");
            return (Criteria) this;
        }

        public Criteria andNickEqualTo(String value) {
            addCriterion("nick =", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotEqualTo(String value) {
            addCriterion("nick <>", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickGreaterThan(String value) {
            addCriterion("nick >", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickGreaterThanOrEqualTo(String value) {
            addCriterion("nick >=", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLessThan(String value) {
            addCriterion("nick <", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLessThanOrEqualTo(String value) {
            addCriterion("nick <=", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLike(String value) {
            addCriterion("nick like", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotLike(String value) {
            addCriterion("nick not like", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickIn(List<String> values) {
            addCriterion("nick in", values, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotIn(List<String> values) {
            addCriterion("nick not in", values, "nick");
            return (Criteria) this;
        }

        public Criteria andNickBetween(String value1, String value2) {
            addCriterion("nick between", value1, value2, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotBetween(String value1, String value2) {
            addCriterion("nick not between", value1, value2, "nick");
            return (Criteria) this;
        }

        public Criteria andApproveStatusIsNull() {
            addCriterion("approve_status is null");
            return (Criteria) this;
        }

        public Criteria andApproveStatusIsNotNull() {
            addCriterion("approve_status is not null");
            return (Criteria) this;
        }

        public Criteria andApproveStatusEqualTo(String value) {
            addCriterion("approve_status =", value, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusNotEqualTo(String value) {
            addCriterion("approve_status <>", value, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusGreaterThan(String value) {
            addCriterion("approve_status >", value, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusGreaterThanOrEqualTo(String value) {
            addCriterion("approve_status >=", value, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusLessThan(String value) {
            addCriterion("approve_status <", value, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusLessThanOrEqualTo(String value) {
            addCriterion("approve_status <=", value, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusLike(String value) {
            addCriterion("approve_status like", value, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusNotLike(String value) {
            addCriterion("approve_status not like", value, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusIn(List<String> values) {
            addCriterion("approve_status in", values, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusNotIn(List<String> values) {
            addCriterion("approve_status not in", values, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusBetween(String value1, String value2) {
            addCriterion("approve_status between", value1, value2, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andApproveStatusNotBetween(String value1, String value2) {
            addCriterion("approve_status not between", value1, value2, "approveStatus");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseIsNull() {
            addCriterion("has_showcase is null");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseIsNotNull() {
            addCriterion("has_showcase is not null");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseEqualTo(String value) {
            addCriterion("has_showcase =", value, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseNotEqualTo(String value) {
            addCriterion("has_showcase <>", value, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseGreaterThan(String value) {
            addCriterion("has_showcase >", value, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseGreaterThanOrEqualTo(String value) {
            addCriterion("has_showcase >=", value, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseLessThan(String value) {
            addCriterion("has_showcase <", value, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseLessThanOrEqualTo(String value) {
            addCriterion("has_showcase <=", value, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseLike(String value) {
            addCriterion("has_showcase like", value, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseNotLike(String value) {
            addCriterion("has_showcase not like", value, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseIn(List<String> values) {
            addCriterion("has_showcase in", values, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseNotIn(List<String> values) {
            addCriterion("has_showcase not in", values, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseBetween(String value1, String value2) {
            addCriterion("has_showcase between", value1, value2, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andHasShowcaseNotBetween(String value1, String value2) {
            addCriterion("has_showcase not between", value1, value2, "hasShowcase");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNull() {
            addCriterion("created is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNotNull() {
            addCriterion("created is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedEqualTo(Date value) {
            addCriterion("created =", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotEqualTo(Date value) {
            addCriterion("created <>", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThan(Date value) {
            addCriterion("created >", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("created >=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThan(Date value) {
            addCriterion("created <", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThanOrEqualTo(Date value) {
            addCriterion("created <=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedIn(List<Date> values) {
            addCriterion("created in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotIn(List<Date> values) {
            addCriterion("created not in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedBetween(Date value1, Date value2) {
            addCriterion("created between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotBetween(Date value1, Date value2) {
            addCriterion("created not between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andModifiedIsNull() {
            addCriterion("modified is null");
            return (Criteria) this;
        }

        public Criteria andModifiedIsNotNull() {
            addCriterion("modified is not null");
            return (Criteria) this;
        }

        public Criteria andModifiedEqualTo(Date value) {
            addCriterion("modified =", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedNotEqualTo(Date value) {
            addCriterion("modified <>", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedGreaterThan(Date value) {
            addCriterion("modified >", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedGreaterThanOrEqualTo(Date value) {
            addCriterion("modified >=", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedLessThan(Date value) {
            addCriterion("modified <", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedLessThanOrEqualTo(Date value) {
            addCriterion("modified <=", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedIn(List<Date> values) {
            addCriterion("modified in", values, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedNotIn(List<Date> values) {
            addCriterion("modified not in", values, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedBetween(Date value1, Date value2) {
            addCriterion("modified between", value1, value2, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedNotBetween(Date value1, Date value2) {
            addCriterion("modified not between", value1, value2, "modified");
            return (Criteria) this;
        }

        public Criteria andCidIsNull() {
            addCriterion("cid is null");
            return (Criteria) this;
        }

        public Criteria andCidIsNotNull() {
            addCriterion("cid is not null");
            return (Criteria) this;
        }

        public Criteria andCidEqualTo(String value) {
            addCriterion("cid =", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotEqualTo(String value) {
            addCriterion("cid <>", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidGreaterThan(String value) {
            addCriterion("cid >", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidGreaterThanOrEqualTo(String value) {
            addCriterion("cid >=", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLessThan(String value) {
            addCriterion("cid <", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLessThanOrEqualTo(String value) {
            addCriterion("cid <=", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLike(String value) {
            addCriterion("cid like", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotLike(String value) {
            addCriterion("cid not like", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidIn(List<String> values) {
            addCriterion("cid in", values, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotIn(List<String> values) {
            addCriterion("cid not in", values, "cid");
            return (Criteria) this;
        }

        public Criteria andCidBetween(String value1, String value2) {
            addCriterion("cid between", value1, value2, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotBetween(String value1, String value2) {
            addCriterion("cid not between", value1, value2, "cid");
            return (Criteria) this;
        }

        public Criteria andHasDiscountIsNull() {
            addCriterion("has_discount is null");
            return (Criteria) this;
        }

        public Criteria andHasDiscountIsNotNull() {
            addCriterion("has_discount is not null");
            return (Criteria) this;
        }

        public Criteria andHasDiscountEqualTo(String value) {
            addCriterion("has_discount =", value, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountNotEqualTo(String value) {
            addCriterion("has_discount <>", value, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountGreaterThan(String value) {
            addCriterion("has_discount >", value, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountGreaterThanOrEqualTo(String value) {
            addCriterion("has_discount >=", value, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountLessThan(String value) {
            addCriterion("has_discount <", value, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountLessThanOrEqualTo(String value) {
            addCriterion("has_discount <=", value, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountLike(String value) {
            addCriterion("has_discount like", value, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountNotLike(String value) {
            addCriterion("has_discount not like", value, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountIn(List<String> values) {
            addCriterion("has_discount in", values, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountNotIn(List<String> values) {
            addCriterion("has_discount not in", values, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountBetween(String value1, String value2) {
            addCriterion("has_discount between", value1, value2, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andHasDiscountNotBetween(String value1, String value2) {
            addCriterion("has_discount not between", value1, value2, "hasDiscount");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeIsNull() {
            addCriterion("jdp_hashcode is null");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeIsNotNull() {
            addCriterion("jdp_hashcode is not null");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeEqualTo(String value) {
            addCriterion("jdp_hashcode =", value, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeNotEqualTo(String value) {
            addCriterion("jdp_hashcode <>", value, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeGreaterThan(String value) {
            addCriterion("jdp_hashcode >", value, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeGreaterThanOrEqualTo(String value) {
            addCriterion("jdp_hashcode >=", value, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeLessThan(String value) {
            addCriterion("jdp_hashcode <", value, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeLessThanOrEqualTo(String value) {
            addCriterion("jdp_hashcode <=", value, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeLike(String value) {
            addCriterion("jdp_hashcode like", value, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeNotLike(String value) {
            addCriterion("jdp_hashcode not like", value, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeIn(List<String> values) {
            addCriterion("jdp_hashcode in", values, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeNotIn(List<String> values) {
            addCriterion("jdp_hashcode not in", values, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeBetween(String value1, String value2) {
            addCriterion("jdp_hashcode between", value1, value2, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpHashcodeNotBetween(String value1, String value2) {
            addCriterion("jdp_hashcode not between", value1, value2, "jdpHashcode");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteIsNull() {
            addCriterion("jdp_delete is null");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteIsNotNull() {
            addCriterion("jdp_delete is not null");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteEqualTo(Integer value) {
            addCriterion("jdp_delete =", value, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteNotEqualTo(Integer value) {
            addCriterion("jdp_delete <>", value, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteGreaterThan(Integer value) {
            addCriterion("jdp_delete >", value, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteGreaterThanOrEqualTo(Integer value) {
            addCriterion("jdp_delete >=", value, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteLessThan(Integer value) {
            addCriterion("jdp_delete <", value, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteLessThanOrEqualTo(Integer value) {
            addCriterion("jdp_delete <=", value, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteIn(List<Integer> values) {
            addCriterion("jdp_delete in", values, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteNotIn(List<Integer> values) {
            addCriterion("jdp_delete not in", values, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteBetween(Integer value1, Integer value2) {
            addCriterion("jdp_delete between", value1, value2, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpDeleteNotBetween(Integer value1, Integer value2) {
            addCriterion("jdp_delete not between", value1, value2, "jdpDelete");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedIsNull() {
            addCriterion("jdp_created is null");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedIsNotNull() {
            addCriterion("jdp_created is not null");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedEqualTo(Date value) {
            addCriterion("jdp_created =", value, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedNotEqualTo(Date value) {
            addCriterion("jdp_created <>", value, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedGreaterThan(Date value) {
            addCriterion("jdp_created >", value, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("jdp_created >=", value, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedLessThan(Date value) {
            addCriterion("jdp_created <", value, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedLessThanOrEqualTo(Date value) {
            addCriterion("jdp_created <=", value, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedIn(List<Date> values) {
            addCriterion("jdp_created in", values, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedNotIn(List<Date> values) {
            addCriterion("jdp_created not in", values, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedBetween(Date value1, Date value2) {
            addCriterion("jdp_created between", value1, value2, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpCreatedNotBetween(Date value1, Date value2) {
            addCriterion("jdp_created not between", value1, value2, "jdpCreated");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedIsNull() {
            addCriterion("jdp_modified is null");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedIsNotNull() {
            addCriterion("jdp_modified is not null");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedEqualTo(Date value) {
            addCriterion("jdp_modified =", value, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedNotEqualTo(Date value) {
            addCriterion("jdp_modified <>", value, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedGreaterThan(Date value) {
            addCriterion("jdp_modified >", value, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedGreaterThanOrEqualTo(Date value) {
            addCriterion("jdp_modified >=", value, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedLessThan(Date value) {
            addCriterion("jdp_modified <", value, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedLessThanOrEqualTo(Date value) {
            addCriterion("jdp_modified <=", value, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedIn(List<Date> values) {
            addCriterion("jdp_modified in", values, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedNotIn(List<Date> values) {
            addCriterion("jdp_modified not in", values, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedBetween(Date value1, Date value2) {
            addCriterion("jdp_modified between", value1, value2, "jdpModified");
            return (Criteria) this;
        }

        public Criteria andJdpModifiedNotBetween(Date value1, Date value2) {
            addCriterion("jdp_modified not between", value1, value2, "jdpModified");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}