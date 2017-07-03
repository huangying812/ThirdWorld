package szz.com.baselib.entity.events;

/**
 * 作者：Ying.Huang on 2017/6/23 15:46
 * Version v1.0
 * 描述：
 */

public class GetServerPack {

    public String msg;
    public String cmd;

    public GetServerPack(String msg, String curCmd) {
        this.msg = msg;
        this.cmd = curCmd;
    }
}
