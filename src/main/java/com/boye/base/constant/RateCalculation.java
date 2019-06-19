package com.boye.base.constant;

import java.math.BigDecimal;
import com.boye.bean.entity.ShopConfig;

public class RateCalculation {
	
	private BigDecimal order_money;
	
	private BigDecimal shop_money;
	
	private BigDecimal agent_money;
	
	private BigDecimal sum_money;
	
	private BigDecimal service_charge;
	
	private BigDecimal platform_money;
	
	private ShopConfig shopConfig;
	
	private int calculation_type = 1;
	
	private boolean hasAgent = false;

	private void calculation() {
		if (order_money == null || shopConfig == null) return;
		if (hasAgent) {
			calculationPlatformRateAndAgent();
		} else {
			calculationPlatformRate();
		}
	}

	private void calculationPlatformRateAndAgent() {
		if (calculation_type == 1) {
			sum_money = order_money.multiply(new BigDecimal(shopConfig.sumRate()));
			platform_money = order_money.multiply(new BigDecimal(shopConfig.platformRate()));
			shop_money = order_money.subtract(sum_money);
			agent_money = sum_money.subtract(platform_money);
			service_charge = BigDecimal.ZERO;
		} 
//		else if (calculation_type == 2) {
//			sum_money = order_money.multiply(new BigDecimal(shopConfig.sumRate()));
//			platform_money = order_money.multiply(new BigDecimal(shopConfig.platformRate()));
//			shop_money = order_money.subtract(BigDecimal.ZERO);
//			agent_money = sum_money.subtract(platform_money);
//			service_charge = sum_money.add(BigDecimal.ZERO);
//		}
		
	}

	private void calculationPlatformRate() {
		if (calculation_type == 1) {
			platform_money = order_money.multiply(new BigDecimal(shopConfig.getPay_rate() / 100f));
			shop_money = order_money.subtract(platform_money);
			sum_money = order_money.subtract(shop_money);
			agent_money = BigDecimal.ZERO;
			service_charge = BigDecimal.ZERO;
		} 
//		else if (calculation_type == 2) {
//			platform_money = order_money.multiply(new BigDecimal(shopConfig.getPay_rate() / 100f));
//			agent_money = BigDecimal.ZERO;
//			shop_money = order_money.subtract(BigDecimal.ZERO);
//			sum_money = platform_money.add(agent_money);
//			service_charge = sum_money.add(BigDecimal.ZERO);
//			
//		}
		
	}

	public BigDecimal getSum_money() {
		return sum_money;
	}

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public int getCalculation_type() {
		return calculation_type;
	}

	public void setCalculation_type(int calculation_type) {
		this.calculation_type = calculation_type;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money;
		calculation();
	}

	public BigDecimal getService_charge() {
		return service_charge;
	}

	public void setService_charge(BigDecimal service_charge) {
		this.service_charge = service_charge;
	}

	public ShopConfig getShopConfig() {
		return shopConfig;
	}

	public void setShopConfig(ShopConfig shopConfig) {
		this.shopConfig = shopConfig;
		calculation();
	}

	public boolean isHasAgent() {
		return hasAgent;
	}

	public void setHasAgent(boolean hasAgent) {
		this.hasAgent = hasAgent;
		calculation();
	}

	public BigDecimal getShop_money() {
		return shop_money;
	}

	public BigDecimal getAgent_money() {
		return agent_money;
	}

	public BigDecimal getPlatform_money() {
		return platform_money;
	}
}
