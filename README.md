# tea_drinking_system
此專案唯有關於模擬實體茶飲系統的流程的專案，透過不同物件與簡單介面繪製而成，其分為飲品、訂單、員工、介面等四大模組

### 飲品
Drink為記錄飲品的物件，當有需記錄飲品時會以association的方式新增作為變數，如Menu、Order內就使用到
Ingredient也是相同的概念只是存入的是Inventory, Formula

### order
order又分為一般Order與deliveryOrder
- Order為訂單的物件，填寫著下單人與各項飲品資訊使用Drink物件記錄相關飲品與OrderItem整合成數量、Drink、總計的封包
- deliveryOrder繼承Order，除了有Order的功能外因為送餐需要地址所以有增加地址與deliveryID

### employee
分為Cashier, Maker, Delivery, Employer 這幾個均繼承Employee
- Cashier：又分為兩種
  - Inventory：查看庫存兼下單(通過該飲品的Formula查找需要的材料數量再進入Inventory類別查找是否足夠)，當庫存不足時會自動通知Employer訂購，預設缺貨原料項目的20份，會寫入佇列中確認是誰接的單
  - Ready：將完成訂單訂單叫號，或放入外送佇列
- Maker：製作飲品
- Delivery：外送
- ----------以上均有執行序搶單機制，確保每位員工都有上工，有使用執行序進行互斥存取
  
- Employer：只有Employer可以雇用員工，更改菜單，訂購原物料

### 介面設計分為3個部分
- 顧客點餐
  - 開始點餐：會藉由簡易的介面下單，再通過上述物件進入點餐流程
  - 查看菜單：通過Menu 物件印出菜單內容
- 店長管理
  - 菜單管理：透過Employer物件新增、編輯、刪除不同飲品，新增時會一併新增該製作原料配方
  - 員工管理：雇用、裁員、列出員工
- 查看日誌
  - 分成"Customer", "Maker", "Cashier", "Employer", "Delivery", "Other"當不同物件的角色做事時則可以將其logger對應寫入該紀錄格中
