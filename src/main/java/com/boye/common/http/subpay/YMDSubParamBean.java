package com.boye.common.http.subpay;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.boye.common.utils.APISecurityUtils;
import com.boye.common.utils.MD5;
import com.boye.common.utils.RsaUtils;

@XmlRootElement(name = "yemadai")
@XmlAccessorType(XmlAccessType.FIELD)
public class YMDSubParamBean {
	
	private String accountNumber;
	
	private String notifyURL;
	
	private String tt;
	
	private String signType;
	
	private List<TransferList> transferList;
	
	public Map<String, Object> hasSignParamMap() {
		return null;
	}
	
	public String generateSign(String key) {
		if (transferList == null || transferList.size() == 0) return "";
		RsaUtils rsaUtils = RsaUtils.getInstance();
		StringBuffer sb = new StringBuffer();
		TransferList data = transferList.get(0);
		sb.append("transId=" + data.getTransId());
		sb.append("&accountNumber=" + accountNumber);
		sb.append("&cardNo=" + data.getCardNo());
		sb.append("&amount=" + data.getAmount());
		String signParam = null;
		System.out.println(sb.toString());
		try {
			signParam = rsaUtils.signData(sb.toString(), key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(signParam);
		return signParam;
	}

	public StringBuffer returnParamStr() {
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		
		StringBuffer sb = new StringBuffer();
//        Set<Entry<String, Object>> entrySet = paramMap.entrySet();
//        for (Map.Entry entry : entrySet) {
//            if (entry.getValue() != null)
//            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	public String getTt() {
		return tt;
	}

	public void setTt(String tt) {
		this.tt = tt;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public List<TransferList> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<TransferList> transferList) {
		this.transferList = transferList;
	}
	
	@XmlRootElement(name = "transferList")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TransferList {
		
		private String transId;
		private String bankCode;
		private String provice;
		private String city;
		private String branchName;
		private String accountName;
		private String idNo;
		private String phone;
		private String cardNo;
		private String amount;
		private String remark;
		private String secureCode;
		public String getTransId() {
			return transId;
		}
		public void setTransId(String transId) {
			this.transId = transId;
		}
		public String getBankCode() {
			return bankCode;
		}
		public void setBankCode(String bankCode) {
			this.bankCode = bankCode;
		}
		public String getProvice() {
			return provice;
		}
		public void setProvice(String provice) {
			this.provice = provice;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getBranchName() {
			return branchName;
		}
		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}
		public String getAccountName() {
			return accountName;
		}
		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}
		public String getIdNo() {
			return idNo;
		}
		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getCardNo() {
			return cardNo;
		}
		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getSecureCode() {
			return secureCode;
		}
		public void setSecureCode(String secureCode) {
			this.secureCode = secureCode;
		}
		
	}
}
