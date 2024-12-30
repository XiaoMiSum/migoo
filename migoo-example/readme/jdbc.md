# Docker MySql 环境搭建

1. 拉取镜像

        docker pull mysql

2. 启动 MySql，将容器 6379端口映射到宿主机 46379端口

        docker run -d --name mysql -p 43306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql

3. 使用数据库客户端连接 mysql

        host: localhost   port: 43306   password: 123456

4. 创建数据库：migoo_test

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

# 执行 MiGoo MySql 测试

      运行 jdbc-example 中的 Test

# 执行结果

- 项目根目录下生成 out-put，并保存HTML 报告

  ![mysql_example_reports](images/mysql_example_reports.png)