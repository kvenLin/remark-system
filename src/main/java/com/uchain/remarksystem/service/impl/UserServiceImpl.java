package com.uchain.remarksystem.service.impl;

import com.uchain.remarksystem.DTO.VerifyCode;
import com.uchain.remarksystem.dao.UserMapper;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.form.user.*;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.redis.RedisService;
import com.uchain.remarksystem.redis.VerifyCodeKey;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.security.JwtTokenUtil;
import com.uchain.remarksystem.service.UserProjectService;
import com.uchain.remarksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private UserProjectService userProjectService;

    @Override
    public boolean insert(User user) {
        if (userMapper.insert(user)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        if (userMapper.updateByPrimaryKey(user)==1){
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public User selectByEmpNum(String empNum) {
        return userMapper.selectByEmpNum(empNum);
    }

    @Override
    public User selectById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> selectByRandom(String info) {
        return userMapper.selectByRandom(info);
    }

    @Override
    public List<User> selectByRole(Integer role) {
        return userMapper.selectByRole(role);
    }

    @Override
    public Result addUser(UserAddForm userAddForm) {
        if (selectByEmpNum(userAddForm.getEmpNum())!=null){
            return Result.error(CodeMsg.USER_ALREADY_EXIST);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddForm,user);
        user.setCreatedBy(getCurrentUser().getId());
        user.setUpdatedBy(getCurrentUser().getId());
        //设置初始密码为123456
        user.setPassword("123456");
        if (insert(user)) {
            return Result.success(user);
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public Result deleteUser(Long userId) {
        //删除前需要判断当前用户是否可以进行删除
        // 若参加了还未完成的项目则不能删除
        List<Project> projects = userProjectService.selectUnfinishedProjectByUserId(userId);
        if (projects.size()!=0){
            return Result.error(CodeMsg.PROJECT_UN_FINISH_CAN_NOT_DELETE_USER);
        }
        // 若未参加或者参加的项目已经结束则可以删除
        delete(userId);
        userProjectService.deleteByUser(userId);
        return Result.success();
    }

    @Override
    public List<User> allUser() {
        return userMapper.selectAll();
    }

    @Override
    public List<User> selectUserByProjectId(Long projectId) {
        return userMapper.selectUserByProjectId(projectId);
    }

    @Override
    public Result updateUser(UserUpdateForm userUpdateForm) {
        User user = selectById(userUpdateForm.getId());
        if (user==null){
            return Result.error(CodeMsg.USER_NOT_EXIST);
        }
        BeanUtils.copyProperties(userUpdateForm,user);
        //设置更新用户的id
        user.setUpdatedBy(getCurrentUser().getId());
        if (update(user)) {
            return Result.success(user);
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public String sendVerifyCode(String clientIp) throws IOException {
        VerifyCode code = new VerifyCode();
        //获取验证码图片
        BufferedImage image = code.getImage();

        //获取验证码内容
        String text = code.getText();
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        randomCode.append(text);
        //加入缓存
        redisService.set(VerifyCodeKey.getByClientIp, clientIp,text.toLowerCase());//60秒时间输入验证码
        log.info("redis-signcode==>"+randomCode.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        ImageIO.write(image, "jpg", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String jpg_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
        jpg_base64 = jpg_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return jpg_base64;
    }

    @Override
    public boolean checkVerifyCode(String clientIp, String code) {
        code = code.toLowerCase();
        String verifyCode = redisService.get(VerifyCodeKey.getByClientIp, clientIp, String.class);
        if (code.equals(verifyCode)){
            return true;
        }
        return false;
    }

    @Override
    public Result register(String clientIp,UserRegisterForm userRegisterForm) {
        //校验验证码是否正确
        if (!checkVerifyCode(clientIp,userRegisterForm.getVerifyCode())){
            return Result.error(CodeMsg.CHECK_CODE_ERROR);
        }
        //校验成功,删除验证码缓存
        redisService.delete(VerifyCodeKey.getByClientIp,clientIp);
        User user = selectByEmpNum(userRegisterForm.getEmpNum());
        if (user!=null){
            return Result.error(CodeMsg.USER_ALREADY_EXIST);
        }
        user = new User();
        BeanUtils.copyProperties(userRegisterForm,user);
        //注册用户为未审核用户,需要后台进行授权
        user.setRole(0);
        if (insert(user)) {
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public Result login(String clientIp,LoginForm loginForm) {
        User user = selectByEmpNum(loginForm.getEmpNum());
        //检验验证码是否正确
        log.info("clientIp:{}",clientIp);
        if (!checkVerifyCode(clientIp,loginForm.getVerifyCode())) {
            return Result.error(CodeMsg.VERIFY_CODE_ERROR);
        }
        //检验用户是否存在
        if (user==null){
            return Result.error(CodeMsg.USER_NOT_EXIST);
        }
        //判断用户是否是有效用户
        if (user.getRole()==0){
            return Result.error(CodeMsg.USER_CHECKING);
        }
        log.info("验证用户密码..........");
        Authentication token = new UsernamePasswordAuthenticationToken(loginForm.getEmpNum(),loginForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        log.info("验证通过............");
        //认证通过放入容器中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginForm.getEmpNum());
        log.info("加载UserDetails:{}",userDetails.getUsername());
        //生成token
        final String realToken = jwtTokenUtil.generateToken(userDetails);
        redisService.delete(VerifyCodeKey.getByClientIp,clientIp);
        Map map = new HashMap();
        map.put("role",user.getRole());
        map.put("token",realToken);
        return Result.success(map);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if (authentication!=null&&!name.equals("anonymousUser")){
            return selectByEmpNum(name);
        }
        return null;
    }

    @Override
    public Result changePassword(ChangeForm changeForm,String clientIp) {
        String code = redisService.get(VerifyCodeKey.getByClientIp, clientIp, String.class);
        if (!changeForm.getVerifyCode().equals(code)){
            return Result.error(CodeMsg.VERIFY_CODE_ERROR);
        }
        if (changeForm.getNewPassword().equals(changeForm.getPassword())){
            return Result.error(CodeMsg.NEW_PASSWORD_SAME);
        }
        if (!changeForm.getNewPassword().equals(changeForm.getCheckNewPassword())){
            return Result.error(CodeMsg.CHECK_PASSWORD_ERROR);
        }
        User user = selectByEmpNum(changeForm.getEmpNum());
        if (user==null){
            return Result.error(CodeMsg.USER_NOT_EXIST);
        }
        if (!user.getPassword().equals(changeForm.getPassword())){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        user.setPassword(changeForm.getNewPassword());
        if (update(user)){
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Result resetPassword(Long userId) {
        User user = selectById(userId);
        if (user==null){
            return Result.error(CodeMsg.USER_NOT_EXIST);
        }
        user.setPassword("123456");
        if (update(user)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

}
