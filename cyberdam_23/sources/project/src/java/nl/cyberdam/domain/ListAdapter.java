package nl.cyberdam.domain;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ListAdapter  extends XmlAdapter<Object[],List<?>> {
	public ListAdapter() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object[] marshal(List<?> v) throws Exception {
		return v.toArray();
	}
	@Override
	public List<?> unmarshal(Object[] v) throws Exception {
		return Arrays.asList(v);
	}
}
