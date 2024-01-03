package br.com.meuApp.app;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CustomInterceptor extends HandlerInterceptorAdapter {

    @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
              if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            ValidMatriz annotation = method.getAnnotation(ValidMatriz.class);

            if (annotation != null) {

                String nameParamIdPacote = annotation.nameParamIdPacote();
                String nameParamIdAmbiente = annotation.nameParamIdAmbiente();
                Long idPacote = 0l;
                Long idAmbiente = 0l;

                MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
                Map<String, String> pathVariables = (Map<String, String>) request.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");


                for (MethodParameter parameter : methodParameters) {

                    PathVariable pathVariable = parameter.getParameter().getAnnotation(PathVariable.class);
                    RequestParam requestParam = parameter.getParameter().getAnnotation(RequestParam.class);

                    if (requestParam !=null){
                        idPacote = Long.valueOf(request.getParameter(nameParamIdPacote));
                        idAmbiente = Long.valueOf(request.getParameter(nameParamIdAmbiente));
                    }

                    if (pathVariable !=null){
                         idPacote = Long.valueOf(pathVariables.get(nameParamIdPacote));
                        idAmbiente = Long.valueOf(pathVariables.get(nameParamIdAmbiente));
                    }

                }

                request.setAttribute("Custom-Header", "ValorDoInterceptor");
                request.setAttribute("pacote", idPacote.toString());
                request.setAttribute("ambiente", idAmbiente.toString());

                System.out.println(idPacote + "  - " + idAmbiente);


            }
        }



        return true; // Continua o fluxo da requisição
    }

}
