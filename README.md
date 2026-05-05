# NebulaRPA

<div align="center">

![NebulaRPA Logo](./docs/images/icon_128px.png)

**🤖RPA Desktop Application**

<p align="center">
  <a href="#">Nebula RPA Official Site</a> ·
  <a href="./BUILD_GUIDE.md">Deployment Guide</a> ·
  <a href="#">User Documentation</a> ·
  <a href="#">FAQ</a>
</p>


English | [简体中文](README.zh.md)

</div>

## 📋 Overview

NebulaRPA is an enterprise-grade Robotic Process Automation (RPA) desktop application. Through a visual designer, it supports low-code/no-code development, enabling users to rapidly build workflows and automate desktop software and web pages.

[Nebula Agent] is the native Agent platform supported by this project. Users can directly call RPA workflow nodes in Nebula Agent, and also use Agent workflows in NebulaRPA, achieving efficient collaboration between automation processes and intelligent agent systems, empowering broader business automation scenarios.

## 🚀 Quick Start

### System Requirements
- 💻 **Client Operating System**: Windows 10/11 (primary support)
- 🧠 **RAM** >= 8 GiB

### **Server**: Deploy with Docker

Recommended for quick deployment:

```bash

# Enter docker directory
cd docker

# Copy .env
cp .env.example .env

# Modify casdoor service configuration in .env (8000 is the default port)
CASDOOR_EXTERNAL_ENDPOINT="http://{YOUR_SERVER_IP}:8000"

# 🚀 Start all services
docker compose up -d

# 📊 Check service status
docker compose ps
```

- After all services have started, open your browser and go to: `http://{YOUR_SERVER_IP}:32742/api/rpa-auth/user/login-check` (32742 is the default port; change it if you modified the configuration).
- If you see `{"code":"900001","data":null,"message":"unauthorized"}`, it means the deployment is correct and the connection is working properly.
- Open your browser and go to: `http://{YOUR_SERVER_IP}:8000` (8000 is the default port; change it if you modified the configuration).
- If you see the Casdoor login page, it means Casdoor is deployed correctly.
- For production deployment and security hardening, refer to the [Deployment Guide](./docker/QUICK_START.md).

### **Client**: Source Deployment/Binary Deployment

#### Environment Dependencies
| Tool | Version | Description |
|-----|---------|------------|
| **Node.js** | >= 22 | JavaScript runtime |
| **Python** | 3.13.x | RPA engine core |
| **Java** | JDK 8+ | Backend runtime |
| **pnpm** | >= 9 | Node.js package manager |
| **UV** | 0.8+ | Python package management tool |
| **7-Zip** | - | Create deployment archives |
| **SWIG** | - | Connect Python with C/C++ |

For specific installation instructions and common issues, refer to [Build Guide](./BUILD_GUIDE.md).

#### One-Click Build

1. **Prepare Python Environment**
   ```bash
   # Prepare a Python 3.13.x installation directory (can be a local folder or system installation path)
   # The script will copy this directory to create python_core
   ```

2. **Run Build Script**
   ```bash
   # Full build (engine + frontend + desktop app) from project root directory
   ./build.bat --python-exe "C:\Program Files\Python313\python.exe"
   
   # Or use default configuration (if Python is in default path)
   ./build.bat
   
   # Wait for completion
   # Build successful when console displays "Full Build Complete!"
   ```

   > **Note:** Please ensure the specified Python interpreter is a clean installation without additional third-party packages to minimize package size.

   **Build process includes:**
   1. ✅ Detect/copy Python environment to `build/python_core`
   2. ✅ Install RPA engine dependencies
   3. ✅ Compress Python core to `resources/python_core.7z`
   4. ✅ Install frontend dependencies
   5. ✅ Build frontend web application
   6. ✅ Build desktop application

3. 📦 Install the packaged client

#### ⚙️ After installation, modify the server address in `resources/conf.yaml` in the installation directory:

    ```yaml
    # 32742 is the default port; change it if you modified the configuration
    remote_addr: http://YOUR_SERVER_ADDRESS:32742/
    skip_engine_start: false
    ```

## 🏗️ Architecture Overview

The project adopts a frontend-backend separation architecture. The frontend is built with Vue 3 + TypeScript and Electron for desktop applications; the backend uses Java Spring Boot and Python FastAPI to build microservices supporting business and AI capabilities. The engine layer is based on Python, integrating 20+ RPA components with support for image recognition and UI automation. The entire system is deployed via Docker with high observability and scalability, designed for complex RPA scenarios.

![Architecture Overview](./docs/images/Structure.png "Architecture Overview")

## 📦 Component Ecosystem

### Core Component Packages
- **astronverse.system**: System operations, process management, screenshots
- **astronverse.browser**: Browser automation, web page operations
- **astronverse.gui**: GUI automation, mouse and keyboard operations
- **astronverse.excel**: Excel spreadsheet operations, data processing
- **astronverse.vision**: Computer vision, image recognition
- **astronverse.ai**: AI intelligent service integration
- **astronverse.network**: Network requests, API calls
- **astronverse.email**: Email sending and receiving
- **astronverse.docx**: Word document processing
- **astronverse.pdf**: PDF document operations
- **astronverse.encrypt**: Encryption and decryption functions

### Execution Framework
- **astronverse.actionlib**: Atomic operation definition and execution
- **astronverse.executor**: Workflow execution engine
- **astronverse.picker**: Workflow element picker engine
- **astronverse.scheduler**: Engine scheduler
- **astronverse.trigger**: Engine trigger

### Shared Libraries
- **astronverse.baseline**: RPA framework core
- **astronverse.websocketserver**: WebSocket communication
- **astronverse.websocketclient**: WebSocket communication
- **astronverse.locator**: Element locating technology

### Development Guidelines
- ✅ Follow existing code style
- ✅ Add necessary test cases
- ✅ Update relevant documentation
- ✅ Ensure all checks pass

## 📄 License

This project is open source under the [Open Source License](LICENSE).

---