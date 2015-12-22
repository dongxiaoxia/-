package com.miracle.mby.utils;

import com.miracle.mby.account.service.UserService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 短讯、邮件等消息通知模版
 *
 * @author dongxiaoxia
 * @create 2015-12-22 11:19
 */
public class MessageTemplateUtils {

    private static final Configuration cfg;
    private static final StringTemplateLoader stringLoader;
    private static Properties messageTemplateConfig;

    private static final Logger logger = LoggerFactory.getLogger(MessageTemplateUtils.class);

    static {
        cfg = new Configuration();
        stringLoader = new StringTemplateLoader();
        messageTemplateConfig = new Properties();
        Reader reader = null;
        try {
            InputStream stream = MessageTemplateUtils.class.getResourceAsStream("/messageTemplate.properties");
            reader = new InputStreamReader(stream, "UTF-8");
            messageTemplateConfig.load(reader);
            Enumeration<?> enu = getProperties().propertyNames();
            while (enu.hasMoreElements()) {
                Object key = enu.nextElement();
                stringLoader.putTemplate((String) key, getProperties().getProperty((String) key));
            }
        } catch (Exception io) {
            logger.error(io.getMessage(), io);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        cfg.setTemplateLoader(stringLoader);
    }

    private static Properties getProperties() {
        if (messageTemplateConfig == null) {
            try {
                Properties p = new Properties();
                p.load(UserService.class.getResourceAsStream("/messageTemplate.properties"));
                messageTemplateConfig = p;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messageTemplateConfig;
    }

    private static Template getTemplate(String templateKey) throws IOException {
        return cfg.getTemplate(templateKey, "utf-8");
    }

    private static String getTemplateString(Map extraData, String key) {

        if (extraData != null) {
            Enumeration<?> enu = getProperties().propertyNames();
            while (enu.hasMoreElements()) {
                Object propertyKey = enu.nextElement();
                extraData.put(propertyKey, getProperties().getProperty((String) propertyKey));
            }
        }

        StringWriter sw = new StringWriter();
        try {
            getTemplate(key).process(extraData, sw);
        } catch (TemplateException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return sw.toString();
    }

    public static String getUserRegisterTemplate(String userName, String companyName, String loginId, String password) {
        Map map = new HashMap();
        map.put("userName", userName);
        map.put("companyName", companyName);
        map.put("loginId", loginId);
        map.put("password", password);
        return getTemplateString(map, "user_register");
    }

    public static String getCompanyRegisterTemplate(String userName, String loginId, String password) {
        Map map = new HashMap();
        map.put("userName", userName);
        map.put("loginId", loginId);
        map.put("password", password);
        return getTemplateString(map, "company_register");
    }

    public static String getSubCompanyCreateTemplate(String userName, String companyName, String loginId, String password) {
        Map map = new HashMap();
        map.put("userName", userName);
        map.put("companyName", companyName);
        map.put("loginId", loginId);
        map.put("password", password);
        return getTemplateString(map, "sub_company_create");
    }

    public static String getAddAdminTemplate(String userName, String companyName) {
        Map map = new HashMap();
        map.put("userName", userName);
        map.put("companyName", companyName);
        return getTemplateString(map, "add_admin");
    }

    public static void main(String[] args) throws IOException, TemplateException {
        System.out.println(MessageTemplateUtils.getUserRegisterTemplate("陈文东", "***公司", "1231312", "4234234"));
        System.out.println(MessageTemplateUtils.getSubCompanyCreateTemplate("陈文东", "***公司", "1231312", "4234234"));
        System.out.println(MessageTemplateUtils.getCompanyRegisterTemplate("陈文东", "1231312", "4234234"));
        System.out.println(MessageTemplateUtils.getAddAdminTemplate("陈文东", "***该in公司"));
    }
}


配置文件：
#短讯、邮件等消息通知模版配置

#用户注册
user_register=尊敬的${userName}用户您好，${companyName}在“目标易”将您设为企业成员，您已成功注册并激活目标易帐号，登录帐号：${loginId}，密码：${password}，请至http://www.mubiaoyi.com或关注“目标易”微信公众号，下载安装目标易客户端。一款集工作管理与企业社交的APP,欢迎您的使用！【目标易】


#企业注册
company_register=尊敬的${userName}用户您好，感谢您注册“目标易”企业帐号。登录帐号：${loginId}，密码：${password}。请至http://www.mubiaoyi.com或关注“目标易”微信公众号，下载安装目标易客户端。一款集工作管理与企业社交的APP,欢迎您的使用！【目标易】

#成员单位
sub_company_create=尊敬的${userName}用户您好，${companyName}在“目标易”将您设为成员单位，您已成功注册并激活目标易帐号，登录帐号：${loginId}，密码：${password}，请至http://www.mubiaoyi.com或关注“目标易”微信公众号，下载安装目标易客户端。一款集工作管理与企业社交的APP,欢迎您的使用！【目标易】

#添加管理员
add_admin=尊敬的${userName}用户您好，您已被设为${companyName}的企业管理员，可登录目标易网站www.mubiaoyi.com查看并管理您的企业信息。请至http://www.mubiaoyi.com或关注“目标易”微信公众号，下载安装目标易客户端。一款集工作管理与企业社交的APP,欢迎您的使用！【目标易】

