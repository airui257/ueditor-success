package com.example;

import com.baidu.ueditor.ActionEnter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by ldb on 2017/4/9.
 */
@Controller
public class UEditorController {

    @RequestMapping("/")
    private String showPage(){
        return "index";
    }

    @RequestMapping(value="/config")
    public void config(HttpServletRequest request, HttpServletResponse response) {
//        response.setContentType("application/json");
//		response.setHeader("Access-Control-Allow-Origin","*");
		response.setHeader("Access-Control-Allow-Headers","X-Requested-With,X_Requested_With");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
//		System.out.println("rootPath = " + rootPath);
        try {
			ActionEnter actionEnter = new ActionEnter(request, rootPath);
			String exec = actionEnter.exec();
			System.out.println("exec = " + exec);
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
