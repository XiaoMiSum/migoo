package io.github.xiaomisum.ryze.protocol.mongo;

import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;

public interface MongoConstantsInterface extends TestElementConstantsInterface {

    String DEF_REF_NAME_KEY = "__mongo_configure_element_default_ref_name__";

    /**
     * 连接地址：mongodb://root:123456@127.0.0.1:27017/admin
     */
    String URL = "url";

    /**
     * 数据库
     */
    String DATABASE = "database";

    /**
     * 集合 相当于 mysql的表
     */
    String COLLECTION = "collection";

    /**
     * 操作：find \ delete \ insert \ update
     */
    String ACTION = "action";

    /**
     * insert, update 操作数据
     */
    String DATA = "data";

    /**
     * 条件
     */
    String CONDITION = "condition";

    String FIND = "find";

    String DELETE = "delete";

    String INSERT = "insert";

    String UPDATE = "update";
}
