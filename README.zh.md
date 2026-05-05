# NebulaRPA

<div align="center">

![NebulaRPA Logo](./docs/images/icon_128px.png)

**🤖 RPA 桌面应用**

<p align="center">
  <a href="#">Nebula RPA 官网</a> ·
  <a href="./BUILD_GUIDE.zh.md">部署指南</a> ·
  <a href="#">使用文档</a> ·
  <a href="#">常见问题</a>
</p>

[English](README.md) | 简体中文

</div>

## 📋 概述

NebulaRPA 是一款企业级机器人流程自动化（RPA）桌面应用。通过可视化设计器支持低代码/无代码开发，用户能快速构建工作流，实现桌面软件和浏览器页面的自动化。

## 🚀 快速开始

### 系统要求
- 💻 **客户端操作系统**：Windows 10/11（主要支持）
- 🧠 **RAM** >= 8 GiB

### **服务端**：使用 Docker 部署

推荐使用 Docker 进行快速部署：

```bash

# 进入 docker 目录
cd docker

# 复制 .env
cp .env.example .env

# 修改 .env 中 Casdoor 的服务配置（8000 为默认端口）
CASDOOR_EXTERNAL_ENDPOINT="http://{YOUR_SERVER_IP}:8000"

# 🚀 启动所有服务
docker compose up -d

# 📊 检查服务状态
docker compose ps
```

- 等服务都启动后，在浏览器访问 `http://{YOUR_SERVER_IP}:32742/api/rpa-auth/user/login-check`（32742 为默认端口，如有修改自行变更）
- 如果显示 `{"code":"900001","data":null,"message":"unauthorized"}`，则表示部署正确且能正常连通。
- 在浏览器访问 `http://{YOUR_SERVER_IP}:8000`（8000 为默认端口，如有修改自行变更）
- 如果显示 Casdoor 的登录页面，则表示 Casdoor 部署正确。
- 生产部署及安全加固请参考 [部署指南](./docker/QUICK_START.md)。

### **客户端**：源码部署/安装包部署

#### 环境依赖
| 工具 | 版本要求 | 说明 |
|-----|---------|------|
| **Node.js** | >= 22 | JavaScript 运行时 |
| **Python** | 3.13.x | RPA 引擎核心 |
| **Java** | JDK 8+ | 后端服务运行时 |
| **pnpm** | >= 9 | Node.js 包管理器 |
| **UV** | 0.8+ | Python 包管理工具 |
| **7-Zip** | - | 创建部署归档文件 |
| **SWIG** | - | 连接 Python 与 C/C++ |

具体的依赖安装方式以及常见问题请参考 [构建指南](./BUILD_GUIDE.zh.md)。

#### 一键构建

1. **准备 Python 环境**
   ```bash
   # 准备一个 Python 3.13.x 安装目录（可以是本地文件夹或系统安装路径）
   # 脚本会复制该目录来创建 python_core
   ```

2. **运行构建脚本**
   ```bash
   # 在项目根目录执行完整构建（引擎 + 前端 + 桌面应用）
   ./build.bat --python-exe "C:\Program Files\Python313\python.exe"
   
   # 或使用默认配置（如果 Python 在默认路径）
   ./build.bat
   
   # 等待操作完成
   # 当控制台显示 "Full Build Complete!" 时表示构建成功
   ```

   > **注意：** 请确保指定的 Python 解释器为纯净安装，未安装额外第三方包，以减小打包体积。

   **构建流程包含：**
   1. ✅ 检测/复制 Python 环境到 `build/python_core`
   2. ✅ 安装 RPA 引擎依赖包
   3. ✅ 压缩 Python 核心到 `resources/python_core.7z`
   4. ✅ 安装前端依赖
   5. ✅ 构建前端 Web 应用
   6. ✅ 构建桌面应用

3. 📦 安装打包完成的客户端安装包

#### ⚙️ 安装好后在安装目录下的 `resources/conf.yaml` 中修改服务端地址：

   ```yaml
   # 32742 为默认端口，如有修改自行变更
   remote_addr: http://YOUR_SERVER_ADDRESS:32742/
   skip_engine_start: false
   ```

## 🏗️ 架构概览

本项目采用前后端分离架构，前端基于 Vue 3 + TypeScript 与 Electron 构建桌面应用；后端以 Java Spring Boot 与 Python FastAPI 构建微服务，支撑业务与 AI 能力；引擎层基于 Python，集成 20+ RPA 组件，支持图像识别与 UI 自动化；整体通过 Docker 部署，具备高可观测性与扩展性，专为复杂 RPA 场景设计。

![Architecture Overview](./docs/images/Structure.png "架构概览")

## 📦 组件生态

### 核心组件包
- **astronverse.system**：系统操作、进程管理、截图
- **astronverse.browser**：浏览器自动化、网页操作
- **astronverse.gui**：图形界面自动化、鼠标键盘操作
- **astronverse.excel**：Excel 表格操作、数据处理
- **astronverse.vision**：计算机视觉、图像识别
- **astronverse.ai**：AI 智能服务集成
- **astronverse.network**：网络请求、API 调用
- **astronverse.email**：邮件发送和接收
- **astronverse.docx**：Word 文档处理
- **astronverse.pdf**：PDF 文档操作
- **astronverse.encrypt**：加密解密功能

### 执行框架
- **astronverse.actionlib**：原子操作定义和执行
- **astronverse.executor**：工作流执行引擎
- **astronverse.picker**：工作流拾取元素引擎
- **astronverse.scheduler**：引擎调度器
- **astronverse.trigger**：引擎触发器

### 共享库
- **astronverse.baseline**：RPA 框架核心
- **astronverse.websocketserver**：WebSocket 通信
- **astronverse.websocketclient**：WebSocket 通信
- **astronverse.locator**：元素定位技术

### 开发规范
- ✅ 遵循现有代码风格
- ✅ 添加必要的测试用例
- ✅ 更新相关文档
- ✅ 确保所有检查通过

## 📄 开源协议

本项目基于 [开源协议](LICENSE) 开源。

---
