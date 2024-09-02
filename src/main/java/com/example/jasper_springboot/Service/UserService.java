package com.example.jasper_springboot.Service;

import com.example.jasper_springboot.ExceptionCustom.AppException;
import com.example.jasper_springboot.Model.User;
import com.example.jasper_springboot.Repository.UserRepository;
import com.example.jasper_springboot.Request.UserRequest;
import com.example.jasper_springboot.Request.UserUpdateRequest;
import com.example.jasper_springboot.Response.Message;
import com.example.jasper_springboot.SQLQuery.QueryUltil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import jakarta.persistence.Query;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserService implements IuserService{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository ur;
    @Autowired
    private QueryUltil queryUltil;
    @Override
    public User addUser(UserRequest useradd) {
        User u = new User(useradd);
        if(u != null){
            User user_add_result = ur.save(u);
            return user_add_result;
        }
        else{
            throw new AppException(400,"Đã xảy ra lỗi");
        }
    }
    @Override
    @Transactional
    public Message removeUser(int id) {
        Optional<User> checkUser = ur.findById(id);
        if(checkUser.isPresent())
        {

            String hql = "delete from User u where u.id = "+id;
            int result = queryUltil.deleteQuery(hql);
            if(result == 1)
            {
                return new Message("Xoá thành công");
            }
            else{
                return new Message("Xoá thất bại");
            }
        }else{
            throw new AppException(404,"Không tìm thấy user");
        }
    }
    @Override
    public User updateUser(int id, Map<Object,Object> field){
        Optional<User> optionalUser = ur.findById(id);
        if(optionalUser.isPresent())
        {
            field.forEach((key,value)->{
                System.out.println(key);
                Field field1 = ReflectionUtils.findField(User.class,(String) key);
                if(field1 == null)
                {
                    throw new AppException(404,"Không tồn tại trường");
                }
                field1.setAccessible(true);
                ReflectionUtils.setField(field1,optionalUser.get(),value);
            });
            User userUpdateFinal = optionalUser.get();
            userUpdateFinal.setUpdate_at(new Date());
           User resultUpdate = ur.save(optionalUser.get());

           return resultUpdate;
        }
        else{
            throw new AppException(404,"User không tồn tại");
        }
    }
    public List<User> getAllUser(Integer pageNumber,Integer pageSize, String sort_by)
    {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sort_by));
        Page<User> pu = ur.findAll(pageable);
        if(pu.hasContent())
        {
            return pu.getContent();
        }else{
            throw new AppException(404,"Không tồn tại người dùng nào");
        }
    }
    @Override
    public String exportReport(String path) throws FileNotFoundException, JRException {
        if(path != null)
        {
            Path checkPath = Paths.get(path);
            if(Files.exists(checkPath))
            {
                List<User> employees = ur.findAll();
                File file = ResourceUtils.getFile("classpath:test.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
                Map<String, Object> parameters = new HashMap<>();
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                SimpleXlsxReportConfiguration configXlsx = new SimpleXlsxReportConfiguration();
                configXlsx.setShowGridLines(true);
                configXlsx.setWhitePageBackground(false);
                JRXlsxExporter jrXlsxExporter = new JRXlsxExporter();
                jrXlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                jrXlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(path + "\\Report.xlsx")));
                jrXlsxExporter.setConfiguration(configXlsx);
                jrXlsxExporter.exportReport();
                return "report được tạo ra ở : " + path;
            }
            else{
                throw new AppException(404,"Không tìm thấy đường dẫn");
            }
        }
        else{
            throw new AppException(400,"Bạn phải nhập đường dẫn");
        }
    }
    @Override
    public List<User> fullTextSearch(String textSearch,Integer pageNumber,Integer pageSize, String sort_by){
        Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by(sort_by));
        Page<User> pu = ur.findUsersBySearchText(textSearch,pageable);
        if(pu.hasContent())
        {
            return pu.getContent();
        }else{
            throw new AppException(404,"Không tồn tại người dùng nào");
        }
    }
}
