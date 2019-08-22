package com.kycrm.tmc.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJSONUtil {

	public static SerializerFeature[] seriFeature() {
		List<SerializerFeature> list = new ArrayList<SerializerFeature>();
		list.add(SerializerFeature.WriteMapNullValue);
		list.add(SerializerFeature.WriteNullBooleanAsFalse);
		list.add(SerializerFeature.WriteNullStringAsEmpty);
		list.add(SerializerFeature.WriteNullListAsEmpty);
		SerializerFeature[] a = new SerializerFeature[]{};
		SerializerFeature[] array = list.toArray(a);
		return array;
	}
}