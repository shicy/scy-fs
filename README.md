## 文件系统

### 客户端
* org.scy.fs.FileSysAdapter

### API
* find(SearchForm form, PageInfo pageInfo)
* file(String uuid)
* dir(String path)
* upload(File file, String path)
* upload(File file, String fileName, String path)
* upload(MultipartFile file, String path)
* upload(MultipartFile file, String fileName, String path)
* download(String uuid)
* download(String[] uuids, String zipFileName)
* delete(String uuid)
* delete(String[] uuids)
* delete(String path, boolean includeSubDir, boolean includeFile)
* moveFile(String uuid, String toPath)
* moveFiles(String[] uuids, String toPath)
* moveDir(String fromPath, String toPath)

### 配置项
* 文件根目录：app.fs-service.fileRoot

### 启动服务
运行 org.scy.fs.App

需要引用`jcoms`

### 打包
mvn clean package -f pom.xml -P prod

### 部署
安装网络`docker network create -d bridge --subnet 172.2.2.0/24 mynet`

本地执行`./build/deploy_remote`

或登录服务器，进入`build`目录，执行`./deploy`命令
