package com.example.bean;

public class SportPlaceDetailInformation {

	//sportplace_typeId
	private static int type_���ż��� = 1001;
	private static int type_�������� = 1002;
	private static int type_����ר�� = 1003;
	private static int type_ǿ���� = 1004;
	private static int type_Ұ��̽�� = 1005;
	//sportplace_sort
	private static int sort_���� = 101;
	private static int sort_�ܲ� = 102;
	private static int sort_��ë�� = 103;
	private static int sort_���� = 104;
	private static int sort_��Ӿ = 105;
	private static int sort_���� = 106;
	private static int sort_ƹ���� = 107;
	private static int sort_��԰ = 108;
	private static int sort_���� = 109;
	private static int sort_��ɽ = 110;
	private static int sort_���� = 111;
	private static int sort_���� = 112;
	
	//����id
	private int sportplaceId;
	//��������
	private String sportplace_name;
	//������������
	private String sportplace_location;
	//������ϸ��ַ
	private String sportplace_adress;
	//�����������˶�����
	private int sportplace_typeId;
	//��������
	private String sportplace_value;
	//���ؼ򵥵���������
	private String sportplace_discrb;
	//�������Id
	private int sportplace_sort;
	//�����ﶨλ��ľ���
	private String sportplace_distance;
	//�����붨λ������Ĺ���·��
	private String bus_route;
	//����ID
	private int city_id;
	/*
	 * ��ӵ��ֶ�(ȥ����һ��ImagetURl0)
	 * */
	//��������ͼƬ������һ�������  ��
	private String[] moreImageUrl;
	//����ʱ��
	private String openTime;
	//�ʺϵ���Ⱥ
	private String peopleLimit;
	//����ָ��
	private double makeFriendIndex;
	//�豸������
	private double deivceRequest;
	//��Χ�������,����
	private String []surrondingShop;
	//�û�������
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
