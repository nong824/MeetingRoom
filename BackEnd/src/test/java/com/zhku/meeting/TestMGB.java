package com.zhku.meeting;

import com.zhku.meeting.entities.ApplicationRecord;
import com.zhku.meeting.mapper.ApplicationRecordMapper;
import com.zhku.meeting.mapper.DepartmentMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestMGB {

    @Test
    public void testMbg() throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //指定配置文件路径
        File configFile = new File("src/main/java/com/zhku/meeting/MGB.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }


    @Autowired
    private ApplicationRecordMapper applicationRecordMapper;

    @Test
    public void test() throws ParseException {
        long applyTime = new Date().getTime();
        System.out.println(applyTime);
        long l = System.currentTimeMillis();
        long e = new Date().getTime();
        System.out.println(e);
        System.out.println(l);
        Date time=new Date(l);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String times = sdf.format(time);
        System.out.println(time);

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String time1 = "2020-05-31";
        System.out.println( ft.parse(time1));
        Date d =ft.parse(time1);
        System.out.println(d);


//        ApplicationRecord applicationRecord = applicationRecordMapper.selectByPrimaryKey(1);
//        ApplicationRecord applicationRecord2 = applicationRecordMapper.selectByPrimaryKey(2);
//        Date date1 = applicationRecord.getApplyDate();
//        Date date2 = applicationRecord2.getApplyDate();
//        System.out.println(date1);
//        System.out.println(date2);
//        System.out.println(date1.getTime()>date2.getTime());

    }

}
