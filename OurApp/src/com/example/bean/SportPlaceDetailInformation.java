package com.example.bean;

public class SportPlaceDetailInformation {

	//sportplace_typeId
	private static int type_紧张激烈 = 1001;
	private static int type_安静闲适 = 1002;
	private static int type_年轻专属 = 1003;
	private static int type_强身健体 = 1004;
	private static int type_野外探索 = 1005;
	//sportplace_sort
	private static int sort_篮球 = 101;
	private static int sort_跑步 = 102;
	private static int sort_羽毛球 = 103;
	private static int sort_足球 = 104;
	private static int sort_游泳 = 105;
	private static int sort_健身房 = 106;
	private static int sort_乒乓球 = 107;
	private static int sort_公园 = 108;
	private static int sort_排球 = 109;
	private static int sort_爬山 = 110;
	private static int sort_骑行 = 111;
	private static int sort_其他 = 112;
	
	//场地id
	private int sportplaceId;
	//场地名字
	private String sportplace_name;
	//场地所在区域
	private String sportplace_location;
	//场地详细地址
	private String sportplace_adress;
	//场地所属的运动类型
	private int sportplace_typeId;
	//场地评分
	private String sportplace_value;
	//场地简单的描述介绍
	private String sportplace_discrb;
	//场地类的Id
	private int sportplace_sort;
	//场地里定位点的距离
	private String sportplace_distance;
	//场地离定位点最近的公交路线
	private String bus_route;
	//城市ID
	private int city_id;
	/*
	 * 添加的字段(去掉了一个ImagetURl0)
	 * */
	//多张详情图片，至少一个，最多  ？
	private String[] moreImageUrl;
	//开放时间
	private String openTime;
	//适合的人群
	private String peopleLimit;
	//交友指数
	private double makeFriendIndex;
	//设备完整度
	private double deivceRequest;
	//周围店铺情况,饮料
	private String []surrondingShop;
	//用户的评论
	private SportPlaceComment[] SPcomment;
	public int getSportplaceId() {
		return sportplaceId;
	}
	public void setSportplaceId(int sportplaceId) {
		this.sportplaceId = sportplaceId;
	}
	public String getSportplace_name() {
		return sportplace_name;
	}
	public void setSportplace_name(String sportplace_name) {
		this.sportplace_name = sportplace_name;
	}
	public String getSportplace_location() {
		return sportplace_location;
	}
	public void setSportplace_location(String sportplace_location) {
		this.sportplace_location = sportplace_location;
	}
	public String getSportplace_adress() {
		return sportplace_adress;
	}
	public void setSportplace_adress(String sportplace_adress) {
		this.sportplace_adress = sportplace_adress;
	}
	public int getSportplace_typeId() {
		return sportplace_typeId;
	}
	public void setSportplace_typeId(int sportplace_typeId) {
		this.sportplace_typeId = sportplace_typeId;
	}
	public String getSportplace_value() {
		return sportplace_value;
	}
	public void setSportplace_value(String sportplace_value) {
		this.sportplace_value = sportplace_value;
	}
	public String getSportplace_discrb() {
		return sportplace_discrb;
	}
	public void setSportplace_discrb(String sportplace_discrb) {
		this.sportplace_discrb = sportplace_discrb;
	}
	public int getSportplace_sort() {
		return sportplace_sort;
	}
	public void setSportplace_sort(int sportplace_sort) {
		this.sportplace_sort = sportplace_sort;
	}
	public String getSportplace_distance() {
		return sportplace_distance;
	}
	public void setSportplace_distance(String sportplace_distance) {
		this.sportplace_distance = sportplace_distance;
	}
	public String getBus_route() {
		return bus_route;
	}
	public void setBus_route(String bus_route) {
		this.bus_route = bus_route;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	public String[] getMoreImageUrl() {
		return moreImageUrl;
	}
	public void setMoreImageUrl(String[] moreImageUrl) {
		this.moreImageUrl = moreImageUrl;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getPeopleLimit() {
		return peopleLimit;
	}
	public void setPeopleLimit(String peopleLimit) {
		this.peopleLimit = peopleLimit;
	}
	public double getMakeFriendIndex() {
		return makeFriendIndex;
	}
	public void setMakeFriendIndex(double makeFriendIndex) {
		this.makeFriendIndex = makeFriendIndex;
	}
	public double getDeivceRequest() {
		return deivceRequest;
	}
	public void setDeivceRequest(double deivceRequest) {
		this.deivceRequest = deivceRequest;
	}
	public String[] getSurrondingShop() {
		return surrondingShop;
	}
	public void setSurrondingShop(String[] surrondingShop) {
		this.surrondingShop = surrondingShop;
	}
	public SportPlaceComment[] getSPcomment() {
		return SPcomment;
	}
	public void setSPcomment(SportPlaceComment[] sPcomment) {
		SPcomment = sPcomment;
	}
	
	
	
}
