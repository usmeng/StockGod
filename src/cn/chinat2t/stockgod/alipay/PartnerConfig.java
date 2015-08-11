﻿package cn.chinat2t.stockgod.alipay;

public class PartnerConfig {

	// 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
	public static final String PARTNER = "2088901492591181";
	// 商户收款的支付宝账号
	public static final String SELLER = "lhj@slddw.cn";
	// 商户（RSA）私钥
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKdETEjFViVexfT1UHZXQ6n4OIfiJ5rRs2Q4wFYv5R3wmQfI2TzREnajFJu6jmlCJCCoyxLWzDncmsd++6xwv7m1wazqq3J7S4HKkAlIwjnh3r5pMD6kesUT1SiHjT7onJiq4EgcrU5mf/YYvjEvPrXkBy36KgyWdsDW1vgpjNirAgMBAAECgYBhlyKe7qvlX9dFpX9NnHp5B78L4PJUjPcKiWYGoykdf8v5U8ETj7QjDeEvPvhqB1IG0BSzqCk9tS2FKdxy7tGc+hOjJX8KHHnoPxzvvZOyHS7Sk6J5XmSD1Qjkuf5KnJji5PTxkebkIFjTXA1hP8wvXo1FG2pE12ViQBJQa39hsQJBANTVc6eqUzj0j0F/kkehb8QxoHh2T1NqDRM9IPH39owki3koOeLHCQDpClaDeINjhbkigb75iZ4UKWtsTk7gKLkCQQDJMPXr8mpmvZ5Po6uyr39RuWkmXfN2OUvRxudNJh0IBvYoUD6nG017n5YaQ8qLxvKigN1eWX2RzuZ3JjyGUxKDAkA76Rn1SGzEy5LHzTFQGJFtmMqjHiqWQHQNxldPxY0RRfg80qX4wC44o2DVmYyC77DkvGHpcDTc2rAOHR6t9UuBAkBHVC9sFob4o/NzosBl2miWTdjkvFxrpsaluhIZPRD/3o/5HPIJhtmCocKyzyQJSGCPkoZpQ+sL/cGhrpG4wc0HAkArfvxZ82/qqYgz+JM9X5aHRGtkWh78sgvGpaDNiMq/fMDA5+vKrcEqFDVafBRDkElXbx7+arCJiQY+4dHyxVag";
	// 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
	public static final String RSA_ALIPAY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC52Sk/BZ88vBUK5SF1F/B/im8YI5Rf82Jb9whyKOh5mkeoS5eXdIhrIeoK314rf2od+MlCqlA2SCJCfF+sJnL84b7uPgYArezmnnMGG6IBTcwOKaF6OoWNzvcDZ0TDXTiVEYJwLlScFgPUCOJH5gMo22YXdmrai4jqo8Ar9wxmQQIDAQAB";
	// 支付宝安全支付服务apk的名称，必须与assets目录下的apk名称一致
	public static final String ALIPAY_PLUGIN_NAME = "alipay_plugin_20120428msp.apk";

}