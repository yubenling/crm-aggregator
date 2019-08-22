package com.kycrm.member.domain.utils.pagination;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: MyPagination
* @Description: TODO(分页之中的嵌套另一个分页>>在下一页时使用异步提交)
* @author:jackstraw_yu
* @date 2017年2月18日
*
*/
@SuppressWarnings("serial")
public class MyPagination extends SimplePage {
	private List<?> list;
	private List<String> pageView;

	public MyPagination() {
	}

	public MyPagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	public MyPagination(int pageNo, int pageSize, int totalCount, List<?> list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	public int getFirstResult() {
		return (this.pageNo - 1) * this.pageSize;
	}

	public List<?> getList() {
		return this.list;
	}

	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}

	public List<String> getPageView() {
		return this.pageView;
	}

	public void setPageView(List<String> pageView) {
		this.pageView = pageView;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void pageView() {
		this.pageView = new ArrayList();
		if (this.pageNo != 1) {
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ "1')\" \"><font size=2>首页</font></a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ (this.pageNo - 1)
							+ "')\" \"><font size=2>上一页</font></a>");
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
						.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
								+ (i + 1)
								+ "')\" \">" + (i + 1) + "</a>");
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
						.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
								+ i
								+ "')\" \">"
								+ i + "</a>");
			}
		} else if (this.pageNo < 7) {
			for (int i = 0; i < 8; i++) {
				if (i + 1 == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
				}
				this.pageView
						.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
								+ (i + 1)
								+ "')\" \">" + (i + 1) + "</a>");
			}
			this.pageView.add("...");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ (getTotalPage() - 1)
							+ "')\" \">"
							+ (getTotalPage() - 1) + "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ getTotalPage()
							+ "')\" \">" + getTotalPage() + "</a>");
		} else if (this.pageNo > getTotalPage() - 6) {
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ 1
							+ "')\" \">"
							+ 1
							+ "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ 2
							+ "')\" \">"
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
						.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
								+ (i + 1)
								+ "')\" \">" + (i + 1) + "</a>");
			}
		} else {
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ 1
							+ "')\" \">"
							+ 1
							+ "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ 2
							+ "')\" \">"
							+ 2
							+ "</a>");
			this.pageView.add("...");

			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ (this.pageNo - 2)
							+ "')\" \">"
							+ (this.pageNo - 2)
							+ "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ (this.pageNo - 1)
							+ "')\" \">"
							+ (this.pageNo - 1)
							+ "</a>");
			this.pageView.add("<strong>" + this.pageNo + "</strong>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ (this.pageNo + 1)
							+ "')\" \">"
							+ (this.pageNo + 1)
							+ "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ (this.pageNo + 2)
							+ "')\" \">"
							+ (this.pageNo + 2)
							+ "</a>");

			this.pageView.add("...");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ (getTotalPage() - 1)
							+ "')\" \">"
							+ (getTotalPage() - 1) + "</a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ getTotalPage()
							+ "')\" \">" + getTotalPage() + "</a>");
		}
		if (this.pageNo != getTotalPage()) {
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ (this.pageNo + 1)
							+ "')\" \"><font size=2>下一页</font></a>");
			this.pageView
					.add("<a href=\"javascript:void(0);\" onclick=\"toPage('"
							+ getTotalPage()
							+ "')\" \"><font size=2>尾页</font></a>");
		} else {
			this.pageView.add("<font size=2>下一页</font>");
			this.pageView.add("<font size=2>尾页</font>");
		}
		/*this.pageView
				.add("共<var>"
						+ getTotalPage()
						+ "</var>页 到第<input type='text' id='PAGENO'  size='3' />页 <input type='button' id='skip' class='hand btn60x20' value='确定' onclick=\"toPage("
						+ "$('#PAGENO').val())\" \"/>");*/
	}
}
