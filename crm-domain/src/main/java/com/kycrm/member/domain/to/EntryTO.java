package com.kycrm.member.domain.to;

import java.io.Serializable;

/** 
* @ClassName: EntryTO 
* @Description 自定义entry数据传输对象,用于部分业务逻辑的数据传输<br/>
* hashCode被重写,只使用了value作为比较的字段,慎重使用!!
* 使用场景:<br/>
* 1:订单抽取会员;key-uid,value-List<TradeDTO><br/>
* 2:会员短信群发;key-buyerNick,value-mobile<br/>
* @author jackstraw_yu
* @date 2018年1月31日 上午10:33:13 
* 
* @param <K>
* @param <V> 
*/
public class EntryTO<K,V> implements Serializable{

	private static final long serialVersionUID = 3038727982600017911L;
	
	private K key;
	
	private V value;

	public EntryTO() {
		super();
	}

	public EntryTO(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntryTO<?, ?> other = (EntryTO<?, ?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
