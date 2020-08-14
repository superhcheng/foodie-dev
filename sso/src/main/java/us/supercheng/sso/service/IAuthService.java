package us.supercheng.sso.service;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAuthService {
    boolean checkCASSSO();
    boolean createCASSession(String username, String password, String retUrl, Model model, HttpServletRequest req, HttpServletResponse resp);
    void createCASTKT();
    void createCASTempTKT();
}
