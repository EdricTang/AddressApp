package com.thr.address.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * LocalData �� ISO 8601����ת����������
 * date��չ����ʽ�� ��2018-11-11��
 */
public class LocalDataAdapter extends XmlAdapter<String, LocalDate>{

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return DateUtil.parse(v);
        //return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return DateUtil.format(v);
        //return v.toString();
    }
}
