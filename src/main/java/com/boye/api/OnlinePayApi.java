package com.boye.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.HhlResultBean;
import com.boye.common.http.pay.OnlineResultBean;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlinePayApi {
    private static Logger logger = LoggerFactory.getLogger(OnlinePayApi.class);

    public static String merchNo = "M00000103";
    public static String merchSignKey = "EDAF37ED58CA48E382FCB3BCE635446A";

    //需替换为可用的IP地址
    public static String serverUrl = "http://47.75.178.180";

//    public static String notifyUrl = "http://www.xxx.com/app/returnSuccess";

    //获取h5支付链接
    public static Map<String, Object> aliH5Order(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> map = new HashMap<String, String>();
        JSONObject json = new JSONObject();

        map.put("merchNo", usePayment.getAccount_number());
        map.put("notifyUrl", passagewayInfo.getNotify_url());
        map.put("orderNo", authentication.getPlatform_order_number());
        map.put("transAmount", new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()); //金额以分为单位，同时以字符串格式上送，所有接口对金额字段都这样处理
        map.put("productName", authentication.getOrder_number()); //商品名称
        map.put("deviceIp", "221.222.129.27");

        json.put("merchNo", usePayment.getAccount_number());
        json.put("notifyUrl",  passagewayInfo.getNotify_url());
        json.put("orderNo", authentication.getPlatform_order_number());
        json.put("transAmount", new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()); //金额以分为单位，同时以字符串格式上送，所有接口对金额字段都这样处理
        json.put("productName", authentication.getOrder_number()); //商品名称
        json.put("deviceIp", "221.222.129.27");

        String requestSrc =  genSortedLinkedString(map);

        String signkey = usePayment.getAccount_key();

        String sign = DigestUtils.md5Hex(requestSrc + signkey);
        json.put("sign", sign);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        client.newBuilder();
        RequestBody body = RequestBody.create(JSON, json.toJSONString());

        //不同交易类型的接口地址不同，请调用正确的接口地址
        // 注：pay.online.com 需替换成可用IP
        String interfaceAddress = serverUrl + "/app/doALIH5Pay.do";
        Request request = new Request.Builder().url(interfaceAddress).post(body).build();

        Response response;
        String jsonResponse = "";
        OnlineResultBean qrurl = null;
        Gson gson = new Gson();
        try {
            response = client.newCall(request).execute();

            jsonResponse = response.body().string();
            qrurl = (OnlineResultBean) gson.fromJson(jsonResponse, OnlineResultBean.class);
            if(qrurl.getRespCode().equals("00000")){
                result.put("code", 1);
                result.put("data", qrurl.getQrcodeUrl());
            }else {
                result.put("code", 2);
                result.put("data", "");
            }
            result.put("passageway_order_number", qrurl.getOrderNo());
            result.put("msg", qrurl.getRespDesc());

        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 2);
            result.put("msg", "远程服务响应异常");
            result.put("data", "");
        }

//        System.out.println("Server JsonResponse: " + jsonResponse);

        //根据Server端的应答结果做相应处理:
        return result;
    }
    public static void orderQuery() {

        Map<String, String> map = new HashMap<String, String>();
        JSONObject json = new JSONObject();

        map.put("merchNo", merchNo);
        map.put("requestId", "20180422151115310010005");

        json.put("merchNo", merchNo);
        json.put("requestId", "20180422151115310010005");

        String requestSrc = genSortedLinkedString(map);

        String signkey = merchSignKey;

        String sign = DigestUtils.md5Hex(requestSrc + signkey);
        json.put("sign", sign);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        client.newBuilder();
        RequestBody body = RequestBody.create(JSON, json.toJSONString());

        //不同交易类型的接口地址不同，请调用正确的接口地址
        // 注：pay.online.com 需替换成可用IP
        String interfaceAddress = serverUrl + "/app/doOrderQuery.do";
        Request request = new Request.Builder().url(interfaceAddress).post(body).build();

        Response response;
        String jsonResponse = "";
        try {
            response = client.newCall(request).execute();
            jsonResponse = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server JsonResponse: " + jsonResponse);

        //根据Server端的应答结果做相应处理:

    }

    public static String genSortedLinkedString (Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);

        String sortedstr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);
            if (i == keys.size() - 1) {
                sortedstr = sortedstr + key + "=" + value; // 拼接时，最后一个末尾不包括&字符
            } else {
                sortedstr = sortedstr + key + "=" + value + "&";
            }
        }
        return sortedstr;
    }
}
