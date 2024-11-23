# 🌟 MGUI — Convenient Menu for Minecraft Servers

## 📖 Description
MGUI is a simple and intuitive plugin for organizing server menus in Minecraft. It allows players to easily navigate between servers through a user-friendly graphical interface.

---

## 🚀 Installation
1. Install the plugin on **all servers** that you want to display in the menu.
2. Install the plugin on the **proxy server** (BungeeCord or Velocity).

### 📌 Requirements
- **Java 16+**
- **Proxy Server**: BungeeCord or Velocity
- **Server**: Paper 1.16+ (or its forks)

❗ **IMPORTANT**: The plugin must also be installed on all servers specified in the configuration.

---

## 🎯 Features
- Asynchronous data transfer between servers.
- Automatic display of currently available servers.
- Real-time server statistics updates.
- Pagination for managing large numbers of servers.
- Support for placeholders via **PlaceholderAPI**.
- External configuration for unified management across multiple servers.
- Categorization of servers for better organization.
- Fully customizable graphical interface.
- Automatic connection to an available server.
- Localization

---

## 🌍 Localizations

MGUI supports multiple languages ​​for more flexible customization and use. You can change the menu language by selecting the appropriate localization.

### Available Localizations:
- 🇺🇸 **English** (`en`)
- 🇷🇺 **Russian** (`ru`)

### How to Change Language:
1. Open the file `config.yml`
2. Change the value to the desired language code (`en` or `ru`)

**Example:**

```yaml
configPaths:
  settings: 'settings.yml'
  gui: 'lang/en/gui.yml'  # Change from 'en' to 'ru' to switch language
  # For Russian, use:
  # gui: 'lang/ru/gui.yml'

  configureServers: 'lang/en/configure-servers.yml'  # Change from 'en' to 'ru' to switch language
  # For Russian, use:
  # configureServers: 'lang/ru/configure-servers.yml'
```

### How to Create Language:
1. Open the directory lang.
2. Create a folder with a short name.
3. Move files from another localization and translate, then change the directory path as indicated above.

---

## ⚙️ Configuration

### 1️⃣ Using an External Configuration
1. Copy the configuration files from the plugin folder (except `config.yml`) to a convenient location, such as a shared directory for all servers.
2. In `config.yml`, specify the paths to the copied files relative to the current directory.  
   **Example**:  
   If the file is located at:  
   `Lobby-1/plugins/MGUI/FILE.yml`,  
   specify the path as:  
   `../../../globals/MGUI/FILE.yml`  
   **Recommendation**: Create a dedicated `globals/MGUI` folder for all external configurations.

### 2️⃣ Adding New Servers
1. Open the file `configure-servers.yml`.
2. Add new servers in the following format:
   - **game**: The category for display (e.g., "Mini-Games").
   - **servers**: A nested list of server names in your proxy.
   - **name**: The category name displayed in the menu.

### 3️⃣ Changing Text, Menu Size, and Placeholders
1. Open the file `gui.yml`.
2. To change the menu size, modify the parameter:  
   `parameters.size` (default: `54`).
3. To configure text and placeholders, modify:  
   `items.SERVER.placeholderLines`.
- ❗ Important: Ensure the placeholder order matches your `lore`.
- ❗ Important: Placeholders that were not transferred (do not exist on another server) will not be displayed and the replaced string will be removed from lore.

### 4️⃣ Adding a Placeholder for All Servers
1. Open the file `settings.yml`.
2. Locate the `placeholders` parameter and add the desired placeholders.  
   Example of a default configuration:  
   ```yaml
   placeholders:
     '{server_tps}': '%server_tps%'
   ```
   To add a new placeholder, such as server uptime, the configuration will look like this:
   ```yaml
   placeholders:
     '{server_tps}': '%server_tps%'
     '{server_uptime}': '%server_uptime%'
   ```
3. These placeholders will now be sent and used by all servers.
   
---

## 🛠️ Commands
1. `/mgui reload` — Reloads the configuration (including servers, menu text, and other settings).
   
   ❗ Important: Configurations are loaded from the paths specified in config.yml. You can change the paths and reload the plugin with this command.
   
3. `/mgui <category>` — Opens the server menu for the specified category.

---

## 🎨 Screenshots
<img src="https://github.com/user-attachments/assets/7acaadca-d2b0-417f-ac87-5d0150303e86" width="400"/>
<img src="https://github.com/user-attachments/assets/7eb0c881-b7a4-4635-944c-9f4caa2d1bc2" width="400"/>
<img src="https://github.com/user-attachments/assets/dc441f58-bd29-468f-ae47-b0c96d3069ba" width="850"/>
<img src="https://github.com/user-attachments/assets/2ada8a61-0beb-453b-a951-a6f1a953a1ed" width="850"/>


---

💡 **Tip**: Use **PlaceholderAPI** to add unique data (e.g., online players, server status) to the menu.

✨ Enjoy seamless server management with **MGUI**!
