package com.thr.address.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * LocalData 和 ISO 8601互相转化的适配器
 * date的展现形式如 ‘2018-11-11’
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
