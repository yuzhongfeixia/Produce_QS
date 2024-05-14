package hd.produce.security.cn.data;

import java.util.ArrayList;

public class CityInfo {
	private String cityName;
	private String cityCode;
	private ArrayList<CountyInfo> countInfos;
	
	public void setCityName(String cityName){
		this.cityName = cityName;
	}
	public String getCityName(){
		return cityName;
	}
	public void setCityCode(String cityCode){
		this.cityCode = cityCode;
	}
	public String getCityCode(){
		return cityCode;
	}
	public void setCountyList(ArrayList<CountyInfo> countInfos){
		this.countInfos = countInfos;
	}
	public ArrayList<CountyInfo> getCountyList(){
		return countInfos;
	}
}
