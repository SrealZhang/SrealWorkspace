package example.neo2.daogenerator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by neo2 on 2016/7/6.
 */
public class DaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(160706, "greendao");

        addCustomerOrder(schema);
        addChatHistory(schema);
        addContractHistory(schema);
        //D:/SVN/Chat/Chatting App/app/src/main/java-gen/com.app.greendao
        new de.greenrobot.daogenerator.DaoGenerator().generateAll(schema, "D:/SVN/Chat/ChattingApp/app/src/main/java-gen");
    }

    //登陆数据（该表只有一列）
    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("NeoUser");
        customer.addIdProperty().autoincrement();
        customer.addStringProperty("name").notNull();
        customer.addStringProperty("password").notNull();
        customer.addStringProperty("headIcon");
    }

    //聊天记录数据
    private static void addChatHistory(Schema schema) {
        Entity customer = schema.addEntity("NeoChatHistory");
        customer.addIdProperty().autoincrement();
        customer.addStringProperty("myJID").notNull();   //发送者
        customer.addStringProperty("myName").notNull();  //发送者名字
        customer.addStringProperty("friendJID").notNull();     //接收者
        customer.addStringProperty("friendName").notNull();     //接收者name
        customer.addLongProperty("time").notNull();        //时间
        customer.addIntProperty("sendState");             //发送状态，是否成功
        customer.addStringProperty("body");                //文本
    }

    //最近联系人消息列表
    private static void addContractHistory(Schema schema) {
        Entity customer = schema.addEntity("NeoContractLately");
        customer.addStringProperty("friendJID").notNull().primaryKey();
        customer.addStringProperty("friendName").notNull();   //发送者名字
        customer.addStringProperty("myJID").notNull();
        customer.addIntProperty("num");
        customer.addLongProperty("time").notNull();
        customer.addStringProperty("body").notNull();
    }
}
