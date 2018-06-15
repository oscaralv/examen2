package mx.unam.dgpe.sample.controller;

import org.apache.log4j.Logger;
import org.junit.Test;

import io.vertx.core.AbstractVerticle;
import static mx.unam.dgpe.sample.controller.RestUtil.*;

public class TestMyController extends AbstractVerticle {
    private static final Logger logger = Logger.getLogger(TestMyController.class);
    
//    @Test
    public void ok() throws Exception {
//        String result = sendGet("https://www.binance.com/api/v3/ticker/price?symbol=BTCUSDT");
	String result = sendGet("http://192.168.10.129:5050/api/calculadora?operacion=suma&valor1=10&valor2=20");
	logger.info(result);
	result = sendGet("http://192.168.10.129:5050/api/calculadora?operacion=resta&valor1=60&valor2=25");
	logger.info(result);
	result = sendGet("http://192.168.10.129:5050/api/calculadora?operacion=multiplica&valor1=50&valor2=30");
	logger.info(result);
	result = sendGet("http://192.168.10.129:5050/api/calculadora?operacion=divide&valor1=1000&valor2=100");
	logger.info(result);
    }

}
