package szz.com.baselib.entity.events;

/**
 * 作者：Ying.Huang on 2017/6/23 09:50
 * Version v1.0
 * 描述：
 */

public class LoginSuccess {

    public String account;
    public String password;

    public LoginSuccess(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
