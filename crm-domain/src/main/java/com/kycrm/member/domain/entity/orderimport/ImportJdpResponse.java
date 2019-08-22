package com.kycrm.member.domain.entity.orderimport;

import java.io.Serializable;
import java.util.List;

/**
 * 订单导入封装TbTrade
 */
public class ImportJdpResponse implements Serializable{
	private static final long serialVersionUID = 7459988191039296190L;
	
	private trade_fullinfo_get_response trade_fullinfo_get_response;
	
	public class trade_fullinfo_get_response{
		private trade trade;

		public trade getTrade() {
			return trade;
		}

		public void setTrade(trade trade) {
			this.trade = trade;
		}
		
	};
	/**
	 * trade
	 */
	public class trade{
		private String tid;
		private String seller_nick;
		private String buyer_nick;			
		private String buyer_alipay_no;        					
		private String post_fee;                 
		private String point_fee;                	
		private String total_fee;		
		private String buyer_obtain_point_fee; 
		private String payment;					
		private String real_point_fee;            
		private String status;				                 	
		private String receiver_name;		
		private String receiver_address;			
		private String receiver_mobile;	
		private String receiver_phone;
		private String created;					
		private String pay_time;					
		private String num;					
		private String title;	            
		private String end_time;					
		private String received_payment;			
		private String trade_from;		
		private String receiver_state;	
		private String receiver_city;
		private orders orders;
		
		public orders getOrders() {
			return orders;
		}
		public void setOrders(orders orders) {
			this.orders = orders;
		}
		public String getReceiver_phone() {
			return receiver_phone;
		}
		public void setReceiver_phone(String receiver_phone) {
			this.receiver_phone = receiver_phone;
		}
		public String getSeller_nick() {
			return seller_nick;
		}
		public void setSeller_nick(String seller_nick) {
			this.seller_nick = seller_nick;
		}
		public String getTid() {
			return tid;
		}
		public void setTid(String tid) {
			this.tid = tid;
		}
		public String getBuyer_nick() {
			return buyer_nick;
		}
		public void setBuyer_nick(String buyer_nick) {
			this.buyer_nick = buyer_nick;
		}
		public String getBuyer_alipay_no() {
			return buyer_alipay_no;
		}
		public void setBuyer_alipay_no(String buyer_alipay_no) {
			this.buyer_alipay_no = buyer_alipay_no;
		}
		public String getPost_fee() {
			return post_fee;
		}
		public void setPost_fee(String post_fee) {
			this.post_fee = post_fee;
		}
		public String getPoint_fee() {
			return point_fee;
		}
		public void setPoint_fee(String point_fee) {
			this.point_fee = point_fee;
		}
		public String getTotal_fee() {
			return total_fee;
		}
		public void setTotal_fee(String total_fee) {
			this.total_fee = total_fee;
		}
		public String getBuyer_obtain_point_fee() {
			return buyer_obtain_point_fee;
		}
		public void setBuyer_obtain_point_fee(String buyer_obtain_point_fee) {
			this.buyer_obtain_point_fee = buyer_obtain_point_fee;
		}
		public String getPayment() {
			return payment;
		}
		public void setPayment(String payment) {
			this.payment = payment;
		}
		public String getReal_point_fee() {
			return real_point_fee;
		}
		public void setReal_point_fee(String real_point_fee) {
			this.real_point_fee = real_point_fee;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getReceiver_name() {
			return receiver_name;
		}
		public void setReceiver_name(String receiver_name) {
			this.receiver_name = receiver_name;
		}
		public String getReceiver_address() {
			return receiver_address;
		}
		public void setReceiver_address(String receiver_address) {
			this.receiver_address = receiver_address;
		}
		public String getReceiver_mobile() {
			return receiver_mobile;
		}
		public void setReceiver_mobile(String receiver_mobile) {
			this.receiver_mobile = receiver_mobile;
		}
		public String getCreated() {
			return created;
		}
		public void setCreated(String created) {
			this.created = created;
		}
		public String getPay_time() {
			return pay_time;
		}
		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getEnd_time() {
			return end_time;
		}
		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
		public String getReceived_payment() {
			return received_payment;
		}
		public void setReceived_payment(String received_payment) {
			this.received_payment = received_payment;
		}
		public String getTrade_from() {
			return trade_from;
		}
		public void setTrade_from(String trade_from) {
			this.trade_from = trade_from;
		}
		public String getReceiver_state() {
			return receiver_state;
		}
		public void setReceiver_state(String receiver_state) {
			this.receiver_state = receiver_state;
		}
		public String getReceiver_city() {
			return receiver_city;
		}
		public void setReceiver_city(String receiver_city) {
			this.receiver_city = receiver_city;
		}
	}

	
	public class orders{
		private List<order> order;

		public List<order> getOrder() {
			return order;
		}

		public void setOrder(List<order> order) {
			this.order = order;
		}
	}
	
	/**
	 * order
	 */
	public class order {
		private String oid;
		private String price;
		private String order_from;
		private String payment;
		private String num;
		private String num_iid;
		private String title;
		private String status;
		private String total_fee;
		private String refund_status;
		
		public String getRefund_status() {
			return refund_status;
		}
		public void setRefund_status(String refund_status) {
			this.refund_status = refund_status;
		}
		public String getOid() {
			return oid;
		}
		public void setOid(String oid) {
			this.oid = oid;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getOrder_from() {
			return order_from;
		}
		public void setOrder_from(String order_from) {
			this.order_from = order_from;
		}
		public String getPayment() {
			return payment;
		}
		public void setPayment(String payment) {
			this.payment = payment;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getNum_iid() {
			return num_iid;
		}
		public void setNum_iid(String num_iid) {
			this.num_iid = num_iid;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getTotal_fee() {
			return total_fee;
		}
		public void setTotal_fee(String total_fee) {
			this.total_fee = total_fee;
		}
	}
	
	
	public trade newTrade(){
		trade trade = new trade();
		return trade;
	}
	
	public order newOrder(){
		order order = new order();
		return order;
	}
	
	public trade_fullinfo_get_response newTrade_fullinfo_get_response(){
		trade_fullinfo_get_response response = new trade_fullinfo_get_response();
		return response;
	}
	
	public orders newOrders(){
		orders orders = new orders();
		return orders;
	}

	public trade_fullinfo_get_response getTrade_fullinfo_get_response() {
		return trade_fullinfo_get_response;
	}

	public void setTrade_fullinfo_get_response(
			trade_fullinfo_get_response trade_fullinfo_get_response) {
		this.trade_fullinfo_get_response = trade_fullinfo_get_response;
	}
}
