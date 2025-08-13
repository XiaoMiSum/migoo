# Docker MySql 环境搭建

1. 拉取镜像

        docker pull mysql

2. 启动 MySql，将容器 6379端口映射到宿主机 6379端口

        docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql

3. 使用数据库客户端连接 mysql

        host: localhost   port: 3306   password: 123456

4. 创建数据库：ryze-test

5. 创建测试表，并写入数据

        -- ----------------------------
        -- Table structure for t_001
        -- ----------------------------
        DROP TABLE IF EXISTS `t_001`;
        CREATE TABLE `t_001`  (
        `id` int NOT NULL AUTO_INCREMENT,
        `tick` varchar(255) CHARACTER NULL DEFAULT NULL,
        `name` varchar(255) CHARACTER NULL DEFAULT NULL,
        PRIMARY KEY (`id`) USING BTREE
        ) ENGINE = InnoDB AUTO_INCREMENT = 1 ROW_FORMAT = Dynamic;
        
        -- ----------------------------
        -- Records of t_001
        -- ----------------------------
        INSERT INTO `t_001` VALUES (1, 100, 'A1');

# 执行 Ryze MySql 测试

      运行 jdbc-example 中的 Test（可能需要修改测试用例中的数据库密码）

# 执行结果
