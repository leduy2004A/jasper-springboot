package com.example.jasper_springboot.Service;

import com.example.jasper_springboot.Model.User;
import com.example.jasper_springboot.Request.UserRequest;
import com.example.jasper_springboot.Response.Message;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface IuserService {
    public User addUser(UserRequest useradd);
    public Message removeUser(int id);
    public User updateUser(int id, Map<Object,Object> field);
public String exportReport(String path) throws FileNotFoundException, JRException;
    public List<User> fullTextSearch(String textSearch, Integer pageNumber, Integer pageSize, String sort_by);
}
