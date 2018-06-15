package mx.unam.dgpe.sample.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.math.BigInteger;

public class MyController extends AbstractVerticle {
    private static final Logger logger = Logger.getLogger(MyController.class);
    private static String pba="";
    
    public void start(Future<Void> fut) {
        logger.info("Inicializando Vertical");
        Router router = Router.router(vertx);
        //router.route("/*").handler(StaticHandler.create("assets")); // para invocar asi: http://localhost:8080/index.html
        // el directorio "upload-folder" será creado en la misma ubicación que el jar fue ejecutado
        router.route().handler(BodyHandler.create().setUploadsDirectory("upload-folder"));
        router.get("/api/primero").handler(this::primero);
        router.post("/api/segundo").handler(this::segundo);
	router.get("/api/calculadora").handler(this::calculadora);
	router.get("/api/factorial").handler(this::factorial);
        
        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx.createHttpServer().requestHandler(router::accept).listen(
                config().getInteger("http.port", 7070), result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });        
        pba=System.getenv("PBA");
        logger.info("Vertical iniciada !!!" + pba);
    }  
    private void primero(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        String mode = request.getParam("mode");
        String jsonResponse = procesa(mode);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }
    
    private void segundo(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        String decoded = routingContext.getBodyAsString();
        String jsonResponse = procesa(decoded);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }

    private void calculadora(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        String operacion = request.getParam("operacion");
        int valor1 = Integer.parseInt(request.getParam("valor1"));
        int valor2 = Integer.parseInt(request.getParam("valor2"));
        String jsonResponse = calcula(operacion,valor1,valor2,request);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }

    private void factorial(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        int numero = Integer.parseInt(request.getParam("numero"));
        String jsonResponse = devuelveFactorial(numero,request);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }
   
    private String devuelveFactorial(int n, HttpServerRequest request){
	BigInteger resultado = calculaFactorial(n);
	//int digitos = String.valueOf(resultado).length();

	Map<Object, Object> info = new HashMap<>();
        info.put("resultado", resultado);
        info.put("nombre", "oscar");
	info.put("digitos",resultado.bitCount());

	info.put("Current Node IP", request.localAddress().host());
	info.put("Caller IP", request.remoteAddress().host());
	info.put("Absolute url", request.absoluteURI());
	info.put("path", request.path());
	info.put("query", request.query());
	info.put("uri", request.uri());
	return Json.encodePrettily(info);
    }

    private BigInteger calculaFactorial(int n) {
   	BigInteger fact = BigInteger.valueOf(1);
    	for (int i = 2; i <= n; i++){
        	fact = fact.multiply(BigInteger.valueOf(i));
	}
	return fact;
    }

    private String procesa(String decoded) {
        Map<String, String> autos = new HashMap<>();
        autos.put("primero", "Ferrari");
        autos.put("segundo", "Lamborgini");
        autos.put("tercero", "Bugatti");
        
        Map<Object, Object> info = new HashMap<>();
        info.put("decoded", decoded);
        info.put("nombre", "gustavo");
        info.put("edad", "21");
        info.put("autos", autos);
	info.put("variable",pba);
        return Json.encodePrettily(info);
    }

    private String calcula(String operacion, int num1, int num2, HttpServerRequest request){
        int resultado = 0;

        switch(operacion){
            case "suma":
                resultado = num1 + num2;
                break;
            case "resta":
                resultado = num1 - num2;
                break;
            case "multiplica":
                resultado = num1 * num2;
                break;
            case "divide":
                resultado = num1 / num2;
                break;
            case "modulo":
                resultado = num1 % num2;
                break;
        }
        Map<Object, Object> info = new HashMap<>();
        info.put("resultado", resultado);
        info.put("nombre", "oscar");
        info.put("edad", "21");
//	info.put("variable",pba);

	info.put("Current Node IP", request.localAddress().host());
	info.put("Caller IP", request.remoteAddress().host());
	info.put("Absolute url", request.absoluteURI());
	info.put("path", request.path());
	info.put("query", request.query());
	info.put("uri", request.uri());

	return Json.encodePrettily(info);
    }

}
