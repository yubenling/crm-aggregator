package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 交易来源
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:53:09
 * @Tags
 */
public class OrderFrom extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:53:07
	 */
	private static final long serialVersionUID = 1L;

	// 交易来源【手机端】
	private boolean wap;

	// 交易来源【PC端】
	private boolean pc;

	// 交易来源【聚划算】
	private boolean jhs;

	// 交易来源【用户导入】
	private boolean importByUser;

	public OrderFrom() {
		super();

	}

	public OrderFrom(boolean wap, boolean pc, boolean jhs, boolean importByUser) {
		super();
		this.wap = wap;
		this.pc = pc;
		this.jhs = jhs;
		this.importByUser = importByUser;
	}

	public boolean isWap() {
		return wap;
	}

	public void setWap(boolean wap) {
		this.wap = wap;
	}

	public boolean isPc() {
		return pc;
	}

	public void setPc(boolean pc) {
		this.pc = pc;
	}

	public boolean isJhs() {
		return jhs;
	}

	public void setJhs(boolean jhs) {
		this.jhs = jhs;
	}

	public boolean isImportByUser() {
		return importByUser;
	}

	public void setImportByUser(boolean importByUser) {
		this.importByUser = importByUser;
	}

	@Override
	public String toString() {
		return "OrderFrom [wap=" + wap + ", pc=" + pc + ", jhs=" + jhs + ", importByUser=" + importByUser + "]";
	}

}
