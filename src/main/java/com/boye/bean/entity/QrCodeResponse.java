package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.entity.BaseEntity;

public class QrCodeResponse extends BaseEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8377343987558096881L;
	
	
	private String amount;// 金额
    private String platOrderNo;// 系统订单号，后端自己生成唯一标识
    private String url;// 生成成功的二维码对应的url
    private int type;// 1为支付宝，2为微信
    
    
    public QrCodeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QrCodeResponse(String amount ,String platOrderNo,String url,int type){
        this.amount = amount;
        this.platOrderNo = platOrderNo;
        this.url = url;
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPlatOrderNo() {
        return platOrderNo;
    }

    public void setPlatOrderNo(String platOrderNo) {
        this.platOrderNo = platOrderNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "QrCodeResponse{" +
                "amount='" + amount + '\'' +
                ", platOrderNo='" + platOrderNo + '\'' +
                ", url='" + url + '\'' +
                ", type=" + type +
                '}';
    }
}
