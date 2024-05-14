package hd.produce.security.cn.data;

import hd.utils.cn.StringUtils;

import java.io.Serializable;

import org.ksoap2.serialization.SoapObject;

public class LinkInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String code;
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.name = StringUtils.getStrProperty(soapObject, "typename");
        this.code = StringUtils.getStrProperty(soapObject, "typecode");
    }


}
