# Docker Manage

Docker Manage 是一个基于 `docker-java` 的管理系统，旨在简化 Docker 容器和镜像的管理。

## 功能
- 查询镜像
- 拉取镜像
- 备份镜像（待开发）
- 还原镜像（待开发）

## 安装
1. 克隆仓库：
   ```bash
   git clone https://gitee.com/steve5634/docker-manage.git
   ```

2. 进入项目目录并构建：
   ```bash
   cd docker-manage
   mvn clean install
   ```

## 使用
启动服务：
```bash
java -jar target/docker-manage.jar
```

## 贡献
欢迎贡献代码！请提交 Pull Request 或报告 Issue。