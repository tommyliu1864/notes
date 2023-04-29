package org.example;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

public class MyRealm extends AuthenticatingRealm {

    // 自定义的登录认证方法，Shiro 的 login 方法底层会调用该类的认证方法完成登录认证
    // 需要配置自定义的 realm 生效，在 ini 文件中配置，或 Springboot 中配置
    // 该方法只是获取进行对比的信息，认证逻辑还是按照 Shiro 的底层认证逻辑完成认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 登录用户的身份信息
        String principal = authenticationToken.getPrincipal().toString();
        // 登录用户的凭证信息（密码）
        // String credential = new String((char[]) authenticationToken.getCredentials());
        // System.out.println("principal:" + principal + ", credential:" + credential);

        // 正常流程应该是，根据当前登录用户名从数据库查询出密码（凭证），然后返回给shiro做校验
        // 参数1：返回数据库中正确的用户名；参数2：返回数据库中正确密码；参数4：提供当前realm的名字 this.getName()获得
        if ("zhangsan".equals(principal)) {
            return new SimpleAuthenticationInfo(
                    "zhangsan",
                    "7174f64b13022acd3c56e2781e098a5f",
                    ByteSource.Util.bytes("salt"),
                    this.getName()
            );
        }
        return null;
    }
}
