# tea_drinking_system

本專案為模擬實體茶飲店運作流程的 Java 系統，透過物件導向設計與簡易 Swing 介面實現。主要分為四大模組：

- 飲品管理（Drink）
- 訂單管理（Order）
- 員工管理（Employee）
- 使用者介面（UI）

---

## 飲品模組（Drink Module）

- `Drink`：用於記錄單項飲品的資訊，常與 `Menu` 和 `Order` 組合使用。
- `Ingredient`：紀錄原物料，與 `Inventory` 與 `Formula` 模組相關聯。

---

## 訂單模組（Order Module）

- `Order`：一般訂單，紀錄下單者與所選飲品，透過 `OrderItem` 組成（包含數量、飲品、總價）。
- `DeliveryOrder`：繼承 `Order`，並擴充地址與 `deliveryID` 欄位以支援外送需求。

---

## 員工模組（Employee Module）

所有員工類型皆繼承自 `Employee` 類別，並具備基本身分與排班機制。

### 員工類型

- `Cashier`（收銀員）分為：
  - **Inventory 任務**：檢查庫存與下單。當原料不足時，自動通知 `Employer` 補貨（預設補貨數量為缺料項目的 20 份），並記錄到任務佇列。
  - **Ready 任務**：叫號或將完成訂單放入外送佇列。

- `Maker`（製作員）：根據訂單製作飲品。

- `Delivery`（外送員）：負責將飲品外送至顧客指定地址。

上述角色皆透過 **多執行緒搶單機制** 分配任務，並使用同步鎖確保資源安全互斥。

- `Employer`（雇主）：唯一有權限：
  - 雇用/裁員
  - 管理菜單
  - 訂購原物料

---

## 使用者介面模組（UI Module）

簡易 Swing 圖形介面，分為三大區塊：

### 顧客端

- **開始點餐**：由顧客透過圖形介面進行點餐，送出後進入訂單處理流程。
- **查看菜單**：顯示 `Menu` 物件中的所有飲品資訊。

### 店長端

- **菜單管理**：由 `Employer` 新增、編輯、刪除飲品；新增時可同時建立其 `Formula`。
- **員工管理**：支援雇用、裁員與顯示現有員工。

### 系統日誌

- 系統動作皆可紀錄於日誌中，並依據角色分為以下分類：
  - `Customer`
  - `Maker`
  - `Cashier`
  - `Employer`
  - `Delivery`
  - `Other`

---

## 技術細節

- 語言：Java 17+
- 圖形介面：Java Swing
- 執行緒處理：Java 多執行緒與同步鎖（`synchronized`, `wait`, `notify` 等）
- 模組化設計：物件導向程式設計（OOP）
- 儲存：暫時以記憶體為主（可擴充持久層）

