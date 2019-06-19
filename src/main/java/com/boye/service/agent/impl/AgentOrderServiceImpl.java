package com.boye.service.agent.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.OrderInfo;
import com.boye.dao.OrderDao;
import com.boye.dao.PassagewayDao;
import com.boye.service.agent.AgentOrderService;
import com.boye.service.impl.BaseServiceImpl;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class AgentOrderServiceImpl extends BaseServiceImpl implements AgentOrderService{
	
	@Resource
	private OrderDao orderDao;
	
	@Resource
	private PassagewayDao passagewayDao;
	
//	@Autowired
//	private PassagewayAccountDao passagewayAccountDao;
//	
//	@Autowired
//	private PaymentDao paymentDao;
//	
//	@Autowired
//	private ITaskService taskService;
//	
//	@Autowired
//	private IDictService dictService;
//	
//	@Autowired
//	private IProvideService provideService;
//	
//    @Autowired
//    private ServerConfigurer serverConf;
	
//	@Override
//	public OrderInfo getOrderState(String order_num,Long shop_id) {
//		//根据订单号查找订单
//		OrderInfo order = orderDao.getOrderByOrderNumber(shop_id,order_num);
//		order = checkOrderState(order);
//		return order;
//	}
	
	@Override
	public OrderInfo orderStateByUser(String order_num, Long shop_id) {
		//根据订单号查找订单
		OrderInfo order = orderDao.getOrderByOrderNumber(shop_id,order_num);
		//order = checkOrderState(order);
		return order;
	}
	
//	@Override
//	public OrderInfo getOrderState(String order_id) {
//		// 根据订单ID查找订单
//		OrderInfo order = orderDao.getObjectById(new OrderInfo(Long.parseLong(order_id)));
//		order = checkOrderState(order);
//		return order;
//	}

//	private OrderInfo checkOrderState(OrderInfo order) {
//		if(order == null)  return null;
//		PaymentAccount payment = paymentDao.getObjectById(new PaymentAccount(order.getPayment_id()));
//		if (payment == null) return null ;
//		//判断订单状态是否确认
//		if(order.getOrder_state() == 0){
//			//获取支付方式，同步第三方查询接口
//			PassagewayInfo passageway = passagewayDao.getObjectById(new PassagewayInfo(order.getPassageway_id()));
//			if(passageway == null) return null ;
//			ProvideInfo provide = provideService.getProvideByPassagewayCode(passageway.getPassageway_code());
//			if (provide == null) return null;
//			//判断属于哪个第三方
//			switch (provide.getProvide_code()) {
//			case "jhzf":
//				//建行订单查询
//				String url = dictService.getObjectByKeyAndType(payment.getAccount_number(), 1); 
//				if (url == null)  return null;
//				SearchPayBo bankPay = JhPayApi.queryBankAccount(CommonUtils.getBankRequestSn(), order, payment, url);
//				if(bankPay!=null){
//					order.setOrder_state(bankPay.getState().getCode()); 
//					if(bankPay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					} else if (bankPay.getState().getCode() == -2) {
//						orderDao.updateByPrimaryKey(order);
//					}
//				}
//				break;
//			case "h5tl":
//				//h5支付订单查询
//				H5QueryParamBean h5QueryParam = new H5QueryParamBean();
//				h5QueryParam.setAct(H5QueryParamBean.order_act);
//				h5QueryParam.setPid(payment.getAccount_number());
//				h5QueryParam.setOut_trade_no(order.getPlatform_order_number());
//				SearchPayBo h5Pay = H5PayApi.queryPayInfo(h5QueryParam);
//				if(h5Pay!=null){
//					order.setOrder_state(h5Pay.getState().getCode());
//					if(h5Pay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			case "ytcpu":
//				//YTCPU订单查询
//				YtcpuQueryParamBean queryParam = new YtcpuQueryParamBean();
//				queryParam.setOut_order_no(order.getPlatform_order_number());
//				queryParam.setPartner_id(payment.getAccount_number());
//				queryParam.setKey(payment.getAccount_key());
//				queryParam.setSign(queryParam.generateSign());
//				SearchPayBo ytcpuPay = YtcpuPayApi.queryPayInfo(queryParam);
//				if(ytcpuPay!=null){
//					order.setOrder_state(ytcpuPay.getState().getCode());
//					System.out.println(ytcpuPay.getState().getCode());
//					if(ytcpuPay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			case "hhlquick":
//			case "hhlwy":
//			case "hhlurl":
//			case "hhlh5":
//				List<PassagewayAccount> paList  = passagewayAccountDao.getAccountByPlatformOrderNumber(order.getPlatform_order_number());
//				if (paList.size() == 0) break;
//				PassagewayAccount pa = paList.get(0);
//				HhlQueryParamBean hhlQueryParam = new HhlQueryParamBean();
//				hhlQueryParam.setOut_trade_no(pa.getPassageway_order_number());
//				hhlQueryParam.setMerchant_open_id(payment.getAccount_number());
//				hhlQueryParam.setTimestamp((new Date().getTime()) + "");
//				hhlQueryParam.setKey(payment.getAccount_key());
//				hhlQueryParam.setSign(hhlQueryParam.generateSign());
//				SearchPayBo hhlPay = HhlPayApi.queryPayInfo(hhlQueryParam);
//				if(hhlPay!=null){
//					order.setOrder_state(hhlPay.getState().getCode());
//					System.out.println(hhlPay.getState().getCode());
//					if(hhlPay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			case "kltwg":
//				KltQueryParamBean kltQueryParam = new KltQueryParamBean();
//				kltQueryParam.setOrderNo(order.getPlatform_order_number());
//				kltQueryParam.setVersion("18");
//				kltQueryParam.setSignType("1");
//				kltQueryParam.setMerchantId(payment.getAccount_number());
//				kltQueryParam.setKey(payment.getAccount_key());
//				kltQueryParam.setSign(kltQueryParam.generateSign());
//				SearchPayBo kltwgPay = KltWGPayApi.queryPayInfo(kltQueryParam);
//				if(kltwgPay!=null){
//					order.setOrder_state(kltwgPay.getState().getCode());
//					System.out.println(kltwgPay.getState().getCode());
//					if(kltwgPay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			case "ysh5":
//				YsH5QueryParamBean ysh5QueryParam = new YsH5QueryParamBean();
//				ysh5QueryParam.setMch_id(payment.getAccount_number());
//				ysh5QueryParam.setOrder_no("");
//				ysh5QueryParam.setOut_order_no(order.getPlatform_order_number());
//				ysh5QueryParam.setSign_type("MD5");
//				ysh5QueryParam.setTransaction_id("");
//				ysh5QueryParam.setKey(payment.getAccount_key());
//				ysh5QueryParam.setSignature(ysh5QueryParam.generateSign());
//				SearchPayBo ysh5Pay = YsH5PayApi.queryPayInfo(ysh5QueryParam);
//				if(ysh5Pay!=null){
//					order.setOrder_state(ysh5Pay.getState().getCode());
//					System.out.println(ysh5Pay.getState().getCode());
//					if(ysh5Pay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			case "newquickpay":
//				NewQuickQueryParamBean newQuickQueryParam = new NewQuickQueryParamBean();
//				newQuickQueryParam.setCustId(payment.getAccount_number());
//				newQuickQueryParam.setCustOrderNo(order.getPlatform_order_number());
//				newQuickQueryParam.setOrgNo(payment.getCounter_number());
//				newQuickQueryParam.setVersion("2.1");
//				newQuickQueryParam.setKey(payment.getAccount_key());
//				newQuickQueryParam.setSign(newQuickQueryParam.generateSign());
//				SearchPayBo newQuickPay = NewQuickPayApi.queryPayInfo(newQuickQueryParam);
//				if(newQuickPay!=null){
//					order.setOrder_state(newQuickPay.getState().getCode());
//					System.out.println(newQuickPay.getState().getCode());
//					if(newQuickPay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			case "amwy":
//				AMWYQueryParamBean amwyQueryParam = new AMWYQueryParamBean();
//				amwyQueryParam.setData(new AMWYQueryParamBean.DataContent());
//				amwyQueryParam.setHead(new AMWYQueryParamBean.HeadContent());
////				amwyQueryParam.getHead().setFrontURL(passagewayInfo.getReturn_url());
//				amwyQueryParam.getHead().setMchid(payment.getAccount_number());
//				amwyQueryParam.getHead().setChannel(passageway.getPay_type());
////				payParam.getHead().setProxyMchid("177800000004412");
//				String code = UUID.randomUUID().toString().replaceAll("-", "");
//				amwyQueryParam.getHead().setReqNo(code);
//				amwyQueryParam.getHead().setReqType("query_pay_request");
//				amwyQueryParam.getHead().setAppId("DEFAULT");
//				amwyQueryParam.getHead().setSignType("RSA1");
//				amwyQueryParam.getHead().setVersion("1.0");
//				amwyQueryParam.getData().setOri_seq(order.getPlatform_order_number());
//				amwyQueryParam.getHead().setSign(amwyQueryParam.generateSign(serverConf.getAmwyPrivateKey()));
//				SearchPayBo amwyPay = AMWYPayApi.queryPayInfo(amwyQueryParam);
//				if(amwyPay!=null){
//					order.setOrder_state(amwyPay.getState().getCode());
//					System.out.println(amwyPay.getState().getCode());
//					if(amwyPay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			case "pgyer":
//				PgyerQueryParamBean pgyerQueryParam = new PgyerQueryParamBean();
//				pgyerQueryParam.setMerchant_no(payment.getAccount_number());
//				pgyerQueryParam.setOrder_no(order.getPlatform_order_number());
//				pgyerQueryParam.setService("orderquery");
//				pgyerQueryParam.setKey(payment.getAccount_key());
//				pgyerQueryParam.setSign(pgyerQueryParam.generateSign());
//				SearchPayBo pgyerPay = PgyerPayApi.queryPayInfo(pgyerQueryParam);
//				if(pgyerPay!=null){
//					order.setOrder_state(pgyerPay.getState().getCode());
//					System.out.println(pgyerPay.getState().getCode());
//					if(pgyerPay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			case "facaipay":
//				FaCaiPayQueryParamBean facaiQueryParam = new FaCaiPayQueryParamBean();
//				facaiQueryParam.setOrderid(order.getPlatform_order_number());
//				facaiQueryParam.setMerchant(payment.getAccount_number());
//				facaiQueryParam.setKey(payment.getAccount_key());
//				facaiQueryParam.setSign(facaiQueryParam.generateSign());
//				SearchPayBo facaiPay = FaCaiPayApi.queryPayInfo(facaiQueryParam);
//				if(facaiPay!=null){
//					order.setOrder_state(facaiPay.getState().getCode());
//					System.out.println(facaiPay.getState().getCode());
//					if(facaiPay.getState().getCode() == 1) {
//						taskService.sendTaskByQuery(order.getPlatform_order_number());
//					}
//				}
//				break;
//			default:
//				break;
//			}
//		}
//		return orderDao.getObjectById(order);
//	}

	
}
