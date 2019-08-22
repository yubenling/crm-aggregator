package com.kycrm.member.component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.Serialization;

public class SerializationOptimizerImpl implements Serialization {

	@Override
	public byte getContentTypeId() {
		return 0;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
		return null;
	}

	@Override
	public ObjectInput deserialize(URL url, InputStream input) throws IOException {
		return null;
	}

}
