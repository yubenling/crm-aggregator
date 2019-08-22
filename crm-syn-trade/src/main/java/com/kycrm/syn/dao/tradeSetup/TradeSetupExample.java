package com.kycrm.syn.dao.tradeSetup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradeSetupExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TradeSetupExample() {
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

        public Criteria andBadEvaluateInformIsNull() {
            addCriterion("bad_evaluate_inform is null");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformIsNotNull() {
            addCriterion("bad_evaluate_inform is not null");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformEqualTo(Integer value) {
            addCriterion("bad_evaluate_inform =", value, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformNotEqualTo(Integer value) {
            addCriterion("bad_evaluate_inform <>", value, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformGreaterThan(Integer value) {
            addCriterion("bad_evaluate_inform >", value, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformGreaterThanOrEqualTo(Integer value) {
            addCriterion("bad_evaluate_inform >=", value, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformLessThan(Integer value) {
            addCriterion("bad_evaluate_inform <", value, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformLessThanOrEqualTo(Integer value) {
            addCriterion("bad_evaluate_inform <=", value, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformIn(List<Integer> values) {
            addCriterion("bad_evaluate_inform in", values, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformNotIn(List<Integer> values) {
            addCriterion("bad_evaluate_inform not in", values, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformBetween(Integer value1, Integer value2) {
            addCriterion("bad_evaluate_inform between", value1, value2, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andBadEvaluateInformNotBetween(Integer value1, Integer value2) {
            addCriterion("bad_evaluate_inform not between", value1, value2, "badEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andDelayDateIsNull() {
            addCriterion("delay_date is null");
            return (Criteria) this;
        }

        public Criteria andDelayDateIsNotNull() {
            addCriterion("delay_date is not null");
            return (Criteria) this;
        }

        public Criteria andDelayDateEqualTo(Integer value) {
            addCriterion("delay_date =", value, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateNotEqualTo(Integer value) {
            addCriterion("delay_date <>", value, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateGreaterThan(Integer value) {
            addCriterion("delay_date >", value, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateGreaterThanOrEqualTo(Integer value) {
            addCriterion("delay_date >=", value, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateLessThan(Integer value) {
            addCriterion("delay_date <", value, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateLessThanOrEqualTo(Integer value) {
            addCriterion("delay_date <=", value, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateIn(List<Integer> values) {
            addCriterion("delay_date in", values, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateNotIn(List<Integer> values) {
            addCriterion("delay_date not in", values, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateBetween(Integer value1, Integer value2) {
            addCriterion("delay_date between", value1, value2, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayDateNotBetween(Integer value1, Integer value2) {
            addCriterion("delay_date not between", value1, value2, "delayDate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateIsNull() {
            addCriterion("delay_evaluate is null");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateIsNotNull() {
            addCriterion("delay_evaluate is not null");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateEqualTo(Integer value) {
            addCriterion("delay_evaluate =", value, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateNotEqualTo(Integer value) {
            addCriterion("delay_evaluate <>", value, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateGreaterThan(Integer value) {
            addCriterion("delay_evaluate >", value, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateGreaterThanOrEqualTo(Integer value) {
            addCriterion("delay_evaluate >=", value, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateLessThan(Integer value) {
            addCriterion("delay_evaluate <", value, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateLessThanOrEqualTo(Integer value) {
            addCriterion("delay_evaluate <=", value, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateIn(List<Integer> values) {
            addCriterion("delay_evaluate in", values, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateNotIn(List<Integer> values) {
            addCriterion("delay_evaluate not in", values, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateBetween(Integer value1, Integer value2) {
            addCriterion("delay_evaluate between", value1, value2, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andDelayEvaluateNotBetween(Integer value1, Integer value2) {
            addCriterion("delay_evaluate not between", value1, value2, "delayEvaluate");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackIsNull() {
            addCriterion("evaluate_black is null");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackIsNotNull() {
            addCriterion("evaluate_black is not null");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackEqualTo(Integer value) {
            addCriterion("evaluate_black =", value, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackNotEqualTo(Integer value) {
            addCriterion("evaluate_black <>", value, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackGreaterThan(Integer value) {
            addCriterion("evaluate_black >", value, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackGreaterThanOrEqualTo(Integer value) {
            addCriterion("evaluate_black >=", value, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackLessThan(Integer value) {
            addCriterion("evaluate_black <", value, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackLessThanOrEqualTo(Integer value) {
            addCriterion("evaluate_black <=", value, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackIn(List<Integer> values) {
            addCriterion("evaluate_black in", values, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackNotIn(List<Integer> values) {
            addCriterion("evaluate_black not in", values, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackBetween(Integer value1, Integer value2) {
            addCriterion("evaluate_black between", value1, value2, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackNotBetween(Integer value1, Integer value2) {
            addCriterion("evaluate_black not between", value1, value2, "evaluateBlack");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentIsNull() {
            addCriterion("evaluate_black_content is null");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentIsNotNull() {
            addCriterion("evaluate_black_content is not null");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentEqualTo(String value) {
            addCriterion("evaluate_black_content =", value, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentNotEqualTo(String value) {
            addCriterion("evaluate_black_content <>", value, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentGreaterThan(String value) {
            addCriterion("evaluate_black_content >", value, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentGreaterThanOrEqualTo(String value) {
            addCriterion("evaluate_black_content >=", value, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentLessThan(String value) {
            addCriterion("evaluate_black_content <", value, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentLessThanOrEqualTo(String value) {
            addCriterion("evaluate_black_content <=", value, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentLike(String value) {
            addCriterion("evaluate_black_content like", value, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentNotLike(String value) {
            addCriterion("evaluate_black_content not like", value, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentIn(List<String> values) {
            addCriterion("evaluate_black_content in", values, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentNotIn(List<String> values) {
            addCriterion("evaluate_black_content not in", values, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentBetween(String value1, String value2) {
            addCriterion("evaluate_black_content between", value1, value2, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackContentNotBetween(String value1, String value2) {
            addCriterion("evaluate_black_content not between", value1, value2, "evaluateBlackContent");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeIsNull() {
            addCriterion("evaluate_black_type is null");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeIsNotNull() {
            addCriterion("evaluate_black_type is not null");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeEqualTo(String value) {
            addCriterion("evaluate_black_type =", value, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeNotEqualTo(String value) {
            addCriterion("evaluate_black_type <>", value, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeGreaterThan(String value) {
            addCriterion("evaluate_black_type >", value, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeGreaterThanOrEqualTo(String value) {
            addCriterion("evaluate_black_type >=", value, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeLessThan(String value) {
            addCriterion("evaluate_black_type <", value, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeLessThanOrEqualTo(String value) {
            addCriterion("evaluate_black_type <=", value, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeLike(String value) {
            addCriterion("evaluate_black_type like", value, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeNotLike(String value) {
            addCriterion("evaluate_black_type not like", value, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeIn(List<String> values) {
            addCriterion("evaluate_black_type in", values, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeNotIn(List<String> values) {
            addCriterion("evaluate_black_type not in", values, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeBetween(String value1, String value2) {
            addCriterion("evaluate_black_type between", value1, value2, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateBlackTypeNotBetween(String value1, String value2) {
            addCriterion("evaluate_black_type not between", value1, value2, "evaluateBlackType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeIsNull() {
            addCriterion("evaluate_type is null");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeIsNotNull() {
            addCriterion("evaluate_type is not null");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeEqualTo(String value) {
            addCriterion("evaluate_type =", value, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeNotEqualTo(String value) {
            addCriterion("evaluate_type <>", value, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeGreaterThan(String value) {
            addCriterion("evaluate_type >", value, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeGreaterThanOrEqualTo(String value) {
            addCriterion("evaluate_type >=", value, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeLessThan(String value) {
            addCriterion("evaluate_type <", value, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeLessThanOrEqualTo(String value) {
            addCriterion("evaluate_type <=", value, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeLike(String value) {
            addCriterion("evaluate_type like", value, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeNotLike(String value) {
            addCriterion("evaluate_type not like", value, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeIn(List<String> values) {
            addCriterion("evaluate_type in", values, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeNotIn(List<String> values) {
            addCriterion("evaluate_type not in", values, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeBetween(String value1, String value2) {
            addCriterion("evaluate_type between", value1, value2, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andEvaluateTypeNotBetween(String value1, String value2) {
            addCriterion("evaluate_type not between", value1, value2, "evaluateType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeIsNull() {
            addCriterion("execute_type is null");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeIsNotNull() {
            addCriterion("execute_type is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeEqualTo(Integer value) {
            addCriterion("execute_type =", value, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeNotEqualTo(Integer value) {
            addCriterion("execute_type <>", value, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeGreaterThan(Integer value) {
            addCriterion("execute_type >", value, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("execute_type >=", value, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeLessThan(Integer value) {
            addCriterion("execute_type <", value, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeLessThanOrEqualTo(Integer value) {
            addCriterion("execute_type <=", value, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeIn(List<Integer> values) {
            addCriterion("execute_type in", values, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeNotIn(List<Integer> values) {
            addCriterion("execute_type not in", values, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeBetween(Integer value1, Integer value2) {
            addCriterion("execute_type between", value1, value2, "executeType");
            return (Criteria) this;
        }

        public Criteria andExecuteTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("execute_type not between", value1, value2, "executeType");
            return (Criteria) this;
        }

        public Criteria andFilterBlackIsNull() {
            addCriterion("filter_black is null");
            return (Criteria) this;
        }

        public Criteria andFilterBlackIsNotNull() {
            addCriterion("filter_black is not null");
            return (Criteria) this;
        }

        public Criteria andFilterBlackEqualTo(Integer value) {
            addCriterion("filter_black =", value, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackNotEqualTo(Integer value) {
            addCriterion("filter_black <>", value, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackGreaterThan(Integer value) {
            addCriterion("filter_black >", value, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackGreaterThanOrEqualTo(Integer value) {
            addCriterion("filter_black >=", value, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackLessThan(Integer value) {
            addCriterion("filter_black <", value, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackLessThanOrEqualTo(Integer value) {
            addCriterion("filter_black <=", value, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackIn(List<Integer> values) {
            addCriterion("filter_black in", values, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackNotIn(List<Integer> values) {
            addCriterion("filter_black not in", values, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackBetween(Integer value1, Integer value2) {
            addCriterion("filter_black between", value1, value2, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterBlackNotBetween(Integer value1, Integer value2) {
            addCriterion("filter_black not between", value1, value2, "filterBlack");
            return (Criteria) this;
        }

        public Criteria andFilterHassentIsNull() {
            addCriterion("filter_hassent is null");
            return (Criteria) this;
        }

        public Criteria andFilterHassentIsNotNull() {
            addCriterion("filter_hassent is not null");
            return (Criteria) this;
        }

        public Criteria andFilterHassentEqualTo(Integer value) {
            addCriterion("filter_hassent =", value, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentNotEqualTo(Integer value) {
            addCriterion("filter_hassent <>", value, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentGreaterThan(Integer value) {
            addCriterion("filter_hassent >", value, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentGreaterThanOrEqualTo(Integer value) {
            addCriterion("filter_hassent >=", value, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentLessThan(Integer value) {
            addCriterion("filter_hassent <", value, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentLessThanOrEqualTo(Integer value) {
            addCriterion("filter_hassent <=", value, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentIn(List<Integer> values) {
            addCriterion("filter_hassent in", values, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentNotIn(List<Integer> values) {
            addCriterion("filter_hassent not in", values, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentBetween(Integer value1, Integer value2) {
            addCriterion("filter_hassent between", value1, value2, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterHassentNotBetween(Integer value1, Integer value2) {
            addCriterion("filter_hassent not between", value1, value2, "filterHassent");
            return (Criteria) this;
        }

        public Criteria andFilterOnceIsNull() {
            addCriterion("filter_once is null");
            return (Criteria) this;
        }

        public Criteria andFilterOnceIsNotNull() {
            addCriterion("filter_once is not null");
            return (Criteria) this;
        }

        public Criteria andFilterOnceEqualTo(Integer value) {
            addCriterion("filter_once =", value, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceNotEqualTo(Integer value) {
            addCriterion("filter_once <>", value, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceGreaterThan(Integer value) {
            addCriterion("filter_once >", value, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceGreaterThanOrEqualTo(Integer value) {
            addCriterion("filter_once >=", value, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceLessThan(Integer value) {
            addCriterion("filter_once <", value, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceLessThanOrEqualTo(Integer value) {
            addCriterion("filter_once <=", value, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceIn(List<Integer> values) {
            addCriterion("filter_once in", values, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceNotIn(List<Integer> values) {
            addCriterion("filter_once not in", values, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceBetween(Integer value1, Integer value2) {
            addCriterion("filter_once between", value1, value2, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andFilterOnceNotBetween(Integer value1, Integer value2) {
            addCriterion("filter_once not between", value1, value2, "filterOnce");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeIsNull() {
            addCriterion("max_execute_time is null");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeIsNotNull() {
            addCriterion("max_execute_time is not null");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeEqualTo(Date value) {
            addCriterion("max_execute_time =", value, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeNotEqualTo(Date value) {
            addCriterion("max_execute_time <>", value, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeGreaterThan(Date value) {
            addCriterion("max_execute_time >", value, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("max_execute_time >=", value, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeLessThan(Date value) {
            addCriterion("max_execute_time <", value, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeLessThanOrEqualTo(Date value) {
            addCriterion("max_execute_time <=", value, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeIn(List<Date> values) {
            addCriterion("max_execute_time in", values, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeNotIn(List<Date> values) {
            addCriterion("max_execute_time not in", values, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeBetween(Date value1, Date value2) {
            addCriterion("max_execute_time between", value1, value2, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxExecuteTimeNotBetween(Date value1, Date value2) {
            addCriterion("max_execute_time not between", value1, value2, "maxExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeIsNull() {
            addCriterion("max_inform_time is null");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeIsNotNull() {
            addCriterion("max_inform_time is not null");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeEqualTo(String value) {
            addCriterion("max_inform_time =", value, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeNotEqualTo(String value) {
            addCriterion("max_inform_time <>", value, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeGreaterThan(String value) {
            addCriterion("max_inform_time >", value, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeGreaterThanOrEqualTo(String value) {
            addCriterion("max_inform_time >=", value, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeLessThan(String value) {
            addCriterion("max_inform_time <", value, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeLessThanOrEqualTo(String value) {
            addCriterion("max_inform_time <=", value, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeLike(String value) {
            addCriterion("max_inform_time like", value, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeNotLike(String value) {
            addCriterion("max_inform_time not like", value, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeIn(List<String> values) {
            addCriterion("max_inform_time in", values, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeNotIn(List<String> values) {
            addCriterion("max_inform_time not in", values, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeBetween(String value1, String value2) {
            addCriterion("max_inform_time between", value1, value2, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxInformTimeNotBetween(String value1, String value2) {
            addCriterion("max_inform_time not between", value1, value2, "maxInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentIsNull() {
            addCriterion("max_payment is null");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentIsNotNull() {
            addCriterion("max_payment is not null");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentEqualTo(BigDecimal value) {
            addCriterion("max_payment =", value, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentNotEqualTo(BigDecimal value) {
            addCriterion("max_payment <>", value, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentGreaterThan(BigDecimal value) {
            addCriterion("max_payment >", value, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("max_payment >=", value, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentLessThan(BigDecimal value) {
            addCriterion("max_payment <", value, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentLessThanOrEqualTo(BigDecimal value) {
            addCriterion("max_payment <=", value, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentIn(List<BigDecimal> values) {
            addCriterion("max_payment in", values, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentNotIn(List<BigDecimal> values) {
            addCriterion("max_payment not in", values, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("max_payment between", value1, value2, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxPaymentNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("max_payment not between", value1, value2, "maxPayment");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumIsNull() {
            addCriterion("max_product_num is null");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumIsNotNull() {
            addCriterion("max_product_num is not null");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumEqualTo(Integer value) {
            addCriterion("max_product_num =", value, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumNotEqualTo(Integer value) {
            addCriterion("max_product_num <>", value, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumGreaterThan(Integer value) {
            addCriterion("max_product_num >", value, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_product_num >=", value, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumLessThan(Integer value) {
            addCriterion("max_product_num <", value, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumLessThanOrEqualTo(Integer value) {
            addCriterion("max_product_num <=", value, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumIn(List<Integer> values) {
            addCriterion("max_product_num in", values, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumNotIn(List<Integer> values) {
            addCriterion("max_product_num not in", values, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumBetween(Integer value1, Integer value2) {
            addCriterion("max_product_num between", value1, value2, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMaxProductNumNotBetween(Integer value1, Integer value2) {
            addCriterion("max_product_num not between", value1, value2, "maxProductNum");
            return (Criteria) this;
        }

        public Criteria andMemberLevelIsNull() {
            addCriterion("member_level is null");
            return (Criteria) this;
        }

        public Criteria andMemberLevelIsNotNull() {
            addCriterion("member_level is not null");
            return (Criteria) this;
        }

        public Criteria andMemberLevelEqualTo(String value) {
            addCriterion("member_level =", value, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelNotEqualTo(String value) {
            addCriterion("member_level <>", value, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelGreaterThan(String value) {
            addCriterion("member_level >", value, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelGreaterThanOrEqualTo(String value) {
            addCriterion("member_level >=", value, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelLessThan(String value) {
            addCriterion("member_level <", value, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelLessThanOrEqualTo(String value) {
            addCriterion("member_level <=", value, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelLike(String value) {
            addCriterion("member_level like", value, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelNotLike(String value) {
            addCriterion("member_level not like", value, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelIn(List<String> values) {
            addCriterion("member_level in", values, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelNotIn(List<String> values) {
            addCriterion("member_level not in", values, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelBetween(String value1, String value2) {
            addCriterion("member_level between", value1, value2, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMemberLevelNotBetween(String value1, String value2) {
            addCriterion("member_level not between", value1, value2, "memberLevel");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeIsNull() {
            addCriterion("min_execute_time is null");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeIsNotNull() {
            addCriterion("min_execute_time is not null");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeEqualTo(Date value) {
            addCriterion("min_execute_time =", value, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeNotEqualTo(Date value) {
            addCriterion("min_execute_time <>", value, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeGreaterThan(Date value) {
            addCriterion("min_execute_time >", value, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("min_execute_time >=", value, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeLessThan(Date value) {
            addCriterion("min_execute_time <", value, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeLessThanOrEqualTo(Date value) {
            addCriterion("min_execute_time <=", value, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeIn(List<Date> values) {
            addCriterion("min_execute_time in", values, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeNotIn(List<Date> values) {
            addCriterion("min_execute_time not in", values, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeBetween(Date value1, Date value2) {
            addCriterion("min_execute_time between", value1, value2, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinExecuteTimeNotBetween(Date value1, Date value2) {
            addCriterion("min_execute_time not between", value1, value2, "minExecuteTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeIsNull() {
            addCriterion("min_inform_time is null");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeIsNotNull() {
            addCriterion("min_inform_time is not null");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeEqualTo(String value) {
            addCriterion("min_inform_time =", value, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeNotEqualTo(String value) {
            addCriterion("min_inform_time <>", value, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeGreaterThan(String value) {
            addCriterion("min_inform_time >", value, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeGreaterThanOrEqualTo(String value) {
            addCriterion("min_inform_time >=", value, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeLessThan(String value) {
            addCriterion("min_inform_time <", value, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeLessThanOrEqualTo(String value) {
            addCriterion("min_inform_time <=", value, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeLike(String value) {
            addCriterion("min_inform_time like", value, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeNotLike(String value) {
            addCriterion("min_inform_time not like", value, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeIn(List<String> values) {
            addCriterion("min_inform_time in", values, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeNotIn(List<String> values) {
            addCriterion("min_inform_time not in", values, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeBetween(String value1, String value2) {
            addCriterion("min_inform_time between", value1, value2, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinInformTimeNotBetween(String value1, String value2) {
            addCriterion("min_inform_time not between", value1, value2, "minInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPaymentIsNull() {
            addCriterion("min_payment is null");
            return (Criteria) this;
        }

        public Criteria andMinPaymentIsNotNull() {
            addCriterion("min_payment is not null");
            return (Criteria) this;
        }

        public Criteria andMinPaymentEqualTo(BigDecimal value) {
            addCriterion("min_payment =", value, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentNotEqualTo(BigDecimal value) {
            addCriterion("min_payment <>", value, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentGreaterThan(BigDecimal value) {
            addCriterion("min_payment >", value, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("min_payment >=", value, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentLessThan(BigDecimal value) {
            addCriterion("min_payment <", value, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentLessThanOrEqualTo(BigDecimal value) {
            addCriterion("min_payment <=", value, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentIn(List<BigDecimal> values) {
            addCriterion("min_payment in", values, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentNotIn(List<BigDecimal> values) {
            addCriterion("min_payment not in", values, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("min_payment between", value1, value2, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinPaymentNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("min_payment not between", value1, value2, "minPayment");
            return (Criteria) this;
        }

        public Criteria andMinProductNumIsNull() {
            addCriterion("min_product_num is null");
            return (Criteria) this;
        }

        public Criteria andMinProductNumIsNotNull() {
            addCriterion("min_product_num is not null");
            return (Criteria) this;
        }

        public Criteria andMinProductNumEqualTo(Integer value) {
            addCriterion("min_product_num =", value, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumNotEqualTo(Integer value) {
            addCriterion("min_product_num <>", value, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumGreaterThan(Integer value) {
            addCriterion("min_product_num >", value, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("min_product_num >=", value, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumLessThan(Integer value) {
            addCriterion("min_product_num <", value, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumLessThanOrEqualTo(Integer value) {
            addCriterion("min_product_num <=", value, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumIn(List<Integer> values) {
            addCriterion("min_product_num in", values, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumNotIn(List<Integer> values) {
            addCriterion("min_product_num not in", values, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumBetween(Integer value1, Integer value2) {
            addCriterion("min_product_num between", value1, value2, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andMinProductNumNotBetween(Integer value1, Integer value2) {
            addCriterion("min_product_num not between", value1, value2, "minProductNum");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformIsNull() {
            addCriterion("natural_evaluate_inform is null");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformIsNotNull() {
            addCriterion("natural_evaluate_inform is not null");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformEqualTo(Integer value) {
            addCriterion("natural_evaluate_inform =", value, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformNotEqualTo(Integer value) {
            addCriterion("natural_evaluate_inform <>", value, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformGreaterThan(Integer value) {
            addCriterion("natural_evaluate_inform >", value, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformGreaterThanOrEqualTo(Integer value) {
            addCriterion("natural_evaluate_inform >=", value, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformLessThan(Integer value) {
            addCriterion("natural_evaluate_inform <", value, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformLessThanOrEqualTo(Integer value) {
            addCriterion("natural_evaluate_inform <=", value, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformIn(List<Integer> values) {
            addCriterion("natural_evaluate_inform in", values, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformNotIn(List<Integer> values) {
            addCriterion("natural_evaluate_inform not in", values, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformBetween(Integer value1, Integer value2) {
            addCriterion("natural_evaluate_inform between", value1, value2, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNaturalEvaluateInformNotBetween(Integer value1, Integer value2) {
            addCriterion("natural_evaluate_inform not between", value1, value2, "naturalEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andProductTypeIsNull() {
            addCriterion("product_type is null");
            return (Criteria) this;
        }

        public Criteria andProductTypeIsNotNull() {
            addCriterion("product_type is not null");
            return (Criteria) this;
        }

        public Criteria andProductTypeEqualTo(Integer value) {
            addCriterion("product_type =", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotEqualTo(Integer value) {
            addCriterion("product_type <>", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThan(Integer value) {
            addCriterion("product_type >", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("product_type >=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThan(Integer value) {
            addCriterion("product_type <", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThanOrEqualTo(Integer value) {
            addCriterion("product_type <=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeIn(List<Integer> values) {
            addCriterion("product_type in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotIn(List<Integer> values) {
            addCriterion("product_type not in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeBetween(Integer value1, Integer value2) {
            addCriterion("product_type between", value1, value2, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("product_type not between", value1, value2, "productType");
            return (Criteria) this;
        }

        public Criteria andProductsIsNull() {
            addCriterion("products is null");
            return (Criteria) this;
        }

        public Criteria andProductsIsNotNull() {
            addCriterion("products is not null");
            return (Criteria) this;
        }

        public Criteria andProductsEqualTo(String value) {
            addCriterion("products =", value, "products");
            return (Criteria) this;
        }

        public Criteria andProductsNotEqualTo(String value) {
            addCriterion("products <>", value, "products");
            return (Criteria) this;
        }

        public Criteria andProductsGreaterThan(String value) {
            addCriterion("products >", value, "products");
            return (Criteria) this;
        }

        public Criteria andProductsGreaterThanOrEqualTo(String value) {
            addCriterion("products >=", value, "products");
            return (Criteria) this;
        }

        public Criteria andProductsLessThan(String value) {
            addCriterion("products <", value, "products");
            return (Criteria) this;
        }

        public Criteria andProductsLessThanOrEqualTo(String value) {
            addCriterion("products <=", value, "products");
            return (Criteria) this;
        }

        public Criteria andProductsLike(String value) {
            addCriterion("products like", value, "products");
            return (Criteria) this;
        }

        public Criteria andProductsNotLike(String value) {
            addCriterion("products not like", value, "products");
            return (Criteria) this;
        }

        public Criteria andProductsIn(List<String> values) {
            addCriterion("products in", values, "products");
            return (Criteria) this;
        }

        public Criteria andProductsNotIn(List<String> values) {
            addCriterion("products not in", values, "products");
            return (Criteria) this;
        }

        public Criteria andProductsBetween(String value1, String value2) {
            addCriterion("products between", value1, value2, "products");
            return (Criteria) this;
        }

        public Criteria andProductsNotBetween(String value1, String value2) {
            addCriterion("products not between", value1, value2, "products");
            return (Criteria) this;
        }

        public Criteria andSellerFlagIsNull() {
            addCriterion("seller_flag is null");
            return (Criteria) this;
        }

        public Criteria andSellerFlagIsNotNull() {
            addCriterion("seller_flag is not null");
            return (Criteria) this;
        }

        public Criteria andSellerFlagEqualTo(String value) {
            addCriterion("seller_flag =", value, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagNotEqualTo(String value) {
            addCriterion("seller_flag <>", value, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagGreaterThan(String value) {
            addCriterion("seller_flag >", value, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagGreaterThanOrEqualTo(String value) {
            addCriterion("seller_flag >=", value, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagLessThan(String value) {
            addCriterion("seller_flag <", value, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagLessThanOrEqualTo(String value) {
            addCriterion("seller_flag <=", value, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagLike(String value) {
            addCriterion("seller_flag like", value, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagNotLike(String value) {
            addCriterion("seller_flag not like", value, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagIn(List<String> values) {
            addCriterion("seller_flag in", values, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagNotIn(List<String> values) {
            addCriterion("seller_flag not in", values, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagBetween(String value1, String value2) {
            addCriterion("seller_flag between", value1, value2, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerFlagNotBetween(String value1, String value2) {
            addCriterion("seller_flag not between", value1, value2, "sellerFlag");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkIsNull() {
            addCriterion("seller_remark is null");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkIsNotNull() {
            addCriterion("seller_remark is not null");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkEqualTo(Integer value) {
            addCriterion("seller_remark =", value, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkNotEqualTo(Integer value) {
            addCriterion("seller_remark <>", value, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkGreaterThan(Integer value) {
            addCriterion("seller_remark >", value, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkGreaterThanOrEqualTo(Integer value) {
            addCriterion("seller_remark >=", value, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkLessThan(Integer value) {
            addCriterion("seller_remark <", value, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkLessThanOrEqualTo(Integer value) {
            addCriterion("seller_remark <=", value, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkIn(List<Integer> values) {
            addCriterion("seller_remark in", values, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkNotIn(List<Integer> values) {
            addCriterion("seller_remark not in", values, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkBetween(Integer value1, Integer value2) {
            addCriterion("seller_remark between", value1, value2, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSellerRemarkNotBetween(Integer value1, Integer value2) {
            addCriterion("seller_remark not between", value1, value2, "sellerRemark");
            return (Criteria) this;
        }

        public Criteria andSmsContentIsNull() {
            addCriterion("sms_content is null");
            return (Criteria) this;
        }

        public Criteria andSmsContentIsNotNull() {
            addCriterion("sms_content is not null");
            return (Criteria) this;
        }

        public Criteria andSmsContentEqualTo(String value) {
            addCriterion("sms_content =", value, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentNotEqualTo(String value) {
            addCriterion("sms_content <>", value, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentGreaterThan(String value) {
            addCriterion("sms_content >", value, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentGreaterThanOrEqualTo(String value) {
            addCriterion("sms_content >=", value, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentLessThan(String value) {
            addCriterion("sms_content <", value, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentLessThanOrEqualTo(String value) {
            addCriterion("sms_content <=", value, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentLike(String value) {
            addCriterion("sms_content like", value, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentNotLike(String value) {
            addCriterion("sms_content not like", value, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentIn(List<String> values) {
            addCriterion("sms_content in", values, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentNotIn(List<String> values) {
            addCriterion("sms_content not in", values, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentBetween(String value1, String value2) {
            addCriterion("sms_content between", value1, value2, "smsContent");
            return (Criteria) this;
        }

        public Criteria andSmsContentNotBetween(String value1, String value2) {
            addCriterion("sms_content not between", value1, value2, "smsContent");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andTaskLevelIsNull() {
            addCriterion("task_level is null");
            return (Criteria) this;
        }

        public Criteria andTaskLevelIsNotNull() {
            addCriterion("task_level is not null");
            return (Criteria) this;
        }

        public Criteria andTaskLevelEqualTo(Integer value) {
            addCriterion("task_level =", value, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelNotEqualTo(Integer value) {
            addCriterion("task_level <>", value, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelGreaterThan(Integer value) {
            addCriterion("task_level >", value, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("task_level >=", value, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelLessThan(Integer value) {
            addCriterion("task_level <", value, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelLessThanOrEqualTo(Integer value) {
            addCriterion("task_level <=", value, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelIn(List<Integer> values) {
            addCriterion("task_level in", values, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelNotIn(List<Integer> values) {
            addCriterion("task_level not in", values, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelBetween(Integer value1, Integer value2) {
            addCriterion("task_level between", value1, value2, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("task_level not between", value1, value2, "taskLevel");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNull() {
            addCriterion("task_name is null");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNotNull() {
            addCriterion("task_name is not null");
            return (Criteria) this;
        }

        public Criteria andTaskNameEqualTo(String value) {
            addCriterion("task_name =", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotEqualTo(String value) {
            addCriterion("task_name <>", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThan(String value) {
            addCriterion("task_name >", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThanOrEqualTo(String value) {
            addCriterion("task_name >=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThan(String value) {
            addCriterion("task_name <", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThanOrEqualTo(String value) {
            addCriterion("task_name <=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLike(String value) {
            addCriterion("task_name like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotLike(String value) {
            addCriterion("task_name not like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameIn(List<String> values) {
            addCriterion("task_name in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotIn(List<String> values) {
            addCriterion("task_name not in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameBetween(String value1, String value2) {
            addCriterion("task_name between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotBetween(String value1, String value2) {
            addCriterion("task_name not between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("time is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("time is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Integer value) {
            addCriterion("time =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Integer value) {
            addCriterion("time <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Integer value) {
            addCriterion("time >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("time >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Integer value) {
            addCriterion("time <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Integer value) {
            addCriterion("time <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Integer> values) {
            addCriterion("time in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Integer> values) {
            addCriterion("time not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Integer value1, Integer value2) {
            addCriterion("time between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("time not between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformIsNull() {
            addCriterion("time_out_inform is null");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformIsNotNull() {
            addCriterion("time_out_inform is not null");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformEqualTo(Integer value) {
            addCriterion("time_out_inform =", value, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformNotEqualTo(Integer value) {
            addCriterion("time_out_inform <>", value, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformGreaterThan(Integer value) {
            addCriterion("time_out_inform >", value, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformGreaterThanOrEqualTo(Integer value) {
            addCriterion("time_out_inform >=", value, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformLessThan(Integer value) {
            addCriterion("time_out_inform <", value, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformLessThanOrEqualTo(Integer value) {
            addCriterion("time_out_inform <=", value, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformIn(List<Integer> values) {
            addCriterion("time_out_inform in", values, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformNotIn(List<Integer> values) {
            addCriterion("time_out_inform not in", values, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformBetween(Integer value1, Integer value2) {
            addCriterion("time_out_inform between", value1, value2, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTimeOutInformNotBetween(Integer value1, Integer value2) {
            addCriterion("time_out_inform not between", value1, value2, "timeOutInform");
            return (Criteria) this;
        }

        public Criteria andTradeFromIsNull() {
            addCriterion("trade_from is null");
            return (Criteria) this;
        }

        public Criteria andTradeFromIsNotNull() {
            addCriterion("trade_from is not null");
            return (Criteria) this;
        }

        public Criteria andTradeFromEqualTo(String value) {
            addCriterion("trade_from =", value, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromNotEqualTo(String value) {
            addCriterion("trade_from <>", value, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromGreaterThan(String value) {
            addCriterion("trade_from >", value, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromGreaterThanOrEqualTo(String value) {
            addCriterion("trade_from >=", value, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromLessThan(String value) {
            addCriterion("trade_from <", value, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromLessThanOrEqualTo(String value) {
            addCriterion("trade_from <=", value, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromLike(String value) {
            addCriterion("trade_from like", value, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromNotLike(String value) {
            addCriterion("trade_from not like", value, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromIn(List<String> values) {
            addCriterion("trade_from in", values, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromNotIn(List<String> values) {
            addCriterion("trade_from not in", values, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromBetween(String value1, String value2) {
            addCriterion("trade_from between", value1, value2, "tradeFrom");
            return (Criteria) this;
        }

        public Criteria andTradeFromNotBetween(String value1, String value2) {
            addCriterion("trade_from not between", value1, value2, "tradeFrom");
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andRemindTimeIsNull() {
            addCriterion("remind_time is null");
            return (Criteria) this;
        }

        public Criteria andRemindTimeIsNotNull() {
            addCriterion("remind_time is not null");
            return (Criteria) this;
        }

        public Criteria andRemindTimeEqualTo(Integer value) {
            addCriterion("remind_time =", value, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeNotEqualTo(Integer value) {
            addCriterion("remind_time <>", value, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeGreaterThan(Integer value) {
            addCriterion("remind_time >", value, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("remind_time >=", value, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeLessThan(Integer value) {
            addCriterion("remind_time <", value, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeLessThanOrEqualTo(Integer value) {
            addCriterion("remind_time <=", value, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeIn(List<Integer> values) {
            addCriterion("remind_time in", values, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeNotIn(List<Integer> values) {
            addCriterion("remind_time not in", values, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeBetween(Integer value1, Integer value2) {
            addCriterion("remind_time between", value1, value2, "remindTime");
            return (Criteria) this;
        }

        public Criteria andRemindTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("remind_time not between", value1, value2, "remindTime");
            return (Criteria) this;
        }

        public Criteria andTimeTypeIsNull() {
            addCriterion("time_type is null");
            return (Criteria) this;
        }

        public Criteria andTimeTypeIsNotNull() {
            addCriterion("time_type is not null");
            return (Criteria) this;
        }

        public Criteria andTimeTypeEqualTo(Integer value) {
            addCriterion("time_type =", value, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeNotEqualTo(Integer value) {
            addCriterion("time_type <>", value, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeGreaterThan(Integer value) {
            addCriterion("time_type >", value, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("time_type >=", value, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeLessThan(Integer value) {
            addCriterion("time_type <", value, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeLessThanOrEqualTo(Integer value) {
            addCriterion("time_type <=", value, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeIn(List<Integer> values) {
            addCriterion("time_type in", values, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeNotIn(List<Integer> values) {
            addCriterion("time_type not in", values, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeBetween(Integer value1, Integer value2) {
            addCriterion("time_type between", value1, value2, "timeType");
            return (Criteria) this;
        }

        public Criteria andTimeTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("time_type not between", value1, value2, "timeType");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeIsNull() {
            addCriterion("max_middle_inform_time is null");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeIsNotNull() {
            addCriterion("max_middle_inform_time is not null");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeEqualTo(String value) {
            addCriterion("max_middle_inform_time =", value, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeNotEqualTo(String value) {
            addCriterion("max_middle_inform_time <>", value, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeGreaterThan(String value) {
            addCriterion("max_middle_inform_time >", value, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeGreaterThanOrEqualTo(String value) {
            addCriterion("max_middle_inform_time >=", value, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeLessThan(String value) {
            addCriterion("max_middle_inform_time <", value, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeLessThanOrEqualTo(String value) {
            addCriterion("max_middle_inform_time <=", value, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeLike(String value) {
            addCriterion("max_middle_inform_time like", value, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeNotLike(String value) {
            addCriterion("max_middle_inform_time not like", value, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeIn(List<String> values) {
            addCriterion("max_middle_inform_time in", values, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeNotIn(List<String> values) {
            addCriterion("max_middle_inform_time not in", values, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeBetween(String value1, String value2) {
            addCriterion("max_middle_inform_time between", value1, value2, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxMiddleInformTimeNotBetween(String value1, String value2) {
            addCriterion("max_middle_inform_time not between", value1, value2, "maxMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeIsNull() {
            addCriterion("max_primary_inform_time is null");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeIsNotNull() {
            addCriterion("max_primary_inform_time is not null");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeEqualTo(String value) {
            addCriterion("max_primary_inform_time =", value, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeNotEqualTo(String value) {
            addCriterion("max_primary_inform_time <>", value, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeGreaterThan(String value) {
            addCriterion("max_primary_inform_time >", value, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeGreaterThanOrEqualTo(String value) {
            addCriterion("max_primary_inform_time >=", value, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeLessThan(String value) {
            addCriterion("max_primary_inform_time <", value, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeLessThanOrEqualTo(String value) {
            addCriterion("max_primary_inform_time <=", value, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeLike(String value) {
            addCriterion("max_primary_inform_time like", value, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeNotLike(String value) {
            addCriterion("max_primary_inform_time not like", value, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeIn(List<String> values) {
            addCriterion("max_primary_inform_time in", values, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeNotIn(List<String> values) {
            addCriterion("max_primary_inform_time not in", values, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeBetween(String value1, String value2) {
            addCriterion("max_primary_inform_time between", value1, value2, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxPrimaryInformTimeNotBetween(String value1, String value2) {
            addCriterion("max_primary_inform_time not between", value1, value2, "maxPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeIsNull() {
            addCriterion("max_senior_inform_time is null");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeIsNotNull() {
            addCriterion("max_senior_inform_time is not null");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeEqualTo(String value) {
            addCriterion("max_senior_inform_time =", value, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeNotEqualTo(String value) {
            addCriterion("max_senior_inform_time <>", value, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeGreaterThan(String value) {
            addCriterion("max_senior_inform_time >", value, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeGreaterThanOrEqualTo(String value) {
            addCriterion("max_senior_inform_time >=", value, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeLessThan(String value) {
            addCriterion("max_senior_inform_time <", value, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeLessThanOrEqualTo(String value) {
            addCriterion("max_senior_inform_time <=", value, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeLike(String value) {
            addCriterion("max_senior_inform_time like", value, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeNotLike(String value) {
            addCriterion("max_senior_inform_time not like", value, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeIn(List<String> values) {
            addCriterion("max_senior_inform_time in", values, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeNotIn(List<String> values) {
            addCriterion("max_senior_inform_time not in", values, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeBetween(String value1, String value2) {
            addCriterion("max_senior_inform_time between", value1, value2, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMaxSeniorInformTimeNotBetween(String value1, String value2) {
            addCriterion("max_senior_inform_time not between", value1, value2, "maxSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeIsNull() {
            addCriterion("min_middle_inform_time is null");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeIsNotNull() {
            addCriterion("min_middle_inform_time is not null");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeEqualTo(String value) {
            addCriterion("min_middle_inform_time =", value, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeNotEqualTo(String value) {
            addCriterion("min_middle_inform_time <>", value, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeGreaterThan(String value) {
            addCriterion("min_middle_inform_time >", value, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeGreaterThanOrEqualTo(String value) {
            addCriterion("min_middle_inform_time >=", value, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeLessThan(String value) {
            addCriterion("min_middle_inform_time <", value, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeLessThanOrEqualTo(String value) {
            addCriterion("min_middle_inform_time <=", value, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeLike(String value) {
            addCriterion("min_middle_inform_time like", value, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeNotLike(String value) {
            addCriterion("min_middle_inform_time not like", value, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeIn(List<String> values) {
            addCriterion("min_middle_inform_time in", values, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeNotIn(List<String> values) {
            addCriterion("min_middle_inform_time not in", values, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeBetween(String value1, String value2) {
            addCriterion("min_middle_inform_time between", value1, value2, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinMiddleInformTimeNotBetween(String value1, String value2) {
            addCriterion("min_middle_inform_time not between", value1, value2, "minMiddleInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeIsNull() {
            addCriterion("min_primary_inform_time is null");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeIsNotNull() {
            addCriterion("min_primary_inform_time is not null");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeEqualTo(String value) {
            addCriterion("min_primary_inform_time =", value, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeNotEqualTo(String value) {
            addCriterion("min_primary_inform_time <>", value, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeGreaterThan(String value) {
            addCriterion("min_primary_inform_time >", value, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeGreaterThanOrEqualTo(String value) {
            addCriterion("min_primary_inform_time >=", value, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeLessThan(String value) {
            addCriterion("min_primary_inform_time <", value, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeLessThanOrEqualTo(String value) {
            addCriterion("min_primary_inform_time <=", value, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeLike(String value) {
            addCriterion("min_primary_inform_time like", value, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeNotLike(String value) {
            addCriterion("min_primary_inform_time not like", value, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeIn(List<String> values) {
            addCriterion("min_primary_inform_time in", values, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeNotIn(List<String> values) {
            addCriterion("min_primary_inform_time not in", values, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeBetween(String value1, String value2) {
            addCriterion("min_primary_inform_time between", value1, value2, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinPrimaryInformTimeNotBetween(String value1, String value2) {
            addCriterion("min_primary_inform_time not between", value1, value2, "minPrimaryInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeIsNull() {
            addCriterion("min_senior_inform_time is null");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeIsNotNull() {
            addCriterion("min_senior_inform_time is not null");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeEqualTo(String value) {
            addCriterion("min_senior_inform_time =", value, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeNotEqualTo(String value) {
            addCriterion("min_senior_inform_time <>", value, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeGreaterThan(String value) {
            addCriterion("min_senior_inform_time >", value, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeGreaterThanOrEqualTo(String value) {
            addCriterion("min_senior_inform_time >=", value, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeLessThan(String value) {
            addCriterion("min_senior_inform_time <", value, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeLessThanOrEqualTo(String value) {
            addCriterion("min_senior_inform_time <=", value, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeLike(String value) {
            addCriterion("min_senior_inform_time like", value, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeNotLike(String value) {
            addCriterion("min_senior_inform_time not like", value, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeIn(List<String> values) {
            addCriterion("min_senior_inform_time in", values, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeNotIn(List<String> values) {
            addCriterion("min_senior_inform_time not in", values, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeBetween(String value1, String value2) {
            addCriterion("min_senior_inform_time between", value1, value2, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andMinSeniorInformTimeNotBetween(String value1, String value2) {
            addCriterion("min_senior_inform_time not between", value1, value2, "minSeniorInformTime");
            return (Criteria) this;
        }

        public Criteria andInformMobileIsNull() {
            addCriterion("inform_mobile is null");
            return (Criteria) this;
        }

        public Criteria andInformMobileIsNotNull() {
            addCriterion("inform_mobile is not null");
            return (Criteria) this;
        }

        public Criteria andInformMobileEqualTo(String value) {
            addCriterion("inform_mobile =", value, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileNotEqualTo(String value) {
            addCriterion("inform_mobile <>", value, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileGreaterThan(String value) {
            addCriterion("inform_mobile >", value, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileGreaterThanOrEqualTo(String value) {
            addCriterion("inform_mobile >=", value, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileLessThan(String value) {
            addCriterion("inform_mobile <", value, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileLessThanOrEqualTo(String value) {
            addCriterion("inform_mobile <=", value, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileLike(String value) {
            addCriterion("inform_mobile like", value, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileNotLike(String value) {
            addCriterion("inform_mobile not like", value, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileIn(List<String> values) {
            addCriterion("inform_mobile in", values, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileNotIn(List<String> values) {
            addCriterion("inform_mobile not in", values, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileBetween(String value1, String value2) {
            addCriterion("inform_mobile between", value1, value2, "informMobile");
            return (Criteria) this;
        }

        public Criteria andInformMobileNotBetween(String value1, String value2) {
            addCriterion("inform_mobile not between", value1, value2, "informMobile");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformIsNull() {
            addCriterion("neutral_evaluate_inform is null");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformIsNotNull() {
            addCriterion("neutral_evaluate_inform is not null");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformEqualTo(Integer value) {
            addCriterion("neutral_evaluate_inform =", value, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformNotEqualTo(Integer value) {
            addCriterion("neutral_evaluate_inform <>", value, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformGreaterThan(Integer value) {
            addCriterion("neutral_evaluate_inform >", value, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformGreaterThanOrEqualTo(Integer value) {
            addCriterion("neutral_evaluate_inform >=", value, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformLessThan(Integer value) {
            addCriterion("neutral_evaluate_inform <", value, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformLessThanOrEqualTo(Integer value) {
            addCriterion("neutral_evaluate_inform <=", value, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformIn(List<Integer> values) {
            addCriterion("neutral_evaluate_inform in", values, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformNotIn(List<Integer> values) {
            addCriterion("neutral_evaluate_inform not in", values, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformBetween(Integer value1, Integer value2) {
            addCriterion("neutral_evaluate_inform between", value1, value2, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andNeutralEvaluateInformNotBetween(Integer value1, Integer value2) {
            addCriterion("neutral_evaluate_inform not between", value1, value2, "neutralEvaluateInform");
            return (Criteria) this;
        }

        public Criteria andChosenTimeIsNull() {
            addCriterion("chosen_time is null");
            return (Criteria) this;
        }

        public Criteria andChosenTimeIsNotNull() {
            addCriterion("chosen_time is not null");
            return (Criteria) this;
        }

        public Criteria andChosenTimeEqualTo(Date value) {
            addCriterion("chosen_time =", value, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeNotEqualTo(Date value) {
            addCriterion("chosen_time <>", value, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeGreaterThan(Date value) {
            addCriterion("chosen_time >", value, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("chosen_time >=", value, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeLessThan(Date value) {
            addCriterion("chosen_time <", value, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeLessThanOrEqualTo(Date value) {
            addCriterion("chosen_time <=", value, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeIn(List<Date> values) {
            addCriterion("chosen_time in", values, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeNotIn(List<Date> values) {
            addCriterion("chosen_time not in", values, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeBetween(Date value1, Date value2) {
            addCriterion("chosen_time between", value1, value2, "chosenTime");
            return (Criteria) this;
        }

        public Criteria andChosenTimeNotBetween(Date value1, Date value2) {
            addCriterion("chosen_time not between", value1, value2, "chosenTime");
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

        public Criteria andProvinceIsNull() {
            addCriterion("province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andTradeBlockIsNull() {
            addCriterion("trade_block is null");
            return (Criteria) this;
        }

        public Criteria andTradeBlockIsNotNull() {
            addCriterion("trade_block is not null");
            return (Criteria) this;
        }

        public Criteria andTradeBlockEqualTo(Boolean value) {
            addCriterion("trade_block =", value, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockNotEqualTo(Boolean value) {
            addCriterion("trade_block <>", value, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockGreaterThan(Boolean value) {
            addCriterion("trade_block >", value, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockGreaterThanOrEqualTo(Boolean value) {
            addCriterion("trade_block >=", value, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockLessThan(Boolean value) {
            addCriterion("trade_block <", value, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockLessThanOrEqualTo(Boolean value) {
            addCriterion("trade_block <=", value, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockIn(List<Boolean> values) {
            addCriterion("trade_block in", values, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockNotIn(List<Boolean> values) {
            addCriterion("trade_block not in", values, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockBetween(Boolean value1, Boolean value2) {
            addCriterion("trade_block between", value1, value2, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andTradeBlockNotBetween(Boolean value1, Boolean value2) {
            addCriterion("trade_block not between", value1, value2, "tradeBlock");
            return (Criteria) this;
        }

        public Criteria andOpenTimeIsNull() {
            addCriterion("open_time is null");
            return (Criteria) this;
        }

        public Criteria andOpenTimeIsNotNull() {
            addCriterion("open_time is not null");
            return (Criteria) this;
        }

        public Criteria andOpenTimeEqualTo(Date value) {
            addCriterion("open_time =", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotEqualTo(Date value) {
            addCriterion("open_time <>", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeGreaterThan(Date value) {
            addCriterion("open_time >", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("open_time >=", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeLessThan(Date value) {
            addCriterion("open_time <", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeLessThanOrEqualTo(Date value) {
            addCriterion("open_time <=", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeIn(List<Date> values) {
            addCriterion("open_time in", values, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotIn(List<Date> values) {
            addCriterion("open_time not in", values, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeBetween(Date value1, Date value2) {
            addCriterion("open_time between", value1, value2, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotBetween(Date value1, Date value2) {
            addCriterion("open_time not between", value1, value2, "openTime");
            return (Criteria) this;
        }

        public Criteria andInUseIsNull() {
            addCriterion("in_use is null");
            return (Criteria) this;
        }

        public Criteria andInUseIsNotNull() {
            addCriterion("in_use is not null");
            return (Criteria) this;
        }

        public Criteria andInUseEqualTo(Integer value) {
            addCriterion("in_use =", value, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseNotEqualTo(Integer value) {
            addCriterion("in_use <>", value, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseGreaterThan(Integer value) {
            addCriterion("in_use >", value, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseGreaterThanOrEqualTo(Integer value) {
            addCriterion("in_use >=", value, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseLessThan(Integer value) {
            addCriterion("in_use <", value, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseLessThanOrEqualTo(Integer value) {
            addCriterion("in_use <=", value, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseIn(List<Integer> values) {
            addCriterion("in_use in", values, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseNotIn(List<Integer> values) {
            addCriterion("in_use not in", values, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseBetween(Integer value1, Integer value2) {
            addCriterion("in_use between", value1, value2, "inUse");
            return (Criteria) this;
        }

        public Criteria andInUseNotBetween(Integer value1, Integer value2) {
            addCriterion("in_use not between", value1, value2, "inUse");
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