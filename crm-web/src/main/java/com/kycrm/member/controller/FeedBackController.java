package com.kycrm.member.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.feedback.FeedBack;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.feedback.IFeedBackService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.UploadPathUtil;
import com.thoughtworks.xstream.core.util.Base64Encoder;

@Controller
@RequestMapping("/systemManage")
public class FeedBackController extends BaseController{
	private static final Log logger = LogFactory.getLog(FeedBackController.class);
	
	@Autowired
	private IFeedBackService feedBackService;
	@Autowired
	private SessionProvider sessionProvider;
	
	/**
	 * 首页反馈 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/feedBackMessage",method = RequestMethod.POST)
	public String feedBackMessage(@RequestBody String params, 
			HttpServletRequest request,HttpServletResponse response) {
		
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		
		Map<String, String> map = new HashMap<String, String>();
    	if (null != params && !"".equals(params)) {
    		try {
    			map = JsonUtil.paramsJsonToObject(params,Map.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		
		try {

			// 保存图片，返回图片名称
			String feedbackImage = saveImages(map.get("fileImages"));
			FeedBack feedBack = new FeedBack();
			feedBack.setUid(user.getId());
			feedBack.setUserId(user.getTaobaoUserNick());
			feedBack.setFeedbackContent(map.get("feedbackContent"));
			feedBack.setFeedbackImage(feedbackImage);
			feedBack.setContactMode(map.get("contactMode"));
			feedBack.setFeedbackRead(false);
			feedBack.setCreatedBy(user.getTaobaoUserNick());
			feedBack.setLastModifiedBy(user.getTaobaoUserNick());
			// 保存反馈信息
			feedBackService.insertFeedBack(feedBack);

			return successReusltMap(ApiResult.FEEDBACK_SUCCESS).toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return failureReusltMap(ApiResult.FEEDBACK_FAILURE).toJson();
	}
	
	
	
	/**
	 * 循环将base64编码的图片进行保存
	 * @param fileImages
	 * @return
	 * @throws IOException
	 */
	private String saveImages(String images) throws Exception {
		String filePath = createFolder();
		String feedbackImage = "";
		if(null != images && !images.equals("")){
			String[] fileImages = images.split(",");
			for (int i = 0; i < fileImages.length; i++) {
				String ImageName = gainImageName();
				String imgFilePath = filePath+ImageName;
				boolean generateImage = generateImage(fileImages[i], imgFilePath);
				if(generateImage){
					logger.info("图片保存完成.........."+imgFilePath);
					feedbackImage+=ImageName+",";
				}
			}
		}
		if(!"".equals(feedbackImage)){
			feedbackImage = feedbackImage.substring(0, feedbackImage.length()-1);
		}
		return feedbackImage;
	}




	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private StringBuilder gainRequestData(HttpServletRequest request) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		char[] buff = new char[1024*1024];
		int len;
		while ((len = reader.read(buff)) != -1) {
			sb.append(buff, 0, len);
		}
		return sb;
	}

	/**
	 * 截取指定参数的内容，进行解码返回
	 * @param str
	 * @param temp
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String splitString(String str, String temp) throws Exception {
		String result = null;
		if (str.indexOf(temp) != -1) {
			if (str.substring(str.indexOf(temp)).indexOf("&") != -1) {
				result = str.substring(str.indexOf(temp)).substring(str.substring(str.indexOf(temp)).indexOf("=") + 1,
						str.substring(str.indexOf(temp)).indexOf("&"));
			} else {
				result = str.substring(str.indexOf(temp)).substring(str.substring(str.indexOf(temp)).indexOf("=") + 1);
			}
		}
		if(null !=result){
			result=URLDecoder.decode(result,"UTF-8");
		}
		return result;
	}

	
	/**
	 * base64字符串转化成图片  
	 * 对字节数组字符串进行Base64解码并生成图片  
	 * @param imgStr
	 * @return
	 */
	private boolean generateImage(String imgStr, String imgFilePath) throws Exception {
    	//图像数据为空
        if (imgStr == null){
        	return false;  
        }
        Base64Encoder decoder = new Base64Encoder();  
        try{  
            //Base64解码  
            byte[] b = decoder.decode(imgStr);
            for(int i=0;i<b.length;++i) {
                if(b[i]<0){//调整异常数据 
                    b[i]+=256;  
                }  
            }  
            OutputStream out = new FileOutputStream(imgFilePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }catch (Exception e){
        	e.printStackTrace();
            return false;  
        }  
    }  
	
    /**
     * 创建文件夹
     * @return
     */
    private String createFolder() throws Exception {
    	String imagePath = UploadPathUtil.FEEDBACK_IMAGE_PATH;
  		File fileP = new File(imagePath);
  		if (!fileP.exists()){
  			fileP.mkdirs();
  		}
		return imagePath;
	}
  		
  		
	/**
	 * 创建图片名字，
	 * @return
	 * @throws IOException 
	 */
	private String gainImageName() throws Exception {
		UUID randomUUID = UUID.randomUUID();
		String strUUID = randomUUID.toString().split("-")[0];
		Date date = new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		String formatStr = strUUID+"_"+format.format(date);
		String imageName = formatStr+".jpg";
		return imageName;
	}
}
