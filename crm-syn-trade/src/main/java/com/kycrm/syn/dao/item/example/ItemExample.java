package com.kycrm.syn.dao.item.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ItemExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(Long value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(Long value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(Long value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(Long value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(Long value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(Long value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<Long> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<Long> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(Long value1, Long value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(Long value1, Long value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
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

        public Criteria andSellerCidsIsNull() {
            addCriterion("seller_cids is null");
            return (Criteria) this;
        }

        public Criteria andSellerCidsIsNotNull() {
            addCriterion("seller_cids is not null");
            return (Criteria) this;
        }

        public Criteria andSellerCidsEqualTo(String value) {
            addCriterion("seller_cids =", value, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsNotEqualTo(String value) {
            addCriterion("seller_cids <>", value, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsGreaterThan(String value) {
            addCriterion("seller_cids >", value, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsGreaterThanOrEqualTo(String value) {
            addCriterion("seller_cids >=", value, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsLessThan(String value) {
            addCriterion("seller_cids <", value, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsLessThanOrEqualTo(String value) {
            addCriterion("seller_cids <=", value, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsLike(String value) {
            addCriterion("seller_cids like", value, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsNotLike(String value) {
            addCriterion("seller_cids not like", value, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsIn(List<String> values) {
            addCriterion("seller_cids in", values, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsNotIn(List<String> values) {
            addCriterion("seller_cids not in", values, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsBetween(String value1, String value2) {
            addCriterion("seller_cids between", value1, value2, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andSellerCidsNotBetween(String value1, String value2) {
            addCriterion("seller_cids not between", value1, value2, "sellerCids");
            return (Criteria) this;
        }

        public Criteria andPropsIsNull() {
            addCriterion("props is null");
            return (Criteria) this;
        }

        public Criteria andPropsIsNotNull() {
            addCriterion("props is not null");
            return (Criteria) this;
        }

        public Criteria andPropsEqualTo(String value) {
            addCriterion("props =", value, "props");
            return (Criteria) this;
        }

        public Criteria andPropsNotEqualTo(String value) {
            addCriterion("props <>", value, "props");
            return (Criteria) this;
        }

        public Criteria andPropsGreaterThan(String value) {
            addCriterion("props >", value, "props");
            return (Criteria) this;
        }

        public Criteria andPropsGreaterThanOrEqualTo(String value) {
            addCriterion("props >=", value, "props");
            return (Criteria) this;
        }

        public Criteria andPropsLessThan(String value) {
            addCriterion("props <", value, "props");
            return (Criteria) this;
        }

        public Criteria andPropsLessThanOrEqualTo(String value) {
            addCriterion("props <=", value, "props");
            return (Criteria) this;
        }

        public Criteria andPropsLike(String value) {
            addCriterion("props like", value, "props");
            return (Criteria) this;
        }

        public Criteria andPropsNotLike(String value) {
            addCriterion("props not like", value, "props");
            return (Criteria) this;
        }

        public Criteria andPropsIn(List<String> values) {
            addCriterion("props in", values, "props");
            return (Criteria) this;
        }

        public Criteria andPropsNotIn(List<String> values) {
            addCriterion("props not in", values, "props");
            return (Criteria) this;
        }

        public Criteria andPropsBetween(String value1, String value2) {
            addCriterion("props between", value1, value2, "props");
            return (Criteria) this;
        }

        public Criteria andPropsNotBetween(String value1, String value2) {
            addCriterion("props not between", value1, value2, "props");
            return (Criteria) this;
        }

        public Criteria andInputPidsIsNull() {
            addCriterion("input_pids is null");
            return (Criteria) this;
        }

        public Criteria andInputPidsIsNotNull() {
            addCriterion("input_pids is not null");
            return (Criteria) this;
        }

        public Criteria andInputPidsEqualTo(String value) {
            addCriterion("input_pids =", value, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsNotEqualTo(String value) {
            addCriterion("input_pids <>", value, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsGreaterThan(String value) {
            addCriterion("input_pids >", value, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsGreaterThanOrEqualTo(String value) {
            addCriterion("input_pids >=", value, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsLessThan(String value) {
            addCriterion("input_pids <", value, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsLessThanOrEqualTo(String value) {
            addCriterion("input_pids <=", value, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsLike(String value) {
            addCriterion("input_pids like", value, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsNotLike(String value) {
            addCriterion("input_pids not like", value, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsIn(List<String> values) {
            addCriterion("input_pids in", values, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsNotIn(List<String> values) {
            addCriterion("input_pids not in", values, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsBetween(String value1, String value2) {
            addCriterion("input_pids between", value1, value2, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputPidsNotBetween(String value1, String value2) {
            addCriterion("input_pids not between", value1, value2, "inputPids");
            return (Criteria) this;
        }

        public Criteria andInputStrIsNull() {
            addCriterion("input_str is null");
            return (Criteria) this;
        }

        public Criteria andInputStrIsNotNull() {
            addCriterion("input_str is not null");
            return (Criteria) this;
        }

        public Criteria andInputStrEqualTo(String value) {
            addCriterion("input_str =", value, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrNotEqualTo(String value) {
            addCriterion("input_str <>", value, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrGreaterThan(String value) {
            addCriterion("input_str >", value, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrGreaterThanOrEqualTo(String value) {
            addCriterion("input_str >=", value, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrLessThan(String value) {
            addCriterion("input_str <", value, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrLessThanOrEqualTo(String value) {
            addCriterion("input_str <=", value, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrLike(String value) {
            addCriterion("input_str like", value, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrNotLike(String value) {
            addCriterion("input_str not like", value, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrIn(List<String> values) {
            addCriterion("input_str in", values, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrNotIn(List<String> values) {
            addCriterion("input_str not in", values, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrBetween(String value1, String value2) {
            addCriterion("input_str between", value1, value2, "inputStr");
            return (Criteria) this;
        }

        public Criteria andInputStrNotBetween(String value1, String value2) {
            addCriterion("input_str not between", value1, value2, "inputStr");
            return (Criteria) this;
        }

        public Criteria andNumIsNull() {
            addCriterion("num is null");
            return (Criteria) this;
        }

        public Criteria andNumIsNotNull() {
            addCriterion("num is not null");
            return (Criteria) this;
        }

        public Criteria andNumEqualTo(Integer value) {
            addCriterion("num =", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotEqualTo(Integer value) {
            addCriterion("num <>", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThan(Integer value) {
            addCriterion("num >", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("num >=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThan(Integer value) {
            addCriterion("num <", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThanOrEqualTo(Integer value) {
            addCriterion("num <=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumIn(List<Integer> values) {
            addCriterion("num in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotIn(List<Integer> values) {
            addCriterion("num not in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumBetween(Integer value1, Integer value2) {
            addCriterion("num between", value1, value2, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotBetween(Integer value1, Integer value2) {
            addCriterion("num not between", value1, value2, "num");
            return (Criteria) this;
        }

        public Criteria andListTimeIsNull() {
            addCriterion("list_time is null");
            return (Criteria) this;
        }

        public Criteria andListTimeIsNotNull() {
            addCriterion("list_time is not null");
            return (Criteria) this;
        }

        public Criteria andListTimeEqualTo(Date value) {
            addCriterion("list_time =", value, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeNotEqualTo(Date value) {
            addCriterion("list_time <>", value, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeGreaterThan(Date value) {
            addCriterion("list_time >", value, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("list_time >=", value, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeLessThan(Date value) {
            addCriterion("list_time <", value, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeLessThanOrEqualTo(Date value) {
            addCriterion("list_time <=", value, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeIn(List<Date> values) {
            addCriterion("list_time in", values, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeNotIn(List<Date> values) {
            addCriterion("list_time not in", values, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeBetween(Date value1, Date value2) {
            addCriterion("list_time between", value1, value2, "listTime");
            return (Criteria) this;
        }

        public Criteria andListTimeNotBetween(Date value1, Date value2) {
            addCriterion("list_time not between", value1, value2, "listTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeIsNull() {
            addCriterion("delist_time is null");
            return (Criteria) this;
        }

        public Criteria andDelistTimeIsNotNull() {
            addCriterion("delist_time is not null");
            return (Criteria) this;
        }

        public Criteria andDelistTimeEqualTo(Date value) {
            addCriterion("delist_time =", value, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeNotEqualTo(Date value) {
            addCriterion("delist_time <>", value, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeGreaterThan(Date value) {
            addCriterion("delist_time >", value, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("delist_time >=", value, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeLessThan(Date value) {
            addCriterion("delist_time <", value, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeLessThanOrEqualTo(Date value) {
            addCriterion("delist_time <=", value, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeIn(List<Date> values) {
            addCriterion("delist_time in", values, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeNotIn(List<Date> values) {
            addCriterion("delist_time not in", values, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeBetween(Date value1, Date value2) {
            addCriterion("delist_time between", value1, value2, "delistTime");
            return (Criteria) this;
        }

        public Criteria andDelistTimeNotBetween(Date value1, Date value2) {
            addCriterion("delist_time not between", value1, value2, "delistTime");
            return (Criteria) this;
        }

        public Criteria andStuffStatusIsNull() {
            addCriterion("stuff_status is null");
            return (Criteria) this;
        }

        public Criteria andStuffStatusIsNotNull() {
            addCriterion("stuff_status is not null");
            return (Criteria) this;
        }

        public Criteria andStuffStatusEqualTo(Date value) {
            addCriterion("stuff_status =", value, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusNotEqualTo(Date value) {
            addCriterion("stuff_status <>", value, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusGreaterThan(Date value) {
            addCriterion("stuff_status >", value, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusGreaterThanOrEqualTo(Date value) {
            addCriterion("stuff_status >=", value, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusLessThan(Date value) {
            addCriterion("stuff_status <", value, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusLessThanOrEqualTo(Date value) {
            addCriterion("stuff_status <=", value, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusIn(List<Date> values) {
            addCriterion("stuff_status in", values, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusNotIn(List<Date> values) {
            addCriterion("stuff_status not in", values, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusBetween(Date value1, Date value2) {
            addCriterion("stuff_status between", value1, value2, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andStuffStatusNotBetween(Date value1, Date value2) {
            addCriterion("stuff_status not between", value1, value2, "stuffStatus");
            return (Criteria) this;
        }

        public Criteria andZipIsNull() {
            addCriterion("zip is null");
            return (Criteria) this;
        }

        public Criteria andZipIsNotNull() {
            addCriterion("zip is not null");
            return (Criteria) this;
        }

        public Criteria andZipEqualTo(String value) {
            addCriterion("zip =", value, "zip");
            return (Criteria) this;
        }

        public Criteria andZipNotEqualTo(String value) {
            addCriterion("zip <>", value, "zip");
            return (Criteria) this;
        }

        public Criteria andZipGreaterThan(String value) {
            addCriterion("zip >", value, "zip");
            return (Criteria) this;
        }

        public Criteria andZipGreaterThanOrEqualTo(String value) {
            addCriterion("zip >=", value, "zip");
            return (Criteria) this;
        }

        public Criteria andZipLessThan(String value) {
            addCriterion("zip <", value, "zip");
            return (Criteria) this;
        }

        public Criteria andZipLessThanOrEqualTo(String value) {
            addCriterion("zip <=", value, "zip");
            return (Criteria) this;
        }

        public Criteria andZipLike(String value) {
            addCriterion("zip like", value, "zip");
            return (Criteria) this;
        }

        public Criteria andZipNotLike(String value) {
            addCriterion("zip not like", value, "zip");
            return (Criteria) this;
        }

        public Criteria andZipIn(List<String> values) {
            addCriterion("zip in", values, "zip");
            return (Criteria) this;
        }

        public Criteria andZipNotIn(List<String> values) {
            addCriterion("zip not in", values, "zip");
            return (Criteria) this;
        }

        public Criteria andZipBetween(String value1, String value2) {
            addCriterion("zip between", value1, value2, "zip");
            return (Criteria) this;
        }

        public Criteria andZipNotBetween(String value1, String value2) {
            addCriterion("zip not between", value1, value2, "zip");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("city is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("city is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("city =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("city <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("city >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("city >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("city <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("city <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("city like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("city not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("city in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("city not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("city between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("city not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andCountryIsNull() {
            addCriterion("country is null");
            return (Criteria) this;
        }

        public Criteria andCountryIsNotNull() {
            addCriterion("country is not null");
            return (Criteria) this;
        }

        public Criteria andCountryEqualTo(String value) {
            addCriterion("country =", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotEqualTo(String value) {
            addCriterion("country <>", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryGreaterThan(String value) {
            addCriterion("country >", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryGreaterThanOrEqualTo(String value) {
            addCriterion("country >=", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLessThan(String value) {
            addCriterion("country <", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLessThanOrEqualTo(String value) {
            addCriterion("country <=", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLike(String value) {
            addCriterion("country like", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotLike(String value) {
            addCriterion("country not like", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryIn(List<String> values) {
            addCriterion("country in", values, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotIn(List<String> values) {
            addCriterion("country not in", values, "country");
            return (Criteria) this;
        }

        public Criteria andCountryBetween(String value1, String value2) {
            addCriterion("country between", value1, value2, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotBetween(String value1, String value2) {
            addCriterion("country not between", value1, value2, "country");
            return (Criteria) this;
        }

        public Criteria andDistrictIsNull() {
            addCriterion("district is null");
            return (Criteria) this;
        }

        public Criteria andDistrictIsNotNull() {
            addCriterion("district is not null");
            return (Criteria) this;
        }

        public Criteria andDistrictEqualTo(String value) {
            addCriterion("district =", value, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictNotEqualTo(String value) {
            addCriterion("district <>", value, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictGreaterThan(String value) {
            addCriterion("district >", value, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictGreaterThanOrEqualTo(String value) {
            addCriterion("district >=", value, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictLessThan(String value) {
            addCriterion("district <", value, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictLessThanOrEqualTo(String value) {
            addCriterion("district <=", value, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictLike(String value) {
            addCriterion("district like", value, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictNotLike(String value) {
            addCriterion("district not like", value, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictIn(List<String> values) {
            addCriterion("district in", values, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictNotIn(List<String> values) {
            addCriterion("district not in", values, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictBetween(String value1, String value2) {
            addCriterion("district between", value1, value2, "district");
            return (Criteria) this;
        }

        public Criteria andDistrictNotBetween(String value1, String value2) {
            addCriterion("district not between", value1, value2, "district");
            return (Criteria) this;
        }

        public Criteria andFreightPayerIsNull() {
            addCriterion("freight_payer is null");
            return (Criteria) this;
        }

        public Criteria andFreightPayerIsNotNull() {
            addCriterion("freight_payer is not null");
            return (Criteria) this;
        }

        public Criteria andFreightPayerEqualTo(String value) {
            addCriterion("freight_payer =", value, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerNotEqualTo(String value) {
            addCriterion("freight_payer <>", value, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerGreaterThan(String value) {
            addCriterion("freight_payer >", value, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerGreaterThanOrEqualTo(String value) {
            addCriterion("freight_payer >=", value, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerLessThan(String value) {
            addCriterion("freight_payer <", value, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerLessThanOrEqualTo(String value) {
            addCriterion("freight_payer <=", value, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerLike(String value) {
            addCriterion("freight_payer like", value, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerNotLike(String value) {
            addCriterion("freight_payer not like", value, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerIn(List<String> values) {
            addCriterion("freight_payer in", values, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerNotIn(List<String> values) {
            addCriterion("freight_payer not in", values, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerBetween(String value1, String value2) {
            addCriterion("freight_payer between", value1, value2, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andFreightPayerNotBetween(String value1, String value2) {
            addCriterion("freight_payer not between", value1, value2, "freightPayer");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andIs3dIsNull() {
            addCriterion("is_3D is null");
            return (Criteria) this;
        }

        public Criteria andIs3dIsNotNull() {
            addCriterion("is_3D is not null");
            return (Criteria) this;
        }

        public Criteria andIs3dEqualTo(Byte value) {
            addCriterion("is_3D =", value, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dNotEqualTo(Byte value) {
            addCriterion("is_3D <>", value, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dGreaterThan(Byte value) {
            addCriterion("is_3D >", value, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_3D >=", value, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dLessThan(Byte value) {
            addCriterion("is_3D <", value, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dLessThanOrEqualTo(Byte value) {
            addCriterion("is_3D <=", value, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dIn(List<Byte> values) {
            addCriterion("is_3D in", values, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dNotIn(List<Byte> values) {
            addCriterion("is_3D not in", values, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dBetween(Byte value1, Byte value2) {
            addCriterion("is_3D between", value1, value2, "is3d");
            return (Criteria) this;
        }

        public Criteria andIs3dNotBetween(Byte value1, Byte value2) {
            addCriterion("is_3D not between", value1, value2, "is3d");
            return (Criteria) this;
        }

        public Criteria andScoreIsNull() {
            addCriterion("score is null");
            return (Criteria) this;
        }

        public Criteria andScoreIsNotNull() {
            addCriterion("score is not null");
            return (Criteria) this;
        }

        public Criteria andScoreEqualTo(Integer value) {
            addCriterion("score =", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotEqualTo(Integer value) {
            addCriterion("score <>", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreGreaterThan(Integer value) {
            addCriterion("score >", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("score >=", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreLessThan(Integer value) {
            addCriterion("score <", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreLessThanOrEqualTo(Integer value) {
            addCriterion("score <=", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreIn(List<Integer> values) {
            addCriterion("score in", values, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotIn(List<Integer> values) {
            addCriterion("score not in", values, "score");
            return (Criteria) this;
        }

        public Criteria andScoreBetween(Integer value1, Integer value2) {
            addCriterion("score between", value1, value2, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("score not between", value1, value2, "score");
            return (Criteria) this;
        }

        public Criteria andSellPromiseIsNull() {
            addCriterion("sell_promise is null");
            return (Criteria) this;
        }

        public Criteria andSellPromiseIsNotNull() {
            addCriterion("sell_promise is not null");
            return (Criteria) this;
        }

        public Criteria andSellPromiseEqualTo(Byte value) {
            addCriterion("sell_promise =", value, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseNotEqualTo(Byte value) {
            addCriterion("sell_promise <>", value, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseGreaterThan(Byte value) {
            addCriterion("sell_promise >", value, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseGreaterThanOrEqualTo(Byte value) {
            addCriterion("sell_promise >=", value, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseLessThan(Byte value) {
            addCriterion("sell_promise <", value, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseLessThanOrEqualTo(Byte value) {
            addCriterion("sell_promise <=", value, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseIn(List<Byte> values) {
            addCriterion("sell_promise in", values, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseNotIn(List<Byte> values) {
            addCriterion("sell_promise not in", values, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseBetween(Byte value1, Byte value2) {
            addCriterion("sell_promise between", value1, value2, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andSellPromiseNotBetween(Byte value1, Byte value2) {
            addCriterion("sell_promise not between", value1, value2, "sellPromise");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andItemDescIsNull() {
            addCriterion("item_desc is null");
            return (Criteria) this;
        }

        public Criteria andItemDescIsNotNull() {
            addCriterion("item_desc is not null");
            return (Criteria) this;
        }

        public Criteria andItemDescEqualTo(String value) {
            addCriterion("item_desc =", value, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescNotEqualTo(String value) {
            addCriterion("item_desc <>", value, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescGreaterThan(String value) {
            addCriterion("item_desc >", value, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescGreaterThanOrEqualTo(String value) {
            addCriterion("item_desc >=", value, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescLessThan(String value) {
            addCriterion("item_desc <", value, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescLessThanOrEqualTo(String value) {
            addCriterion("item_desc <=", value, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescLike(String value) {
            addCriterion("item_desc like", value, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescNotLike(String value) {
            addCriterion("item_desc not like", value, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescIn(List<String> values) {
            addCriterion("item_desc in", values, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescNotIn(List<String> values) {
            addCriterion("item_desc not in", values, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescBetween(String value1, String value2) {
            addCriterion("item_desc between", value1, value2, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andItemDescNotBetween(String value1, String value2) {
            addCriterion("item_desc not between", value1, value2, "itemDesc");
            return (Criteria) this;
        }

        public Criteria andSkuIdIsNull() {
            addCriterion("sku_id is null");
            return (Criteria) this;
        }

        public Criteria andSkuIdIsNotNull() {
            addCriterion("sku_id is not null");
            return (Criteria) this;
        }

        public Criteria andSkuIdEqualTo(Long value) {
            addCriterion("sku_id =", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotEqualTo(Long value) {
            addCriterion("sku_id <>", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdGreaterThan(Long value) {
            addCriterion("sku_id >", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("sku_id >=", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdLessThan(Long value) {
            addCriterion("sku_id <", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdLessThanOrEqualTo(Long value) {
            addCriterion("sku_id <=", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdIn(List<Long> values) {
            addCriterion("sku_id in", values, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotIn(List<Long> values) {
            addCriterion("sku_id not in", values, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdBetween(Long value1, Long value2) {
            addCriterion("sku_id between", value1, value2, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotBetween(Long value1, Long value2) {
            addCriterion("sku_id not between", value1, value2, "skuId");
            return (Criteria) this;
        }

        public Criteria andPropertiesIsNull() {
            addCriterion("properties is null");
            return (Criteria) this;
        }

        public Criteria andPropertiesIsNotNull() {
            addCriterion("properties is not null");
            return (Criteria) this;
        }

        public Criteria andPropertiesEqualTo(String value) {
            addCriterion("properties =", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesNotEqualTo(String value) {
            addCriterion("properties <>", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesGreaterThan(String value) {
            addCriterion("properties >", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesGreaterThanOrEqualTo(String value) {
            addCriterion("properties >=", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesLessThan(String value) {
            addCriterion("properties <", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesLessThanOrEqualTo(String value) {
            addCriterion("properties <=", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesLike(String value) {
            addCriterion("properties like", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesNotLike(String value) {
            addCriterion("properties not like", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesIn(List<String> values) {
            addCriterion("properties in", values, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesNotIn(List<String> values) {
            addCriterion("properties not in", values, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesBetween(String value1, String value2) {
            addCriterion("properties between", value1, value2, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesNotBetween(String value1, String value2) {
            addCriterion("properties not between", value1, value2, "properties");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNull() {
            addCriterion("quantity is null");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNotNull() {
            addCriterion("quantity is not null");
            return (Criteria) this;
        }

        public Criteria andQuantityEqualTo(Integer value) {
            addCriterion("quantity =", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotEqualTo(Integer value) {
            addCriterion("quantity <>", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThan(Integer value) {
            addCriterion("quantity >", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("quantity >=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThan(Integer value) {
            addCriterion("quantity <", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("quantity <=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityIn(List<Integer> values) {
            addCriterion("quantity in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotIn(List<Integer> values) {
            addCriterion("quantity not in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityBetween(Integer value1, Integer value2) {
            addCriterion("quantity between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("quantity not between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andSkuPriceIsNull() {
            addCriterion("sku_price is null");
            return (Criteria) this;
        }

        public Criteria andSkuPriceIsNotNull() {
            addCriterion("sku_price is not null");
            return (Criteria) this;
        }

        public Criteria andSkuPriceEqualTo(String value) {
            addCriterion("sku_price =", value, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceNotEqualTo(String value) {
            addCriterion("sku_price <>", value, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceGreaterThan(String value) {
            addCriterion("sku_price >", value, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceGreaterThanOrEqualTo(String value) {
            addCriterion("sku_price >=", value, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceLessThan(String value) {
            addCriterion("sku_price <", value, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceLessThanOrEqualTo(String value) {
            addCriterion("sku_price <=", value, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceLike(String value) {
            addCriterion("sku_price like", value, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceNotLike(String value) {
            addCriterion("sku_price not like", value, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceIn(List<String> values) {
            addCriterion("sku_price in", values, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceNotIn(List<String> values) {
            addCriterion("sku_price not in", values, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceBetween(String value1, String value2) {
            addCriterion("sku_price between", value1, value2, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuPriceNotBetween(String value1, String value2) {
            addCriterion("sku_price not between", value1, value2, "skuPrice");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedIsNull() {
            addCriterion("sku_created is null");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedIsNotNull() {
            addCriterion("sku_created is not null");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedEqualTo(String value) {
            addCriterion("sku_created =", value, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedNotEqualTo(String value) {
            addCriterion("sku_created <>", value, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedGreaterThan(String value) {
            addCriterion("sku_created >", value, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedGreaterThanOrEqualTo(String value) {
            addCriterion("sku_created >=", value, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedLessThan(String value) {
            addCriterion("sku_created <", value, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedLessThanOrEqualTo(String value) {
            addCriterion("sku_created <=", value, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedLike(String value) {
            addCriterion("sku_created like", value, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedNotLike(String value) {
            addCriterion("sku_created not like", value, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedIn(List<String> values) {
            addCriterion("sku_created in", values, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedNotIn(List<String> values) {
            addCriterion("sku_created not in", values, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedBetween(String value1, String value2) {
            addCriterion("sku_created between", value1, value2, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuCreatedNotBetween(String value1, String value2) {
            addCriterion("sku_created not between", value1, value2, "skuCreated");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedIsNull() {
            addCriterion("sku_modified is null");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedIsNotNull() {
            addCriterion("sku_modified is not null");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedEqualTo(String value) {
            addCriterion("sku_modified =", value, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedNotEqualTo(String value) {
            addCriterion("sku_modified <>", value, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedGreaterThan(String value) {
            addCriterion("sku_modified >", value, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedGreaterThanOrEqualTo(String value) {
            addCriterion("sku_modified >=", value, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedLessThan(String value) {
            addCriterion("sku_modified <", value, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedLessThanOrEqualTo(String value) {
            addCriterion("sku_modified <=", value, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedLike(String value) {
            addCriterion("sku_modified like", value, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedNotLike(String value) {
            addCriterion("sku_modified not like", value, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedIn(List<String> values) {
            addCriterion("sku_modified in", values, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedNotIn(List<String> values) {
            addCriterion("sku_modified not in", values, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedBetween(String value1, String value2) {
            addCriterion("sku_modified between", value1, value2, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuModifiedNotBetween(String value1, String value2) {
            addCriterion("sku_modified not between", value1, value2, "skuModified");
            return (Criteria) this;
        }

        public Criteria andSkuStatusIsNull() {
            addCriterion("sku_status is null");
            return (Criteria) this;
        }

        public Criteria andSkuStatusIsNotNull() {
            addCriterion("sku_status is not null");
            return (Criteria) this;
        }

        public Criteria andSkuStatusEqualTo(String value) {
            addCriterion("sku_status =", value, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNotEqualTo(String value) {
            addCriterion("sku_status <>", value, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusGreaterThan(String value) {
            addCriterion("sku_status >", value, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusGreaterThanOrEqualTo(String value) {
            addCriterion("sku_status >=", value, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusLessThan(String value) {
            addCriterion("sku_status <", value, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusLessThanOrEqualTo(String value) {
            addCriterion("sku_status <=", value, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusLike(String value) {
            addCriterion("sku_status like", value, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNotLike(String value) {
            addCriterion("sku_status not like", value, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusIn(List<String> values) {
            addCriterion("sku_status in", values, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNotIn(List<String> values) {
            addCriterion("sku_status not in", values, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusBetween(String value1, String value2) {
            addCriterion("sku_status between", value1, value2, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNotBetween(String value1, String value2) {
            addCriterion("sku_status not between", value1, value2, "skuStatus");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameIsNull() {
            addCriterion("sku_properties_name is null");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameIsNotNull() {
            addCriterion("sku_properties_name is not null");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameEqualTo(String value) {
            addCriterion("sku_properties_name =", value, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameNotEqualTo(String value) {
            addCriterion("sku_properties_name <>", value, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameGreaterThan(String value) {
            addCriterion("sku_properties_name >", value, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameGreaterThanOrEqualTo(String value) {
            addCriterion("sku_properties_name >=", value, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameLessThan(String value) {
            addCriterion("sku_properties_name <", value, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameLessThanOrEqualTo(String value) {
            addCriterion("sku_properties_name <=", value, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameLike(String value) {
            addCriterion("sku_properties_name like", value, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameNotLike(String value) {
            addCriterion("sku_properties_name not like", value, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameIn(List<String> values) {
            addCriterion("sku_properties_name in", values, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameNotIn(List<String> values) {
            addCriterion("sku_properties_name not in", values, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameBetween(String value1, String value2) {
            addCriterion("sku_properties_name between", value1, value2, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuPropertiesNameNotBetween(String value1, String value2) {
            addCriterion("sku_properties_name not between", value1, value2, "skuPropertiesName");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdIsNull() {
            addCriterion("sku_spec_id is null");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdIsNotNull() {
            addCriterion("sku_spec_id is not null");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdEqualTo(Integer value) {
            addCriterion("sku_spec_id =", value, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdNotEqualTo(Integer value) {
            addCriterion("sku_spec_id <>", value, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdGreaterThan(Integer value) {
            addCriterion("sku_spec_id >", value, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("sku_spec_id >=", value, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdLessThan(Integer value) {
            addCriterion("sku_spec_id <", value, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdLessThanOrEqualTo(Integer value) {
            addCriterion("sku_spec_id <=", value, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdIn(List<Integer> values) {
            addCriterion("sku_spec_id in", values, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdNotIn(List<Integer> values) {
            addCriterion("sku_spec_id not in", values, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdBetween(Integer value1, Integer value2) {
            addCriterion("sku_spec_id between", value1, value2, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuSpecIdNotBetween(Integer value1, Integer value2) {
            addCriterion("sku_spec_id not between", value1, value2, "skuSpecId");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityIsNull() {
            addCriterion("sku_with_hold_quantity is null");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityIsNotNull() {
            addCriterion("sku_with_hold_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityEqualTo(Integer value) {
            addCriterion("sku_with_hold_quantity =", value, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityNotEqualTo(Integer value) {
            addCriterion("sku_with_hold_quantity <>", value, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityGreaterThan(Integer value) {
            addCriterion("sku_with_hold_quantity >", value, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("sku_with_hold_quantity >=", value, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityLessThan(Integer value) {
            addCriterion("sku_with_hold_quantity <", value, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("sku_with_hold_quantity <=", value, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityIn(List<Integer> values) {
            addCriterion("sku_with_hold_quantity in", values, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityNotIn(List<Integer> values) {
            addCriterion("sku_with_hold_quantity not in", values, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityBetween(Integer value1, Integer value2) {
            addCriterion("sku_with_hold_quantity between", value1, value2, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuWithHoldQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("sku_with_hold_quantity not between", value1, value2, "skuWithHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeIsNull() {
            addCriterion("sku_delivery_time is null");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeIsNotNull() {
            addCriterion("sku_delivery_time is not null");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeEqualTo(String value) {
            addCriterion("sku_delivery_time =", value, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeNotEqualTo(String value) {
            addCriterion("sku_delivery_time <>", value, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeGreaterThan(String value) {
            addCriterion("sku_delivery_time >", value, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeGreaterThanOrEqualTo(String value) {
            addCriterion("sku_delivery_time >=", value, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeLessThan(String value) {
            addCriterion("sku_delivery_time <", value, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeLessThanOrEqualTo(String value) {
            addCriterion("sku_delivery_time <=", value, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeLike(String value) {
            addCriterion("sku_delivery_time like", value, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeNotLike(String value) {
            addCriterion("sku_delivery_time not like", value, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeIn(List<String> values) {
            addCriterion("sku_delivery_time in", values, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeNotIn(List<String> values) {
            addCriterion("sku_delivery_time not in", values, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeBetween(String value1, String value2) {
            addCriterion("sku_delivery_time between", value1, value2, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andSkuDeliveryTimeNotBetween(String value1, String value2) {
            addCriterion("sku_delivery_time not between", value1, value2, "skuDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andChangePropIsNull() {
            addCriterion("change_prop is null");
            return (Criteria) this;
        }

        public Criteria andChangePropIsNotNull() {
            addCriterion("change_prop is not null");
            return (Criteria) this;
        }

        public Criteria andChangePropEqualTo(String value) {
            addCriterion("change_prop =", value, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropNotEqualTo(String value) {
            addCriterion("change_prop <>", value, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropGreaterThan(String value) {
            addCriterion("change_prop >", value, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropGreaterThanOrEqualTo(String value) {
            addCriterion("change_prop >=", value, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropLessThan(String value) {
            addCriterion("change_prop <", value, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropLessThanOrEqualTo(String value) {
            addCriterion("change_prop <=", value, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropLike(String value) {
            addCriterion("change_prop like", value, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropNotLike(String value) {
            addCriterion("change_prop not like", value, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropIn(List<String> values) {
            addCriterion("change_prop in", values, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropNotIn(List<String> values) {
            addCriterion("change_prop not in", values, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropBetween(String value1, String value2) {
            addCriterion("change_prop between", value1, value2, "changeProp");
            return (Criteria) this;
        }

        public Criteria andChangePropNotBetween(String value1, String value2) {
            addCriterion("change_prop not between", value1, value2, "changeProp");
            return (Criteria) this;
        }

        public Criteria andPropsNameIsNull() {
            addCriterion("props_name is null");
            return (Criteria) this;
        }

        public Criteria andPropsNameIsNotNull() {
            addCriterion("props_name is not null");
            return (Criteria) this;
        }

        public Criteria andPropsNameEqualTo(String value) {
            addCriterion("props_name =", value, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameNotEqualTo(String value) {
            addCriterion("props_name <>", value, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameGreaterThan(String value) {
            addCriterion("props_name >", value, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameGreaterThanOrEqualTo(String value) {
            addCriterion("props_name >=", value, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameLessThan(String value) {
            addCriterion("props_name <", value, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameLessThanOrEqualTo(String value) {
            addCriterion("props_name <=", value, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameLike(String value) {
            addCriterion("props_name like", value, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameNotLike(String value) {
            addCriterion("props_name not like", value, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameIn(List<String> values) {
            addCriterion("props_name in", values, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameNotIn(List<String> values) {
            addCriterion("props_name not in", values, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameBetween(String value1, String value2) {
            addCriterion("props_name between", value1, value2, "propsName");
            return (Criteria) this;
        }

        public Criteria andPropsNameNotBetween(String value1, String value2) {
            addCriterion("props_name not between", value1, value2, "propsName");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceIsNull() {
            addCriterion("promoted_service is null");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceIsNotNull() {
            addCriterion("promoted_service is not null");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceEqualTo(String value) {
            addCriterion("promoted_service =", value, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceNotEqualTo(String value) {
            addCriterion("promoted_service <>", value, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceGreaterThan(String value) {
            addCriterion("promoted_service >", value, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceGreaterThanOrEqualTo(String value) {
            addCriterion("promoted_service >=", value, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceLessThan(String value) {
            addCriterion("promoted_service <", value, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceLessThanOrEqualTo(String value) {
            addCriterion("promoted_service <=", value, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceLike(String value) {
            addCriterion("promoted_service like", value, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceNotLike(String value) {
            addCriterion("promoted_service not like", value, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceIn(List<String> values) {
            addCriterion("promoted_service in", values, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceNotIn(List<String> values) {
            addCriterion("promoted_service not in", values, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceBetween(String value1, String value2) {
            addCriterion("promoted_service between", value1, value2, "promotedService");
            return (Criteria) this;
        }

        public Criteria andPromotedServiceNotBetween(String value1, String value2) {
            addCriterion("promoted_service not between", value1, value2, "promotedService");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentIsNull() {
            addCriterion("is_lightning_consignment is null");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentIsNotNull() {
            addCriterion("is_lightning_consignment is not null");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentEqualTo(Byte value) {
            addCriterion("is_lightning_consignment =", value, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentNotEqualTo(Byte value) {
            addCriterion("is_lightning_consignment <>", value, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentGreaterThan(Byte value) {
            addCriterion("is_lightning_consignment >", value, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_lightning_consignment >=", value, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentLessThan(Byte value) {
            addCriterion("is_lightning_consignment <", value, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentLessThanOrEqualTo(Byte value) {
            addCriterion("is_lightning_consignment <=", value, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentIn(List<Byte> values) {
            addCriterion("is_lightning_consignment in", values, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentNotIn(List<Byte> values) {
            addCriterion("is_lightning_consignment not in", values, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentBetween(Byte value1, Byte value2) {
            addCriterion("is_lightning_consignment between", value1, value2, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsLightningConsignmentNotBetween(Byte value1, Byte value2) {
            addCriterion("is_lightning_consignment not between", value1, value2, "isLightningConsignment");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoIsNull() {
            addCriterion("is_fenxiao is null");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoIsNotNull() {
            addCriterion("is_fenxiao is not null");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoEqualTo(Integer value) {
            addCriterion("is_fenxiao =", value, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoNotEqualTo(Integer value) {
            addCriterion("is_fenxiao <>", value, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoGreaterThan(Integer value) {
            addCriterion("is_fenxiao >", value, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_fenxiao >=", value, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoLessThan(Integer value) {
            addCriterion("is_fenxiao <", value, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoLessThanOrEqualTo(Integer value) {
            addCriterion("is_fenxiao <=", value, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoIn(List<Integer> values) {
            addCriterion("is_fenxiao in", values, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoNotIn(List<Integer> values) {
            addCriterion("is_fenxiao not in", values, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoBetween(Integer value1, Integer value2) {
            addCriterion("is_fenxiao between", value1, value2, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andIsFenxiaoNotBetween(Integer value1, Integer value2) {
            addCriterion("is_fenxiao not between", value1, value2, "isFenxiao");
            return (Criteria) this;
        }

        public Criteria andAuctionPointIsNull() {
            addCriterion("auction_point is null");
            return (Criteria) this;
        }

        public Criteria andAuctionPointIsNotNull() {
            addCriterion("auction_point is not null");
            return (Criteria) this;
        }

        public Criteria andAuctionPointEqualTo(Integer value) {
            addCriterion("auction_point =", value, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointNotEqualTo(Integer value) {
            addCriterion("auction_point <>", value, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointGreaterThan(Integer value) {
            addCriterion("auction_point >", value, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointGreaterThanOrEqualTo(Integer value) {
            addCriterion("auction_point >=", value, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointLessThan(Integer value) {
            addCriterion("auction_point <", value, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointLessThanOrEqualTo(Integer value) {
            addCriterion("auction_point <=", value, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointIn(List<Integer> values) {
            addCriterion("auction_point in", values, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointNotIn(List<Integer> values) {
            addCriterion("auction_point not in", values, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointBetween(Integer value1, Integer value2) {
            addCriterion("auction_point between", value1, value2, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andAuctionPointNotBetween(Integer value1, Integer value2) {
            addCriterion("auction_point not between", value1, value2, "auctionPoint");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasIsNull() {
            addCriterion("property_alias is null");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasIsNotNull() {
            addCriterion("property_alias is not null");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasEqualTo(String value) {
            addCriterion("property_alias =", value, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasNotEqualTo(String value) {
            addCriterion("property_alias <>", value, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasGreaterThan(String value) {
            addCriterion("property_alias >", value, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasGreaterThanOrEqualTo(String value) {
            addCriterion("property_alias >=", value, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasLessThan(String value) {
            addCriterion("property_alias <", value, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasLessThanOrEqualTo(String value) {
            addCriterion("property_alias <=", value, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasLike(String value) {
            addCriterion("property_alias like", value, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasNotLike(String value) {
            addCriterion("property_alias not like", value, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasIn(List<String> values) {
            addCriterion("property_alias in", values, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasNotIn(List<String> values) {
            addCriterion("property_alias not in", values, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasBetween(String value1, String value2) {
            addCriterion("property_alias between", value1, value2, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andPropertyAliasNotBetween(String value1, String value2) {
            addCriterion("property_alias not between", value1, value2, "propertyAlias");
            return (Criteria) this;
        }

        public Criteria andTemplateIdIsNull() {
            addCriterion("template_id is null");
            return (Criteria) this;
        }

        public Criteria andTemplateIdIsNotNull() {
            addCriterion("template_id is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateIdEqualTo(String value) {
            addCriterion("template_id =", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdNotEqualTo(String value) {
            addCriterion("template_id <>", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdGreaterThan(String value) {
            addCriterion("template_id >", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdGreaterThanOrEqualTo(String value) {
            addCriterion("template_id >=", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdLessThan(String value) {
            addCriterion("template_id <", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdLessThanOrEqualTo(String value) {
            addCriterion("template_id <=", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdLike(String value) {
            addCriterion("template_id like", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdNotLike(String value) {
            addCriterion("template_id not like", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdIn(List<String> values) {
            addCriterion("template_id in", values, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdNotIn(List<String> values) {
            addCriterion("template_id not in", values, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdBetween(String value1, String value2) {
            addCriterion("template_id between", value1, value2, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdNotBetween(String value1, String value2) {
            addCriterion("template_id not between", value1, value2, "templateId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdIsNull() {
            addCriterion("after_sale_id is null");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdIsNotNull() {
            addCriterion("after_sale_id is not null");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdEqualTo(Long value) {
            addCriterion("after_sale_id =", value, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdNotEqualTo(Long value) {
            addCriterion("after_sale_id <>", value, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdGreaterThan(Long value) {
            addCriterion("after_sale_id >", value, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdGreaterThanOrEqualTo(Long value) {
            addCriterion("after_sale_id >=", value, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdLessThan(Long value) {
            addCriterion("after_sale_id <", value, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdLessThanOrEqualTo(Long value) {
            addCriterion("after_sale_id <=", value, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdIn(List<Long> values) {
            addCriterion("after_sale_id in", values, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdNotIn(List<Long> values) {
            addCriterion("after_sale_id not in", values, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdBetween(Long value1, Long value2) {
            addCriterion("after_sale_id between", value1, value2, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andAfterSaleIdNotBetween(Long value1, Long value2) {
            addCriterion("after_sale_id not between", value1, value2, "afterSaleId");
            return (Criteria) this;
        }

        public Criteria andIsXinpinIsNull() {
            addCriterion("is_xinpin is null");
            return (Criteria) this;
        }

        public Criteria andIsXinpinIsNotNull() {
            addCriterion("is_xinpin is not null");
            return (Criteria) this;
        }

        public Criteria andIsXinpinEqualTo(Byte value) {
            addCriterion("is_xinpin =", value, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinNotEqualTo(Byte value) {
            addCriterion("is_xinpin <>", value, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinGreaterThan(Byte value) {
            addCriterion("is_xinpin >", value, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_xinpin >=", value, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinLessThan(Byte value) {
            addCriterion("is_xinpin <", value, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinLessThanOrEqualTo(Byte value) {
            addCriterion("is_xinpin <=", value, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinIn(List<Byte> values) {
            addCriterion("is_xinpin in", values, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinNotIn(List<Byte> values) {
            addCriterion("is_xinpin not in", values, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinBetween(Byte value1, Byte value2) {
            addCriterion("is_xinpin between", value1, value2, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andIsXinpinNotBetween(Byte value1, Byte value2) {
            addCriterion("is_xinpin not between", value1, value2, "isXinpin");
            return (Criteria) this;
        }

        public Criteria andSubStockIsNull() {
            addCriterion("sub_stock is null");
            return (Criteria) this;
        }

        public Criteria andSubStockIsNotNull() {
            addCriterion("sub_stock is not null");
            return (Criteria) this;
        }

        public Criteria andSubStockEqualTo(Integer value) {
            addCriterion("sub_stock =", value, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockNotEqualTo(Integer value) {
            addCriterion("sub_stock <>", value, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockGreaterThan(Integer value) {
            addCriterion("sub_stock >", value, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockGreaterThanOrEqualTo(Integer value) {
            addCriterion("sub_stock >=", value, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockLessThan(Integer value) {
            addCriterion("sub_stock <", value, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockLessThanOrEqualTo(Integer value) {
            addCriterion("sub_stock <=", value, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockIn(List<Integer> values) {
            addCriterion("sub_stock in", values, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockNotIn(List<Integer> values) {
            addCriterion("sub_stock not in", values, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockBetween(Integer value1, Integer value2) {
            addCriterion("sub_stock between", value1, value2, "subStock");
            return (Criteria) this;
        }

        public Criteria andSubStockNotBetween(Integer value1, Integer value2) {
            addCriterion("sub_stock not between", value1, value2, "subStock");
            return (Criteria) this;
        }

        public Criteria andFeaturesIsNull() {
            addCriterion("features is null");
            return (Criteria) this;
        }

        public Criteria andFeaturesIsNotNull() {
            addCriterion("features is not null");
            return (Criteria) this;
        }

        public Criteria andFeaturesEqualTo(String value) {
            addCriterion("features =", value, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesNotEqualTo(String value) {
            addCriterion("features <>", value, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesGreaterThan(String value) {
            addCriterion("features >", value, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesGreaterThanOrEqualTo(String value) {
            addCriterion("features >=", value, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesLessThan(String value) {
            addCriterion("features <", value, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesLessThanOrEqualTo(String value) {
            addCriterion("features <=", value, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesLike(String value) {
            addCriterion("features like", value, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesNotLike(String value) {
            addCriterion("features not like", value, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesIn(List<String> values) {
            addCriterion("features in", values, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesNotIn(List<String> values) {
            addCriterion("features not in", values, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesBetween(String value1, String value2) {
            addCriterion("features between", value1, value2, "features");
            return (Criteria) this;
        }

        public Criteria andFeaturesNotBetween(String value1, String value2) {
            addCriterion("features not between", value1, value2, "features");
            return (Criteria) this;
        }

        public Criteria andItemWeightIsNull() {
            addCriterion("item_weight is null");
            return (Criteria) this;
        }

        public Criteria andItemWeightIsNotNull() {
            addCriterion("item_weight is not null");
            return (Criteria) this;
        }

        public Criteria andItemWeightEqualTo(String value) {
            addCriterion("item_weight =", value, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightNotEqualTo(String value) {
            addCriterion("item_weight <>", value, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightGreaterThan(String value) {
            addCriterion("item_weight >", value, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightGreaterThanOrEqualTo(String value) {
            addCriterion("item_weight >=", value, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightLessThan(String value) {
            addCriterion("item_weight <", value, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightLessThanOrEqualTo(String value) {
            addCriterion("item_weight <=", value, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightLike(String value) {
            addCriterion("item_weight like", value, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightNotLike(String value) {
            addCriterion("item_weight not like", value, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightIn(List<String> values) {
            addCriterion("item_weight in", values, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightNotIn(List<String> values) {
            addCriterion("item_weight not in", values, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightBetween(String value1, String value2) {
            addCriterion("item_weight between", value1, value2, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemWeightNotBetween(String value1, String value2) {
            addCriterion("item_weight not between", value1, value2, "itemWeight");
            return (Criteria) this;
        }

        public Criteria andItemSizeIsNull() {
            addCriterion("item_size is null");
            return (Criteria) this;
        }

        public Criteria andItemSizeIsNotNull() {
            addCriterion("item_size is not null");
            return (Criteria) this;
        }

        public Criteria andItemSizeEqualTo(String value) {
            addCriterion("item_size =", value, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeNotEqualTo(String value) {
            addCriterion("item_size <>", value, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeGreaterThan(String value) {
            addCriterion("item_size >", value, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeGreaterThanOrEqualTo(String value) {
            addCriterion("item_size >=", value, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeLessThan(String value) {
            addCriterion("item_size <", value, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeLessThanOrEqualTo(String value) {
            addCriterion("item_size <=", value, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeLike(String value) {
            addCriterion("item_size like", value, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeNotLike(String value) {
            addCriterion("item_size not like", value, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeIn(List<String> values) {
            addCriterion("item_size in", values, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeNotIn(List<String> values) {
            addCriterion("item_size not in", values, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeBetween(String value1, String value2) {
            addCriterion("item_size between", value1, value2, "itemSize");
            return (Criteria) this;
        }

        public Criteria andItemSizeNotBetween(String value1, String value2) {
            addCriterion("item_size not between", value1, value2, "itemSize");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityIsNull() {
            addCriterion("with_hold_quantity is null");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityIsNotNull() {
            addCriterion("with_hold_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityEqualTo(Integer value) {
            addCriterion("with_hold_quantity =", value, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityNotEqualTo(Integer value) {
            addCriterion("with_hold_quantity <>", value, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityGreaterThan(Integer value) {
            addCriterion("with_hold_quantity >", value, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("with_hold_quantity >=", value, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityLessThan(Integer value) {
            addCriterion("with_hold_quantity <", value, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("with_hold_quantity <=", value, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityIn(List<Integer> values) {
            addCriterion("with_hold_quantity in", values, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityNotIn(List<Integer> values) {
            addCriterion("with_hold_quantity not in", values, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityBetween(Integer value1, Integer value2) {
            addCriterion("with_hold_quantity between", value1, value2, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andWithHoldQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("with_hold_quantity not between", value1, value2, "withHoldQuantity");
            return (Criteria) this;
        }

        public Criteria andSellPointIsNull() {
            addCriterion("sell_point is null");
            return (Criteria) this;
        }

        public Criteria andSellPointIsNotNull() {
            addCriterion("sell_point is not null");
            return (Criteria) this;
        }

        public Criteria andSellPointEqualTo(String value) {
            addCriterion("sell_point =", value, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointNotEqualTo(String value) {
            addCriterion("sell_point <>", value, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointGreaterThan(String value) {
            addCriterion("sell_point >", value, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointGreaterThanOrEqualTo(String value) {
            addCriterion("sell_point >=", value, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointLessThan(String value) {
            addCriterion("sell_point <", value, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointLessThanOrEqualTo(String value) {
            addCriterion("sell_point <=", value, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointLike(String value) {
            addCriterion("sell_point like", value, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointNotLike(String value) {
            addCriterion("sell_point not like", value, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointIn(List<String> values) {
            addCriterion("sell_point in", values, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointNotIn(List<String> values) {
            addCriterion("sell_point not in", values, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointBetween(String value1, String value2) {
            addCriterion("sell_point between", value1, value2, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andSellPointNotBetween(String value1, String value2) {
            addCriterion("sell_point not between", value1, value2, "sellPoint");
            return (Criteria) this;
        }

        public Criteria andValidThruIsNull() {
            addCriterion("valid_thru is null");
            return (Criteria) this;
        }

        public Criteria andValidThruIsNotNull() {
            addCriterion("valid_thru is not null");
            return (Criteria) this;
        }

        public Criteria andValidThruEqualTo(Integer value) {
            addCriterion("valid_thru =", value, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruNotEqualTo(Integer value) {
            addCriterion("valid_thru <>", value, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruGreaterThan(Integer value) {
            addCriterion("valid_thru >", value, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruGreaterThanOrEqualTo(Integer value) {
            addCriterion("valid_thru >=", value, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruLessThan(Integer value) {
            addCriterion("valid_thru <", value, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruLessThanOrEqualTo(Integer value) {
            addCriterion("valid_thru <=", value, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruIn(List<Integer> values) {
            addCriterion("valid_thru in", values, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruNotIn(List<Integer> values) {
            addCriterion("valid_thru not in", values, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruBetween(Integer value1, Integer value2) {
            addCriterion("valid_thru between", value1, value2, "validThru");
            return (Criteria) this;
        }

        public Criteria andValidThruNotBetween(Integer value1, Integer value2) {
            addCriterion("valid_thru not between", value1, value2, "validThru");
            return (Criteria) this;
        }

        public Criteria andAutoFillIsNull() {
            addCriterion("auto_fill is null");
            return (Criteria) this;
        }

        public Criteria andAutoFillIsNotNull() {
            addCriterion("auto_fill is not null");
            return (Criteria) this;
        }

        public Criteria andAutoFillEqualTo(String value) {
            addCriterion("auto_fill =", value, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillNotEqualTo(String value) {
            addCriterion("auto_fill <>", value, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillGreaterThan(String value) {
            addCriterion("auto_fill >", value, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillGreaterThanOrEqualTo(String value) {
            addCriterion("auto_fill >=", value, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillLessThan(String value) {
            addCriterion("auto_fill <", value, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillLessThanOrEqualTo(String value) {
            addCriterion("auto_fill <=", value, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillLike(String value) {
            addCriterion("auto_fill like", value, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillNotLike(String value) {
            addCriterion("auto_fill not like", value, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillIn(List<String> values) {
            addCriterion("auto_fill in", values, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillNotIn(List<String> values) {
            addCriterion("auto_fill not in", values, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillBetween(String value1, String value2) {
            addCriterion("auto_fill between", value1, value2, "autoFill");
            return (Criteria) this;
        }

        public Criteria andAutoFillNotBetween(String value1, String value2) {
            addCriterion("auto_fill not between", value1, value2, "autoFill");
            return (Criteria) this;
        }

        public Criteria andDescModulesIsNull() {
            addCriterion("desc_modules is null");
            return (Criteria) this;
        }

        public Criteria andDescModulesIsNotNull() {
            addCriterion("desc_modules is not null");
            return (Criteria) this;
        }

        public Criteria andDescModulesEqualTo(String value) {
            addCriterion("desc_modules =", value, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesNotEqualTo(String value) {
            addCriterion("desc_modules <>", value, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesGreaterThan(String value) {
            addCriterion("desc_modules >", value, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesGreaterThanOrEqualTo(String value) {
            addCriterion("desc_modules >=", value, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesLessThan(String value) {
            addCriterion("desc_modules <", value, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesLessThanOrEqualTo(String value) {
            addCriterion("desc_modules <=", value, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesLike(String value) {
            addCriterion("desc_modules like", value, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesNotLike(String value) {
            addCriterion("desc_modules not like", value, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesIn(List<String> values) {
            addCriterion("desc_modules in", values, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesNotIn(List<String> values) {
            addCriterion("desc_modules not in", values, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesBetween(String value1, String value2) {
            addCriterion("desc_modules between", value1, value2, "descModules");
            return (Criteria) this;
        }

        public Criteria andDescModulesNotBetween(String value1, String value2) {
            addCriterion("desc_modules not between", value1, value2, "descModules");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdIsNull() {
            addCriterion("custom_made_type_id is null");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdIsNotNull() {
            addCriterion("custom_made_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdEqualTo(String value) {
            addCriterion("custom_made_type_id =", value, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdNotEqualTo(String value) {
            addCriterion("custom_made_type_id <>", value, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdGreaterThan(String value) {
            addCriterion("custom_made_type_id >", value, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdGreaterThanOrEqualTo(String value) {
            addCriterion("custom_made_type_id >=", value, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdLessThan(String value) {
            addCriterion("custom_made_type_id <", value, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdLessThanOrEqualTo(String value) {
            addCriterion("custom_made_type_id <=", value, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdLike(String value) {
            addCriterion("custom_made_type_id like", value, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdNotLike(String value) {
            addCriterion("custom_made_type_id not like", value, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdIn(List<String> values) {
            addCriterion("custom_made_type_id in", values, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdNotIn(List<String> values) {
            addCriterion("custom_made_type_id not in", values, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdBetween(String value1, String value2) {
            addCriterion("custom_made_type_id between", value1, value2, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andCustomMadeTypeIdNotBetween(String value1, String value2) {
            addCriterion("custom_made_type_id not between", value1, value2, "customMadeTypeId");
            return (Criteria) this;
        }

        public Criteria andWirelessDescIsNull() {
            addCriterion("wireless_desc is null");
            return (Criteria) this;
        }

        public Criteria andWirelessDescIsNotNull() {
            addCriterion("wireless_desc is not null");
            return (Criteria) this;
        }

        public Criteria andWirelessDescEqualTo(String value) {
            addCriterion("wireless_desc =", value, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescNotEqualTo(String value) {
            addCriterion("wireless_desc <>", value, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescGreaterThan(String value) {
            addCriterion("wireless_desc >", value, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescGreaterThanOrEqualTo(String value) {
            addCriterion("wireless_desc >=", value, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescLessThan(String value) {
            addCriterion("wireless_desc <", value, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescLessThanOrEqualTo(String value) {
            addCriterion("wireless_desc <=", value, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescLike(String value) {
            addCriterion("wireless_desc like", value, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescNotLike(String value) {
            addCriterion("wireless_desc not like", value, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescIn(List<String> values) {
            addCriterion("wireless_desc in", values, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescNotIn(List<String> values) {
            addCriterion("wireless_desc not in", values, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescBetween(String value1, String value2) {
            addCriterion("wireless_desc between", value1, value2, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andWirelessDescNotBetween(String value1, String value2) {
            addCriterion("wireless_desc not between", value1, value2, "wirelessDesc");
            return (Criteria) this;
        }

        public Criteria andBarcodeIsNull() {
            addCriterion("barcode is null");
            return (Criteria) this;
        }

        public Criteria andBarcodeIsNotNull() {
            addCriterion("barcode is not null");
            return (Criteria) this;
        }

        public Criteria andBarcodeEqualTo(String value) {
            addCriterion("barcode =", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotEqualTo(String value) {
            addCriterion("barcode <>", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeGreaterThan(String value) {
            addCriterion("barcode >", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeGreaterThanOrEqualTo(String value) {
            addCriterion("barcode >=", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLessThan(String value) {
            addCriterion("barcode <", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLessThanOrEqualTo(String value) {
            addCriterion("barcode <=", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLike(String value) {
            addCriterion("barcode like", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotLike(String value) {
            addCriterion("barcode not like", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeIn(List<String> values) {
            addCriterion("barcode in", values, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotIn(List<String> values) {
            addCriterion("barcode not in", values, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeBetween(String value1, String value2) {
            addCriterion("barcode between", value1, value2, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotBetween(String value1, String value2) {
            addCriterion("barcode not between", value1, value2, "barcode");
            return (Criteria) this;
        }

        public Criteria andNewprepayIsNull() {
            addCriterion("newprepay is null");
            return (Criteria) this;
        }

        public Criteria andNewprepayIsNotNull() {
            addCriterion("newprepay is not null");
            return (Criteria) this;
        }

        public Criteria andNewprepayEqualTo(String value) {
            addCriterion("newprepay =", value, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayNotEqualTo(String value) {
            addCriterion("newprepay <>", value, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayGreaterThan(String value) {
            addCriterion("newprepay >", value, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayGreaterThanOrEqualTo(String value) {
            addCriterion("newprepay >=", value, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayLessThan(String value) {
            addCriterion("newprepay <", value, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayLessThanOrEqualTo(String value) {
            addCriterion("newprepay <=", value, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayLike(String value) {
            addCriterion("newprepay like", value, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayNotLike(String value) {
            addCriterion("newprepay not like", value, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayIn(List<String> values) {
            addCriterion("newprepay in", values, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayNotIn(List<String> values) {
            addCriterion("newprepay not in", values, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayBetween(String value1, String value2) {
            addCriterion("newprepay between", value1, value2, "newprepay");
            return (Criteria) this;
        }

        public Criteria andNewprepayNotBetween(String value1, String value2) {
            addCriterion("newprepay not between", value1, value2, "newprepay");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(String value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(String value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(String value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(String value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(String value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(String value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLike(String value) {
            addCriterion("price like", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotLike(String value) {
            addCriterion("price not like", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<String> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<String> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(String value1, String value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(String value1, String value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPostFeeIsNull() {
            addCriterion("post_fee is null");
            return (Criteria) this;
        }

        public Criteria andPostFeeIsNotNull() {
            addCriterion("post_fee is not null");
            return (Criteria) this;
        }

        public Criteria andPostFeeEqualTo(String value) {
            addCriterion("post_fee =", value, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeNotEqualTo(String value) {
            addCriterion("post_fee <>", value, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeGreaterThan(String value) {
            addCriterion("post_fee >", value, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeGreaterThanOrEqualTo(String value) {
            addCriterion("post_fee >=", value, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeLessThan(String value) {
            addCriterion("post_fee <", value, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeLessThanOrEqualTo(String value) {
            addCriterion("post_fee <=", value, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeLike(String value) {
            addCriterion("post_fee like", value, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeNotLike(String value) {
            addCriterion("post_fee not like", value, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeIn(List<String> values) {
            addCriterion("post_fee in", values, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeNotIn(List<String> values) {
            addCriterion("post_fee not in", values, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeBetween(String value1, String value2) {
            addCriterion("post_fee between", value1, value2, "postFee");
            return (Criteria) this;
        }

        public Criteria andPostFeeNotBetween(String value1, String value2) {
            addCriterion("post_fee not between", value1, value2, "postFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIsNull() {
            addCriterion("express_fee is null");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIsNotNull() {
            addCriterion("express_fee is not null");
            return (Criteria) this;
        }

        public Criteria andExpressFeeEqualTo(String value) {
            addCriterion("express_fee =", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotEqualTo(String value) {
            addCriterion("express_fee <>", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeGreaterThan(String value) {
            addCriterion("express_fee >", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeGreaterThanOrEqualTo(String value) {
            addCriterion("express_fee >=", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeLessThan(String value) {
            addCriterion("express_fee <", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeLessThanOrEqualTo(String value) {
            addCriterion("express_fee <=", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeLike(String value) {
            addCriterion("express_fee like", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotLike(String value) {
            addCriterion("express_fee not like", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIn(List<String> values) {
            addCriterion("express_fee in", values, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotIn(List<String> values) {
            addCriterion("express_fee not in", values, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeBetween(String value1, String value2) {
            addCriterion("express_fee between", value1, value2, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotBetween(String value1, String value2) {
            addCriterion("express_fee not between", value1, value2, "expressFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeIsNull() {
            addCriterion("ems_fee is null");
            return (Criteria) this;
        }

        public Criteria andEmsFeeIsNotNull() {
            addCriterion("ems_fee is not null");
            return (Criteria) this;
        }

        public Criteria andEmsFeeEqualTo(String value) {
            addCriterion("ems_fee =", value, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeNotEqualTo(String value) {
            addCriterion("ems_fee <>", value, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeGreaterThan(String value) {
            addCriterion("ems_fee >", value, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeGreaterThanOrEqualTo(String value) {
            addCriterion("ems_fee >=", value, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeLessThan(String value) {
            addCriterion("ems_fee <", value, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeLessThanOrEqualTo(String value) {
            addCriterion("ems_fee <=", value, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeLike(String value) {
            addCriterion("ems_fee like", value, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeNotLike(String value) {
            addCriterion("ems_fee not like", value, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeIn(List<String> values) {
            addCriterion("ems_fee in", values, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeNotIn(List<String> values) {
            addCriterion("ems_fee not in", values, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeBetween(String value1, String value2) {
            addCriterion("ems_fee between", value1, value2, "emsFee");
            return (Criteria) this;
        }

        public Criteria andEmsFeeNotBetween(String value1, String value2) {
            addCriterion("ems_fee not between", value1, value2, "emsFee");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeIsNull() {
            addCriterion("global_stock_type is null");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeIsNotNull() {
            addCriterion("global_stock_type is not null");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeEqualTo(String value) {
            addCriterion("global_stock_type =", value, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeNotEqualTo(String value) {
            addCriterion("global_stock_type <>", value, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeGreaterThan(String value) {
            addCriterion("global_stock_type >", value, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeGreaterThanOrEqualTo(String value) {
            addCriterion("global_stock_type >=", value, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeLessThan(String value) {
            addCriterion("global_stock_type <", value, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeLessThanOrEqualTo(String value) {
            addCriterion("global_stock_type <=", value, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeLike(String value) {
            addCriterion("global_stock_type like", value, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeNotLike(String value) {
            addCriterion("global_stock_type not like", value, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeIn(List<String> values) {
            addCriterion("global_stock_type in", values, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeNotIn(List<String> values) {
            addCriterion("global_stock_type not in", values, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeBetween(String value1, String value2) {
            addCriterion("global_stock_type between", value1, value2, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockTypeNotBetween(String value1, String value2) {
            addCriterion("global_stock_type not between", value1, value2, "globalStockType");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryIsNull() {
            addCriterion("global_stock_country is null");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryIsNotNull() {
            addCriterion("global_stock_country is not null");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryEqualTo(String value) {
            addCriterion("global_stock_country =", value, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryNotEqualTo(String value) {
            addCriterion("global_stock_country <>", value, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryGreaterThan(String value) {
            addCriterion("global_stock_country >", value, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryGreaterThanOrEqualTo(String value) {
            addCriterion("global_stock_country >=", value, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryLessThan(String value) {
            addCriterion("global_stock_country <", value, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryLessThanOrEqualTo(String value) {
            addCriterion("global_stock_country <=", value, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryLike(String value) {
            addCriterion("global_stock_country like", value, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryNotLike(String value) {
            addCriterion("global_stock_country not like", value, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryIn(List<String> values) {
            addCriterion("global_stock_country in", values, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryNotIn(List<String> values) {
            addCriterion("global_stock_country not in", values, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryBetween(String value1, String value2) {
            addCriterion("global_stock_country between", value1, value2, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andGlobalStockCountryNotBetween(String value1, String value2) {
            addCriterion("global_stock_country not between", value1, value2, "globalStockCountry");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlIsNull() {
            addCriterion("large_screen_image_url is null");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlIsNotNull() {
            addCriterion("large_screen_image_url is not null");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlEqualTo(String value) {
            addCriterion("large_screen_image_url =", value, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlNotEqualTo(String value) {
            addCriterion("large_screen_image_url <>", value, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlGreaterThan(String value) {
            addCriterion("large_screen_image_url >", value, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlGreaterThanOrEqualTo(String value) {
            addCriterion("large_screen_image_url >=", value, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlLessThan(String value) {
            addCriterion("large_screen_image_url <", value, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlLessThanOrEqualTo(String value) {
            addCriterion("large_screen_image_url <=", value, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlLike(String value) {
            addCriterion("large_screen_image_url like", value, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlNotLike(String value) {
            addCriterion("large_screen_image_url not like", value, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlIn(List<String> values) {
            addCriterion("large_screen_image_url in", values, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlNotIn(List<String> values) {
            addCriterion("large_screen_image_url not in", values, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlBetween(String value1, String value2) {
            addCriterion("large_screen_image_url between", value1, value2, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andLargeScreenImageUrlNotBetween(String value1, String value2) {
            addCriterion("large_screen_image_url not between", value1, value2, "largeScreenImageUrl");
            return (Criteria) this;
        }

        public Criteria andOptlockIsNull() {
            addCriterion("optlock is null");
            return (Criteria) this;
        }

        public Criteria andOptlockIsNotNull() {
            addCriterion("optlock is not null");
            return (Criteria) this;
        }

        public Criteria andOptlockEqualTo(Integer value) {
            addCriterion("optlock =", value, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockNotEqualTo(Integer value) {
            addCriterion("optlock <>", value, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockGreaterThan(Integer value) {
            addCriterion("optlock >", value, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockGreaterThanOrEqualTo(Integer value) {
            addCriterion("optlock >=", value, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockLessThan(Integer value) {
            addCriterion("optlock <", value, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockLessThanOrEqualTo(Integer value) {
            addCriterion("optlock <=", value, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockIn(List<Integer> values) {
            addCriterion("optlock in", values, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockNotIn(List<Integer> values) {
            addCriterion("optlock not in", values, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockBetween(Integer value1, Integer value2) {
            addCriterion("optlock between", value1, value2, "optlock");
            return (Criteria) this;
        }

        public Criteria andOptlockNotBetween(Integer value1, Integer value2) {
            addCriterion("optlock not between", value1, value2, "optlock");
            return (Criteria) this;
        }

        public Criteria andCreatedbyIsNull() {
            addCriterion("createdBy is null");
            return (Criteria) this;
        }

        public Criteria andCreatedbyIsNotNull() {
            addCriterion("createdBy is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedbyEqualTo(String value) {
            addCriterion("createdBy =", value, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyNotEqualTo(String value) {
            addCriterion("createdBy <>", value, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyGreaterThan(String value) {
            addCriterion("createdBy >", value, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyGreaterThanOrEqualTo(String value) {
            addCriterion("createdBy >=", value, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyLessThan(String value) {
            addCriterion("createdBy <", value, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyLessThanOrEqualTo(String value) {
            addCriterion("createdBy <=", value, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyLike(String value) {
            addCriterion("createdBy like", value, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyNotLike(String value) {
            addCriterion("createdBy not like", value, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyIn(List<String> values) {
            addCriterion("createdBy in", values, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyNotIn(List<String> values) {
            addCriterion("createdBy not in", values, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyBetween(String value1, String value2) {
            addCriterion("createdBy between", value1, value2, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreatedbyNotBetween(String value1, String value2) {
            addCriterion("createdBy not between", value1, value2, "createdby");
            return (Criteria) this;
        }

        public Criteria andCreateddateIsNull() {
            addCriterion("createdDate is null");
            return (Criteria) this;
        }

        public Criteria andCreateddateIsNotNull() {
            addCriterion("createdDate is not null");
            return (Criteria) this;
        }

        public Criteria andCreateddateEqualTo(Date value) {
            addCriterion("createdDate =", value, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateNotEqualTo(Date value) {
            addCriterion("createdDate <>", value, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateGreaterThan(Date value) {
            addCriterion("createdDate >", value, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateGreaterThanOrEqualTo(Date value) {
            addCriterion("createdDate >=", value, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateLessThan(Date value) {
            addCriterion("createdDate <", value, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateLessThanOrEqualTo(Date value) {
            addCriterion("createdDate <=", value, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateIn(List<Date> values) {
            addCriterion("createdDate in", values, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateNotIn(List<Date> values) {
            addCriterion("createdDate not in", values, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateBetween(Date value1, Date value2) {
            addCriterion("createdDate between", value1, value2, "createddate");
            return (Criteria) this;
        }

        public Criteria andCreateddateNotBetween(Date value1, Date value2) {
            addCriterion("createdDate not between", value1, value2, "createddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyIsNull() {
            addCriterion("lastModifiedBy is null");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyIsNotNull() {
            addCriterion("lastModifiedBy is not null");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyEqualTo(String value) {
            addCriterion("lastModifiedBy =", value, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyNotEqualTo(String value) {
            addCriterion("lastModifiedBy <>", value, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyGreaterThan(String value) {
            addCriterion("lastModifiedBy >", value, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyGreaterThanOrEqualTo(String value) {
            addCriterion("lastModifiedBy >=", value, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyLessThan(String value) {
            addCriterion("lastModifiedBy <", value, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyLessThanOrEqualTo(String value) {
            addCriterion("lastModifiedBy <=", value, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyLike(String value) {
            addCriterion("lastModifiedBy like", value, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyNotLike(String value) {
            addCriterion("lastModifiedBy not like", value, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyIn(List<String> values) {
            addCriterion("lastModifiedBy in", values, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyNotIn(List<String> values) {
            addCriterion("lastModifiedBy not in", values, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyBetween(String value1, String value2) {
            addCriterion("lastModifiedBy between", value1, value2, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifiedbyNotBetween(String value1, String value2) {
            addCriterion("lastModifiedBy not between", value1, value2, "lastmodifiedby");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateIsNull() {
            addCriterion("lastModifiedDate is null");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateIsNotNull() {
            addCriterion("lastModifiedDate is not null");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateEqualTo(Date value) {
            addCriterion("lastModifiedDate =", value, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateNotEqualTo(Date value) {
            addCriterion("lastModifiedDate <>", value, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateGreaterThan(Date value) {
            addCriterion("lastModifiedDate >", value, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateGreaterThanOrEqualTo(Date value) {
            addCriterion("lastModifiedDate >=", value, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateLessThan(Date value) {
            addCriterion("lastModifiedDate <", value, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateLessThanOrEqualTo(Date value) {
            addCriterion("lastModifiedDate <=", value, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateIn(List<Date> values) {
            addCriterion("lastModifiedDate in", values, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateNotIn(List<Date> values) {
            addCriterion("lastModifiedDate not in", values, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateBetween(Date value1, Date value2) {
            addCriterion("lastModifiedDate between", value1, value2, "lastmodifieddate");
            return (Criteria) this;
        }

        public Criteria andLastmodifieddateNotBetween(Date value1, Date value2) {
            addCriterion("lastModifiedDate not between", value1, value2, "lastmodifieddate");
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