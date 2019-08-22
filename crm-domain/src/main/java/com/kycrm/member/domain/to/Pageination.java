package com.kycrm.member.domain.to;
import java.util.ArrayList;
import java.util.List;
public class Pageination<T> {
	private int pageSize = 100;
	private int pageNo = 1;
	private int upPage;
	private int nextPage;
	private long totalCount;
	private int totalPage;
	private List<T> datas;
	private String pageUrl;
	private List<String> pageView;
	
	public int getFirstResult() {
	 return (this.getPageNo() - 1) * this.getPageSize();
	}
	public int getLastResult() {
	 return this.getPageNo() * this.getPageSize();
	}
	public void setTotalPage() {
	this.totalPage = (int) ((this.totalCount % this.pageSize > 0) ? (this.totalCount / this.pageSize + 1)
	 : this.totalCount / this.pageSize);
	}
	public void setUpPage() {
	 this.upPage = (this.pageNo > 1) ? this.pageNo - 1 : this.pageNo;
	}
	public void setNextPage() {
	 this.nextPage = (this.pageNo == this.totalPage) ? this.pageNo : this.pageNo + 1;
	}
	public int getNextPage() {
	return nextPage;
	}
	public int getTotalPage() {
		int totalPage = (int) (this.totalCount / this.pageSize);
		if ((totalPage == 0) || (this.totalCount % this.pageSize != 0)) {
			totalPage++;
		}
		return totalPage;
	}
	public int getUpPage() {
	return upPage;
	}
	public int getPageSize() {
	return pageSize;
	}
	public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
	}
	public int getPageNo() {
	return pageNo;
	}
	public void setPageNo(int pageNo) {
	this.pageNo = pageNo;
	}
	public long getTotalCount() {
	return totalCount;
	}
	public void setTotalCount(long totalCount2) {
	this.totalCount = totalCount2;
	}
	public List<T> getDatas() {
	return datas;
	}
	public void setDatas(List<T> datas) {
	this.datas = datas;
	}
	public String getPageUrl() {
	return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
	this.pageUrl = pageUrl;
	}
	public List<String> getPageView() {
		return this.pageView;
	}

	public void setPageView(List<String> pageView) {
		this.pageView = pageView;
	}
	public Pageination(int pageNo, int pageSize, long totalCount2) {
		this.setPageNo(pageNo);
		this.setPageSize(pageSize);
		this.setTotalCount(totalCount2);
		this.init();
	}
	public Pageination( ) {
	}
	/**
	 * 初始化计算分页
	 */
	private void init() {
		this.setTotalPage();// 设置一共页数
		this.setUpPage();// 设置上一页
		this.setNextPage();// 设置下一页
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void pageView(String url, String params) {
		this.pageView = new ArrayList();
		if (this.pageNo != 1) {
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo=1'\"><font size=2>首页</font></a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ (this.pageNo - 1)
							+ "'\"><font size=2>上一页</font></a>");
		} else {
			this.pageView.add("<font size=2>首页</font>");
			this.pageView.add("<font size=2>上一页</font>");
		}
		if (getTotalPage() <= 10) {
			for (int i = 0; i < getTotalPage(); i++) {
				if (i + 1 == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
					if (this.pageNo == getTotalPage()) {
						break;
					}
				}
				this.pageView
						.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
								+ url
								+ "?"
								+ params
								+ "&pageNo="
								+ (i + 1)
								+ "'\">" + (i + 1) + "</a>");
			}
		} else if (getTotalPage() <= 20) {
			int l = 0;
			int r = 0;
			if (this.pageNo < 5) {
				l = this.pageNo - 1;
				r = 10 - l - 1;
			} else if (getTotalPage() - this.pageNo < 5) {
				r = getTotalPage() - this.pageNo;
				l = 9 - r;
			} else {
				l = 4;
				r = 5;
			}
			int tmp = this.pageNo - l;
			for (int i = tmp; i < tmp + 10; i++) {
				if (i == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
					if (this.pageNo == getTotalPage()) {
						break;
					}
				}
				this.pageView
						.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
								+ url
								+ "?"
								+ params
								+ "&pageNo="
								+ i
								+ "'\">"
								+ i + "</a>");
			}
		} else if (this.pageNo < 7) {
			for (int i = 0; i < 8; i++) {
				if (i + 1 == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
				}
				this.pageView
						.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
								+ url
								+ "?"
								+ params
								+ "&pageNo="
								+ (i + 1)
								+ "'\">" + (i + 1) + "</a>");
			}
			this.pageView.add("...");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ (getTotalPage() - 1)
							+ "'\">"
							+ (getTotalPage() - 1) + "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ getTotalPage()
							+ "'\">" + getTotalPage() + "</a>");
		} else if (this.pageNo > getTotalPage() - 6) {
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ 1
							+ "'\">"
							+ 1
							+ "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ 2
							+ "'\">"
							+ 2
							+ "</a>");
			this.pageView.add("...");
			for (int i = getTotalPage() - 8; i < getTotalPage(); i++) {
				if (i + 1 == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
					if (this.pageNo == getTotalPage()) {
						break;
					}
				}
				this.pageView
						.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
								+ url
								+ "?"
								+ params
								+ "&pageNo="
								+ (i + 1)
								+ "'\">" + (i + 1) + "</a>");
			}
		} else {
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ 1
							+ "'\">"
							+ 1
							+ "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ 2
							+ "'\">"
							+ 2
							+ "</a>");
			this.pageView.add("...");

			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ (this.pageNo - 2)
							+ "'\">"
							+ (this.pageNo - 2)
							+ "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ (this.pageNo - 1)
							+ "'\">"
							+ (this.pageNo - 1)
							+ "</a>");
			this.pageView.add("<strong>" + this.pageNo + "</strong>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ (this.pageNo + 1)
							+ "'\">"
							+ (this.pageNo + 1)
							+ "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ (this.pageNo + 2)
							+ "'\">"
							+ (this.pageNo + 2)
							+ "</a>");

			this.pageView.add("...");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ (getTotalPage() - 1)
							+ "'\">"
							+ (getTotalPage() - 1) + "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ getTotalPage()
							+ "'\">" + getTotalPage() + "</a>");
		}
		if (this.pageNo != getTotalPage()) {
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ (this.pageNo + 1)
							+ "'\"><font size=2>下一页</font></a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='"
							+ url
							+ "?"
							+ params
							+ "&pageNo="
							+ getTotalPage()
							+ "'\"><font size=2>尾页</font></a>");
		} else {
			this.pageView.add("<font size=2>下一页</font>");
			this.pageView.add("<font size=2>尾页</font>");
		}
		this.pageView
				.add("共<var>"
						+ getTotalPage()
						+ "</var>页 到第<input type='text' id='PAGENO'  size='3' />页 <input type='button' id='skip' class='hand btn60x20' value='确定' onclick=\"javascript:window.location.href = '"
						+ url + "?" + params
						+ "&pageNo=' + $('#PAGENO').val() \"/>");
	}
}