package com.kycrm.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Encoder;

public class PayUtil {
	public static final String TRADE_FINISHED = "TRADE_FINISHED";//交易完成
	public static final String TRADE_SUCCESS = "TRADE_SUCCESS";//支付成功
	public static final String TRADE_CLOSED = "TRADE_CLOSED";//支付关闭
	
	//logo图片路径
	private static String logoPath = "";
	
    /**
     * 获取LogoPath
     * @param request 
     */
    public static String createLogoPath(HttpServletRequest request) {
    	try {
			if(null == logoPath || "".equals(logoPath)){
				logoPath = request.getServletContext().getRealPath("")+File.separator+"crm"+File.separator+"images"+File.separator+"QRlogo.png";
			}
		} catch (Exception e) {
			logoPath = "";
		}
		return logoPath;
	}

	/**
     * 创建文件夹
     * @param day 
     * @return
     */
    public static String createFolder(String day) throws Exception {
    	String imagePath = UploadPathUtil.QRCODE_IMAGE_PATH+day;
  		File fileP = new File(imagePath);
  		if (!fileP.exists()){
  			fileP.mkdirs();
  		}
		return imagePath;
	}
    
    
    /**
     * 校验短信条数
     */
    public static boolean checkoutSmsAndMoney(Double money,Integer rechargeNum){
    	boolean flag = false;
    	Integer num = null;
		if(money<=200){
			num=(int) (money/0.055);
		}else if(money>200&&money<=1000){
			num=(int) (money/0.050);
		}else if(money>1000&&money<=1500){
			num=(int) (money/0.048);
		}else if(money>1500&&money<=2500){
			num=(int) (money/0.047);
		}else if(money>2500&&money<=4500){
			num=(int) (money/0.046);
		}else if(money>4500&&money<=10000){
			num=(int) (money/0.045);
		}else if(money>10000&&money<=20000){
			num=(int) (money/0.040);
		}else if(money>20000&&money<=40000){
			num=(int) (money/0.038);
		}else if(money>40000){
			num=(int) (money/0.037);
		}
		if(null != num){
			if(num.equals(rechargeNum)){
				flag = true;
			}
		}
		return flag;
    }
    
    /**
     * 获取单价
     */
    public static String univalence(Double money){
    	String price = "0.055";
    	if(money<=200){
			price = "0.055";
    	}else if(money>200&&money<=1000){
			price = "0.050";
    	}else if(money>1000&&money<=1500){
			price = "0.048";
    	}else if(money>1500&&money<=2500){
			price = "0.047";
    	}else if(money>2500&&money<=4500){
			price = "0.046";
    	}else if(money>4500&&money<=10000){
			price = "0.045";
    	}else if(money>10000&&money<=20000){
			price = "0.040";
    	}else if(money>20000&&money<=40000){
			price = "0.038";
    	}else if(money>40000){
			price = "0.037";
		}
		return price;
    }
    
    /**
     * 校验单价
     */
	public static boolean comparePrice(Double total_amount, Double rechargePrice) {
		try {
			return total_amount.intValue()==rechargePrice.intValue();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取标题
	 */
	public static String getSubject(Integer rechargeNum, Double totalAmount) {
		return "自定义充值短信"+rechargeNum+"条("+totalAmount+"元)";
	}

	/**
	 * 生成二维码图片返回路径 
	 * @throws IOException 
	 */
	public static String createQrCodeImage(String logoPath,
			String outTradeNo,String qrCode) throws Exception {
			//生成二维码图片
			BufferedImage image = ZxingUtils.createImage(qrCode, logoPath, true);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
	        ImageIO.write(image, "png", baos);//写入流中
	        byte[] bytes = baos.toByteArray();//转换成字节
	        BASE64Encoder encoder = new BASE64Encoder();
	        String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
	        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
	        return png_base64;
	}
}
