2026 OOP course homework
基於 Java Swing 構建的 **UML 編輯器**，採用 **Model-View-Controller (MVC)** 架構與 **State Pattern (狀態模式)** 來管理核心繪圖邏輯。

### 架構樹狀圖

```text
src/main/java/com/example/umleditor/
├── App.java                   # 程式進入點 (Entry Point)
├── controller/                # [Controller] 負責處理使用者輸入與狀態切換
│   ├── EditorMode.java        # 定義編輯器模式列舉 (如 SELECT, RECT, ASSOCIATION)
│   ├── EditorState.java       # 狀態抽象類別，定義標準滑鼠事件介面
│   ├── StateContext.java      # 狀態上下文，維護當前狀態並將事件委託給具體狀態
│   ├── CreateLinkState.java   # 具體狀態：處理各種連線的建立邏輯
│   ├── CreateObjectState.java # 具體狀態：處理圖形物件的建立邏輯
│   └── SelectState.java       # 具體狀態：處理圖形的選取、移動、縮放邏輯
├── model/                     # [Model] 負責資料結構與業務邏輯
│   ├── CanvasModel.java       # 管理畫布上的所有圖形物件與選取狀態
│   ├── GraphicObject.java     # 圖形物件抽象基底類別
│   ├── Label.java             # 負責處理物件的標籤文字與背景顏色資料
│   ├── Port.java              # 負責處理物件邊緣的連接埠邏輯
│   ├── links/                 # 連線邏輯資料夾
│   │   ├── ConnectionLink.java      # 連線抽象基底類別
│   │   ├── AssociationLink.java     # 關聯連線
│   │   ├── CompositionLink.java     # 聚合連線
│   │   └── GeneralizationLink.java  # 泛化連線
│   └── objects/               # 圖形邏輯資料夾
│       ├── BasicObject.java         # 基本物件抽象類別
│       ├── CompositeObject.java     # 複合物件 (支援 Group 結構)
│       ├── OvalObject.java          # 橢圓物件
│       └── RectObject.java          # 矩形物件
└── view/                      # [View] 負責使用者介面呈現
    ├── MainFrame.java         # 主視窗容器，組合所有的 UI 面板
    ├── CanvasPanel.java       # 畫布面板，負責讀取 model 並繪製物件
    ├── ToolbarPanel.java      # 工具列面板，提供模式切換按鈕
    └── LabelDialog.java       # 標籤編輯對話框，提供文字與顏色修改的圖形介面
```
<img width="1578" height="1190" alt="image" src="https://github.com/user-attachments/assets/cea76c97-8668-4057-9dde-1ceb74f86f5a" />

