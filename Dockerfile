FROM java:8

# 制度工作目录
WORKDIR /mnt/app

# 复制打包好的项目执行文件
COPY fs_service/target/fs-*.jar ./app.jar

# 默认挂载日志目录
VOLUME /mnt/app/logs

# 默认挂载文件存储目录
VOLUME /mnt/app/fstorage

# 开放端口 12304
EXPOSE 12304

# 设置时区，添加时区软连接
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo Asia/Shanghai > /etc/timezone

# 容器启动开始允许项目
ENTRYPOINT exec java -jar app.jar
