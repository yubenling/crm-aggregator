<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	
<div id="rightsidebarBox">
	<div class="rightsidebarBox">
		<div class="rightsidebarDiv kefu">
			<i></i>	
			<div class="kefuDiv">
				<p class="title">联系我们</p>
				<div class="kefuLink">
					<a href="http://amos.alicdn.com/getcid.aw?v=2&amp;uid=%E5%8C%97%E4%BA%AC%E5%86%B0%E7%82%B9%E9%9B%B6%E5%BA%A6&amp;site=cntaobao&amp;s=1&amp;groupid=0&amp;charset=utf-8" target="_blank" class="clearfix">
						<span>在线客服</span>
						<img src="${ctx}/images/kftx.png">
					</a>
				</div>
				<img style="width: 1.62rem; height: .45rem; display: block; padding-left: .2rem; margin-top: .1rem;" src="${ctx}/images/t.png">
			</div>
		</div>
		<div class="rightsidebarDiv yijianfankui">
			<i></i>	
		</div>	
		<div class="rightsidebarDiv blackTop">
			<i></i>	
		</div>	
	</div>
</div>
<div class="yijianfankuiBox">
	<div class="titleH3 clearfix">
		意见反馈
		
		<em></em>
	</div>
	<div class="bodyBox">
		<p class="title">您好，客云感谢您填写意见反馈，我们会听取您的意见反馈优化产品，感谢您对客云的支持。</p>	
		<div class="bodyDiv clearfix">
			<label class="clearfix"><i>*</i>反馈内容</label>
			<textarea id="feedbackContent"></textarea>
		</div>
		<div class="bodyDiv clearfix">
			<label class="clearfix">图片说明</label>
			<div class="uploadImgBox">
				<div class="clearfix">
					<div class="uploadImgDiv">
						<a class="uploadImgA" href="javascript:;">+ 添加图片</a>
						<input type="file" id="uploadInput" multiple="multiple" name="fileAttach" onchange="xmTanUploadImg(this)">  
					</div>	
					<p>最多可上传5张图片，支持：jpg/png格式</p>
				</div>
				<div class="uploadImgUl">
					<ul class="clearfix" id="imgboxid">

					</ul>	
				</div>
			</div>
			
		</div>
		<div class="bodyDiv clearfix">
			<label class="clearfix">联系方式</label>
			<div class="telInput">
				<input type="text" name="" id="contactMode" value="" placeholder="手机／QQ／邮箱／旺旺">	
				<p>如果对您的反馈我们有不清楚的地方，请让我们通过联系方式联系到您。</p>
			</div>
		</div>
		<div class="bodyBtn">
			<a href="javascript:;">提交</a>
		</div>
	</div>
	
</div>
<div id="markBgRight"></div>
<div class="msgAlert"></div>
<input type="hidden" id="url" value="${ctx}">


